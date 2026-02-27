#!/usr/bin/env python3
"""
Organization-Wide Repository Monitor Agent
Scans all repositories, all branches, detects issues, and suggests fixes automatically.
"""

import os
import sys
import json
import re
import subprocess
from pathlib import Path
from typing import Dict, List, Optional, Set
from datetime import datetime, timedelta
import requests
from collections import defaultdict

class OrgMonitorAgent:
    """Monitor all repositories and branches for issues"""
    
    def __init__(self):
        self.github_token = os.getenv("GITHUB_TOKEN")
        self.github_user = os.getenv("GITHUB_USER", "KONJETIVEERASHANKARUDU")
        self.api_base = "https://api.github.com"
        self.headers = {
            "Authorization": f"token {self.github_token}",
            "Accept": "application/vnd.github.v3+json"
        }
        self.issues_found = []
        
    def get_all_repositories(self) -> List[Dict]:
        """Get all repositories for the user/organization"""
        
        print(f"🔍 Fetching all repositories for {self.github_user}...")
        
        repos = []
        page = 1
        
        while True:
            url = f"{self.api_base}/users/{self.github_user}/repos"
            params = {
                "page": page,
                "per_page": 100,
                "sort": "updated",
                "direction": "desc"
            }
            
            response = requests.get(url, headers=self.headers, params=params)
            
            if response.status_code != 200:
                print(f"✗ Error fetching repos: {response.status_code}")
                break
            
            batch = response.json()
            if not batch:
                break
                
            repos.extend(batch)
            page += 1
        
        print(f"✓ Found {len(repos)} repositories")
        return repos
    
    def get_all_branches(self, repo_name: str) -> List[Dict]:
        """Get all branches for a repository"""
        
        url = f"{self.api_base}/repos/{self.github_user}/{repo_name}/branches"
        
        try:
            response = requests.get(url, headers=self.headers)
            if response.status_code == 200:
                branches = response.json()
                return branches
            else:
                return []
        except Exception as e:
            print(f"✗ Error fetching branches for {repo_name}: {e}")
            return []
    
    def check_workflow_status(self, repo_name: str) -> Dict:
        """Check CI/CD workflow status for repository"""
        
        url = f"{self.api_base}/repos/{self.github_user}/{repo_name}/actions/runs"
        params = {"per_page": 10}
        
        try:
            response = requests.get(url, headers=self.headers, params=params)
            if response.status_code != 200:
                return {"status": "unknown", "runs": []}
            
            runs = response.json().get("workflow_runs", [])
            
            # Analyze recent runs
            failed_runs = [r for r in runs if r["conclusion"] == "failure"]
            success_runs = [r for r in runs if r["conclusion"] == "success"]
            
            failure_rate = len(failed_runs) / len(runs) if runs else 0
            
            return {
                "status": "healthy" if failure_rate < 0.3 else "failing",
                "failure_rate": failure_rate,
                "total_runs": len(runs),
                "failed_runs": len(failed_runs),
                "recent_failures": failed_runs[:3]
            }
        except Exception as e:
            return {"status": "error", "error": str(e)}
    
    def detect_duplicate_code(self, repo_name: str, branch: str) -> List[Dict]:
        """Detect duplicate code in repository"""
        
        # Clone or use existing repo
        repo_path = Path(f"/tmp/{repo_name}")
        
        duplicates = []
        
        try:
            # Clone if not exists
            if not repo_path.exists():
                clone_url = f"https://github.com/{self.github_user}/{repo_name}.git"
                subprocess.run(
                    ["git", "clone", "--depth", "1", "-b", branch, clone_url, str(repo_path)],
                    capture_output=True,
                    timeout=60
                )
            
            # Simple duplicate detection using file hashing
            file_hashes = defaultdict(list)
            
            for file_path in repo_path.rglob("*.java"):
                if "target" in file_path.parts or "build" in file_path.parts:
                    continue
                
                try:
                    content = file_path.read_text()
                    lines = content.split('\n')
                    
                    # Check for duplicate lines (5+ consecutive lines)
                    for i in range(len(lines) - 5):
                        block = '\n'.join(lines[i:i+5])
                        block_stripped = re.sub(r'\s+', '', block)
                        
                        if len(block_stripped) > 50:  # Meaningful code
                            file_hashes[block_stripped].append({
                                "file": str(file_path.relative_to(repo_path)),
                                "line": i + 1,
                                "code": block
                            })
                except Exception:
                    continue
            
            # Find duplicates
            for block_hash, occurrences in file_hashes.items():
                if len(occurrences) > 1:
                    duplicates.append({
                        "type": "duplicate_code",
                        "severity": "medium",
                        "occurrences": occurrences,
                        "suggestion": "Extract to a common method or utility class"
                    })
            
        except subprocess.TimeoutExpired:
            print(f"  ⏱ Timeout cloning {repo_name}")
        except Exception as e:
            print(f"  ✗ Error analyzing {repo_name}: {e}")
        
        return duplicates[:10]  # Return top 10
    
    def check_code_quality(self, repo_name: str, branch: str) -> Dict:
        """Check code quality metrics"""
        
        repo_path = Path(f"/tmp/{repo_name}")
        
        issues = {
            "long_methods": [],
            "complex_files": [],
            "missing_tests": [],
            "large_files": []
        }
        
        if not repo_path.exists():
            return issues
        
        try:
            # Check for long files
            for file_path in repo_path.rglob("*.java"):
                if "target" in file_path.parts:
                    continue
                
                try:
                    lines = file_path.read_text().split('\n')
                    line_count = len(lines)
                    
                    if line_count > 500:
                        issues["large_files"].append({
                            "file": str(file_path.relative_to(repo_path)),
                            "lines": line_count,
                            "suggestion": "Consider splitting into smaller classes"
                        })
                    
                    # Check for long methods (simple heuristic)
                    in_method = False
                    method_start = 0
                    method_name = ""
                    
                    for i, line in enumerate(lines):
                        if re.search(r'(public|private|protected)\s+\w+\s+\w+\s*\(', line):
                            in_method = True
                            method_start = i
                            method_name = line.strip()
                        elif in_method and line.strip().startswith('}'):
                            method_length = i - method_start
                            if method_length > 50:
                                issues["long_methods"].append({
                                    "file": str(file_path.relative_to(repo_path)),
                                    "method": method_name,
                                    "lines": method_length,
                                    "suggestion": "Break down into smaller methods"
                                })
                            in_method = False
                
                except Exception:
                    continue
            
            # Check for missing tests
            src_files = list(repo_path.rglob("src/main/**/*.java"))
            test_files = list(repo_path.rglob("src/test/**/*.java"))
            
            if len(src_files) > 0 and len(test_files) == 0:
                issues["missing_tests"].append({
                    "message": "No test files found",
                    "suggestion": "Add unit tests for better code quality"
                })
            elif len(src_files) > len(test_files) * 3:
                issues["missing_tests"].append({
                    "message": f"Low test coverage: {len(src_files)} source files, {len(test_files)} test files",
                    "suggestion": "Add more test coverage"
                })
        
        except Exception as e:
            print(f"  ✗ Error checking quality: {e}")
        
        return issues
    
    def analyze_pr_status(self, repo_name: str) -> Dict:
        """Check open PRs and their status"""
        
        url = f"{self.api_base}/repos/{self.github_user}/{repo_name}/pulls"
        params = {"state": "open"}
        
        try:
            response = requests.get(url, headers=self.headers, params=params)
            if response.status_code != 200:
                return {"open_prs": 0, "failing_prs": []}
            
            prs = response.json()
            failing_prs = []
            
            for pr in prs:
                # Check PR CI status
                statuses_url = pr["statuses_url"]
                status_response = requests.get(statuses_url, headers=self.headers)
                
                if status_response.status_code == 200:
                    statuses = status_response.json()
                    if statuses and statuses[0].get("state") == "failure":
                        failing_prs.append({
                            "number": pr["number"],
                            "title": pr["title"],
                            "url": pr["html_url"],
                            "branch": pr["head"]["ref"]
                        })
            
            return {
                "open_prs": len(prs),
                "failing_prs": failing_prs
            }
        
        except Exception as e:
            return {"open_prs": 0, "error": str(e)}
    
    def suggest_fixes(self, repo_name: str, issues: Dict) -> List[Dict]:
        """Generate fix suggestions for detected issues"""
        
        suggestions = []
        
        # Duplicate code fixes
        if issues.get("duplicates"):
            suggestions.append({
                "title": f"🔄 Remove {len(issues['duplicates'])} duplicate code blocks",
                "priority": "medium",
                "repo": repo_name,
                "actions": [
                    "Extract common code to utility methods",
                    "Use inheritance or composition patterns",
                    "Consider creating shared libraries"
                ],
                "auto_fixable": False
            })
        
        # Code quality fixes
        quality = issues.get("quality", {})
        
        if quality.get("large_files"):
            suggestions.append({
                "title": f"📏 Refactor {len(quality['large_files'])} large files",
                "priority": "low",
                "repo": repo_name,
                "files": [f["file"] for f in quality["large_files"][:5]],
                "actions": [
                    "Split large classes into smaller, focused classes",
                    "Apply Single Responsibility Principle",
                    "Extract inner classes if applicable"
                ],
                "auto_fixable": False
            })
        
        if quality.get("long_methods"):
            suggestions.append({
                "title": f"🔧 Simplify {len(quality['long_methods'])} long methods",
                "priority": "medium",
                "repo": repo_name,
                "actions": [
                    "Break methods into smaller, focused functions",
                    "Extract helper methods",
                    "Reduce cyclomatic complexity"
                ],
                "auto_fixable": False
            })
        
        if quality.get("missing_tests"):
            suggestions.append({
                "title": "🧪 Add missing tests",
                "priority": "high",
                "repo": repo_name,
                "actions": [
                    "Create unit tests for main classes",
                    "Aim for 80%+ code coverage",
                    "Add integration tests for critical paths"
                ],
                "auto_fixable": False
            })
        
        # CI/CD health fixes
        if issues.get("workflow", {}).get("status") == "failing":
            failure_rate = issues["workflow"].get("failure_rate", 0)
            suggestions.append({
                "title": f"🚨 Fix failing CI/CD pipeline ({failure_rate*100:.0f}% failure rate)",
                "priority": "critical",
                "repo": repo_name,
                "actions": [
                    "Review recent failed workflow runs",
                    "Check for flaky tests",
                    "Investigate infrastructure issues",
                    "Enable CI Failure Recovery Agent"
                ],
                "auto_fixable": True,
                "command": "Enable automatic failure detection and retry"
            })
        
        # Failing PR fixes
        if issues.get("prs", {}).get("failing_prs"):
            for pr in issues["prs"]["failing_prs"]:
                suggestions.append({
                    "title": f"❌ Fix failing PR #{pr['number']}: {pr['title']}",
                    "priority": "high",
                    "repo": repo_name,
                    "url": pr["url"],
                    "actions": [
                        f"Review PR #{pr['number']} build logs",
                        "Run tests locally on branch: " + pr["branch"],
                        "Apply CI Failure Agent suggestions"
                    ],
                    "auto_fixable": False
                })
        
        return suggestions
    
    def create_github_issue(self, repo_name: str, suggestion: Dict):
        """Create GitHub issue for suggestion"""
        
        url = f"{self.api_base}/repos/{self.github_user}/{repo_name}/issues"
        
        issue_body = f"""## 🤖 Automated Code Quality Suggestion

**Priority:** {suggestion['priority'].upper()}

### Description
{suggestion['title']}

### Recommended Actions
"""
        
        for action in suggestion['actions']:
            issue_body += f"- {action}\n"
        
        if suggestion.get('files'):
            issue_body += "\n### Affected Files\n"
            for file in suggestion['files']:
                issue_body += f"- `{file}`\n"
        
        issue_body += f"""
### Auto-Fixable
{'✅ Yes - Agent can apply fixes automatically' if suggestion.get('auto_fixable') else '❌ No - Manual intervention required'}

---
*Generated by Org Monitor Agent on {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}*
"""
        
        issue_data = {
            "title": f"[Auto] {suggestion['title']}",
            "body": issue_body,
            "labels": ["automated", "code-quality", suggestion['priority']]
        }
        
        try:
            response = requests.post(url, headers=self.headers, json=issue_data)
            if response.status_code == 201:
                issue = response.json()
                print(f"  ✓ Created issue #{issue['number']}: {suggestion['title']}")
                return issue
            else:
                print(f"  ✗ Failed to create issue: {response.status_code}")
        except Exception as e:
            print(f"  ✗ Error creating issue: {e}")
        
        return None
    
    def scan_repository(self, repo: Dict, check_branches: bool = True) -> Dict:
        """Comprehensive scan of a single repository"""
        
        repo_name = repo["name"]
        print(f"\n📦 Scanning {repo_name}...")
        
        results = {
            "repo": repo_name,
            "url": repo["html_url"],
            "issues": {},
            "suggestions": []
        }
        
        # 1. Check CI/CD status
        print(f"  🔍 Checking CI/CD status...")
        workflow_status = self.check_workflow_status(repo_name)
        results["issues"]["workflow"] = workflow_status
        
        if workflow_status["status"] == "failing":
            print(f"    ⚠️  CI/CD failing ({workflow_status['failure_rate']*100:.0f}% failure rate)")
        
        # 2. Check PRs
        print(f"  🔍 Checking open PRs...")
        pr_status = self.analyze_pr_status(repo_name)
        results["issues"]["prs"] = pr_status
        
        if pr_status.get("failing_prs"):
            print(f"    ⚠️  {len(pr_status['failing_prs'])} failing PRs")
        
        # 3. Check main/default branch
        default_branch = repo.get("default_branch", "main")
        
        print(f"  🔍 Analyzing code quality on {default_branch}...")
        
        # Detect duplicates
        duplicates = self.detect_duplicate_code(repo_name, default_branch)
        if duplicates:
            results["issues"]["duplicates"] = duplicates
            print(f"    ⚠️  Found {len(duplicates)} duplicate code blocks")
        
        # Check code quality
        quality_issues = self.check_code_quality(repo_name, default_branch)
        results["issues"]["quality"] = quality_issues
        
        if quality_issues["large_files"]:
            print(f"    ⚠️  {len(quality_issues['large_files'])} large files (>500 lines)")
        if quality_issues["long_methods"]:
            print(f"    ⚠️  {len(quality_issues['long_methods'])} long methods (>50 lines)")
        if quality_issues["missing_tests"]:
            print(f"    ⚠️  Test coverage issues detected")
        
        # 4. Generate suggestions
        print(f"  💡 Generating fix suggestions...")
        suggestions = self.suggest_fixes(repo_name, results["issues"])
        results["suggestions"] = suggestions
        
        if suggestions:
            print(f"    ✓ Generated {len(suggestions)} suggestions")
        
        return results
    
    def generate_report(self, all_results: List[Dict]) -> str:
        """Generate comprehensive report"""
        
        report = f"""
{'='*70}
ORGANIZATION-WIDE REPOSITORY SCAN REPORT
{'='*70}

Scan Date: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}
User/Org: {self.github_user}
Repositories Scanned: {len(all_results)}

"""
        
        # Summary statistics
        total_issues = 0
        critical_issues = 0
        repos_with_issues = 0
        
        for result in all_results:
            if result["suggestions"]:
                repos_with_issues += 1
                total_issues += len(result["suggestions"])
                critical_issues += sum(1 for s in result["suggestions"] if s["priority"] == "critical")
        
        report += f"""
SUMMARY
-------
Repositories with issues: {repos_with_issues}/{len(all_results)}
Total issues found: {total_issues}
Critical issues: {critical_issues}
Health score: {((len(all_results) - repos_with_issues) / len(all_results) * 100):.1f}%

"""
        
        # Detailed results per repo
        report += "\nDETAILED RESULTS\n" + "="*70 + "\n"
        
        for result in all_results:
            if not result["suggestions"]:
                continue
            
            report += f"""
Repository: {result['repo']}
URL: {result['url']}
Issues: {len(result['suggestions'])}

Suggestions:
"""
            
            for idx, suggestion in enumerate(result["suggestions"], 1):
                priority_emoji = {
                    "critical": "🔴",
                    "high": "🟠",
                    "medium": "🟡",
                    "low": "🟢"
                }.get(suggestion["priority"], "⚪")
                
                report += f"""
{idx}. {priority_emoji} {suggestion['title']}
   Priority: {suggestion['priority'].upper()}
   Actions:
"""
                for action in suggestion["actions"]:
                    report += f"   - {action}\n"
        
        report += "\n" + "="*70 + "\n"
        
        return report
    
    def run_scan(self, create_issues: bool = False, max_repos: int = None):
        """Run comprehensive scan"""
        
        print("🤖 Organization Monitor Agent Starting...\n")
        
        # Get all repositories
        repos = self.get_all_repositories()
        
        if max_repos:
            repos = repos[:max_repos]
            print(f"ℹ️  Limiting scan to {max_repos} repositories\n")
        
        # Scan each repository
        all_results = []
        
        for repo in repos:
            try:
                result = self.scan_repository(repo)
                all_results.append(result)
                
                # Optionally create GitHub issues
                if create_issues and result["suggestions"]:
                    print(f"  📝 Creating GitHub issues...")
                    for suggestion in result["suggestions"]:
                        self.create_github_issue(repo["name"], suggestion)
            
            except Exception as e:
                print(f"  ✗ Error scanning {repo['name']}: {e}")
                continue
        
        # Generate report
        print("\n📊 Generating report...")
        report = self.generate_report(all_results)
        
        # Save report
        report_file = Path("org_monitor_report.txt")
        report_file.write_text(report)
        print(f"✓ Report saved to {report_file}\n")
        
        print(report)
        
        return all_results


def main():
    """Main execution"""
    
    # Configuration
    create_issues = os.getenv("CREATE_ISSUES", "false").lower() == "true"
    max_repos = int(os.getenv("MAX_REPOS", "0")) or None
    
    if not os.getenv("GITHUB_TOKEN"):
        print("❌ GITHUB_TOKEN environment variable required")
        sys.exit(1)
    
    agent = OrgMonitorAgent()
    
    try:
        results = agent.run_scan(create_issues=create_issues, max_repos=max_repos)
        
        # Exit with error if critical issues found
        critical_count = sum(
            1 for r in results 
            for s in r.get("suggestions", []) 
            if s["priority"] == "critical"
        )
        
        if critical_count > 0:
            print(f"\n⚠️  {critical_count} critical issues found!")
            sys.exit(1)
        
        print("\n✅ Scan completed successfully")
        
    except Exception as e:
        print(f"\n❌ Scan failed: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()
