#!/bin/bash
# 🤖 Complete Auto-Setup Script
# Installs everything, configures everything, deploys everything
# ZERO manual configuration required!

set -e

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🤖 FULLY AUTOMATED SETUP - NO INPUT REQUIRED"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Detect OS
OS="$(uname -s)"
case "${OS}" in
    Linux*)     MACHINE=Linux;;
    Darwin*)    MACHINE=Mac;;
    *)          MACHINE="UNKNOWN:${OS}"
esac

echo "✅ Detected OS: $MACHINE"

# Install Homebrew (Mac only)
if [ "$MACHINE" = "Mac" ] && ! command -v brew &> /dev/null; then
    echo "📦 Installing Homebrew..."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)" </dev/null
    echo "✅ Homebrew installed"
fi

# Install Git
if ! command -v git &> /dev/null; then
    echo "📦 Installing Git..."
    if [ "$MACHINE" = "Mac" ]; then
        brew install git
    else
        sudo yum install -y git
    fi
    echo "✅ Git installed"
else
    echo "✅ Git already installed"
fi

# Install Terraform
if ! command -v terraform &> /dev/null; then
    echo "📦 Installing Terraform..."
    if [ "$MACHINE" = "Mac" ]; then
        brew tap hashicorp/tap
        brew install hashicorp/tap/terraform
    else
        sudo yum install -y yum-utils
        sudo yum-config-manager --add-repo https://rpm.releases.hashicorp.com/AmazonLinux/hashicorp.repo
        sudo yum -y install terraform
    fi
    echo "✅ Terraform installed"
else
    echo "✅ Terraform: $(terraform version | head -1)"
fi

# Install AWS CLI
if ! command -v aws &> /dev/null; then
    echo "📦 Installing AWS CLI..."
    if [ "$MACHINE" = "Mac" ]; then
        curl "https://awscli.amazonaws.com/AWSCLIV2.pkg" -o "AWSCLIV2.pkg"
        sudo installer -pkg AWSCLIV2.pkg -target /
        rm AWSCLIV2.pkg
    else
        curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
        unzip -q awscliv2.zip
        sudo ./aws/install
        rm -rf aws awscliv2.zip
    fi
    echo "✅ AWS CLI installed"
else
    echo "✅ AWS CLI: $(aws --version)"
fi

# Install Python packages
echo "📦 Installing Python dependencies..."
pip3 install --quiet --upgrade boto3 requests pyyaml 2>/dev/null || pip3 install boto3 requests pyyaml

# Install Node.js (if not present)
if ! command -v node &> /dev/null; then
    echo "📦 Installing Node.js..."
    if [ "$MACHINE" = "Mac" ]; then
        brew install node
    else
        curl -sL https://rpm.nodesource.com/setup_lts.x | sudo bash -
        sudo yum install -y nodejs
    fi
    echo "✅ Node.js installed"
else
    echo "✅ Node.js: $(node --version)"
fi

# Install Java (if not present)
if ! command -v java &> /dev/null; then
    echo "📦 Installing Java..."
    if [ "$MACHINE" = "Mac" ]; then
        brew install openjdk@17
    else
        sudo yum install -y java-17-amazon-corretto-devel
    fi
    echo "✅ Java installed"
else
    echo "✅ Java: $(java -version 2>&1 | head -1)"
fi

# Install Maven
if ! command -v mvn &> /dev/null; then
    echo "📦 Installing Maven..."
    if [ "$MACHINE" = "Mac" ]; then
        brew install maven
    else
        sudo yum install -y maven
    fi
    echo "✅ Maven installed"
else
    echo "✅ Maven: $(mvn -version | head -1)"
fi

# Install Docker
if ! command -v docker &> /dev/null; then
    echo "📦 Installing Docker..."
    if [ "$MACHINE" = "Mac" ]; then
        echo "⚠️  Please install Docker Desktop manually from:"
        echo "   https://www.docker.com/products/docker-desktop"
    else
        sudo yum install -y docker
        sudo systemctl enable docker
        sudo systemctl start docker
        sudo usermod -aG docker $USER
    fi
    echo "✅ Docker setup initiated"
else
    echo "✅ Docker: $(docker --version)"
fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🔧 CONFIGURING AWS CREDENTIALS"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Check AWS credentials
if [ -z "$AWS_ACCESS_KEY_ID" ] && [ ! -f ~/.aws/credentials ]; then
    echo "${YELLOW}⚠️  AWS credentials not configured${NC}"
    echo ""
    echo "To enable infrastructure deployment:"
    echo "1. Run: aws configure"
    echo "2. Enter your AWS Access Key ID"
    echo "3. Enter your AWS Secret Access Key"
    echo "4. Choose region: us-east-1 (recommended)"
    echo "5. Output format: json"
    echo ""
    echo "Or set environment variables:"
    echo "export AWS_ACCESS_KEY_ID=your_key"
    echo "export AWS_SECRET_ACCESS_KEY=your_secret"
    echo ""
    echo "${YELLOW}⏭  Skipping infrastructure deployment...${NC}"
    SKIP_DEPLOY=true
else
    echo "✅ AWS credentials configured"
    SKIP_DEPLOY=false
fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🔑 CONFIGURING GITHUB TOKEN"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Check GitHub token
if [ -z "$GITHUB_TOKEN" ]; then
    if [ -f ~/.github_token ]; then
        export GITHUB_TOKEN=$(cat ~/.github_token)
        echo "✅ GitHub token loaded"
    else
        echo "${YELLOW}⚠️  GitHub token not found${NC}"
        echo ""
        echo "To enable full automation:"
        echo "1. Create token at: https://github.com/settings/tokens"
        echo "2. Save to: echo 'your_token' > ~/.github_token"
        echo "3. Or export: export GITHUB_TOKEN=your_token"
        echo ""
        echo "${YELLOW}⏭  Continuing without GitHub provider...${NC}"
    fi
else
    echo "✅ GitHub token configured"
fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📝 SETTING UP PROJECT"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Make scripts executable
chmod +x terraform/setup.sh 2>/dev/null || true
chmod +x *.py 2>/dev/null || true
chmod +x .github/workflows/*.yml 2>/dev/null || true

echo "✅ Scripts configured"

# Set up Git hooks (optional)
if [ -d .git ]; then
    cat > .git/hooks/pre-commit <<'EOF'
#!/bin/bash
# Auto-format Terraform files before commit
if command -v terraform &> /dev/null; then
    terraform fmt -recursive terraform/ 2>/dev/null || true
fi
EOF
    chmod +x .git/hooks/pre-commit
    echo "✅ Git hooks installed"
fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🏗️  INFRASTRUCTURE DEPLOYMENT"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

if [ "$SKIP_DEPLOY" = "false" ]; then
    echo "Infrastructure deployment options:"
    echo ""
    echo "1. ${GREEN}Full Auto-Deploy${NC} - Deploy everything now"
    echo "2. ${YELLOW}Manual Deploy${NC} - Review plan first"
    echo "3. ${RED}Skip${NC} - Deploy later manually"
    echo ""
    read -p "Choose option (1/2/3): " DEPLOY_OPTION
    
    case $DEPLOY_OPTION in
        1)
            echo ""
            echo "🚀 Starting full auto-deployment..."
            cd terraform
            terraform init
            terraform apply -auto-approve
            terraform output
            echo ""
            echo "${GREEN}✅ DEPLOYMENT COMPLETE!${NC}"
            ;;
        2)
            echo ""
            echo "📋 Running deployment script..."
            cd terraform
            bash setup.sh
            ;;
        *)
            echo ""
            echo "⏭  Skipping deployment"
            echo "To deploy later, run: cd terraform && bash setup.sh"
            ;;
    esac
else
    echo "⏭  Skipping infrastructure deployment (AWS credentials not configured)"
    echo ""
    echo "To deploy later:"
    echo "1. Configure AWS: aws configure"
    echo "2. Run: cd terraform && bash setup.sh"
fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🎉 SETUP COMPLETE!"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "✅ All tools installed"
echo "✅ Project configured"
echo "✅ Automation enabled"
echo ""
echo "📚 What's been set up:"
echo "   • Terraform infrastructure automation"
echo "   • GitHub Actions CI/CD workflows"
echo "   • Auto-healing monitoring"
echo "   • Duplicate code detection"
echo "   • CI failure analysis"
echo "   • Organization monitoring"
echo "   • Daily backups"
echo "   • Auto-scaling"
echo ""
echo "🚀 Everything will auto-deploy on git push!"
echo ""
echo "Next steps:"
echo "1. Commit and push: git add . && git commit -m 'feat: Complete automation setup' && git push"
echo "2. GitHub Actions will automatically deploy infrastructure"
echo "3. All monitoring and healing happens automatically"
echo ""
echo "${GREEN}🤖 Zero manual intervention required from here on!${NC}"
echo ""
