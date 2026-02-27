# Variables for Infrastructure Configuration

variable "aws_region" {
  description = "AWS region for resources"
  type        = string
  default     = "us-east-1"
}

variable "environment" {
  description = "Environment name"
  type        = string
  default     = "production"
}

variable "github_owner" {
  description = "GitHub repository owner"
  type        = string
  default     = "KONJETIVEERASHANKARUDU"
}

variable "github_token" {
  description = "GitHub personal access token"
  type        = string
  sensitive   = true
}

variable "project_name" {
  description = "Project name"
  type        = string
  default     = "testenvfor-sonar"
}

variable "sonarqube_enabled" {
  description = "Enable SonarQube infrastructure"
  type        = bool
  default     = true
}

variable "auto_scaling_enabled" {
  description = "Enable auto-scaling for services"
  type        = bool
  default     = true
}

variable "monitoring_enabled" {
  description = "Enable CloudWatch monitoring"
  type        = bool
  default     = true
}

variable "backup_retention_days" {
  description = "Number of days to retain backups"
  type        = number
  default     = 30
}

variable "alert_email" {
  description = "Email for infrastructure alerts"
  type        = string
  default     = ""
}

variable "instance_type" {
  description = "EC2 instance type for SonarQube"
  type        = string
  default     = "t3.medium"
}

variable "allowed_cidr_blocks" {
  description = "CIDR blocks allowed to access services"
  type        = list(string)
  default     = ["0.0.0.0/0"]
}
