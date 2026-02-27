# Terraform Infrastructure Configuration
# Automated AWS infrastructure for CI/CD pipeline

terraform {
  required_version = ">= 1.0"
  
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
    github = {
      source  = "integrations/github"
      version = "~> 5.0"
    }
  }
  
  # Remote state storage (uncomment after creating S3 bucket)
  # backend "s3" {
  #   bucket         = "testenvfor-sonar-terraform-state"
  #   key            = "infrastructure/terraform.tfstate"
  #   region         = "us-east-1"
  #   dynamodb_table = "terraform-lock-table"
  #   encrypt        = true
  # }
}

provider "aws" {
  region = var.aws_region
  
  default_tags {
    tags = {
      Project     = "testenvfor-sonar"
      Environment = var.environment
      ManagedBy   = "Terraform"
      Owner       = "KONJETIVEERASHANKARUDU"
      AutoHealing = "enabled"
    }
  }
}

provider "github" {
  owner = var.github_owner
  token = var.github_token
}
