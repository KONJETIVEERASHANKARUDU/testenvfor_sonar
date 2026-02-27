#!/bin/bash

# ============================================================================
# SonarQube Java Requirements Checker
# ============================================================================
# This script checks your current Java setup and provides recommendations
# for upgrading to SonarQube 2026.1
#
# Usage: ./check_java_requirements.sh
# ============================================================================

set -e

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color
BOLD='\033[1m'

# Unicode symbols
CHECK_MARK="✅"
CROSS_MARK="❌"
WARNING="⚠️"
INFO="ℹ️"
ROCKET="🚀"

echo ""
echo "============================================================================"
echo -e "${BOLD}${CYAN}    SonarQube Java Requirements Checker${NC}"
echo "============================================================================"
echo ""

# ============================================================================
# Function: Check if command exists
# ============================================================================
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# ============================================================================
# Function: Get Java version number
# ============================================================================
get_java_version() {
    local java_cmd=$1
    if command_exists "$java_cmd"; then
        "$java_cmd" -version 2>&1 | grep -oP 'version "?\K[0-9]+' | head -1
    else
        echo "0"
    fi
}

# ============================================================================
# 1. Check System Java
# ============================================================================
echo -e "${BOLD}${BLUE}[1] Checking System Java Installation${NC}"
echo "============================================================================"

SYSTEM_JAVA_VERSION=$(get_java_version java)

if [ "$SYSTEM_JAVA_VERSION" -eq 0 ]; then
    echo -e "${CROSS_MARK} ${RED}Java not found in PATH${NC}"
    SYSTEM_JAVA_OK=false
else
    echo -e "${CHECK_MARK} ${GREEN}Java found: Version $SYSTEM_JAVA_VERSION${NC}"
    java -version 2>&1 | head -3 | sed 's/^/    /'
    
    if [ "$SYSTEM_JAVA_VERSION" -ge 21 ]; then
        echo -e "${CHECK_MARK} ${GREEN}Java 21+ is installed - Ready for SonarQube 2026.1!${NC}"
        SYSTEM_JAVA_OK=true
    elif [ "$SYSTEM_JAVA_VERSION" -ge 17 ]; then
        echo -e "${WARNING} ${YELLOW}Java $SYSTEM_JAVA_VERSION is installed - NOT sufficient for SonarQube 2026.1${NC}"
        echo -e "    ${INFO} SonarQube 2026.1 requires Java 21+"
        SYSTEM_JAVA_OK=false
    else
        echo -e "${CROSS_MARK} ${RED}Java $SYSTEM_JAVA_VERSION is too old${NC}"
        SYSTEM_JAVA_OK=false
    fi
fi

echo ""

# ============================================================================
# 2. Check Maven Project Configuration
# ============================================================================
echo -e "${BOLD}${BLUE}[2] Checking Maven Project Configuration${NC}"
echo "============================================================================"

if [ -f "pom.xml" ]; then
    echo -e "${CHECK_MARK} ${GREEN}pom.xml found${NC}"
    
    # Extract Java version from pom.xml
    if command_exists xmllint; then
        MAVEN_SOURCE=$(xmllint --xpath "string(//*[local-name()='maven.compiler.source'])" pom.xml 2>/dev/null || echo "")
        MAVEN_TARGET=$(xmllint --xpath "string(//*[local-name()='maven.compiler.target'])" pom.xml 2>/dev/null || echo "")
    else
        # Fallback to grep if xmllint not available
        MAVEN_SOURCE=$(grep -oP '<maven\.compiler\.source>\K[^<]+' pom.xml 2>/dev/null | head -1 || echo "")
        MAVEN_TARGET=$(grep -oP '<maven\.compiler\.target>\K[^<]+' pom.xml 2>/dev/null | head -1 || echo "")
    fi
    
    if [ -n "$MAVEN_SOURCE" ] && [ -n "$MAVEN_TARGET" ]; then
        echo -e "    ${INFO} Compiler Source: ${CYAN}$MAVEN_SOURCE${NC}"
        echo -e "    ${INFO} Compiler Target: ${CYAN}$MAVEN_TARGET${NC}"
        
        echo ""
        echo -e "${BOLD}${GREEN}${CHECK_MARK} GOOD NEWS:${NC}"
        echo -e "    Your application code uses Java $MAVEN_SOURCE"
        echo -e "    ${CHECK_MARK} ${GREEN}NO CODE CHANGES NEEDED!${NC}"
        echo -e "    Your code can stay at Java $MAVEN_SOURCE even with SonarQube 2026.1"
        
        POM_VERSION=$MAVEN_SOURCE
    else
        echo -e "    ${WARNING} ${YELLOW}Could not detect Java version from pom.xml${NC}"
        POM_VERSION="unknown"
    fi
else
    echo -e "${WARNING} ${YELLOW}pom.xml not found${NC}"
    echo -e "    ${INFO} This script is designed for Maven projects"
    POM_VERSION="unknown"
fi

echo ""

# ============================================================================
# 3. Check CI/CD Configuration
# ============================================================================
echo -e "${BOLD}${BLUE}[3] Checking CI/CD Configuration${NC}"
echo "============================================================================"

CICD_FOUND=false

# Check GitHub Actions
if [ -f ".github/workflows/sonarqube.yml" ]; then
    echo -e "${CHECK_MARK} ${GREEN}GitHub Actions workflow found: .github/workflows/sonarqube.yml${NC}"
    CICD_FOUND=true
    
    # Check for Java version in workflow
    WORKFLOW_JAVA=$(grep -oP "java-version:\s*['\"]?\K[0-9]+" .github/workflows/sonarqube.yml 2>/dev/null | head -1 || echo "")
    
    if [ -n "$WORKFLOW_JAVA" ]; then
        echo -e "    ${INFO} Current Java version in workflow: ${CYAN}$WORKFLOW_JAVA${NC}"
        
        if [ "$WORKFLOW_JAVA" -ge 21 ]; then
            echo -e "    ${CHECK_MARK} ${GREEN}CI/CD already uses Java 21+ for SonarQube scanner${NC}"
            CICD_NEEDS_UPDATE=false
        else
            echo -e "    ${WARNING} ${YELLOW}CI/CD needs update to Java 21 for SonarQube scanner${NC}"
            CICD_NEEDS_UPDATE=true
        fi
    else
        echo -e "    ${WARNING} ${YELLOW}Could not detect Java version in workflow${NC}"
        CICD_NEEDS_UPDATE=true
    fi
elif [ -f ".github/workflows/build.yml" ]; then
    echo -e "${CHECK_MARK} ${GREEN}GitHub Actions workflow found: .github/workflows/build.yml${NC}"
    CICD_FOUND=true
    CICD_NEEDS_UPDATE=true
elif [ -f "azure-pipelines.yml" ]; then
    echo -e "${CHECK_MARK} ${GREEN}Azure Pipelines configuration found${NC}"
    CICD_FOUND=true
    CICD_NEEDS_UPDATE=true
elif [ -f ".gitlab-ci.yml" ]; then
    echo -e "${CHECK_MARK} ${GREEN}GitLab CI configuration found${NC}"
    CICD_FOUND=true
    CICD_NEEDS_UPDATE=true
elif [ -f "Jenkinsfile" ]; then
    echo -e "${CHECK_MARK} ${GREEN}Jenkins pipeline found${NC}"
    CICD_FOUND=true
    CICD_NEEDS_UPDATE=true
else
    echo -e "${WARNING} ${YELLOW}No CI/CD configuration found${NC}"
    echo -e "    ${INFO} Checked for: GitHub Actions, Azure Pipelines, GitLab CI, Jenkins"
fi

echo ""

# ============================================================================
# 4. Check SonarQube Configuration
# ============================================================================
echo -e "${BOLD}${BLUE}[4] Checking SonarQube Configuration${NC}"
echo "============================================================================"

if [ -f "sonar-project.properties" ]; then
    echo -e "${CHECK_MARK} ${GREEN}sonar-project.properties found${NC}"
    
    # Check Java source version
    SONAR_JAVA_SOURCE=$(grep -oP 'sonar\.java\.source=\K[0-9]+' sonar-project.properties 2>/dev/null || echo "")
    if [ -n "$SONAR_JAVA_SOURCE" ]; then
        echo -e "    ${INFO} SonarQube Java source: ${CYAN}$SONAR_JAVA_SOURCE${NC}"
    fi
    
    # Check for multi-language support
    SONAR_SOURCES=$(grep -oP 'sonar\.sources=\K.+' sonar-project.properties 2>/dev/null || echo "")
    if [ "$SONAR_SOURCES" = "." ]; then
        echo -e "    ${CHECK_MARK} ${GREEN}Multi-language support enabled${NC}"
    fi
else
    echo -e "${INFO} ${YELLOW}sonar-project.properties not found (optional)${NC}"
    echo -e "    ${INFO} Configuration can be provided via command-line or pom.xml"
fi

echo ""

# ============================================================================
# 5. Summary and Recommendations
# ============================================================================
echo ""
echo "============================================================================"
echo -e "${BOLD}${CYAN}    SUMMARY & RECOMMENDATIONS${NC}"
echo "============================================================================"
echo ""

# Determine overall status
NEEDS_ACTION=false

echo -e "${BOLD}Current Status:${NC}"
echo "─────────────────────────────────────"

# Application Code Status
if [ "$POM_VERSION" != "unknown" ]; then
    echo -e "${CHECK_MARK} Your application code: ${GREEN}Java $POM_VERSION${NC}"
    echo -e "    └─ ${GREEN}NO CODE CHANGES NEEDED!${NC}"
else
    echo -e "${WARNING} Your application code: ${YELLOW}Version unknown${NC}"
fi

# System Java Status
if [ "$SYSTEM_JAVA_OK" = true ]; then
    echo -e "${CHECK_MARK} System Java: ${GREEN}Java $SYSTEM_JAVA_VERSION (Ready for SonarQube 2026.1)${NC}"
else
    echo -e "${CROSS_MARK} System Java: ${RED}Java $SYSTEM_JAVA_VERSION (Requires Java 21+)${NC}"
    NEEDS_ACTION=true
fi

# CI/CD Status
if [ "$CICD_FOUND" = true ]; then
    if [ "$CICD_NEEDS_UPDATE" = false ]; then
        echo -e "${CHECK_MARK} CI/CD Pipeline: ${GREEN}Already configured for Java 21+${NC}"
    else
        echo -e "${WARNING} CI/CD Pipeline: ${YELLOW}Needs update to Java 21${NC}"
        NEEDS_ACTION=true
    fi
else
    echo -e "${INFO} CI/CD Pipeline: ${YELLOW}Not detected${NC}"
fi

echo ""
echo "─────────────────────────────────────"
echo ""

# ============================================================================
# Action Plan
# ============================================================================
if [ "$NEEDS_ACTION" = true ]; then
    echo -e "${BOLD}${YELLOW}${WARNING} ACTION REQUIRED:${NC}"
    echo ""
    
    ACTION_NUM=1
    
    # Install Java 21 if needed
    if [ "$SYSTEM_JAVA_OK" = false ]; then
        echo -e "${BOLD}${ACTION_NUM}. Install Java 21 (for SonarQube Scanner)${NC}"
        echo ""
        
        # Detect OS and provide instructions
        if [[ "$OSTYPE" == "darwin"* ]]; then
            echo "   ${INFO} macOS detected - Run:"
            echo "       brew install openjdk@21"
            echo ""
        elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
            echo "   ${INFO} Linux detected - Run:"
            echo "       sudo apt update"
            echo "       sudo apt install openjdk-21-jdk"
            echo ""
        else
            echo "   ${INFO} Download from: https://adoptium.net/temurin/releases/?version=21"
            echo ""
        fi
        
        ACTION_NUM=$((ACTION_NUM + 1))
    fi
    
    # Update CI/CD if needed
    if [ "$CICD_NEEDS_UPDATE" = true ] && [ "$CICD_FOUND" = true ]; then
        echo -e "${BOLD}${ACTION_NUM}. Update CI/CD Pipeline${NC}"
        echo ""
        echo "   ${INFO} Add Java 21 setup for SonarQube scanner (keep Java $POM_VERSION for your app):"
        echo ""
        echo "   Example for GitHub Actions:"
        echo "   ─────────────────────────────────────"
        echo "   # Step 1: Build your app with current Java"
        echo "   - name: Setup Java $POM_VERSION for Building"
        echo "     uses: actions/setup-java@v4"
        echo "     with:"
        echo "       java-version: '$POM_VERSION'"
        echo "       distribution: 'temurin'"
        echo ""
        echo "   - name: Build Application"
        echo "     run: mvn clean package"
        echo ""
        echo "   # Step 2: Scan with Java 21"
        echo "   - name: Setup Java 21 for SonarQube"
        echo "     uses: actions/setup-java@v4"
        echo "     with:"
        echo "       java-version: '21'"
        echo "       distribution: 'temurin'"
        echo ""
        echo "   - name: Run SonarQube Analysis"
        echo "     run: mvn sonar:sonar"
        echo "   ─────────────────────────────────────"
        echo ""
    fi
    
    echo -e "${BOLD}${INFO} Important Notes:${NC}"
    echo "   • Your Java $POM_VERSION code does NOT need any changes"
    echo "   • Only the SonarQube scanner needs Java 21"
    echo "   • Java 21 scanner can analyze Java $POM_VERSION code perfectly"
    echo ""
    
else
    echo -e "${BOLD}${GREEN}${CHECK_MARK} ALL CHECKS PASSED!${NC}"
    echo ""
    echo "   Your setup is ready for SonarQube 2026.1!"
    echo "   • System has Java 21+ installed"
    echo "   • CI/CD is properly configured"
    echo "   • Your Java $POM_VERSION code requires no changes"
    echo ""
fi

# ============================================================================
# Additional Resources
# ============================================================================
echo "─────────────────────────────────────"
echo -e "${BOLD}${INFO} Additional Resources:${NC}"
echo ""
echo "   • Visual Comparison: java_version_visual_comparison.md"
echo "   • Code Migration Guide: java_code_migration_guide.md (optional)"
echo "   • SonarQube Upgrade Guide: SonarQube-Server-Upgrade-Guide.html"
echo "   • Multi-Language Support: MULTI_LANGUAGE_SUPPORT.md"
echo ""

# ============================================================================
# Optional: Code Modernization
# ============================================================================
if [ "$POM_VERSION" != "unknown" ] && [ "$POM_VERSION" -lt 21 ]; then
    echo "─────────────────────────────────────"
    echo -e "${BOLD}${CYAN}${ROCKET} OPTIONAL: Modernize Your Code to Java 21${NC}"
    echo ""
    echo "   Want to use Java 21 features in your application?"
    echo "   • Pattern matching"
    echo "   • Records"
    echo "   • Text blocks"
    echo "   • Switch expressions"
    echo "   • Virtual threads"
    echo ""
    echo "   ${INFO} Use OpenRewrite for automated migration (5-10 minutes):"
    echo "   └─ See: java_code_migration_guide.md"
    echo ""
    echo "   ${WARNING} This is OPTIONAL - not required for SonarQube upgrade!"
    echo ""
fi

# ============================================================================
# Final Summary
# ============================================================================
echo "============================================================================"
echo -e "${BOLD}${CYAN}    KEY TAKEAWAY${NC}"
echo "============================================================================"
echo ""
if [ "$POM_VERSION" != "unknown" ]; then
    echo -e "   ${CHECK_MARK} ${GREEN}Your Java $POM_VERSION code stays unchanged${NC}"
    echo -e "   ${CHECK_MARK} ${GREEN}Only SonarQube scanner needs Java 21${NC}"
    echo -e "   ${CHECK_MARK} ${GREEN}This is a CI/CD configuration change only${NC}"
else
    echo -e "   ${INFO} Update CI/CD to use Java 21 for SonarQube scanner"
    echo -e "   ${INFO} Your application code can remain at its current Java version"
fi
echo ""
echo "============================================================================"
echo ""

exit 0
