#!/bin/bash
# Automated Terraform Setup Script
# No manual input required - everything auto-configures

set -e

echo "🚀 Starting automated infrastructure setup..."

# Detect OS
OS="$(uname -s)"
case "${OS}" in
    Linux*)     MACHINE=Linux;;
    Darwin*)    MACHINE=Mac;;
    *)          MACHINE="UNKNOWN:${OS}"
esac

echo "✓ Detected OS: $MACHINE"

# Install Terraform if not present
if ! command -v terraform &> /dev/null; then
    echo "📦 Installing Terraform..."
    if [ "$MACHINE" = "Mac" ]; then
        brew tap hashicorp/tap
        brew install hashicorp/tap/terraform
    elif [ "$MACHINE" = "Linux" ]; then
        sudo yum install -y yum-utils
        sudo yum-config-manager --add-repo https://rpm.releases.hashicorp.com/AmazonLinux/hashicorp.repo
        sudo yum -y install terraform
    fi
    echo "✓ Terraform installed"
else
    echo "✓ Terraform already installed: $(terraform version | head -1)"
fi

# Install AWS CLI if not present
if ! command -v aws &> /dev/null; then
    echo "📦 Installing AWS CLI..."
    if [ "$MACHINE" = "Mac" ]; then
        curl "https://awscli.amazonaws.com/AWSCLIV2.pkg" -o "AWSCLIV2.pkg"
        sudo installer -pkg AWSCLIV2.pkg -target /
        rm AWSCLIV2.pkg
    elif [ "$MACHINE" = "Linux" ]; then
        curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
        unzip awscliv2.zip
        sudo ./aws/install
        rm -rf aws awscliv2.zip
    fi
    echo "✓ AWS CLI installed"
else
    echo "✓ AWS CLI already installed: $(aws --version)"
fi

# Check for AWS credentials
if [ -z "$AWS_ACCESS_KEY_ID" ] && [ ! -f ~/.aws/credentials ]; then
    echo "⚠️  AWS credentials not found"
    echo "💡 Please run: aws configure"
    echo "   Or set AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY environment variables"
    exit 1
fi

echo "✓ AWS credentials configured"

# Auto-detect GitHub token
if [ -z "$GITHUB_TOKEN" ]; then
    if [ -f ~/.github_token ]; then
        export GITHUB_TOKEN=$(cat ~/.github_token)
        echo "✓ GitHub token loaded from ~/.github_token"
    else
        echo "⚠️  GITHUB_TOKEN not set"
        echo "💡 Set it with: export GITHUB_TOKEN=your_token"
        echo "   Or save to ~/.github_token"
        echo "   Continuing without GitHub provider..."
        export TF_VAR_github_token="placeholder"
    fi
else
    echo "✓ GitHub token found in environment"
fi

# Navigate to terraform directory
cd "$(dirname "$0")"

echo ""
echo "🏗️  Initializing Terraform..."
terraform init

echo ""
echo "📋 Validating configuration..."
terraform validate

echo ""
echo "📊 Planning infrastructure..."
terraform plan -out=tfplan

echo ""
echo "🎯 Ready to deploy!"
echo ""
read -p "Deploy infrastructure now? (yes/no): " DEPLOY

if [ "$DEPLOY" = "yes" ]; then
    echo ""
    echo "🚀 Deploying infrastructure..."
    terraform apply tfplan
    
    echo ""
    echo "✅ Deployment complete!"
    echo ""
    echo "📊 Infrastructure outputs:"
    terraform output
    
    echo ""
    echo "🔧 Next steps:"
    echo "1. SonarQube will be available in ~5 minutes"
    echo "2. Access URL: $(terraform output -raw sonarqube_url 2>/dev/null || echo 'See outputs above')"
    echo "3. Default credentials: admin / admin"
    echo "4. Configure GitHub Actions secrets with AWS credentials"
    echo "5. Auto-healing and monitoring are enabled"
else
    echo "❌ Deployment cancelled"
    rm -f tfplan
fi
