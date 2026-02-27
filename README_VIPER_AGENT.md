# VIPER Onboarding AI Agent

🤖 **Automated AI agent for onboarding new repositories to the VIPER CI/CD platform**

## Overview

This AI agent automates the VIPER onboarding process by:
- 🔍 Analyzing repository structure and detecting technology stack
- 📝 Generating `viper_config.yaml` configuration
- 🔄 Creating GitHub Actions workflows
- ✅ Validating configurations
- 📊 Providing detailed reports and recommendations

## Features

### Core Capabilities
- **Multi-language Support**: Java, Node.js, Python, .NET, Go, Terraform
- **Smart Detection**: Automatically identifies build tools, test frameworks, and database migrations
- **Test Mode**: Safe testing environment without modifying actual files
- **Validation**: Checks existing VIPER configurations for issues
- **Recommendations**: Provides actionable suggestions for improvement

### Supported Technologies
| Language | Build Tools | Test Frameworks | Container |
|----------|-------------|-----------------|-----------|
| Java | Maven, Gradle | JUnit, TestNG | ✅ |
| Node.js | npm, yarn | Jest, Mocha | ✅ |
| Python | pip, poetry | pytest, unittest | ✅ |
| .NET | dotnet | xUnit, NUnit | ✅ |
| Go | go modules | go test | ✅ |

## Installation

### Prerequisites
- Python 3.8 or higher
- pip package manager

### Install Dependencies
```bash
pip install -r requirements.txt
```

### Verify Installation
```bash
python viper_onboarding_agent.py --help
```

## Usage

### Command Structure
```bash
python viper_onboarding_agent.py <command> <path> [options]
```

### Commands

#### 1. **Analyze** - Repository Analysis Only
Analyzes a repository without making any changes.

```bash
# Analyze in test mode
python viper_onboarding_agent.py analyze /path/to/repo --test-mode

# Example output:
# ✓ Detected language: java
# ✓ Detected build tool: maven
# ✓ Dockerfile: Found
# ✓ Tests: Found
# ⚠️ Recommendations: Add database migration tool
```

#### 2. **Generate** - Create VIPER Configuration Files
Generates `viper_config.yaml` and GitHub Actions workflow.

```bash
# Generate files in test mode (preview only)
python viper_onboarding_agent.py generate /path/to/repo --test-mode

# Generate files for real
python viper_onboarding_agent.py generate /path/to/repo --output ./config

# Generate with custom service name
python viper_onboarding_agent.py generate /path/to/repo \
  --service-name my-awesome-service \
  --output ./config
```

#### 3. **Validate** - Check Existing Configuration
Validates an existing `viper_config.yaml` file.

```bash
# Validate configuration
python viper_onboarding_agent.py validate /path/to/repo/viper_config.yaml

# Example output:
# ✅ VALIDATION PASSED
# or
# ❌ VALIDATION FAILED
#   - Missing required field: security
#   - Unsupported version: 1.0.0
```

#### 4. **Onboard** - Complete Onboarding Process
Runs full analysis, generates files, and creates detailed report.

```bash
# Full onboarding in test mode
python viper_onboarding_agent.py onboard /path/to/repo \
  --test-mode \
  --report onboarding-report.md

# Real onboarding with custom service name
python viper_onboarding_agent.py onboard /path/to/repo \
  --service-name payment-service \
  --output /path/to/repo \
  --report report.md
```

### Options

| Option | Short | Description |
|--------|-------|-------------|
| `--test-mode` | `-t` | Run without writing files (safe testing) |
| `--output` | `-o` | Output directory for generated files |
| `--service-name` | `-s` | Override service name |
| `--report` | `-r` | Save report to specified file |

## Real-World Examples

### Example 1: New Java Microservice
```bash
# Navigate to your Java service
cd /projects/payment-service

# Analyze first (test mode)
python ../viper_onboarding_agent.py analyze . --test-mode

# Generate VIPER configuration
python ../viper_onboarding_agent.py generate . --service-name payment-service

# Commit files
git add viper_config.yaml .github/workflows/viper.yml
git commit -m "Add VIPER configuration"
git push
```

### Example 2: Existing Node.js API
```bash
# Full onboarding with report
python viper_onboarding_agent.py onboard /projects/user-api \
  --service-name user-api \
  --report user-api-onboarding.md

# Review report
cat user-api-onboarding.md

# If satisfied, commit changes
cd /projects/user-api
git add viper_config.yaml .github/workflows/viper.yml
git commit -m "Onboard to VIPER platform"
```

### Example 3: Validate After Manual Changes
```bash
# After manually editing configuration
python viper_onboarding_agent.py validate ./viper_config.yaml

# Fix issues if validation fails
# Then validate again
```

## Generated Files

### 1. `viper_config.yaml`
Complete VIPER configuration with:
- Service metadata (name, language, type)
- Build configuration (tool, version, commands)
- Test configuration (framework, coverage)
- Security settings (Snyk, SonarQube)
- Deployment settings (K8s, environments)
- Monitoring setup (Datadog)

**Example:**
```yaml
version: '2.5.0'
service:
  name: payment-service
  language: java
  type: microservice
build:
  tool: maven
  jdk_version: '17'
  artifact_type: jar
test:
  enabled: true
  command: mvn test
  coverage:
    enabled: true
    tool: jacoco
    threshold: 80
security:
  snyk:
    enabled: true
    fail_on_critical: true
  sonarqube:
    enabled: true
    quality_gate: true
deployment:
  container:
    enabled: true
    registry: artifactory.vertex.com
  kubernetes:
    enabled: true
    namespace: default
    replicas: 2
  environments:
    - dev
    - qa
    - stage
    - prod
monitoring:
  datadog:
    enabled: true
```

### 2. `.github/workflows/viper.yml`
GitHub Actions workflow that triggers VIPER pipeline.

**Example:**
```yaml
name: VIPER CI/CD
on:
  pull_request:
    branches:
      - main
      - develop
  push:
    branches:
      - main
      - develop
jobs:
  viper-ci:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v3
      - name: Run VIPER Pipeline
        uses: vertex/viper-action@v2.5.0
        with:
          config-path: viper_config.yaml
        env:
          SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
```

## Test Environment Setup

### Creating Test Repository
```bash
# Create test directory structure
mkdir -p test-repos/java-service/src/{main,test}/java
mkdir -p test-repos/node-service/{src,test}
mkdir -p test-repos/python-service/{src,tests}

# Add sample files
echo 'pom.xml' > test-repos/java-service/pom.xml
echo 'Dockerfile' > test-repos/java-service/Dockerfile

# Test onboarding
python viper_onboarding_agent.py onboard test-repos/java-service \
  --test-mode \
  --report test-report.md
```

### Running Multiple Test Cases
```bash
#!/bin/bash
# test_all_languages.sh

for repo in test-repos/*; do
  echo "Testing: $repo"
  python viper_onboarding_agent.py analyze "$repo" --test-mode
  echo "---"
done
```

## Agent Capabilities Matrix

| Capability | Status | Description |
|------------|--------|-------------|
| Language Detection | ✅ | Detects 6 major languages |
| Build Tool Detection | ✅ | Maven, Gradle, npm, pip, etc. |
| Test Detection | ✅ | Identifies test directories and frameworks |
| Docker Support | ✅ | Checks for Dockerfile |
| DB Migrations | ✅ | Detects Flyway, Liquibase |
| Config Generation | ✅ | Creates viper_config.yaml |
| Workflow Generation | ✅ | Creates GitHub Actions workflow |
| Validation | ✅ | Validates existing configs |
| Test Mode | ✅ | Safe preview without changes |
| Recommendations | ✅ | Actionable improvement suggestions |

## Roadmap / Future Enhancements

### Planned Features
- [ ] **AI-Powered Optimization**: Use LLM to suggest optimal configurations
- [ ] **GitLab Support**: Generate GitLab CI/CD pipelines
- [ ] **Multi-repo Onboarding**: Batch process multiple repositories
- [ ] **Configuration Templates**: Pre-built templates for common patterns
- [ ] **Integration Testing**: Automatic dry-run of generated configs
- [ ] **AWS Integration**: Auto-provision test environments
- [ ] **Terraform Generation**: Create infrastructure as code
- [ ] **Monitoring Setup**: Auto-configure Datadog dashboards
- [ ] **Rollback Support**: Keep config history and rollback
- [ ] **Interactive Mode**: Guided CLI prompts for customization

### Under Development
- **Enhanced Language Support**: Rust, Ruby, PHP
- **Advanced Detection**: Microservice architecture patterns
- **Cost Estimation**: Predict cloud costs for deployment
- **Security Scanning**: Pre-validate against security policies

## Troubleshooting

### Common Issues

**Issue: "Repository path not found"**
```bash
# Solution: Use absolute path
python viper_onboarding_agent.py analyze $(pwd)/my-repo
```

**Issue: "Could not detect language"**
```bash
# Solution: Add language indicator file
touch pom.xml  # for Java
touch package.json  # for Node.js
```

**Issue: "Validation failed: Missing required field"**
```bash
# Solution: Regenerate configuration
python viper_onboarding_agent.py generate . --output .
```

## Integration with Existing Workflows

### CI/CD Pipeline Integration
```yaml
# .github/workflows/viper-onboarding.yml
name: Auto VIPER Onboarding
on:
  workflow_dispatch:
jobs:
  onboard:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Setup Python
        uses: actions/setup-python@v4
        with:
          python-version: '3.11'
      - name: Install Agent
        run: pip install pyyaml
      - name: Run Onboarding
        run: |
          python viper_onboarding_agent.py onboard . \
            --test-mode \
            --report onboarding-report.md
      - name: Upload Report
        uses: actions/upload-artifact@v3
        with:
          name: onboarding-report
          path: onboarding-report.md
```

### Slack Notification Integration
```python
# Add Slack webhook notification
import requests

def notify_slack(report):
    webhook_url = os.environ.get('SLACK_WEBHOOK')
    requests.post(webhook_url, json={'text': report})
```

## Best Practices

### 1. Always Test First
```bash
# ALWAYS start with test mode
python viper_onboarding_agent.py onboard . --test-mode --report preview.md

# Review the preview report
cat preview.md

# If satisfied, run for real
python viper_onboarding_agent.py onboard . --report final.md
```

### 2. Version Control
```bash
# Create feature branch for VIPER onboarding
git checkout -b feature/viper-onboarding

# Generate configuration
python viper_onboarding_agent.py generate .

# Commit and create PR
git add viper_config.yaml .github/workflows/viper.yml
git commit -m "Add VIPER configuration"
git push origin feature/viper-onboarding
```

### 3. Incremental Onboarding
```bash
# Start with CI only (testing and security)
# Manually edit viper_config.yaml:
# deployment.kubernetes.enabled: false

# Create PR to test CI pipeline
# Once CI is stable, enable CD
```

## Support

### Resources
- **VIPER Documentation**: https://viper.vertex.com/docs
- **Confluence**: https://confluence.vertex.com/viper
- **Slack Channel**: #viper-support

### Getting Help
```bash
# Show all commands
python viper_onboarding_agent.py --help

# Report issues
# Include agent logs from the output
```

## License

Internal Vertex Inc. tool - Not for external distribution

## Contributors

- VIPER DevOps Team
- Automation Engineering Team

---

**Version**: 1.0.0  
**Last Updated**: February 27, 2026  
**Status**: Production Ready ✅
