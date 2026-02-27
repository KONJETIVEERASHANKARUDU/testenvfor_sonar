# 📋 Quick Reference: Per-Repository Upgrade Checklist

**Repository Name:** ___________________________  
**Date Started:** ___________  
**Assigned To:** ___________________________  
**Priority:** ⬜ High  ⬜ Medium  ⬜ Low  

---

## 1️⃣ DISCOVERY (15 minutes)

### A. Clone Repository
```bash
git clone https://github.com/vertexinc/REPO_NAME
cd REPO_NAME
```
⬜ Repository cloned successfully

### B. Check Current Configuration
```bash
# Check for CI/CD files
ls -la .github/workflows/*.yml
ls -la azure-pipelines.yml
ls -la Jenkinsfile
ls -la .gitlab-ci.yml

# Check for SonarQube config
ls -la sonar-project.properties
ls -la pom.xml
```

⬜ Found CI/CD configuration: ___________________________  
⬜ Found SonarQube configuration  

### C. Identify Current Java Versions
```bash
# App Java version (from pom.xml)
grep "maven.compiler.source" pom.xml

# Scanner Java version (from workflow)
grep "java-version" .github/workflows/*.yml
```

**App Java Version:** ___________  
**Scanner Java Version:** ___________  

### D. Run Automated Check
```bash
# Copy check script to this repo
cp ../testenvfor_sonar/check_java_requirements.sh .
chmod +x check_java_requirements.sh
./check_java_requirements.sh
```

⬜ Check script run completed  
⬜ Reviewed recommendations  

---

## 2️⃣ PLANNING (5 minutes)

### Files That Need Updating:
⬜ `.github/workflows/sonarqube.yml` (or similar)  
⬜ `sonar-project.properties`  
⬜ `pom.xml`  
⬜ `azure-pipelines.yml`  
⬜ `Jenkinsfile`  
⬜ `.gitlab-ci.yml`  
⬜ Other: ___________________________  

### Changes Needed:
⬜ Add Java 21 setup step for SonarQube scanner  
⬜ Keep existing Java version for application build  
⬜ Update Maven plugin versions  
⬜ Update JaCoCo version  
⬜ Add multi-language support  
⬜ Update exclusions  
⬜ Other: ___________________________  

---

## 3️⃣ IMPLEMENTATION (30-60 minutes)

### A. Create Feature Branch
```bash
git checkout -b feature/sonarqube-2026-upgrade
```
⬜ Feature branch created

### B. Update CI/CD Workflow

**For GitHub Actions:**
```yaml
# Step 1: Build with current Java
- name: Set up JDK for Building
  uses: actions/setup-java@v4
  with:
    java-version: '17'  # or your current version
    distribution: 'temurin'

- name: Build Application
  run: mvn clean package

# Step 2: Scan with Java 21
- name: Set up JDK 21 for SonarQube
  uses: actions/setup-java@v4
  with:
    java-version: '21'
    distribution: 'temurin'

- name: Run SonarQube Analysis
  run: mvn sonar:sonar
```
⬜ CI/CD workflow updated

### C. Update sonar-project.properties (if needed)
```properties
# Verify these settings
sonar.projectKey=vertexinc_REPO_NAME
sonar.organization=vertexinc  # or your org name
sonar.java.source=17  # your app's Java version
sonar.java.binaries=target/classes
```
⬜ SonarQube properties verified/updated

### D. Update pom.xml (if needed)
```xml
<properties>
    <!-- Keep your app's Java version -->
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    
    <!-- Update plugins to latest -->
    <sonar.maven.plugin.version>4.0.0.4121</sonar.maven.plugin.version>
    <jacoco.version>0.8.11</jacoco.version>
</properties>
```
⬜ Maven plugins updated

### E. Commit Changes
```bash
git add .
git commit -m "Upgrade SonarQube to 2026.1 - Add Java 21 for scanner

- Updated CI/CD to use Java 21 for SonarQube scanner
- Maintained Java [VERSION] for application build
- Updated Maven plugins to latest versions
- No application code changes required"
```
⬜ Changes committed

### F. Push Branch
```bash
git push origin feature/sonarqube-2026-upgrade
```
⬜ Branch pushed to remote

---

## 4️⃣ TESTING (30 minutes)

### A. Trigger CI/CD Pipeline
⬜ Go to Actions/Pipelines tab  
⬜ Manually trigger workflow  
⬜ Monitor pipeline execution  

### B. Verify Build Success
⬜ Application build completes successfully  
⬜ No compilation errors  
⬜ Tests pass  

### C. Verify SonarQube Analysis
⬜ SonarQube analysis step completes  
⬜ No scanner errors  
⬜ Analysis uploads to SonarCloud/SonarQube  

### D. Check SonarQube Dashboard
⬜ Go to SonarCloud/SonarQube dashboard  
⬜ Find project: `vertexinc_REPO_NAME`  
⬜ Verify latest analysis appears  
⬜ Check Quality Gate status  

**Quality Gate Status:** ⬜ Passed  ⬜ Failed  
**Coverage:** __________%  
**Issues Found:** _________  

### E. Review Analysis Results
⬜ No critical new issues  
⬜ Coverage metrics look correct  
⬜ No unexpected errors in logs  

---

## 5️⃣ CODE REVIEW (15-30 minutes)

### A. Create Pull Request
```bash
# Or use GitHub CLI
gh pr create --title "Upgrade SonarQube to 2026.1" \
  --body "This PR upgrades our SonarQube configuration to work with SonarQube 2026.1.

Changes:
- Added Java 21 setup step for SonarQube scanner
- Maintained Java [VERSION] for application build
- Updated Maven plugins to latest versions
- No application code changes required

Testing:
- ✅ CI/CD pipeline runs successfully
- ✅ SonarQube analysis completes
- ✅ Quality Gate passes
- ✅ All tests pass"
```
⬜ Pull Request created  
**PR Number:** #_________  
**PR URL:** ___________________________  

### B. Request Review
⬜ Assigned reviewers  
⬜ Added labels (if applicable)  
⬜ Linked to tracking issue (if applicable)  

### C. Address Review Comments
⬜ Responded to all comments  
⬜ Made requested changes  
⬜ Re-requested review  

### D. Get Approval
⬜ PR approved by reviewer(s)  
⬜ All checks passing  

---

## 6️⃣ DEPLOYMENT (10 minutes)

### A. Merge Pull Request
⬜ Squash and merge (or merge commit)  
⬜ Delete feature branch  

### B. Verify Main Branch
⬜ CI/CD runs on main branch  
⬜ SonarQube analysis completes on main  
⬜ Quality Gate status verified  

### C. Monitor for Issues
⬜ Check for any errors in logs (first 24 hours)  
⬜ Verify scheduled scans work (if applicable)  

---

## 7️⃣ DOCUMENTATION (5 minutes)

### A. Update Repository README (if needed)
⬜ Document new Java requirements  
⬜ Update build instructions  

### B. Update Tracking Spreadsheet
⬜ Mark repository as: ✅ Completed  
⬜ Add completion date  
⬜ Add any notes learned  

### C. Document Lessons Learned
**What went well:**  
___________________________  
___________________________  

**Issues encountered:**  
___________________________  
___________________________  

**Time taken:** _________ minutes/hours  

**Recommendations for next repo:**  
___________________________  
___________________________  

---

## ✅ COMPLETION CHECKLIST

⬜ Repository successfully upgraded  
⬜ CI/CD pipeline runs successfully  
⬜ SonarQube analysis completing  
⬜ Quality Gate configured and passing  
⬜ No application code changes were required  
⬜ Documentation updated  
⬜ Tracking spreadsheet updated  
⬜ Team notified (if needed)  

---

## 📊 METRICS

| Metric | Value |
|--------|-------|
| **Time to Complete** | _______ hours |
| **Issues Found** | _______ |
| **PRs Required** | _______ |
| **Reviewers** | _______ |
| **Blockers** | _______ |

---

## 🚨 TROUBLESHOOTING

### Common Issues & Solutions

#### Issue 1: "Execution of SonarQube Scanner requires Java 21"
**Solution:** Add Java 21 setup step BEFORE SonarQube analysis  
⬜ Resolved

#### Issue 2: Build fails after adding Java 21 step
**Solution:** Use TWO Java setup steps (one for build, one for scan)  
⬜ Resolved

#### Issue 3: Coverage report not found
**Solution:** 
- Run tests before analysis
- Check coverage path in sonar-project.properties
- Verify JaCoCo plugin is configured  
⬜ Resolved

#### Issue 4: Quality Gate fails
**Solution:**
- Review new issues in SonarQube dashboard
- Configure Quality Gate for "new code only" if needed  
⬜ Resolved

#### Issue 5: Project not found in SonarCloud
**Solution:**
- Verify sonar.projectKey matches
- Check sonar.organization is correct
- Re-import project in SonarCloud if needed  
⬜ Resolved

---

## 📝 NOTES

_Use this space for repo-specific notes, special configurations, or anything important to remember:_

___________________________  
___________________________  
___________________________  
___________________________  
___________________________  

---

## 🔄 ROLLBACK PLAN (if needed)

If something goes wrong:

```bash
# Option 1: Revert the merge commit
git revert -m 1 <merge-commit-hash>
git push origin main

# Option 2: Delete and recreate from backup
git checkout main
git reset --hard <commit-before-merge>
git push origin main --force

# Option 3: Revert just the workflow file
git checkout <commit-before-merge> -- .github/workflows/sonarqube.yml
git commit -m "Revert SonarQube workflow changes"
git push origin main
```

⬜ Rollback plan understood and ready if needed

---

## ✨ SIGN-OFF

**Upgraded By:** ___________________________  
**Date Completed:** ___________________________  
**Reviewed By:** ___________________________  
**Approved By:** ___________________________  

---

*Use this checklist for EACH repository in the Vertex Inc organization*  
*Print or duplicate this file for each repo upgrade*  
*Last Updated: February 24, 2026*
