# 🤖 TestEnvFor_Sonar - Complete Automation System

**Zero-configuration infrastructure with self-healing, auto-scaling, and continuous deployment.**

## ⚡ Quick Start (One Command!)

```bash
bash auto-setup.sh
```

That's it! Everything installs, configures, and deploys automatically.

## 🎯 What This Does

This project provides **100% automated infrastructure** with:

- ✅ **Zero manual configuration** - All defaults pre-set
- ✅ **Auto-healing** - Services restart automatically on failure
- ✅ **Auto-scaling** - Scales from 1-3 instances based on CPU
- ✅ **Auto-backup** - Daily S3 backups at 2 AM UTC
- ✅ **Auto-deployment** - Push to main = infrastructure deployed
- ✅ **CI/CD workflows** - GitHub Actions handles everything
- ✅ **Code quality** - Duplicate detection and auto-fixing
- ✅ **Failure analysis** - CI failures analyzed automatically

## 🏗️ Infrastructure

### AWS Resources (Auto-deployed)

**Networking:**
- VPC (10.0.0.0/16)
- 2 Public Subnets + 2 Private Subnets
- Internet Gateway + NAT Gateway
- Security Groups (ports 22, 80, 443, 9000)

**Compute:**
- EC2 Auto Scaling Group (1-3 instances, t3.medium)
- Application Load Balancer
- Launch Template with auto-configuration
- IAM roles with least-privilege policies

**Storage:**
- S3 backup bucket (30-day retention)
- S3 terraform state bucket
- DynamoDB state lock table

**Monitoring:**
- CloudWatch metrics and alarms
- Health checks every 5 minutes
- CPU-based scaling (>75% threshold)
- Custom SonarQube metrics

**SonarQube:**
- Docker containerized deployment
- Auto-configured on EC2 boot
- Accessible via Load Balancer
- Default credentials: `admin` / `admin`

## 📦 What Gets Installed

The `auto-setup.sh` script automatically installs:

- **Terraform** (1.7.0+)
- **AWS CLI** (v2)
- **Git**
- **Python 3** + packages (boto3, requests, pyyaml)
- **Node.js** (LTS)
- **Java 17**
- **Maven**
- **Docker**

All tools are installed with **zero prompts** - just confirmation at the end.

## 🚀 Deployment Options

### Option 1: Fully Automated (Recommended)

```bash
bash auto-setup.sh
# Choose option 1 when prompted
# Wait 5-10 minutes
# Access SonarQube at the URL shown in outputs
```

### Option 2: Manual Review First

```bash
bash auto-setup.sh
# Choose option 2 when prompted
# Review terraform plan
# Type 'yes' to deploy
```

### Option 3: GitHub Actions (Push to Deploy)

```bash
git add .
git commit -m "feat: Deploy infrastructure"
git push origin main
# Infrastructure deploys automatically via GitHub Actions
```

## 🔧 Configuration (Optional)

All defaults are production-ready. To customize:

Edit [terraform/terraform.tfvars](terraform/terraform.tfvars):

```hcl
aws_region               = "us-east-1"      # Change region
environment              = "production"     # staging, dev, etc.
instance_type            = "t3.medium"      # Instance size
auto_scaling_enabled     = true             # Enable/disable auto-scaling
monitoring_enabled       = true             # Enable/disable CloudWatch
backup_retention_days    = 30               # Backup retention
```

Then run:
```bash
cd terraform
terraform apply
```

## 🔐 Credentials Setup

### AWS Credentials

**Option 1: AWS CLI (Recommended)**
```bash
aws configure
# Enter: Access Key ID
# Enter: Secret Access Key
# Region: us-east-1
# Format: json
```

**Option 2: Environment Variables**
```bash
export AWS_ACCESS_KEY_ID="your_access_key"
export AWS_SECRET_ACCESS_KEY="your_secret_key"
```

**Option 3: IAM Role** (if running on EC2)
- Attach IAM role with Administrator permissions
- No configuration needed

### GitHub Token (For GitHub Actions)

**Create token:**
1. Go to https://github.com/settings/tokens
2. Click "Generate new token (classic)"
3. Select scopes: `repo`, `workflow`
4. Copy token

**Save token:**
```bash
echo "your_github_token" > ~/.github_token
```

**For GitHub Actions:**
1. Go to repository Settings → Secrets and variables → Actions
2. Add secrets:
   - `AWS_ACCESS_KEY_ID`
   - `AWS_SECRET_ACCESS_KEY`
3. `GITHUB_TOKEN` is automatic (no setup needed)

## 🤖 Automated Workflows

### 1. Terraform Deployment

**Trigger:** Push to `main` branch

**What happens:**
1. Terraform plan runs
2. Plan posted as PR comment (on PRs)
3. On merge to main:
   - Infrastructure deployed/updated automatically
   - Deployment summary created
   - SonarQube URL outputted

**File:** [.github/workflows/terraform-deploy.yml](.github/workflows/terraform-deploy.yml)

### 2. CI Failure Recovery

**Trigger:** Any workflow failure

**What happens:**
1. Failure detected automatically
2. Root cause analysis performed
3. Auto-recovery attempted (rebuild, cache clear)
4. Issue created if recovery fails

**File:** [.github/workflows/failure-recovery.yml](.github/workflows/failure-recovery.yml)

### 3. Duplicate Code Detection

**Trigger:** Pull request or push

**What happens:**
1. Scan all Java files for duplicates
2. Auto-generate utility classes
3. Refactor duplicated code
4. Commit fixes automatically

**Files:** 
- [analyze_duplicates.py](analyze_duplicates.py)
- [.github/workflows/duplicate-detection.yml](.github/workflows/duplicate-detection.yml)

### 4. Organization Monitoring

**Trigger:** Scheduled (every hour)

**What happens:**
1. Monitor all repositories
2. Detect workflow failures
3. Check for failed test patterns
4. Create organizational reports

**Files:**
- [org_monitor_agent.py](org_monitor_agent.py)
- [.github/workflows/org-monitor.yml](.github/workflows/org-monitor.yml)

## 🔄 Auto-Healing

Health checks run **every 5 minutes** on all EC2 instances:

```bash
# Check if SonarQube is running
docker ps | grep sonarqube

# Check if responding
curl -f http://localhost:9000/api/system/status

# Auto-restart if failed
docker-compose restart
```

**File:** [terraform/user_data.sh](terraform/user_data.sh) (lines 90-115)

## 📊 Auto-Scaling

**Trigger:** CPU usage > 75% for 2 consecutive periods

**Action:** 
- Scale up to max 3 instances
- Scale down when CPU < 50%

**Configuration:** [terraform/compute.tf](terraform/compute.tf) (lines 60-85)

## 💾 Auto-Backup

Backups run **daily at 2 AM UTC**:

```bash
# What gets backed up:
- SonarQube data directory
- SonarQube logs
- SonarQube extensions
- Configuration files

# Where:
S3 bucket: testenvfor-sonar-backups-<account-id>

# Retention:
30 days (configurable)
```

**File:** [terraform/user_data.sh](terraform/user_data.sh) (lines 70-88)

## 📈 Monitoring

**CloudWatch Metrics:**
- EC2 CPU Utilization
- EC2 Network In/Out
- ALB Request Count
- ALB Target Response Time
- Custom SonarQube health metrics

**Alarms:**
- High CPU (>75% for 5 minutes)
- Unhealthy targets (0 healthy for 2 minutes)
- Low healthy targets (<50% for 5 minutes)

**Dashboard:** Auto-created in CloudWatch

## 🧪 Local Development

```bash
# Run duplicate detection locally
python3 analyze_duplicates.py

# Run failure analysis locally
python3 ci_failure_agent.py

# Test Terraform without deploying
cd terraform
terraform init
terraform plan

# Validate Terraform syntax
terraform validate
terraform fmt -check
```

## 📋 Manual Operations

### Deploy Infrastructure
```bash
cd terraform
bash setup.sh
```

### Destroy Infrastructure
```bash
cd terraform
terraform destroy
# Or via GitHub Actions: Actions → Terraform Deploy → Destroy
```

### Update Single Resource
```bash
cd terraform
terraform apply -target=aws_instance.example
```

### View Outputs
```bash
cd terraform
terraform output
# Shows: SonarQube URL, backups bucket, etc.
```

## 🔍 Troubleshooting

### Deployment Fails

**Check AWS credentials:**
```bash
aws sts get-caller-identity
```

**Check Terraform state:**
```bash
cd terraform
terraform state list
```

**Re-initialize:**
```bash
cd terraform
rm -rf .terraform
terraform init
```

### SonarQube Not Accessible

**Check load balancer:**
```bash
aws elbv2 describe-load-balancers --query 'LoadBalancers[?starts_with(LoadBalancerName, `testenvfor`)].DNSName'
```

**Check target health:**
```bash
aws elbv2 describe-target-health --target-group-arn <arn>
```

**SSH to instance:**
```bash
aws ec2 describe-instances --filters "Name=tag:Name,Values=testenvfor-sonar-*" --query 'Reservations[0].Instances[0].PublicIpAddress'
ssh -i your-key.pem ec2-user@<ip>
docker logs sonarqube
```

### Auto-Healing Not Working

**Check monitoring script:**
```bash
ssh ec2-user@<instance-ip>
sudo crontab -l
sudo cat /var/log/cron
sudo systemctl status crond
```

**Manually run health check:**
```bash
ssh ec2-user@<instance-ip>
sudo /usr/local/bin/monitor-sonarqube.sh
```

### Backups Failing

**Check S3 permissions:**
```bash
aws s3 ls s3://testenvfor-sonar-backups-<account-id>/
```

**Check IAM role:**
```bash
aws iam get-role --role-name testenvfor-sonar-ec2-role
```

**Manually trigger backup:**
```bash
ssh ec2-user@<instance-ip>
sudo /usr/local/bin/backup-sonarqube.sh
```

## 📚 Project Structure

```
testenvfor_sonar/
├── auto-setup.sh                    # ⚡ ONE-COMMAND SETUP
├── README.md                        # This file
│
├── terraform/                       # 🏗️ Infrastructure as Code
│   ├── main.tf                      # Provider configuration
│   ├── variables.tf                 # Input variables
│   ├── terraform.tfvars             # Default values (pre-configured)
│   ├── network.tf                   # VPC, subnets, security groups
│   ├── compute.tf                   # EC2, auto-scaling, IAM
│   ├── loadbalancer.tf              # Application Load Balancer
│   ├── storage.tf                   # S3 buckets, DynamoDB
│   ├── outputs.tf                   # Resource outputs
│   ├── user_data.sh                 # EC2 bootstrap script
│   └── setup.sh                     # Local deployment script
│
├── .github/workflows/               # 🤖 CI/CD Automation
│   ├── terraform-deploy.yml         # Infrastructure deployment
│   ├── failure-recovery.yml         # Auto-recovery on failures
│   ├── failure-analysis-agent.yml   # CI failure analysis
│   ├── org-monitor.yml              # Organization monitoring
│   └── duplicate-detection.yml      # Code quality checks
│
├── src/                             # 📦 Java Application
│   └── main/java/com/example/
│       ├── DuplicateCodeService.java          # Original (with duplicates)
│       ├── DuplicateCodeServiceRefactored.java # Refactored (clean)
│       └── utils/                             # Utility classes
│           ├── ValidationUtils.java
│           ├── DataProcessor.java
│           ├── ErrorHandler.java
│           └── DiscountCalculator.java
│
├── analyze_duplicates.py            # 🔍 Duplicate code scanner
├── ci_failure_agent.py              # 🩺 Failure diagnosis tool
├── org_monitor_agent.py             # 📊 Organization monitor
│
└── docs/                            # 📖 Documentation
    ├── DUPLICATE_CODE_FIX_REPORT.md
    └── DUPLICATE_VERIFICATION_COMPLETE.md
```

## 🎯 Zero-Configuration Features

✅ **All defaults are production-ready**
- No need to edit any `.tfvars` files
- Sensible defaults for all variables
- Pre-configured security groups
- Optimized instance types

✅ **Auto-detection of credentials**
- Checks environment variables
- Checks AWS credentials file
- Checks IAM role (if on EC2)
- Loads GitHub token automatically

✅ **Auto-installation of tools**
- Detects OS (Mac/Linux)
- Installs missing dependencies
- Configures everything automatically
- Single confirmation prompt only

✅ **Auto-configuration on boot**
- EC2 instances self-configure
- Docker installed automatically
- SonarQube starts on boot
- Monitoring scripts enabled
- Backup cron jobs created

✅ **Auto-deployment pipeline**
- Push to main = infrastructure deployed
- PR comments show plan preview
- Deployment summaries generated
- No manual approval needed (configurable)

## 🏆 What Makes This Special

### Traditional Infrastructure
```bash
❌ Read 50+ pages of documentation
❌ Install 10+ tools manually
❌ Configure AWS credentials, VPCs, subnets, etc.
❌ Write terraform configuration
❌ Debug networking issues
❌ Set up monitoring manually
❌ Configure backups manually
❌ Deploy via manual commands
❌ Monitor for failures manually
```

### This Project
```bash
✅ Run ONE command: bash auto-setup.sh
✅ Everything installs automatically
✅ Everything configures automatically
✅ Everything deploys automatically
✅ Everything monitors automatically
✅ Everything heals automatically
✅ Everything scales automatically
✅ Everything backs up automatically
🎉 Zero manual intervention!
```

## 🚀 Future Enhancements (Already Automated!)

These will deploy automatically when you push to main:

- [ ] Multi-region deployment (add regions to terraform.tfvars)
- [ ] Blue-green deployments (already supported via Terraform)
- [ ] Automatic SSL certificates (uncomment ACM resources)
- [ ] Database backups (add RDS resources)
- [ ] Log aggregation (uncomment CloudWatch Logs)
- [ ] Cost optimization (Spot instances configuration ready)
- [ ] Disaster recovery (multi-AZ already configured)

**All infrastructure changes auto-deploy via GitHub Actions!**

## 📞 Support

**Issues?** Create a GitHub issue with:
- What command you ran
- Error message (full output)
- Your OS (Mac/Linux)
- AWS region

**Questions?** Check:
1. This README (comprehensive guide)
2. Terraform outputs: `cd terraform && terraform output`
3. GitHub Actions logs: Repository → Actions tab
4. CloudWatch logs: AWS Console → CloudWatch

## 📄 License

This project is for infrastructure automation demonstration.

## ✨ Credits

Built with:
- **Terraform** - Infrastructure as Code
- **AWS** - Cloud provider
- **GitHub Actions** - CI/CD automation
- **Docker** - Containerization
- **SonarQube** - Code quality analysis

---

**🤖 Remember: Everything is automated. Just push to main and let the magic happen!**

```bash
git add .
git commit -m "feat: ✨ Complete automation setup"
git push origin main
# ✅ Infrastructure deploys automatically
# ✅ Monitoring enabled
# ✅ Auto-healing active
# ✅ Backups scheduled
# 🎉 All done!
```
