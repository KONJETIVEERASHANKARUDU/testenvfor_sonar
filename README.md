# testenvfor_sonar

This repository is configured with SonarQube analysis for continuous code quality inspection.

## 🚀 SonarQube Integration

This project uses SonarQube for static code analysis to detect bugs, vulnerabilities, and code smells.

### Setup Instructions

#### 1. Set up SonarQube Server

Choose one of the following options:

**Option A: SonarCloud (Recommended for free public repositories)**
1. Go to [SonarCloud](https://sonarcloud.io/)
2. Sign up/Login with your GitHub account
3. Click "Analyze new project"
4. Select this repository
5. Follow the setup wizard

**Option B: Self-hosted SonarQube**
1. Install SonarQube Community Edition (free)
2. Access the server URL (e.g., `http://localhost:9000`)
3. Create a new project
4. Generate a token

#### 2. Configure GitHub Secrets

Add the following secrets to your GitHub repository:

1. Go to **Settings** → **Secrets and variables** → **Actions**
2. Click **New repository secret**
3. Add these secrets:

| Secret Name | Description | Example Value |
|-------------|-------------|---------------|
| `SONAR_TOKEN` | Authentication token from SonarQube/SonarCloud | `sqp_xxxxxxxxxxxxx` |
| `SONAR_HOST_URL` | SonarQube server URL | `https://sonarcloud.io` or `http://your-sonar-server:9000` |

**For SonarCloud:**
- `SONAR_HOST_URL` = `https://sonarcloud.io`
- `SONAR_TOKEN` = Generate from: Account → Security → Generate Tokens

**For Self-hosted SonarQube:**
- `SONAR_HOST_URL` = Your SonarQube server URL
- `SONAR_TOKEN` = Generate from: My Account → Security → Generate Tokens

#### 3. Update sonar-project.properties

Edit the `sonar-project.properties` file to match your project:

```properties
sonar.projectKey=your-organization_testenvfor_sonar
sonar.organization=your-organization  # Only for SonarCloud
```

#### 4. Run the Analysis

The workflow will automatically run on:
- Push to `main` branch
- Pull requests to `main` branch
- Manual trigger from Actions tab

To manually trigger:
1. Go to **Actions** tab
2. Select **SonarQube Analysis** workflow
3. Click **Run workflow**

### 📊 View Results

**SonarCloud:**
Visit `https://sonarcloud.io/project/overview?id=your-project-key`

**Self-hosted:**
Visit your SonarQube server URL and navigate to your project

### 📝 Configuration Files

- `.github/workflows/sonarqube.yml` - GitHub Actions workflow
- `sonar-project.properties` - SonarQube project configuration

### 🔧 Customization

To customize the analysis:

1. **Exclude files/directories:** Edit `sonar.exclusions` in `sonar-project.properties`
2. **Add coverage reports:** Uncomment and configure coverage paths
3. **Language-specific settings:** Add language-specific properties

### 📚 Resources

- [SonarQube Documentation](https://docs.sonarqube.org/)
- [SonarCloud Documentation](https://docs.sonarcloud.io/)
- [GitHub Actions for SonarQube](https://github.com/SonarSource/sonarqube-scan-action)

## 📄 License

This project is open source and available under the MIT License.
