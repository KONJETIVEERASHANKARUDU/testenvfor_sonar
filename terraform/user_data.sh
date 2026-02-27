#!/bin/bash
# Auto-configuration script for SonarQube on EC2
# No manual intervention required

set -e

# Update system
yum update -y

# Install required packages
yum install -y java-17-amazon-corretto-devel wget unzip docker git

# Install CloudWatch agent
wget https://s3.amazonaws.com/amazoncloudwatch-agent/amazon_linux/amd64/latest/amazon-cloudwatch-agent.rpm
rpm -U ./amazon-cloudwatch-agent.rpm

# Start Docker
systemctl enable docker
systemctl start docker

# Install Docker Compose
curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# Create SonarQube directories
mkdir -p /opt/sonarqube/{data,logs,extensions}
chown -R 1000:1000 /opt/sonarqube

# Create docker-compose.yml for SonarQube
cat > /opt/sonarqube/docker-compose.yml <<'EOF'
version: "3"
services:
  sonarqube:
    image: sonarqube:latest
    container_name: sonarqube
    restart: always
    environment:
      - SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true
    ports:
      - "9000:9000"
    volumes:
      - /opt/sonarqube/data:/opt/sonarqube/data
      - /opt/sonarqube/logs:/opt/sonarqube/logs
      - /opt/sonarqube/extensions:/opt/sonarqube/extensions
EOF

# Start SonarQube
cd /opt/sonarqube
docker-compose up -d

# Create automatic backup script
cat > /usr/local/bin/backup-sonarqube.sh <<'EOF'
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/tmp/sonarqube-backup-$DATE"
mkdir -p $BACKUP_DIR

# Backup SonarQube data
docker exec sonarqube tar czf - /opt/sonarqube/data > $BACKUP_DIR/sonarqube-data.tar.gz

# Upload to S3
aws s3 cp $BACKUP_DIR/sonarqube-data.tar.gz s3://${backup_bucket}/sonarqube/$DATE/

# Cleanup
rm -rf $BACKUP_DIR
EOF

chmod +x /usr/local/bin/backup-sonarqube.sh

# Schedule daily backups at 2 AM
echo "0 2 * * * root /usr/local/bin/backup-sonarqube.sh" >> /etc/crontab

# Auto-healing: Monitor and restart if needed
cat > /usr/local/bin/monitor-sonarqube.sh <<'EOF'
#!/bin/bash
if ! docker ps | grep -q sonarqube; then
  echo "SonarQube container not running, restarting..."
  cd /opt/sonarqube
  docker-compose up -d
fi

if ! curl -s http://localhost:9000/api/system/status | grep -q UP; then
  echo "SonarQube not responding, restarting..."
  cd /opt/sonarqube
  docker-compose restart
fi
EOF

chmod +x /usr/local/bin/monitor-sonarqube.sh
echo "*/5 * * * * root /usr/local/bin/monitor-sonarqube.sh" >> /etc/crontab

# CloudWatch metrics
if [ "${cloudwatch_enabled}" = "true" ]; then
  cat > /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json <<'CWEOF'
{
  "metrics": {
    "namespace": "SonarQube",
    "metrics_collected": {
      "mem": {
        "measurement": [
          {"name": "mem_used_percent", "rename": "MemoryUsage"}
        ]
      },
      "disk": {
        "measurement": [
          {"name": "used_percent", "rename": "DiskUsage"}
        ]
      }
    }
  }
}
CWEOF
  /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -s -c file:/opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json
fi

echo "SonarQube installation completed successfully!"
