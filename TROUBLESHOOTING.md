# 🔧 Troubleshooting SonarQube Analysis

## Common Issues and Solutions

### ❌ Issue: "Not able to get the latest report"

This usually means one of the following:

---

## 🔍 Step 1: Check GitHub Actions Status

**Open this link:**
👉 https://github.com/KONJETIVEERASHANKARUDU/testenvfor_sonar/actions

### What to Look For:

#### ✅ **Success (Green checkmark)**
- Workflow completed successfully
- Go to SonarCloud to view the report

#### ❌ **Failure (Red X)**
- Click on the failed workflow
- Check the error message
- Most common: "Missing SONAR_TOKEN secret"

#### ⏳ **In Progress (Yellow/Orange)**
- Wait 2-3 minutes for completion
- Refresh the page

#### ⚪ **No Workflows Running**
- Secrets might not be configured
- Workflow might be disabled

---

## 🔐 Step 2: Verify GitHub Secrets Are Added

**Check here:**
👉 https://github.com/KONJETIVEERASHANKARUDU/testenvfor_sonar/settings/secrets/actions

### You Should See:
```
✅ SONAR_TOKEN          Updated X hours ago
✅ SONAR_HOST_URL       Updated X hours ago
```

### ❌ If Secrets Are Missing:

**Add them now:**

1. Click "New repository secret"

2. **First Secret:**
   - Name: `SONAR_TOKEN`
   - Value: `333fa86a4960835677a5c9c993df11d256d6e3d1`

3. **Second Secret:**
   - Name: `SONAR_HOST_URL`
   - Value: `https://sonarcloud.io`

4. **Re-run the workflow:**
   - Go to: https://github.com/KONJETIVEERASHANKARUDU/testenvfor_sonar/actions
   - Click on the latest workflow
   - Click "Re-run all jobs"

---

## 🌐 Step 3: Check SonarCloud Dashboard

**Open this link:**
👉 https://sonarcloud.io/project/overview?id=KONJETIVEERASHANKARUDU_testenvfor_sonar

### Possible Outcomes:

#### ✅ **Dashboard Loads with Analysis Results**
- Success! You should see:
  - Quality Gate status
  - Bugs, Vulnerabilities, Code Smells
  - Security Hotspots
  - Code Coverage

#### ❌ **"Project not found" or "Component key not found"**
- Workflow hasn't completed successfully yet
- Check GitHub Actions first (Step 1)

#### ⚠️ **"No analysis found"**
- First analysis hasn't completed yet
- Check if workflow is running (Step 1)

---

## 🛠️ Step 4: Manual Workflow Trigger

If nothing is happening:

### **Option A: Via Web**
1. Go to: https://github.com/KONJETIVEERASHANKARUDU/testenvfor_sonar/actions
2. Click "SonarQube Analysis" (left sidebar)
3. Click "Run workflow" button (right side)
4. Click green "Run workflow" button
5. Wait 2-3 minutes

### **Option B: Via Command Line**
```bash
cd /Users/veera.konjeti/Desktop/testenvfor_sonar
git commit --allow-empty -m "Trigger analysis"
git push origin main
```

---

## 📊 Step 5: Check Workflow Logs

If workflow is failing:

1. Go to: https://github.com/KONJETIVEERASHANKARUDU/testenvfor_sonar/actions
2. Click on the failed workflow run
3. Click on "SonarQube Scan" job
4. Expand each step to see error details

### Common Error Messages:

#### **"Error: Secrets.SONAR_TOKEN is not set"**
**Solution:** Add the SONAR_TOKEN secret (see Step 2)

#### **"Error: Invalid SonarQube token"**
**Solution:** 
- Token might be wrong
- Go to: https://sonarcloud.io/account/security
- Generate new token
- Update GitHub secret

#### **"Error: Project key not found"**
**Solution:**
- Project not created in SonarCloud
- Go to: https://sonarcloud.io/projects/create
- Import the repository

#### **"Error: 401 Unauthorized"**
**Solution:**
- Token is invalid or expired
- Generate new token from SonarCloud
- Update GitHub secret

---

## ✅ Quick Verification Script

Run this command to check your setup:

```bash
cd /Users/veera.konjeti/Desktop/testenvfor_sonar
./check_status.sh
```

This will show:
- Configuration status
- Links to all relevant pages
- What might be missing

---

## 🎯 Complete Checklist

Use this to verify everything:

- [ ] GitHub secrets are added (SONAR_TOKEN, SONAR_HOST_URL)
- [ ] Workflow file exists (.github/workflows/sonarqube.yml)
- [ ] Latest commit was pushed to main branch
- [ ] GitHub Actions workflow has run (check Actions tab)
- [ ] Workflow completed successfully (green checkmark)
- [ ] SonarCloud project exists and is configured
- [ ] SonarCloud dashboard loads with results

---

## 🆘 Still Having Issues?

### Check These:

1. **GitHub Actions Enabled?**
   - Go to: https://github.com/KONJETIVEERASHANKARUDU/testenvfor_sonar/settings/actions
   - Ensure Actions are enabled

2. **Correct Branch?**
   ```bash
   git branch
   # Should show: * main
   ```

3. **Latest Code Pushed?**
   ```bash
   git status
   # Should show: "Your branch is up to date with 'origin/main'"
   ```

4. **SonarCloud Project Setup?**
   - Go to: https://sonarcloud.io/projects
   - Verify `testenvfor_sonar` is listed

---

## 📞 Support Links

- **GitHub Actions Docs:** https://docs.github.com/en/actions
- **SonarCloud Docs:** https://docs.sonarcloud.io
- **SonarCloud Community:** https://community.sonarsource.com

---

## 🔄 Fresh Start (If Nothing Works)

1. **Verify secrets one more time:**
   https://github.com/KONJETIVEERASHANKARUDU/testenvfor_sonar/settings/secrets/actions

2. **Trigger workflow manually:**
   ```bash
   cd /Users/veera.konjeti/Desktop/testenvfor_sonar
   git commit --allow-empty -m "Force trigger SonarQube"
   git push origin main
   ```

3. **Watch it run:**
   https://github.com/KONJETIVEERASHANKARUDU/testenvfor_sonar/actions

4. **Check results in 3 minutes:**
   https://sonarcloud.io/project/overview?id=KONJETIVEERASHANKARUDU_testenvfor_sonar
