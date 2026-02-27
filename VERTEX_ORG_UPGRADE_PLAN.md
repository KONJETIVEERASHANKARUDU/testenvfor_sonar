# 🏢 Vertex Inc Organization - SonarQube Upgrade Plan

## 📋 Overview

This document provides a **comprehensive upgrade plan** for all repositories in the Vertex Inc GitHub organization (https://github.com/vertexinc) that currently have SonarQube configured.

---

## 🎯 Upgrade Objective

**Upgrade all repositories from current SonarQube setup to SonarQube 2026.1 (or latest SonarCloud)**

### Key Requirements:
- ✅ SonarQube 2026.1 requires **Java 21** for the scanner
- ✅ Application code can remain at current Java version (8, 11, 17, etc.)
- ✅ Only CI/CD pipeline configuration needs updating
- ✅ No application code changes required

---

## 📂 Files to Check in EACH Repository

For each repository in your Vertex Inc organization, check and potentially update these files:

### 1. **GitHub Actions Workflow** (CRITICAL)
**File Location:** `.github/workflows/sonarqube.yml` or `.github/workflows/sonar.yml` or `.github/workflows/build.yml`

**What to Check:**
- Java version used for SonarQube scanner
- SonarScanner CLI version
- Secrets configuration

**What to Update:**
```yaml
# OLD Configuration (needs update):
- name: Set up JDK
  uses: actions/setup-java@v4
  with:
    java-version: '17'  # ❌ Too old for SonarQube 2026.1

# NEW Configuration (updated):
# Step 1: Build app with current Java
- name: Set up JDK for Building
  uses: actions/setup-java@v4
  with:
    java-version: '17'  # or whatever your app uses (8, 11, 17)
    distribution: 'temurin'

- name: Build Application
  run: mvn clean package

# Step 2: Scan with Java 21
- name: Set up JDK 21 for SonarQube
  uses: actions/setup-java@v4
  with:
    java-version: '21'  # ✅ Required for SonarQube 2026.1
    distribution: 'temurin'

- name: Run SonarQube Analysis
  run: mvn sonar:sonar
```

---

### 2. **SonarQube Project Configuration**
**File Location:** `sonar-project.properties` (if exists in root)

**What to Check:**
- Project key and organization
- Source directories
- Exclusions
- Language-specific settings

**What to Update:**
```properties
# Basic Configuration (verify these are correct)
sonar.projectKey=your-org_your-repo-name
sonar.organization=vertexinc  # or your SonarCloud org
sonar.projectName=Your Project Name
sonar.projectVersion=1.0

# Source Configuration
sonar.sources=.  # or src/main/java, src, etc.
sonar.tests=src/test  # adjust to your test directory

# Java Settings (update to match your Java version)
sonar.java.source=17  # or 8, 11, 17, 21
sonar.java.target=17
sonar.java.binaries=target/classes
sonar.java.test.binaries=target/test-classes

# Coverage (if using JaCoCo)
sonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml

# Exclusions
sonar.exclusions=**/target/**,**/node_modules/**,**/dist/**
```

---

### 3. **Maven Configuration**
**File Location:** `pom.xml`

**What to Check:**
- Java compiler version (application level - does NOT need to change)
- SonarQube Maven plugin version
- JaCoCo plugin version (for coverage)

**What to Update (if needed):**
```xml
<properties>
    <!-- Your app's Java version - NO CHANGE NEEDED -->
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    
    <!-- SonarQube plugin - update to latest -->
    <sonar.maven.plugin.version>4.0.0.4121</sonar.maven.plugin.version>
    
    <!-- JaCoCo plugin - update to latest -->
    <jacoco.version>0.8.11</jacoco.version>
</properties>

<build>
    <plugins>
        <!-- SonarQube Maven Plugin -->
        <plugin>
            <groupId>org.sonarsource.scanner.maven</groupId>
            <artifactId>sonar-maven-plugin</artifactId>
            <version>${sonar.maven.plugin.version}</version>
        </plugin>
        
        <!-- JaCoCo for Code Coverage -->
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>${jacoco.version}</version>
            <executions>
                <execution>
                    <goals>
                        <goal>prepare-agent</goal>
                    </goals>
                </execution>
                <execution>
                    <id>report</id>
                    <phase>test</phase>
                    <goals>
                        <goal>report</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

---

### 4. **Azure Pipelines** (if using Azure DevOps)
**File Location:** `azure-pipelines.yml`

**What to Update:**
```yaml
# OLD Configuration:
- task: JavaToolInstaller@0
  inputs:
    versionSpec: '17'
    jdkArchitectureOption: 'x64'

# NEW Configuration:
# Step 1: Build with current Java
- task: JavaToolInstaller@0
  displayName: 'Install Java for Building'
  inputs:
    versionSpec: '17'  # your app's Java version
    jdkArchitectureOption: 'x64'
    jdkSourceOption: 'PreInstalled'

- task: Maven@3
  displayName: 'Build Application'
  inputs:
    goals: 'clean package'

# Step 2: Install Java 21 for SonarQube
- task: JavaToolInstaller@0
  displayName: 'Install Java 21 for SonarQube'
  inputs:
    versionSpec: '21'
    jdkArchitectureOption: 'x64'
    jdkSourceOption: 'PreInstalled'

- task: SonarQubePrepare@5
  inputs:
    SonarQube: 'SonarQube Connection'
    scannerMode: 'CLI'
```

---

### 5. **Jenkins Pipeline**
**File Location:** `Jenkinsfile`

**What to Update:**
```groovy
// OLD Configuration:
stage('Build and Analyze') {
    tools {
        jdk 'Java 17'
    }
    steps {
        sh 'mvn clean verify sonar:sonar'
    }
}

// NEW Configuration:
stage('Build') {
    tools {
        jdk 'Java 17'  // Your app's Java version
    }
    steps {
        sh 'mvn clean package'
    }
}

stage('SonarQube Analysis') {
    tools {
        jdk 'Java 21'  // Required for SonarQube scanner
    }
    environment {
        SONAR_HOST_URL = credentials('sonar-host-url')
        SONAR_TOKEN = credentials('sonar-token')
    }
    steps {
        sh 'mvn sonar:sonar'
    }
}
```

---

### 6. **GitLab CI**
**File Location:** `.gitlab-ci.yml`

**What to Update:**
```yaml
# OLD Configuration:
sonarqube-check:
  image: maven:3.8-openjdk-17
  script:
    - mvn verify sonar:sonar

# NEW Configuration:
build:
  image: maven:3.8-openjdk-17  # Your app's Java version
  script:
    - mvn clean package
  artifacts:
    paths:
      - target/

sonarqube-analysis:
  image: maven:3.8-openjdk-21  # Java 21 for SonarQube
  script:
    - mvn sonar:sonar
  dependencies:
    - build
```

---

## 🔍 Step-by-Step Process for Each Repository

### Phase 1: Discovery (For EACH repo)

1. **Clone the repository:**
   ```bash
   git clone https://github.com/vertexinc/REPO_NAME
   cd REPO_NAME
   ```

2. **Check for SonarQube configuration:**
   ```bash
   # Look for GitHub Actions
   ls -la .github/workflows/*.yml
   
   # Look for SonarQube config
   ls -la sonar-project.properties
   
   # Look for Maven config
   ls -la pom.xml
   
   # Look for other CI/CD
   ls -la azure-pipelines.yml Jenkinsfile .gitlab-ci.yml
   ```

3. **Identify current Java version:**
   ```bash
   # Check pom.xml
   grep -A 2 "maven.compiler.source" pom.xml
   
   # Check GitHub Actions
   grep -A 2 "java-version" .github/workflows/*.yml
   ```

4. **Document findings in spreadsheet** (see template below)

---

### Phase 2: Create Upgrade Branch

For each repository:

```bash
# 1. Create upgrade branch
git checkout -b feature/sonarqube-2026-upgrade

# 2. Make necessary changes to files (see file-specific updates above)

# 3. Commit changes
git add .
git commit -m "Upgrade SonarQube to 2026.1 - Add Java 21 for scanner

- Updated GitHub Actions to use Java 21 for SonarQube scanner
- Maintained Java X for application build and runtime
- Updated sonar-project.properties (if needed)
- Updated Maven plugins to latest versions
- No application code changes required"

# 4. Push branch
git push origin feature/sonarqube-2026-upgrade

# 5. Create Pull Request
```

---

### Phase 3: Testing

For each repository, test:

1. **GitHub Actions runs successfully:**
   - Push triggers workflow
   - Build completes
   - SonarQube analysis completes
   - Quality Gate status visible

2. **SonarQube Dashboard updated:**
   - Go to SonarCloud/SonarQube
   - Verify project shows latest analysis
   - Check for any new issues

3. **Application still works:**
   - Unit tests pass
   - Integration tests pass
   - No runtime errors

---

## 📊 Repository Tracking Spreadsheet

Create a spreadsheet to track progress across all repos:

| Repository Name | Current Java | CI/CD Type | Files to Update | Status | Priority | Notes |
|----------------|--------------|------------|-----------------|--------|----------|-------|
| **repo-name-1** | Java 17 | GitHub Actions | `.github/workflows/sonarqube.yml`<br>`sonar-project.properties`<br>`pom.xml` | ⏳ Not Started | 🔴 High | Has active development |
| **repo-name-2** | Java 11 | Azure Pipelines | `azure-pipelines.yml`<br>`pom.xml` | ⏳ Not Started | 🟡 Medium | Less critical |
| **repo-name-3** | Java 8 | Jenkins | `Jenkinsfile`<br>`sonar-project.properties` | ⏳ Not Started | 🟢 Low | Legacy system |

**Status Values:**
- ⏳ Not Started
- 🔄 In Progress
- ✅ Completed - Testing
- 🎉 Completed - Merged
- ⚠️ Blocked (describe issue in Notes)

---

## 🚨 Common Issues & Solutions

### Issue 1: "Execution of SonarQube Scanner requires Java 21"
**Solution:** Add Java 21 setup step BEFORE SonarQube analysis (see GitHub Actions example above)

---

### Issue 2: "Project not found in SonarCloud"
**Solution:** 
- Verify `sonar.projectKey` and `sonar.organization` are correct
- Check SonarCloud for project existence
- May need to re-import project in SonarCloud

---

### Issue 3: "Build fails after updating to Java 21"
**Solution:** 
- Use TWO Java setup steps (one for build, one for scan)
- Keep your app building with current Java version
- Only scanner needs Java 21

---

### Issue 4: "Coverage report not found"
**Solution:**
- Ensure JaCoCo plugin runs BEFORE SonarQube analysis
- Check coverage file path matches `sonar.coverage.jacoco.xmlReportPaths`
- Run tests before analysis: `mvn clean test` then `mvn sonar:sonar`

---

### Issue 5: "Quality Gate fails on old code"
**Solution:**
- Configure Quality Gate to check "new code" only
- Go to SonarCloud → Project Settings → Quality Gate
- Set "Quality Gate passed on new code only"

---

## 🎯 Priority Levels

### 🔴 High Priority (Upgrade First)
- Repositories with **active development**
- Repositories with **frequent commits**
- Critical production applications
- Repositories with **security vulnerabilities** reported

### 🟡 Medium Priority (Upgrade Second)
- Repositories with **moderate activity**
- Supporting services
- Internal tools

### 🟢 Low Priority (Upgrade Last)
- **Legacy applications** (minimal changes)
- Archived/deprecated projects
- Experimental repositories

---

## 📝 Upgrade Checklist (Per Repository)

Use this checklist for EACH repository:

### Discovery Phase
- [ ] Clone repository locally
- [ ] Identify current SonarQube configuration
- [ ] Check current Java version (app level)
- [ ] Identify CI/CD platform (GitHub Actions, Azure, Jenkins, GitLab)
- [ ] Document current state in tracking spreadsheet

### Planning Phase
- [ ] Determine which files need updating
- [ ] Check if multi-language support needed
- [ ] Verify SonarCloud/SonarQube project exists
- [ ] Review any custom SonarQube configuration

### Implementation Phase
- [ ] Create feature branch: `feature/sonarqube-2026-upgrade`
- [ ] Update CI/CD workflow (add Java 21 for scanner)
- [ ] Update `sonar-project.properties` (if needed)
- [ ] Update `pom.xml` plugin versions (if needed)
- [ ] Commit changes with descriptive message
- [ ] Push branch to remote

### Testing Phase
- [ ] Trigger CI/CD pipeline manually
- [ ] Verify build completes successfully
- [ ] Verify SonarQube analysis completes
- [ ] Check SonarCloud/SonarQube dashboard for results
- [ ] Verify Quality Gate status
- [ ] Run unit tests locally
- [ ] Check for any new errors/warnings

### Review Phase
- [ ] Create Pull Request
- [ ] Add description explaining changes
- [ ] Request code review from team
- [ ] Address any review comments
- [ ] Get approval

### Deployment Phase
- [ ] Merge Pull Request to main/master
- [ ] Verify CI/CD runs on main branch
- [ ] Confirm SonarQube analysis on main branch
- [ ] Monitor for any issues
- [ ] Update tracking spreadsheet: ✅ Completed

### Documentation Phase
- [ ] Update repository README (if needed)
- [ ] Document any repo-specific configuration
- [ ] Share lessons learned with team

---

## 🔧 Automated Discovery Script

Use this script to scan all your repos for SonarQube configuration:

```bash
#!/bin/bash
# scan_vertex_repos.sh

ORG="vertexinc"
OUTPUT_FILE="vertex_sonarqube_repos.csv"

echo "Repository,Has SonarQube,CI/CD Type,Java Version,Files to Update" > "$OUTPUT_FILE"

# Get all repos in organization (requires GitHub CLI: brew install gh)
gh repo list "$ORG" --limit 1000 --json name -q '.[].name' | while read -r repo; do
    echo "Scanning: $repo"
    
    # Clone repo (shallow)
    git clone --depth 1 "https://github.com/$ORG/$repo" "temp_$repo" 2>/dev/null
    cd "temp_$repo" || continue
    
    HAS_SONAR="No"
    CI_TYPE="None"
    JAVA_VERSION="Unknown"
    FILES=""
    
    # Check for GitHub Actions
    if [ -f ".github/workflows/sonarqube.yml" ] || [ -f ".github/workflows/sonar.yml" ]; then
        HAS_SONAR="Yes"
        CI_TYPE="GitHub Actions"
        FILES=".github/workflows/*.yml"
        JAVA_VERSION=$(grep -oP 'java-version:\s*\K[^\s]+' .github/workflows/*.yml | head -1)
    fi
    
    # Check for sonar-project.properties
    if [ -f "sonar-project.properties" ]; then
        HAS_SONAR="Yes"
        FILES="$FILES,sonar-project.properties"
    fi
    
    # Check for pom.xml
    if [ -f "pom.xml" ]; then
        FILES="$FILES,pom.xml"
        if [ "$JAVA_VERSION" = "Unknown" ]; then
            JAVA_VERSION=$(grep -oP 'maven\.compiler\.source>\K[^<]+' pom.xml | head -1)
        fi
    fi
    
    # Check for Azure Pipelines
    if [ -f "azure-pipelines.yml" ]; then
        CI_TYPE="Azure Pipelines"
        FILES="$FILES,azure-pipelines.yml"
    fi
    
    # Check for Jenkins
    if [ -f "Jenkinsfile" ]; then
        CI_TYPE="Jenkins"
        FILES="$FILES,Jenkinsfile"
    fi
    
    # Check for GitLab CI
    if [ -f ".gitlab-ci.yml" ]; then
        CI_TYPE="GitLab CI"
        FILES="$FILES,.gitlab-ci.yml"
    fi
    
    echo "$repo,$HAS_SONAR,$CI_TYPE,$JAVA_VERSION,$FILES" >> "../$OUTPUT_FILE"
    
    cd ..
    rm -rf "temp_$repo"
done

echo "Scan complete! Results in: $OUTPUT_FILE"
```

**Usage:**
```bash
chmod +x scan_vertex_repos.sh
./scan_vertex_repos.sh
```

This creates a CSV file listing all repos with SonarQube configuration!

---

## 📚 Reference Documentation

For detailed technical guidance, refer to these files in the `testenvfor_sonar` repository:

1. **[java_version_visual_comparison.md](java_version_visual_comparison.md)**
   - Side-by-side comparison of before/after
   - Visual explanation of what changes

2. **[check_java_requirements.sh](check_java_requirements.sh)**
   - Automated checker for each repository
   - Run in each repo to get specific recommendations

3. **[java_code_migration_guide.md](java_code_migration_guide.md)**
   - OPTIONAL: If you want to modernize code to Java 21
   - Not required for SonarQube upgrade!

4. **[MULTI_LANGUAGE_SUPPORT.md](MULTI_LANGUAGE_SUPPORT.md)**
   - If repos have multiple languages (Java + JavaScript + Python)

5. **[SonarQube-Server-Upgrade-Guide.html](SonarQube-Server-Upgrade-Guide.html)**
   - Complete upgrade guide with visuals

---

## 🚀 Quick Start Guide for Your Team

### For Each Team Member:

1. **Get the template repository:**
   ```bash
   git clone https://github.com/KONJETIVEERASHANKARUDU/testenvfor_sonar
   ```

2. **Review the upgrade examples:**
   - Open `.github/workflows/sonarqube.yml` (updated version)
   - Open `sonar-project.properties` (configured version)
   - Open `pom.xml` (latest plugins)

3. **Pick a repository to upgrade:**
   - Start with low-priority repo for practice
   - Follow the checklist above

4. **Use the check script in each repo:**
   ```bash
   cp testenvfor_sonar/check_java_requirements.sh your-repo/
   cd your-repo
   ./check_java_requirements.sh
   ```

5. **Update based on recommendations**

---

## 📊 Estimated Timeline

Based on repository count and complexity:

| Repo Count | Estimated Time | Recommendation |
|-----------|----------------|----------------|
| 1-5 repos | 1-2 days | One person |
| 6-20 repos | 3-5 days | 2-3 people |
| 21-50 repos | 1-2 weeks | Team effort |
| 50+ repos | 2-4 weeks | Full team + automation |

**Time per Repository:**
- Simple repo (just GitHub Actions): 15-30 minutes
- Medium repo (GitHub Actions + custom config): 30-60 minutes
- Complex repo (multiple languages, custom setup): 1-2 hours

---

## ✅ Success Criteria

Upgrade is successful when:

1. ✅ All CI/CD pipelines run successfully with Java 21 scanner
2. ✅ All applications still build and run on their original Java version
3. ✅ SonarCloud/SonarQube dashboards show updated analysis
4. ✅ Quality Gates are configured and passing
5. ✅ No application code changes were required
6. ✅ Team is trained on new setup
7. ✅ Documentation is updated

---

## 📞 Support & Questions

**For technical issues:**
- Refer to documentation files in `testenvfor_sonar` repo
- Check [SonarQube 2026.1 Release Notes](https://docs.sonarqube.org/latest/)
- Review [SonarCloud Documentation](https://docs.sonarcloud.io/)

**For upgrade assistance:**
- Run `check_java_requirements.sh` in each repo
- Review error messages in CI/CD logs
- Check Quality Gate configuration

---

## 🎯 Key Takeaways

1. ✅ **No application code changes required** - only CI/CD configuration
2. ✅ **Java 21 is only for SonarQube scanner** - not your application
3. ✅ **Use two-step Java setup** - one for build, one for scan
4. ✅ **Test in low-priority repos first** - gain confidence
5. ✅ **Track progress in spreadsheet** - stay organized
6. ✅ **Automate discovery** - use the scan script
7. ✅ **Document repo-specific notes** - help your team

---

## 📋 Next Steps

1. **Run the automated discovery script** to find all SonarQube-configured repos
2. **Create tracking spreadsheet** with all repositories
3. **Prioritize repositories** (high/medium/low)
4. **Start with 1-2 test repositories** to practice
5. **Roll out to remaining repositories** systematically
6. **Update organization documentation** with new standards

---

*Last Updated: February 24, 2026*  
*SonarQube Target Version: 2026.1 (CE 26.2.0.119303)*  
*Organization: Vertex Inc (https://github.com/vertexinc)*
