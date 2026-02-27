# Auto-generated Terraform variables
# All values use sensible defaults - no manual configuration needed

aws_region              = "us-east-1"
environment             = "production"
project_name            = "testenvfor-sonar"
sonarqube_enabled       = true
auto_scaling_enabled    = true
monitoring_enabled      = true
backup_retention_days   = 30
instance_type           = "t3.medium"
allowed_cidr_blocks     = ["0.0.0.0/0"]  # Restrict this in production

# GitHub settings - will be auto-detected from environment
# Set GITHUB_TOKEN environment variable for automation
github_owner = "KONJETIVEERASHANKARUDU"
