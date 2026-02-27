# 🌐 Multi-Language Support in SonarQube

This repository is configured to analyze **multiple programming languages** automatically using SonarQube.

## 📋 Supported Languages

Your SonarQube setup now supports the following languages:

### ✅ Fully Configured
- **Java** (JDK 21)
- **JavaScript** (ES6+)
- **TypeScript** (with tsconfig.json support)
- **Python** (3.8 - 3.12)
- **Go** (1.22)

### 🔧 Auto-Detected
SonarQube will automatically analyze these languages if found:
- **C#** (.cs files)
- **PHP** (.php files)
- **Ruby** (.rb files)
- **Kotlin** (.kt files)
- **Scala** (.scala files)
- **Swift** (.swift files)
- **HTML/CSS** (.html, .css, .scss, .sass, .less)
- **XML** (.xml files)
- **YAML** (.yml, .yaml files)
- **JSON** (.json files)
- **Dockerfile**
- **Terraform** (.tf files)

## 🚀 How It Works

### 1. **GitHub Actions Workflow**
The `.github/workflows/sonarqube.yml` file now:
- Sets up Java 21, Node.js 20, Python 3.12, and Go 1.22
- Installs dependencies for each language automatically
- Runs tests and generates coverage reports
- Analyzes all languages in a single scan

### 2. **Automatic Language Detection**
SonarQube automatically detects languages based on file extensions:
```
.java    → Java
.js      → JavaScript
.ts      → TypeScript
.py      → Python
.go      → Go
.cs      → C#
.php     → PHP
.rb      → Ruby
.kt      → Kotlin
etc...
```

### 3. **Configuration Files**

#### `sonar-project.properties`
Configured to:
- Scan all source files (except excluded directories)
- Support multiple coverage report formats
- Set language-specific options
- Exclude test files from coverage metrics

#### `.github/workflows/sonarqube.yml`
Configured to:
- Install all language runtimes
- Build and test each language
- Run comprehensive multi-language analysis

## 📦 Project Structure Examples

### Java Project
```
src/
├── main/java/     # Java source files
└── test/java/     # Java test files
target/            # Compiled classes
pom.xml            # Maven configuration
```

### JavaScript/TypeScript Project
```
src/
├── index.js       # JavaScript files
├── app.ts         # TypeScript files
└── __tests__/     # Test files
node_modules/      # Dependencies (excluded)
package.json       # NPM configuration
tsconfig.json      # TypeScript config
```

### Python Project
```
src/
├── main.py        # Python source
└── tests/         # Python tests
requirements.txt   # Dependencies
coverage.xml       # Coverage report
```

### Go Project
```
main.go
cmd/              # Go source
pkg/              # Go packages
*_test.go         # Go tests
go.mod            # Go modules
coverage.out      # Coverage report
```

### Mixed Project (Current)
```
src/
├── main/
│   ├── java/      # ✅ Java code
│   ├── javascript/# ✅ JS code
│   ├── python/    # ✅ Python code
│   └── resources/
└── test/
    ├── java/      # Java tests
    ├── javascript/# JS tests
    └── python/    # Python tests
```

## 🎯 Coverage Reports

### Required Coverage File Paths

Each language has specific coverage report paths:

| Language | Coverage Format | Report Path |
|----------|----------------|-------------|
| **Java** | JaCoCo XML | `target/site/jacoco/jacoco.xml` |
| **JavaScript/TypeScript** | LCOV | `coverage/lcov.info` |
| **Python** | Coverage.py XML | `coverage.xml` |
| **Go** | Go Cover | `coverage.out` |
| **C#** | OpenCover XML | `coverage.opencover.xml` |
| **PHP** | Clover XML | `coverage.xml` |

### Generating Coverage Reports

#### Java (Maven + JaCoCo)
```bash
mvn clean test
# Report generated at: target/site/jacoco/jacoco.xml
```

#### JavaScript (Jest)
```bash
npm test -- --coverage
# Report generated at: coverage/lcov.info
```

#### Python (pytest-cov)
```bash
pytest --cov=src --cov-report=xml
# Report generated at: coverage.xml
```

#### Go
```bash
go test -coverprofile=coverage.out ./...
# Report generated at: coverage.out
```

## 🔒 Exclusions

The following are excluded from analysis:

### Directories
- `**/target/**` - Maven build output
- `**/node_modules/**` - NPM dependencies
- `**/dist/**` - Build distributions
- `**/build/**` - Build output
- `**/coverage/**` - Coverage reports
- `**/vendor/**` - Third-party code
- `**/.git/**` - Git metadata
- `**/.idea/**` - IDE settings
- `**/.vscode/**` - VS Code settings

### Files
- `**/*.min.js` - Minified JavaScript
- `**/*.min.css` - Minified CSS
- `**/*.map` - Source maps
- `**/*.md` - Markdown documentation

### Test Files (Coverage Exclusion)
- `**/test/**`
- `**/tests/**`
- `**/*Test.java`
- `**/*Spec.ts`
- `**/*Spec.js`
- `**/*test.py`
- `**/*_test.go`

## 🛠️ Adding a New Language

### Step 1: Add Runtime Setup (if needed)
Edit `.github/workflows/sonarqube.yml`:

```yaml
- name: Set up Ruby (example)
  uses: ruby/setup-ruby@v1
  with:
    ruby-version: '3.2'
```

### Step 2: Add Dependencies Installation (if needed)
```yaml
- name: Install Ruby dependencies
  run: |
    if [ -f "Gemfile" ]; then
      bundle install
    fi
  continue-on-error: true
```

### Step 3: Add Coverage Configuration
In `sonar-project.properties`:
```properties
# Ruby Settings
sonar.ruby.coverage.reportPaths=coverage/coverage.xml
```

### Step 4: Update Exclusions (if needed)
```properties
sonar.exclusions=\
  **/existing-exclusions/**,\
  **/new-language-specific-exclusion/**
```

## 📊 Viewing Results

### SonarCloud Dashboard
1. Go to: https://sonarcloud.io/project/overview?id=KONJETIVEERASHANKARUDU_testenvfor_sonar
2. Navigate to **"Code"** tab
3. Filter by language using the language selector

### Local SonarQube
1. Go to: http://localhost:9000
2. Select your project
3. View language-specific metrics in the dashboard

## 🔍 Language-Specific Analysis Rules

SonarQube analyzes each language with specific rule sets:

### Java
- **150+ rules** covering:
  - Security vulnerabilities
  - Code smells
  - Bugs
  - Performance issues
  - Best practices

### JavaScript/TypeScript
- **200+ rules** covering:
  - ES6+ features
  - React/Angular/Vue patterns
  - Security issues
  - Type safety (TypeScript)

### Python
- **180+ rules** covering:
  - PEP 8 compliance
  - Security vulnerabilities
  - Common bugs
  - Code complexity

### Go
- **80+ rules** covering:
  - Go conventions
  - Concurrency issues
  - Error handling
  - Performance

## 📈 Quality Gate Configuration

The Quality Gate applies to **all languages**:

- **Coverage**: Minimum 80% on new code
- **Duplications**: < 3% on new code
- **Maintainability Rating**: A
- **Reliability Rating**: A
- **Security Rating**: A
- **Security Hotspots**: All reviewed

## 🎨 IDE Integration

### VS Code
Install: **SonarLint for VS Code**
```bash
code --install-extension SonarSource.sonarlint-vscode
```

### IntelliJ IDEA
Install: **SonarLint Plugin**
- Settings → Plugins → Search "SonarLint" → Install

### PyCharm
Install: **SonarLint Plugin**
- Settings → Plugins → Search "SonarLint" → Install

## 🐛 Troubleshooting

### Issue: Language Not Detected
**Solution**: Ensure files have correct extensions and are not in excluded directories.

### Issue: Coverage Report Not Found
**Solution**: Check that coverage reports are generated in the specified paths before SonarQube analysis.

### Issue: Analysis Takes Too Long
**Solution**: Add more exclusions for unnecessary files (e.g., generated code, vendor directories).

### Issue: Memory Issues
**Solution**: Increase Node.js heap size in workflow:
```yaml
-Dsonar.javascript.node.maxspace=8192
```

## 📚 Additional Resources

- **SonarQube Docs**: https://docs.sonarsource.com/sonarqube/latest/
- **Language Plugins**: https://docs.sonarqube.org/latest/analysis/languages/overview/
- **Coverage Formats**: https://docs.sonarqube.org/latest/analyzing-source-code/test-coverage/overview/
- **GitHub Actions**: https://docs.github.com/en/actions

## ✅ Current Configuration Summary

```
✓ Java 21 with Maven & JaCoCo
✓ JavaScript/TypeScript with Node.js 20
✓ Python 3.12 with pytest-cov support
✓ Go 1.22 with coverage support
✓ Auto-detection for 20+ languages
✓ Comprehensive exclusion rules
✓ Multi-format coverage support
✓ Optimized for CI/CD pipeline
```

## 🚦 Next Steps

1. **Add code** in any supported language to `src/` directory
2. **Push to GitHub** - automatic analysis will run
3. **View results** on SonarCloud dashboard
4. **Fix issues** highlighted by SonarQube
5. **Maintain quality** with continuous monitoring

---

**Note**: Language support is automatic. Just add files with the appropriate extensions, and SonarQube will analyze them! 🎉
