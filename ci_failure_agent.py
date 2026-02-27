#!/usr/bin/env python3
"""
CI Failure Analysis & Auto-Fix Agent
Monitors CI/CD pipeline failures, analyzes issues, and suggests/applies fixes automatically.
"""

import os
import sys
import json
import re
import subprocess
from pathlib import Path
from typing import Dict, List, Optional, Tuple
from datetime import datetime
import requests

class CIFailureAgent:
    """Intelligent agent for analyzing and fixing CI/CD failures"""
    
    def __init__(self, repo_path: str = "."):
        self.repo_path = Path(repo_path)
        self.github_token = os.getenv("GITHUB_TOKEN")
        self.github_repo = os.getenv("GITHUB_REPOSITORY")
        self.run_id = os.getenv("GITHUB_RUN_ID")
        self.workflow_name = os.getenv("GITHUB_WORKFLOW")
        
    def analyze_failure(self, failure_logs: str, job_name: str) -> Dict:
        """Analyze failure logs and categorize the issue"""
        
        failure_patterns = {
            "quality_gate": {
                "patterns": [
                    r"Quality Gate.*failed",
                    r"QUALITY_GATE_STATUS.*FAILED",
                    r"coverage.*below.*threshold",
                    r"code smells.*exceeded",
                    r"duplications.*exceeded"
                ],
                "severity": "high",
                "category": "code_quality"
            },
            "test_failure": {
                "patterns": [
                    r"Tests run:.*Failures: [1-9]",
                    r"FAILED.*tests",
                    r"\d+ test.*failed",
                    r"AssertionError",
                    r"junit.*failures"
                ],
                "severity": "high",
                "category": "tests"
            },
            "build_failure": {
                "patterns": [
                    r"BUILD FAILURE",
                    r"compilation error",
                    r"cannot find symbol",
                    r"package.*does not exist",
                    r"npm ERR!",
                    r"yarn error"
                ],
                "severity": "critical",
                "category": "build"
            },
            "dependency_issue": {
                "patterns": [
                    r"Could not resolve dependencies",
                    r"dependency.*not found",
                    r"Failed to download",
                    r"Unknown dependency",
                    r"PEER DEP"
                ],
                "severity": "high",
                "category": "dependencies"
            },
            "security_vulnerability": {
                "patterns": [
                    r"vulnerability.*found",
                    r"security.*issue",
                    r"CVE-\d{4}-\d+",
                    r"high severity",
                    r"critical severity"
                ],
                "severity": "critical",
                "category": "security"
            },
            "lint_error": {
                "patterns": [
                    r"lint.*error",
                    r"eslint.*error",
                    r"checkstyle.*violation",
                    r"code style.*violation"
                ],
                "severity": "medium",
                "category": "linting"
            },
            "timeout": {
                "patterns": [
                    r"timeout",
                    r"timed out",
                    r"execution.*exceeded"
                ],
                "severity": "medium",
                "category": "performance"
            },
            "docker_issue": {
                "patterns": [
                    r"docker.*error",
                    r"failed to build.*image",
                    r"manifest.*not found",
                    r"pull access denied"
                ],
                "severity": "high",
                "category": "docker"
            }
        }
        
        detected_issues = []
        for issue_type, config in failure_patterns.items():
            for pattern in config["patterns"]:
                if re.search(pattern, failure_logs, re.IGNORECASE):
                    detected_issues.append({
                        "type": issue_type,
                        "severity": config["severity"],
                        "category": config["category"],
                        "pattern_matched": pattern
                    })
                    break
        
        return {
            "job_name": job_name,
            "issues": detected_issues,
            "logs_snippet": self._extract_error_snippet(failure_logs),
            "timestamp": datetime.now().isoformat()
        }
    
    def _extract_error_snippet(self, logs: str, context_lines: int = 10) -> str:
        """Extract relevant error snippet from logs"""
        lines = logs.split('\n')
        error_keywords = ['error', 'failed', 'failure', 'exception', 'fatal']
        
        error_lines = []
        for i, line in enumerate(lines):
            if any(keyword in line.lower() for keyword in error_keywords):
                start = max(0, i - context_lines)
                end = min(len(lines), i + context_lines + 1)
                error_lines.extend(lines[start:end])
                break
        
        return '\n'.join(error_lines[-50:])  # Last 50 lines max
    
    def suggest_fixes(self, analysis: Dict) -> List[Dict]:
        """Generate fix suggestions based on analysis"""
        
        fixes = []
        
        for issue in analysis["issues"]:
            issue_type = issue["type"]
            
            if issue_type == "quality_gate":
                fixes.append({
                    "title": "Fix Quality Gate Issues",
                    "description": "Code quality metrics are below threshold",
                    "suggestions": [
                        "Run SonarQube analysis locally: `mvn clean verify sonar:sonar`",
                        "Check coverage: Aim for 80%+ coverage",
                        "Reduce code smells: Remove duplicated code, fix cognitive complexity",
                        "Review SonarQube dashboard for specific issues"
                    ],
                    "auto_fixable": False,
                    "commands": [
                        "mvn clean test jacoco:report",
                        "# Review target/site/jacoco/index.html for coverage gaps"
                    ]
                })
            
            elif issue_type == "test_failure":
                fixes.append({
                    "title": "Fix Failing Tests",
                    "description": "Unit tests are failing",
                    "suggestions": [
                        "Run tests locally: `mvn test` or `npm test`",
                        "Check test logs for specific failures",
                        "Update test assertions if business logic changed",
                        "Fix broken test data or mocks"
                    ],
                    "auto_fixable": False,
                    "commands": [
                        "mvn test -Dtest=FailedTestClass",
                        "npm test -- --verbose"
                    ]
                })
            
            elif issue_type == "build_failure":
                fixes.append({
                    "title": "Fix Build Errors",
                    "description": "Compilation or build process failed",
                    "suggestions": [
                        "Check for missing imports or dependencies",
                        "Verify Java/Node/Python version compatibility",
                        "Run clean build: `mvn clean install` or `npm ci`",
                        "Check for syntax errors in recent changes"
                    ],
                    "auto_fixable": True,
                    "commands": [
                        "mvn clean compile",
                        "npm install --force"
                    ]
                })
            
            elif issue_type == "dependency_issue":
                fixes.append({
                    "title": "Fix Dependency Issues",
                    "description": "Dependencies could not be resolved",
                    "suggestions": [
                        "Update dependency versions in pom.xml/package.json",
                        "Clear dependency cache",
                        "Check for conflicting versions",
                        "Verify repository accessibility"
                    ],
                    "auto_fixable": True,
                    "commands": [
                        "mvn dependency:purge-local-repository",
                        "mvn dependency:resolve",
                        "npm cache clean --force && npm install"
                    ]
                })
            
            elif issue_type == "security_vulnerability":
                fixes.append({
                    "title": "Fix Security Vulnerabilities",
                    "description": "Security vulnerabilities detected",
                    "suggestions": [
                        "Update vulnerable dependencies to latest secure versions",
                        "Check CVE details and apply patches",
                        "Run: `npm audit fix` or update in pom.xml",
                        "Review Snyk/Trivy reports for specific CVEs"
                    ],
                    "auto_fixable": True,
                    "commands": [
                        "npm audit fix",
                        "mvn versions:use-latest-versions",
                        "# Update specific vulnerable packages"
                    ]
                })
            
            elif issue_type == "lint_error":
                fixes.append({
                    "title": "Fix Linting Issues",
                    "description": "Code style violations detected",
                    "suggestions": [
                        "Run auto-formatter: `mvn spotless:apply` or `npm run lint:fix`",
                        "Fix remaining manual issues",
                        "Update .eslintrc or checkstyle.xml if needed"
                    ],
                    "auto_fixable": True,
                    "commands": [
                        "mvn spotless:apply",
                        "npm run lint:fix",
                        "prettier --write ."
                    ]
                })
            
            elif issue_type == "docker_issue":
                fixes.append({
                    "title": "Fix Docker Build Issues",
                    "description": "Docker image build or push failed",
                    "suggestions": [
                        "Check Dockerfile syntax",
                        "Verify base image exists and is accessible",
                        "Check Docker registry credentials",
                        "Build locally: `docker build -t test .`"
                    ],
                    "auto_fixable": False,
                    "commands": [
                        "docker build --no-cache -t testimage .",
                        "docker login"
                    ]
                })
        
        return fixes
    
    def apply_auto_fix(self, fix: Dict) -> Tuple[bool, str]:
        """Apply automatic fixes if possible"""
        
        if not fix.get("auto_fixable"):
            return False, "Fix requires manual intervention"
        
        results = []
        for command in fix.get("commands", []):
            if command.startswith("#"):
                continue
                
            try:
                print(f"Executing: {command}")
                result = subprocess.run(
                    command,
                    shell=True,
                    cwd=self.repo_path,
                    capture_output=True,
                    text=True,
                    timeout=300
                )
                
                if result.returncode == 0:
                    results.append(f"✓ {command}")
                else:
                    results.append(f"✗ {command}\n{result.stderr[:500]}")
                    
            except subprocess.TimeoutExpired:
                results.append(f"⏱ {command} (timed out)")
            except Exception as e:
                results.append(f"✗ {command}: {str(e)}")
        
        return True, "\n".join(results)
    
    def create_fix_commit(self, fixes_applied: List[str], branch: str):
        """Create a commit with applied fixes"""
        
        try:
            # Check if there are changes
            result = subprocess.run(
                ["git", "status", "--porcelain"],
                cwd=self.repo_path,
                capture_output=True,
                text=True
            )
            
            if not result.stdout.strip():
                return False, "No changes to commit"
            
            # Add all changes
            subprocess.run(["git", "add", "."], cwd=self.repo_path, check=True)
            
            # Create commit
            commit_msg = f"""fix: Auto-fix CI failures

Applied automatic fixes:
{chr(10).join(f'- {fix}' for fix in fixes_applied)}

Generated by CI Failure Agent
[skip ci]
"""
            
            subprocess.run(
                ["git", "commit", "-m", commit_msg],
                cwd=self.repo_path,
                check=True
            )
            
            # Push changes
            subprocess.run(
                ["git", "push", "origin", branch],
                cwd=self.repo_path,
                check=True
            )
            
            return True, f"Fixes committed and pushed to {branch}"
            
        except subprocess.CalledProcessError as e:
            return False, f"Failed to commit: {str(e)}"
    
    def post_github_comment(self, pr_number: int, analysis: Dict, fixes: List[Dict]):
        """Post analysis and suggestions as PR comment"""
        
        if not self.github_token or not self.github_repo:
            print("GitHub token or repo not configured")
            return
        
        # Build comment
        comment = f"""## 🤖 CI Failure Analysis

**Workflow:** {self.workflow_name}  
**Run ID:** {self.run_id}  
**Timestamp:** {datetime.now().strftime('%Y-%m-%d %H:%M:%S UTC')}

### 🔍 Issues Detected

"""
        
        for issue in analysis["issues"]:
            severity_emoji = {
                "critical": "🔴",
                "high": "🟠",
                "medium": "🟡"
            }.get(issue["severity"], "⚪")
            
            comment += f"{severity_emoji} **{issue['type'].replace('_', ' ').title()}** (Severity: {issue['severity']})\n"
        
        comment += "\n### 💡 Suggested Fixes\n\n"
        
        for idx, fix in enumerate(fixes, 1):
            auto_fix_badge = "🔧 Auto-fixable" if fix.get("auto_fixable") else "👤 Manual fix required"
            
            comment += f"""
<details>
<summary><strong>{idx}. {fix['title']}</strong> - {auto_fix_badge}</summary>

**Description:** {fix['description']}

**Suggestions:**
"""
            for suggestion in fix['suggestions']:
                comment += f"- {suggestion}\n"
            
            if fix.get('commands'):
                comment += "\n**Commands to run:**\n```bash\n"
                comment += "\n".join(fix['commands'])
                comment += "\n```\n"
            
            comment += "</details>\n"
        
        # Add error snippet
        if analysis.get("logs_snippet"):
            comment += f"""
### 📋 Error Logs Snippet

```
{analysis['logs_snippet'][:1000]}
```
"""
        
        comment += """
### 🔄 Next Steps

The agent will automatically **retry** the failed job once. If it fails again:
1. Review the suggestions above
2. Apply fixes manually or accept auto-fixes
3. Push changes to re-trigger the pipeline

---
*Generated by CI Failure Agent* 🤖
"""
        
        # Post comment via GitHub API
        url = f"https://api.github.com/repos/{self.github_repo}/issues/{pr_number}/comments"
        headers = {
            "Authorization": f"token {self.github_token}",
            "Accept": "application/vnd.github.v3+json"
        }
        
        response = requests.post(url, headers=headers, json={"body": comment})
        
        if response.status_code == 201:
            print(f"✓ Comment posted to PR #{pr_number}")
        else:
            print(f"✗ Failed to post comment: {response.status_code}")
    
    def retry_failed_job(self) -> bool:
        """Trigger a retry of the failed job"""
        
        if not self.github_token or not self.github_repo or not self.run_id:
            print("GitHub credentials not configured")
            return False
        
        url = f"https://api.github.com/repos/{self.github_repo}/actions/runs/{self.run_id}/rerun-failed-jobs"
        headers = {
            "Authorization": f"token {self.github_token}",
            "Accept": "application/vnd.github.v3+json"
        }
        
        response = requests.post(url, headers=headers)
        
        if response.status_code == 201:
            print("✓ Job retry triggered")
            return True
        else:
            print(f"✗ Failed to trigger retry: {response.status_code}")
            return False
    
    def generate_report(self, analysis: Dict, fixes: List[Dict]) -> str:
        """Generate a detailed report"""
        
        report = f"""
===========================================
CI FAILURE ANALYSIS REPORT
===========================================

Workflow: {self.workflow_name}
Job: {analysis['job_name']}
Timestamp: {analysis['timestamp']}
Run ID: {self.run_id}

ISSUES DETECTED
---------------
"""
        
        for issue in analysis['issues']:
            report += f"""
Type: {issue['type']}
Severity: {issue['severity']}
Category: {issue['category']}
Pattern: {issue['pattern_matched']}
"""
        
        report += "\n\nSUGGESTED FIXES\n---------------\n"
        
        for idx, fix in enumerate(fixes, 1):
            report += f"""
{idx}. {fix['title']}
   Auto-fixable: {fix.get('auto_fixable', False)}
   Description: {fix['description']}
   
   Suggestions:
"""
            for suggestion in fix['suggestions']:
                report += f"   - {suggestion}\n"
        
        report += "\n\nERROR SNIPPET\n-------------\n"
        report += analysis.get('logs_snippet', 'No snippet available')
        
        return report


def main():
    """Main execution flow"""
    
    print("🤖 CI Failure Agent Starting...")
    
    # Get input parameters
    job_name = os.getenv("JOB_NAME", "unknown")
    failure_logs_file = os.getenv("FAILURE_LOGS_FILE")
    pr_number = os.getenv("PR_NUMBER")
    auto_fix = os.getenv("AUTO_FIX", "false").lower() == "true"
    retry_enabled = os.getenv("RETRY_ENABLED", "true").lower() == "true"
    branch = os.getenv("GITHUB_REF_NAME", "main")
    
    agent = CIFailureAgent()
    
    # Read failure logs
    if failure_logs_file and Path(failure_logs_file).exists():
        with open(failure_logs_file) as f:
            logs = f.read()
    else:
        logs = sys.stdin.read() if not sys.stdin.isatty() else ""
    
    if not logs:
        print("⚠️  No failure logs provided")
        sys.exit(1)
    
    # Analyze failure
    print(f"\n🔍 Analyzing failure in job: {job_name}")
    analysis = agent.analyze_failure(logs, job_name)
    
    if not analysis['issues']:
        print("ℹ️  No specific issues detected in logs")
        sys.exit(0)
    
    print(f"✓ Detected {len(analysis['issues'])} issue(s)")
    
    # Generate fix suggestions
    print("\n💡 Generating fix suggestions...")
    fixes = agent.suggest_fixes(analysis)
    print(f"✓ Generated {len(fixes)} fix suggestion(s)")
    
    # Generate report
    report = agent.generate_report(analysis, fixes)
    print("\n" + report)
    
    # Save report
    report_file = Path("ci_failure_report.txt")
    report_file.write_text(report)
    print(f"\n✓ Report saved to {report_file}")
    
    # Post to GitHub PR if available
    if pr_number:
        print(f"\n📝 Posting analysis to PR #{pr_number}")
        agent.post_github_comment(int(pr_number), analysis, fixes)
    
    # Apply auto-fixes if enabled
    if auto_fix:
        print("\n🔧 Applying automatic fixes...")
        fixes_applied = []
        
        for fix in fixes:
            if fix.get("auto_fixable"):
                print(f"\nApplying: {fix['title']}")
                success, result = agent.apply_auto_fix(fix)
                print(result)
                
                if success:
                    fixes_applied.append(fix['title'])
        
        if fixes_applied:
            print("\n📝 Creating fix commit...")
            success, message = agent.create_fix_commit(fixes_applied, branch)
            print(message)
    
    # Retry failed job if enabled
    if retry_enabled:
        print("\n🔄 Triggering job retry...")
        agent.retry_failed_job()
    
    print("\n✓ CI Failure Agent Completed")


if __name__ == "__main__":
    main()
