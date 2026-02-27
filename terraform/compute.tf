# Compute Resources - EC2 for SonarQube

# AMI Data Source - Latest Amazon Linux 2023
data "aws_ami" "amazon_linux" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-*-x86_64"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

# IAM Role for EC2
resource "aws_iam_role" "sonarqube_instance" {
  name = "${var.project_name}-sonarqube-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })

  tags = {
    Name = "${var.project_name}-sonarqube-role"
  }
}

# IAM Policy for CloudWatch, S3, and SSM
resource "aws_iam_role_policy" "sonarqube_policy" {
  name = "${var.project_name}-sonarqube-policy"
  role = aws_iam_role.sonarqube_instance.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "cloudwatch:PutMetricData",
          "cloudwatch:GetMetricStatistics",
          "cloudwatch:ListMetrics",
          "logs:CreateLogGroup",
          "logs:CreateLogStream",
          "logs:PutLogEvents",
          "logs:DescribeLogStreams"
        ]
        Resource = "*"
      },
      {
        Effect = "Allow"
        Action = [
          "s3:GetObject",
          "s3:PutObject",
          "s3:ListBucket"
        ]
        Resource = [
          aws_s3_bucket.backups.arn,
          "${aws_s3_bucket.backups.arn}/*"
        ]
      },
      {
        Effect = "Allow"
        Action = [
          "ssm:GetParameter",
          "ssm:GetParameters",
          "ssm:GetParametersByPath"
        ]
        Resource = "*"
      }
    ]
  })
}

# IAM Instance Profile
resource "aws_iam_instance_profile" "sonarqube" {
  name = "${var.project_name}-sonarqube-profile"
  role = aws_iam_role.sonarqube_instance.name
}

# Launch Template for Auto Scaling
resource "aws_launch_template" "sonarqube" {
  count = var.auto_scaling_enabled ? 1 : 0

  name_prefix   = "${var.project_name}-sonarqube-"
  image_id      = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type

  iam_instance_profile {
    name = aws_iam_instance_profile.sonarqube.name
  }

  vpc_security_group_ids = [aws_security_group.sonarqube.id]

  user_data = base64encode(templatefile("${path.module}/user_data.sh", {
    project_name    = var.project_name
    backup_bucket   = aws_s3_bucket.backups.id
    cloudwatch_enabled = var.monitoring_enabled
  }))

  monitoring {
    enabled = var.monitoring_enabled
  }

  tag_specifications {
    resource_type = "instance"
    tags = {
      Name        = "${var.project_name}-sonarqube"
      AutoHealing = "enabled"
    }
  }

  tag_specifications {
    resource_type = "volume"
    tags = {
      Name = "${var.project_name}-sonarqube-vol"
    }
  }
}

# Auto Scaling Group
resource "aws_autoscaling_group" "sonarqube" {
  count = var.auto_scaling_enabled ? 1 : 0

  name                = "${var.project_name}-sonarqube-asg"
  vpc_zone_identifier = aws_subnet.public[*].id
  target_group_arns   = [aws_lb_target_group.sonarqube[0].arn]
  health_check_type   = "ELB"
  health_check_grace_period = 300

  min_size         = 1
  max_size         = 3
  desired_capacity = 1

  launch_template {
    id      = aws_launch_template.sonarqube[0].id
    version = "$Latest"
  }

  tag {
    key                 = "Name"
    value               = "${var.project_name}-sonarqube"
    propagate_at_launch = true
  }

  tag {
    key                 = "AutoHealing"
    value               = "enabled"
    propagate_at_launch = true
  }
}

# Auto Scaling Policy - CPU Based
resource "aws_autoscaling_policy" "cpu_scaling" {
  count = var.auto_scaling_enabled ? 1 : 0

  name                   = "${var.project_name}-cpu-scaling"
  scaling_adjustment     = 1
  adjustment_type        = "ChangeInCapacity"
  cooldown               = 300
  autoscaling_group_name = aws_autoscaling_group.sonarqube[0].name
}

# CloudWatch Alarm - High CPU
resource "aws_cloudwatch_metric_alarm" "high_cpu" {
  count = var.auto_scaling_enabled && var.monitoring_enabled ? 1 : 0

  alarm_name          = "${var.project_name}-high-cpu"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = "2"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/EC2"
  period              = "120"
  statistic           = "Average"
  threshold           = "75"
  alarm_description   = "This metric monitors ec2 cpu utilization"
  alarm_actions       = [aws_autoscaling_policy.cpu_scaling[0].arn]

  dimensions = {
    AutoScalingGroupName = aws_autoscaling_group.sonarqube[0].name
  }
}
