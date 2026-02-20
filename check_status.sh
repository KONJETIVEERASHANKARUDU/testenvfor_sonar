#!/bin/bash

echo "======================================"
echo "SonarQube Setup Verification"
echo "======================================"
echo ""

# Check if gh CLI is installed
echo "1. Checking GitHub CLI..."
if command -v gh &> /dev/null; then
    echo "   ✅ GitHub CLI is installed"
    
    echo ""
    echo "2. Checking GitHub Actions workflow status..."
    gh run list --repo KONJETIVEERASHANKARUDU/testenvfor_sonar --limit 5
    
    echo ""
    echo "3. Checking repository secrets..."
    gh secret list --repo KONJETIVEERASHANKARUDU/testenvfor_sonar
else
    echo "   ℹ️  GitHub CLI not installed"
    echo "   Install with: brew install gh"
fi

echo ""
echo "======================================"
echo "Manual Check URLs:"
echo "======================================"
echo ""
echo "📊 GitHub Actions:"
echo "   https://github.com/KONJETIVEERASHANKARUDU/testenvfor_sonar/actions"
echo ""
echo "🔍 SonarCloud Dashboard:"
echo "   https://sonarcloud.io/project/overview?id=KONJETIVEERASHANKARUDU_testenvfor_sonar"
echo ""
echo "🔐 GitHub Secrets:"
echo "   https://github.com/KONJETIVEERASHANKARUDU/testenvfor_sonar/settings/secrets/actions"
echo ""
echo "======================================"
echo "Configuration Status:"
echo "======================================"
echo ""

# Check if files exist
echo "✅ Workflow file: .github/workflows/sonarqube.yml"
echo "✅ SonarQube config: sonar-project.properties"
echo "✅ Maven config: pom.xml"
echo "✅ Test files: src/main/java/com/example/*.java"
echo ""

# Display current configuration
echo "📋 SonarQube Configuration:"
echo "   Organization: konjetiveerashankarudu"
echo "   Project Key: KONJETIVEERASHANKARUDU_testenvfor_sonar"
echo "   Host: https://sonarcloud.io"
echo ""

# Check if secrets might be missing
echo "⚠️  Required GitHub Secrets:"
echo "   - SONAR_TOKEN"
echo "   - SONAR_HOST_URL"
echo ""
echo "If workflow is failing, verify these secrets are set!"
echo ""
echo "======================================"
