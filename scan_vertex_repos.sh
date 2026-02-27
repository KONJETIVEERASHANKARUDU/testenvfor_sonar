#!/bin/bash

# ============================================================================
# Vertex Inc - SonarQube Repository Scanner
# ============================================================================
# This script scans all repositories in the vertexinc GitHub organization
# and identifies which ones have SonarQube configured and need upgrading.
#
# Prerequisites:
#   - GitHub CLI installed: brew install gh
#   - Authenticated with GitHub: gh auth login
#
# Usage: ./scan_vertex_repos.sh
# ============================================================================

set -e

# Configuration
ORG="vertexinc"
OUTPUT_FILE="vertex_sonarqube_analysis.csv"
OUTPUT_MD="vertex_sonarqube_analysis.md"
TEMP_DIR="temp_scan_repos"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'
BOLD='\033[1m'

echo ""
echo "============================================================================"
echo -e "${BOLD}${CYAN}Vertex Inc - SonarQube Repository Scanner${NC}"
echo "============================================================================"
echo ""
echo "Organization: $ORG"
echo "Output CSV: $OUTPUT_FILE"
echo "Output Markdown: $OUTPUT_MD"
echo ""

# Check if GitHub CLI is installed
if ! command -v gh &> /dev/null; then
    echo -e "${RED}Error: GitHub CLI (gh) is not installed${NC}"
    echo "Install it with: brew install gh"
    exit 1
fi

# Check if authenticated
if ! gh auth status &> /dev/null; then
    echo -e "${RED}Error: Not authenticated with GitHub${NC}"
    echo "Run: gh auth login"
    exit 1
fi

echo -e "${GREEN}✅ GitHub CLI is installed and authenticated${NC}"
echo ""

# Create temp directory
mkdir -p "$TEMP_DIR"

# Initialize CSV
echo "Repository,Has SonarQube,CI/CD Type,Java Version,App Java Version,Files to Update,Multi-Language,Priority,Notes" > "$OUTPUT_FILE"

# Initialize Markdown
cat > "$OUTPUT_MD" << 'EOF'
# Vertex Inc - SonarQube Repository Analysis

**Generated:** $(date)  
**Organization:** vertexinc  
**Total Repositories Scanned:** TBD

---

## Summary

| Category | Count |
|----------|-------|
| Total Repositories | TBD |
| With SonarQube | TBD |
| Needs Upgrade | TBD |
| GitHub Actions | TBD |
| Azure Pipelines | TBD |
| Jenkins | TBD |
| GitLab CI | TBD |

---

## Repositories with SonarQube Configuration

EOF

# Get all repos
echo -e "${BLUE}Fetching repositories from organization: $ORG${NC}"
echo ""

# Note: This might fail if org is private or you don't have access
# Adjust the limit based on how many repos you expect
REPOS=$(gh repo list "$ORG" --limit 1000 --json name -q '.[].name' 2>/dev/null || echo "")

if [ -z "$REPOS" ]; then
    echo -e "${RED}Error: Could not fetch repositories from $ORG${NC}"
    echo "Possible reasons:"
    echo "  - Organization name is incorrect"
    echo "  - You don't have access to the organization"
    echo "  - Organization is private and you need proper permissions"
    echo ""
    echo "Please verify the organization URL: https://github.com/$ORG"
    exit 1
fi

TOTAL_REPOS=$(echo "$REPOS" | wc -l)
echo -e "${GREEN}Found $TOTAL_REPOS repositories${NC}"
echo ""

CURRENT=0
SONAR_COUNT=0

# Scan each repository
while IFS= read -r repo; do
    CURRENT=$((CURRENT + 1))
    echo -e "${CYAN}[$CURRENT/$TOTAL_REPOS]${NC} Scanning: ${BOLD}$repo${NC}"
    
    # Clone repo (shallow, only recent commit)
    REPO_DIR="$TEMP_DIR/$repo"
    if git clone --depth 1 "https://github.com/$ORG/$repo" "$REPO_DIR" &>/dev/null; then
        cd "$REPO_DIR"
        
        HAS_SONAR="No"
        CI_TYPE="None"
        SCANNER_JAVA="Unknown"
        APP_JAVA="Unknown"
        FILES=""
        MULTI_LANG="No"
        PRIORITY="Low"
        NOTES=""
        
        # Check for GitHub Actions
        if [ -f ".github/workflows/sonarqube.yml" ] || [ -f ".github/workflows/sonar.yml" ] || grep -q "sonar" .github/workflows/*.yml 2>/dev/null; then
            HAS_SONAR="Yes"
            CI_TYPE="GitHub Actions"
            FILES=".github/workflows/*.yml"
            
            # Extract Java version from workflow
            SCANNER_JAVA=$(grep -oP 'java-version:\s*["\']?\K[0-9]+' .github/workflows/*.yml 2>/dev/null | head -1 || echo "Unknown")
            
            # Check if already has Java 21
            if [ "$SCANNER_JAVA" = "21" ]; then
                NOTES="Already upgraded ✅"
                PRIORITY="N/A"
            else
                NOTES="Needs Java 21 for scanner"
                PRIORITY="High"
            fi
        fi
        
        # Check for Azure Pipelines
        if [ -f "azure-pipelines.yml" ]; then
            if grep -q "sonar" azure-pipelines.yml 2>/dev/null; then
                HAS_SONAR="Yes"
                CI_TYPE="Azure Pipelines"
                FILES="$FILES,azure-pipelines.yml"
                SCANNER_JAVA=$(grep -oP 'versionSpec:\s*["\']?\K[0-9]+' azure-pipelines.yml 2>/dev/null | head -1 || echo "Unknown")
            fi
        fi
        
        # Check for Jenkins
        if [ -f "Jenkinsfile" ]; then
            if grep -q -i "sonar" Jenkinsfile 2>/dev/null; then
                HAS_SONAR="Yes"
                CI_TYPE="Jenkins"
                FILES="$FILES,Jenkinsfile"
            fi
        fi
        
        # Check for GitLab CI
        if [ -f ".gitlab-ci.yml" ]; then
            if grep -q -i "sonar" .gitlab-ci.yml 2>/dev/null; then
                HAS_SONAR="Yes"
                CI_TYPE="GitLab CI"
                FILES="$FILES,.gitlab-ci.yml"
            fi
        fi
        
        # Check for sonar-project.properties
        if [ -f "sonar-project.properties" ]; then
            HAS_SONAR="Yes"
            FILES="$FILES,sonar-project.properties"
            
            # Check for multi-language
            if grep -q "sonar.sources=\." sonar-project.properties 2>/dev/null; then
                MULTI_LANG="Possible"
            fi
        fi
        
        # Check for pom.xml (Java application version)
        if [ -f "pom.xml" ]; then
            FILES="$FILES,pom.xml"
            APP_JAVA=$(grep -oP 'maven\.compiler\.source>\K[^<]+' pom.xml 2>/dev/null | head -1 || echo "Unknown")
            
            if [ "$APP_JAVA" != "Unknown" ]; then
                NOTES="$NOTES, App uses Java $APP_JAVA"
            fi
        fi
        
        # Check for package.json (JavaScript/TypeScript)
        if [ -f "package.json" ]; then
            MULTI_LANG="Yes (JS/TS)"
        fi
        
        # Check for requirements.txt (Python)
        if [ -f "requirements.txt" ]; then
            if [ "$MULTI_LANG" = "Yes (JS/TS)" ]; then
                MULTI_LANG="Yes (Multi)"
            else
                MULTI_LANG="Yes (Python)"
            fi
        fi
        
        # Clean up FILES string
        FILES=$(echo "$FILES" | sed 's/^,//')
        
        # Write to CSV
        if [ "$HAS_SONAR" = "Yes" ]; then
            echo "$repo,$HAS_SONAR,$CI_TYPE,$SCANNER_JAVA,$APP_JAVA,\"$FILES\",$MULTI_LANG,$PRIORITY,\"$NOTES\"" >> "../../$OUTPUT_FILE"
            SONAR_COUNT=$((SONAR_COUNT + 1))
            
            # Write to Markdown
            cat >> "../../$OUTPUT_MD" << EOF

### $SONAR_COUNT. $repo

| Property | Value |
|----------|-------|
| **CI/CD** | $CI_TYPE |
| **Scanner Java** | $SCANNER_JAVA |
| **App Java** | $APP_JAVA |
| **Multi-Language** | $MULTI_LANG |
| **Priority** | $PRIORITY |
| **Files to Update** | $FILES |
| **Notes** | $NOTES |
| **GitHub URL** | https://github.com/$ORG/$repo |

EOF
            
            echo -e "  ${GREEN}✅ SonarQube configured ($CI_TYPE)${NC}"
        else
            echo -e "  ${YELLOW}⚠️  No SonarQube configuration found${NC}"
        fi
        
        cd ../..
    else
        echo -e "  ${RED}❌ Could not clone repository (may be empty or inaccessible)${NC}"
    fi
    
    # Clean up repo directory
    rm -rf "$REPO_DIR"
    
    echo ""
done <<< "$REPOS"

# Update summary in markdown
GITHUB_ACTIONS=$(grep -c "GitHub Actions" "$OUTPUT_FILE" || echo 0)
AZURE_COUNT=$(grep -c "Azure Pipelines" "$OUTPUT_FILE" || echo 0)
JENKINS_COUNT=$(grep -c "Jenkins" "$OUTPUT_FILE" || echo 0)
GITLAB_COUNT=$(grep -c "GitLab CI" "$OUTPUT_FILE" || echo 0)
NEEDS_UPGRADE=$(grep -c "Needs Java 21" "$OUTPUT_FILE" || echo 0)

sed -i '' "s/TBD/$TOTAL_REPOS/g" "$OUTPUT_MD"
sed -i '' "s/With SonarQube | TBD/With SonarQube | $SONAR_COUNT/g" "$OUTPUT_MD"
sed -i '' "s/Needs Upgrade | TBD/Needs Upgrade | $NEEDS_UPGRADE/g" "$OUTPUT_MD"
sed -i '' "s/GitHub Actions | TBD/GitHub Actions | $GITHUB_ACTIONS/g" "$OUTPUT_MD"
sed -i '' "s/Azure Pipelines | TBD/Azure Pipelines | $AZURE_COUNT/g" "$OUTPUT_MD"
sed -i '' "s/Jenkins | TBD/Jenkins | $JENKINS_COUNT/g" "$OUTPUT_MD"
sed -i '' "s/GitLab CI | TBD/GitLab CI | $GITLAB_COUNT/g" "$OUTPUT_MD"

# Add recommendations section
cat >> "$OUTPUT_MD" << 'EOF'

---

## Upgrade Recommendations

### Priority Levels

**🔴 High Priority** - Active repositories with SonarQube configured but not yet upgraded to Java 21  
**🟡 Medium Priority** - Repositories with moderate activity  
**🟢 Low Priority** - Legacy or low-activity repositories  
**✅ N/A** - Already upgraded to Java 21

### Next Steps

1. **Review this analysis document**
2. **Create a tracking spreadsheet** from the CSV file
3. **Start with High Priority repositories**
4. **Use the upgrade guide:** VERTEX_ORG_UPGRADE_PLAN.md
5. **Run check script in each repo:** check_java_requirements.sh

### Required Changes Per Repository

For each repository that needs upgrading:

1. **Update CI/CD workflow** - Add Java 21 setup step for SonarQube scanner
2. **Keep application Java version** - No code changes required
3. **Update sonar-project.properties** - If needed for multi-language support
4. **Test the pipeline** - Ensure build and analysis succeed

### Estimated Timeline

- **1-5 repositories:** 1-2 days
- **6-20 repositories:** 3-5 days (2-3 people)
- **21-50 repositories:** 1-2 weeks (team effort)
- **50+ repositories:** 2-4 weeks (full team + automation)

---

*Generated by: scan_vertex_repos.sh*  
*Scan Date: $(date)*
EOF

# Clean up temp directory
rm -rf "$TEMP_DIR"

# Summary
echo "============================================================================"
echo -e "${BOLD}${GREEN}Scan Complete!${NC}"
echo "============================================================================"
echo ""
echo "Summary:"
echo "  Total Repositories: $TOTAL_REPOS"
echo "  With SonarQube: $SONAR_COUNT"
echo ""
echo "Breakdown:"
echo "  GitHub Actions: $GITHUB_ACTIONS"
echo "  Azure Pipelines: $AZURE_COUNT"
echo "  Jenkins: $JENKINS_COUNT"
echo "  GitLab CI: $GITLAB_COUNT"
echo ""
echo "Output Files:"
echo "  📊 CSV: $OUTPUT_FILE"
echo "  📄 Markdown: $OUTPUT_MD"
echo ""
echo "Next Steps:"
echo "  1. Open $OUTPUT_FILE in Excel/Numbers"
echo "  2. Review $OUTPUT_MD for detailed analysis"
echo "  3. Prioritize repositories for upgrade"
echo "  4. Follow VERTEX_ORG_UPGRADE_PLAN.md"
echo ""
echo "============================================================================"
echo ""

exit 0
