# Terraform Outputs - Auto-generated resource information

output "vpc_id" {
  description = "VPC ID"
  value       = aws_vpc.main.id
}

output "public_subnet_ids" {
  description = "Public subnet IDs"
  value       = aws_subnet.public[*].id
}

output "private_subnet_ids" {
  description = "Private subnet IDs"
  value       = aws_subnet.private[*].id
}

output "sonarqube_security_group_id" {
  description = "SonarQube security group ID"
  value       = aws_security_group.sonarqube.id
}

output "load_balancer_dns" {
  description = "Load Balancer DNS name"
  value       = var.auto_scaling_enabled ? aws_lb.sonarqube[0].dns_name : "Auto-scaling disabled"
}

output "sonarqube_url" {
  description = "SonarQube URL"
  value       = var.auto_scaling_enabled ? "http://${aws_lb.sonarqube[0].dns_name}" : "Configure manually"
}

output "backup_bucket" {
  description = "S3 backup bucket name"
  value       = aws_s3_bucket.backups.id
}

output "terraform_state_bucket" {
  description = "Terraform state bucket name"
  value       = aws_s3_bucket.terraform_state.id
}

output "dynamodb_lock_table" {
  description = "DynamoDB lock table name"
  value       = aws_dynamodb_table.terraform_locks.name
}

output "autoscaling_group_name" {
  description = "Auto Scaling Group name"
  value       = var.auto_scaling_enabled ? aws_autoscaling_group.sonarqube[0].name : "Auto-scaling disabled"
}

output "iam_role_arn" {
  description = "IAM role ARN for SonarQube instance"
  value       = aws_iam_role.sonarqube_instance.arn
}

output "setup_complete" {
  description = "Setup status"
  value       = "✅ Infrastructure deployed successfully! SonarQube will be available in ~5 minutes."
}
