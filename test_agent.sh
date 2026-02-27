#!/bin/bash
# Test script for VIPER Onboarding Agent
# Creates sample repositories and tests the agent

set -e

echo "🧪 VIPER Onboarding Agent - Test Suite"
echo "========================================"
echo ""

# Create test directory
TEST_DIR="test_repos"
rm -rf "$TEST_DIR"
mkdir -p "$TEST_DIR"

echo "📁 Creating test repositories..."
echo ""

# Test 1: Java Maven Service
echo "1️⃣  Creating Java Maven service..."
mkdir -p "$TEST_DIR/java-maven-service/src/main/java/com/vertex/payment"
mkdir -p "$TEST_DIR/java-maven-service/src/test/java/com/vertex/payment"
mkdir -p "$TEST_DIR/java-maven-service/db/migration"

cat > "$TEST_DIR/java-maven-service/pom.xml" << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.vertex</groupId>
    <artifactId>payment-service</artifactId>
    <version>1.0.0</version>
    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
EOF

cat > "$TEST_DIR/java-maven-service/Dockerfile" << 'EOF'
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF

echo "   ✅ Java Maven service created"

# Test 2: Node.js Express API
echo "2️⃣  Creating Node.js Express API..."
mkdir -p "$TEST_DIR/node-express-api/src"
mkdir -p "$TEST_DIR/node-express-api/test"

cat > "$TEST_DIR/node-express-api/package.json" << 'EOF'
{
  "name": "user-api",
  "version": "1.0.0",
  "description": "User management API",
  "main": "src/index.js",
  "scripts": {
    "start": "node src/index.js",
    "test": "jest",
    "build": "webpack"
  },
  "dependencies": {
    "express": "^4.18.0"
  },
  "devDependencies": {
    "jest": "^29.0.0"
  }
}
EOF

cat > "$TEST_DIR/node-express-api/Dockerfile" << 'EOF'
FROM node:18-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 3000
CMD ["npm", "start"]
EOF

echo "   ✅ Node.js Express API created"

# Test 3: Python Flask API
echo "3️⃣  Creating Python Flask API..."
mkdir -p "$TEST_DIR/python-flask-api/src"
mkdir -p "$TEST_DIR/python-flask-api/tests"

cat > "$TEST_DIR/python-flask-api/requirements.txt" << 'EOF'
Flask==2.3.0
pytest==7.4.0
pytest-cov==4.1.0
requests==2.31.0
EOF

cat > "$TEST_DIR/python-flask-api/Dockerfile" << 'EOF'
FROM python:3.11-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt
COPY . .
EXPOSE 5000
CMD ["python", "src/app.py"]
EOF

echo "   ✅ Python Flask API created"

# Test 4: Repository without Dockerfile (should get recommendation)
echo "4️⃣  Creating service without Dockerfile..."
mkdir -p "$TEST_DIR/dotnet-api"

cat > "$TEST_DIR/dotnet-api/MyApi.csproj" << 'EOF'
<Project Sdk="Microsoft.NET.Sdk.Web">
  <PropertyGroup>
    <TargetFramework>net8.0</TargetFramework>
  </PropertyGroup>
</Project>
EOF

echo "   ✅ .NET API without Dockerfile created"

echo ""
echo "========================================" 
echo "🤖 Running Agent Tests..."
echo "========================================"
echo ""

# Test each repository
for repo in "$TEST_DIR"/*; do
    repo_name=$(basename "$repo")
    echo ""
    echo "Testing: $repo_name"
    echo "----------------------------------------"
    
    # Run analysis
    python3 viper_onboarding_agent.py analyze "$repo" --test-mode
    
    echo ""
    echo "Generating configuration..."
    python3 viper_onboarding_agent.py generate "$repo" --test-mode --output "$repo"
    
    echo ""
    echo "✅ $repo_name test complete"
    echo ""
done

# Full onboarding test with report
echo ""
echo "========================================" 
echo "📊 Full Onboarding Test with Report"
echo "========================================"
echo ""

python3 viper_onboarding_agent.py onboard "$TEST_DIR/java-maven-service" \
    --test-mode \
    --service-name payment-service \
    --report "test_onboarding_report.md"

echo ""
echo "✅ Report generated: test_onboarding_report.md"
echo ""

# Display report
echo "========================================" 
echo "📄 Onboarding Report Preview"
echo "========================================"
cat test_onboarding_report.md

echo ""
echo "========================================" 
echo "✅ All Tests Passed!"
echo "========================================"
echo ""
echo "Test repositories created in: $TEST_DIR"
echo "Test report saved to: test_onboarding_report.md"
echo ""
echo "Next steps:"
echo "  1. Review test_onboarding_report.md"
echo "  2. Try: python3 viper_onboarding_agent.py onboard <your-repo> --test-mode"
echo "  3. When ready: Remove --test-mode to generate actual files"
echo ""
