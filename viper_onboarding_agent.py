#!/usr/bin/env python3
"""
VIPER Onboarding AI Agent
Automates the process of onboarding new repositories to VIPER platform

Features:
- Analyzes repository structure
- Generates viper_config.yaml
- Creates GitHub Actions workflows
- Validates configuration
- Provides recommendations
"""

import os
import json
import yaml
import argparse
from pathlib import Path
from typing import Dict, List, Optional
from dataclasses import dataclass
from datetime import datetime


@dataclass
class RepoAnalysis:
    """Results from repository analysis"""
    repo_path: str
    language: str
    build_tool: Optional[str]
    has_dockerfile: bool
    has_tests: bool
    has_db_migrations: bool
    test_framework: Optional[str]
    dependencies: List[str]
    recommendations: List[str]


class VIPEROnboardingAgent:
    """AI Agent for automating VIPER onboarding"""
    
    # Language detection patterns
    LANGUAGE_PATTERNS = {
        'java': ['pom.xml', 'build.gradle', 'build.gradle.kts'],
        'node': ['package.json'],
        'python': ['requirements.txt', 'setup.py', 'pyproject.toml'],
        'dotnet': ['*.csproj', '*.sln'],
        'go': ['go.mod', 'go.sum'],
        'terraform': ['main.tf', 'variables.tf']
    }
    
    # Build tool detection
    BUILD_TOOLS = {
        'pom.xml': 'maven',
        'build.gradle': 'gradle',
        'build.gradle.kts': 'gradle',
        'package.json': 'npm',
        'requirements.txt': 'pip',
        'go.mod': 'go',
        '*.csproj': 'dotnet'
    }
    
    def __init__(self, test_mode: bool = False):
        """
        Initialize the agent
        
        Args:
            test_mode: Run in test environment mode (safe operations only)
        """
        self.test_mode = test_mode
        self.log_messages = []
        
    def log(self, message: str, level: str = "INFO"):
        """Log agent actions"""
        timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        log_entry = f"[{timestamp}] [{level}] {message}"
        self.log_messages.append(log_entry)
        print(log_entry)
    
    def analyze_repository(self, repo_path: str) -> RepoAnalysis:
        """
        Analyze repository structure and detect language/tools
        
        Args:
            repo_path: Path to the repository
            
        Returns:
            RepoAnalysis object with findings
        """
        self.log(f"🔍 Analyzing repository: {repo_path}")
        
        path = Path(repo_path)
        if not path.exists():
            raise ValueError(f"Repository path not found: {repo_path}")
        
        # Detect language
        language = self._detect_language(path)
        self.log(f"✓ Detected language: {language}")
        
        # Detect build tool
        build_tool = self._detect_build_tool(path)
        if build_tool:
            self.log(f"✓ Detected build tool: {build_tool}")
        
        # Check for Docker
        has_dockerfile = (path / "Dockerfile").exists()
        self.log(f"{'✓' if has_dockerfile else '✗'} Dockerfile: {'Found' if has_dockerfile else 'Not found'}")
        
        # Check for tests
        has_tests, test_framework = self._detect_tests(path, language)
        self.log(f"{'✓' if has_tests else '✗'} Tests: {'Found' if has_tests else 'Not found'}")
        
        # Check for database migrations
        has_db_migrations = self._detect_db_migrations(path)
        self.log(f"{'✓' if has_db_migrations else '✗'} DB Migrations: {'Found' if has_db_migrations else 'Not found'}")
        
        # Generate recommendations
        recommendations = self._generate_recommendations(
            language, has_dockerfile, has_tests, has_db_migrations
        )
        
        return RepoAnalysis(
            repo_path=repo_path,
            language=language,
            build_tool=build_tool,
            has_dockerfile=has_dockerfile,
            has_tests=has_tests,
            has_db_migrations=has_db_migrations,
            test_framework=test_framework,
            dependencies=[],
            recommendations=recommendations
        )
    
    def _detect_language(self, path: Path) -> str:
        """Detect primary programming language"""
        for language, patterns in self.LANGUAGE_PATTERNS.items():
            for pattern in patterns:
                if list(path.glob(pattern)) or list(path.glob(f"**/{pattern}")):
                    return language
        return "unknown"
    
    def _detect_build_tool(self, path: Path) -> Optional[str]:
        """Detect build tool"""
        for file_pattern, tool in self.BUILD_TOOLS.items():
            if list(path.glob(file_pattern)):
                return tool
        return None
    
    def _detect_tests(self, path: Path, language: str) -> tuple[bool, Optional[str]]:
        """Detect if repository has tests"""
        test_indicators = {
            'java': ['src/test/', 'src/test/java/'],
            'node': ['test/', '__tests__/', '*.test.js', '*.spec.js'],
            'python': ['test_*.py', 'tests/', 'test/'],
            'dotnet': ['*.Tests.csproj', 'Tests/'],
            'go': ['*_test.go']
        }
        
        if language in test_indicators:
            for indicator in test_indicators[language]:
                if list(path.glob(f"**/{indicator}")):
                    return True, self._detect_test_framework(path, language)
        
        return False, None
    
    def _detect_test_framework(self, path: Path, language: str) -> Optional[str]:
        """Detect test framework being used"""
        frameworks = {
            'java': {'pom.xml': ['junit', 'testng', 'mockito']},
            'node': {'package.json': ['jest', 'mocha', 'jasmine']},
            'python': {'requirements.txt': ['pytest', 'unittest', 'nose']}
        }
        
        if language in frameworks:
            for file, keywords in frameworks[language].items():
                file_path = path / file
                if file_path.exists():
                    content = file_path.read_text().lower()
                    for keyword in keywords:
                        if keyword in content:
                            return keyword
        
        return None
    
    def _detect_db_migrations(self, path: Path) -> bool:
        """Detect database migration tools"""
        migration_patterns = [
            'db/migration',
            'flyway',
            'liquibase',
            'migrations/',
            'alembic.ini'
        ]
        
        for pattern in migration_patterns:
            if list(path.glob(f"**/{pattern}")):
                return True
        
        return False
    
    def _generate_recommendations(self, language: str, has_dockerfile: bool, 
                                 has_tests: bool, has_db_migrations: bool) -> List[str]:
        """Generate recommendations for VIPER onboarding"""
        recommendations = []
        
        if not has_dockerfile:
            recommendations.append("⚠️  Add Dockerfile for containerization")
        
        if not has_tests:
            recommendations.append("⚠️  Add unit tests for CI validation")
        
        if language == 'unknown':
            recommendations.append("⚠️  Could not detect language - manual configuration needed")
        
        if language in ['java', 'dotnet'] and not has_db_migrations:
            recommendations.append("ℹ️  Consider adding database migration tool (Flyway/Liquibase)")
        
        return recommendations
    
    def generate_viper_config(self, analysis: RepoAnalysis, 
                            service_name: str = None) -> Dict:
        """
        Generate viper_config.yaml content
        
        Args:
            analysis: Repository analysis results
            service_name: Optional service name override
            
        Returns:
            Dictionary representing viper_config.yaml
        """
        self.log("📝 Generating viper_config.yaml")
        
        if not service_name:
            service_name = Path(analysis.repo_path).name
        
        config = {
            'version': '2.5.0',
            'service': {
                'name': service_name,
                'language': analysis.language,
                'type': 'microservice'
            },
            'build': self._generate_build_config(analysis),
            'test': self._generate_test_config(analysis),
            'security': {
                'snyk': {
                    'enabled': True,
                    'fail_on_critical': True
                },
                'sonarqube': {
                    'enabled': True,
                    'quality_gate': True
                }
            },
            'deployment': self._generate_deployment_config(analysis),
            'monitoring': {
                'datadog': {
                    'enabled': True
                }
            }
        }
        
        self.log("✓ Configuration generated successfully")
        return config
    
    def _generate_build_config(self, analysis: RepoAnalysis) -> Dict:
        """Generate build configuration section"""
        build_configs = {
            'java': {
                'tool': analysis.build_tool or 'maven',
                'jdk_version': '17',
                'artifact_type': 'jar'
            },
            'node': {
                'node_version': '18',
                'package_manager': 'npm',
                'build_command': 'npm run build'
            },
            'python': {
                'python_version': '3.11',
                'requirements_file': 'requirements.txt'
            },
            'dotnet': {
                'dotnet_version': '8.0',
                'build_command': 'dotnet build'
            },
            'go': {
                'go_version': '1.21',
                'build_command': 'go build'
            }
        }
        
        return build_configs.get(analysis.language, {'tool': 'custom'})
    
    def _generate_test_config(self, analysis: RepoAnalysis) -> Dict:
        """Generate test configuration section"""
        if not analysis.has_tests:
            return {'enabled': False}
        
        test_configs = {
            'java': {
                'enabled': True,
                'command': 'mvn test' if analysis.build_tool == 'maven' else 'gradle test',
                'coverage': {
                    'enabled': True,
                    'tool': 'jacoco',
                    'threshold': 80
                }
            },
            'node': {
                'enabled': True,
                'command': 'npm test',
                'coverage': {
                    'enabled': True,
                    'tool': 'istanbul',
                    'threshold': 80
                }
            },
            'python': {
                'enabled': True,
                'command': 'pytest',
                'coverage': {
                    'enabled': True,
                    'tool': 'pytest-cov',
                    'threshold': 80
                }
            }
        }
        
        return test_configs.get(analysis.language, {'enabled': True})
    
    def _generate_deployment_config(self, analysis: RepoAnalysis) -> Dict:
        """Generate deployment configuration section"""
        config = {
            'container': {
                'enabled': analysis.has_dockerfile,
                'registry': 'artifactory.vertex.com'
            },
            'kubernetes': {
                'enabled': True,
                'namespace': 'default',
                'replicas': 2
            },
            'environments': ['dev', 'qa', 'stage', 'prod']
        }
        
        if analysis.has_db_migrations:
            config['database'] = {
                'migrations': {
                    'enabled': True,
                    'tool': 'flyway'
                }
            }
        
        return config
    
    def generate_github_workflow(self, analysis: RepoAnalysis) -> str:
        """
        Generate GitHub Actions workflow for VIPER
        
        Args:
            analysis: Repository analysis results
            
        Returns:
            YAML content for .github/workflows/viper.yml
        """
        self.log("📝 Generating GitHub Actions workflow")
        
        workflow = {
            'name': 'VIPER CI/CD',
            'on': {
                'pull_request': {
                    'branches': ['main', 'develop']
                },
                'push': {
                    'branches': ['main', 'develop']
                }
            },
            'jobs': {
                'viper-ci': {
                    'runs-on': 'ubuntu-latest',
                    'steps': [
                        {
                            'name': 'Checkout code',
                            'uses': 'actions/checkout@v3'
                        },
                        {
                            'name': 'Run VIPER Pipeline',
                            'uses': 'vertex/viper-action@v2.5.0',
                            'with': {
                                'config-path': 'viper_config.yaml'
                            },
                            'env': {
                                'SNYK_TOKEN': '${{ secrets.SNYK_TOKEN }}',
                                'SONAR_TOKEN': '${{ secrets.SONAR_TOKEN }}'
                            }
                        }
                    ]
                }
            }
        }
        
        self.log("✓ Workflow generated successfully")
        return yaml.dump(workflow, sort_keys=False)
    
    def create_viper_files(self, analysis: RepoAnalysis, 
                          output_dir: str, service_name: str = None) -> Dict[str, str]:
        """
        Create all VIPER configuration files
        
        Args:
            analysis: Repository analysis results
            output_dir: Directory to write files
            service_name: Optional service name
            
        Returns:
            Dictionary of filename -> content
        """
        if self.test_mode:
            self.log("🧪 TEST MODE: Files will not be written to disk", "WARN")
        
        files = {}
        
        # Generate viper_config.yaml
        config = self.generate_viper_config(analysis, service_name)
        viper_config_content = yaml.dump(config, sort_keys=False, default_flow_style=False)
        files['viper_config.yaml'] = viper_config_content
        
        # Generate GitHub workflow
        workflow_content = self.generate_github_workflow(analysis)
        files['.github/workflows/viper.yml'] = workflow_content
        
        # Write files if not in test mode
        if not self.test_mode:
            output_path = Path(output_dir)
            output_path.mkdir(parents=True, exist_ok=True)
            
            # Write viper_config.yaml
            config_file = output_path / 'viper_config.yaml'
            config_file.write_text(viper_config_content)
            self.log(f"✓ Created: {config_file}")
            
            # Write GitHub workflow
            workflow_dir = output_path / '.github' / 'workflows'
            workflow_dir.mkdir(parents=True, exist_ok=True)
            workflow_file = workflow_dir / 'viper.yml'
            workflow_file.write_text(workflow_content)
            self.log(f"✓ Created: {workflow_file}")
        else:
            self.log("📄 Generated files (not written in test mode):")
            for filename in files.keys():
                self.log(f"  - {filename}")
        
        return files
    
    def validate_config(self, config_path: str) -> tuple[bool, List[str]]:
        """
        Validate existing viper_config.yaml
        
        Args:
            config_path: Path to viper_config.yaml
            
        Returns:
            Tuple of (is_valid, list of issues)
        """
        self.log(f"🔍 Validating configuration: {config_path}")
        
        issues = []
        
        try:
            with open(config_path, 'r') as f:
                config = yaml.safe_load(f)
            
            # Check required fields
            required_fields = ['version', 'service', 'build', 'security', 'deployment']
            for field in required_fields:
                if field not in config:
                    issues.append(f"Missing required field: {field}")
            
            # Validate version
            if 'version' in config:
                if not config['version'].startswith('2.'):
                    issues.append(f"Unsupported version: {config['version']}")
            
            # Validate service section
            if 'service' in config:
                if 'name' not in config['service']:
                    issues.append("Missing service.name")
                if 'language' not in config['service']:
                    issues.append("Missing service.language")
            
            if issues:
                self.log(f"❌ Validation failed: {len(issues)} issues found", "ERROR")
                for issue in issues:
                    self.log(f"  - {issue}", "ERROR")
                return False, issues
            else:
                self.log("✓ Validation passed", "SUCCESS")
                return True, []
                
        except Exception as e:
            error = f"Failed to parse config: {str(e)}"
            self.log(error, "ERROR")
            return False, [error]
    
    def generate_report(self, analysis: RepoAnalysis, files: Dict[str, str]) -> str:
        """
        Generate onboarding report
        
        Args:
            analysis: Repository analysis results
            files: Generated files
            
        Returns:
            Markdown formatted report
        """
        report = f"""# VIPER Onboarding Report

**Repository:** {analysis.repo_path}
**Generated:** {datetime.now().strftime("%Y-%m-%d %H:%M:%S")}
**Test Mode:** {'Yes' if self.test_mode else 'No'}

---

## 🔍 Repository Analysis

| Property | Value |
|----------|-------|
| Language | {analysis.language} |
| Build Tool | {analysis.build_tool or 'N/A'} |
| Dockerfile | {'✓ Yes' if analysis.has_dockerfile else '✗ No'} |
| Tests | {'✓ Yes' if analysis.has_tests else '✗ No'} |
| Test Framework | {analysis.test_framework or 'N/A'} |
| DB Migrations | {'✓ Yes' if analysis.has_db_migrations else '✗ No'} |

---

## 📝 Generated Files

"""
        for filename in files.keys():
            report += f"- `{filename}`\n"
        
        if analysis.recommendations:
            report += "\n---\n\n## ⚠️ Recommendations\n\n"
            for rec in analysis.recommendations:
                report += f"- {rec}\n"
        
        report += "\n---\n\n## 📋 Next Steps\n\n"
        report += "1. Review generated `viper_config.yaml`\n"
        report += "2. Commit `.github/workflows/viper.yml` to repository\n"
        report += "3. Add required secrets to GitHub repository:\n"
        report += "   - `SNYK_TOKEN`\n"
        report += "   - `SONAR_TOKEN`\n"
        report += "4. Create a pull request to test VIPER pipeline\n"
        report += "5. Monitor first VIPER run and adjust configuration if needed\n"
        
        report += "\n---\n\n## 📊 Agent Logs\n\n```\n"
        report += "\n".join(self.log_messages)
        report += "\n```\n"
        
        return report


def main():
    """Main CLI interface"""
    parser = argparse.ArgumentParser(
        description='VIPER Onboarding AI Agent',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  # Analyze repository in test mode
  python viper_onboarding_agent.py analyze /path/to/repo --test-mode

  # Generate VIPER configuration
  python viper_onboarding_agent.py generate /path/to/repo --output ./viper-config

  # Validate existing configuration
  python viper_onboarding_agent.py validate /path/to/repo/viper_config.yaml

  # Full onboarding with report
  python viper_onboarding_agent.py onboard /path/to/repo --service-name my-service --test-mode
        """
    )
    
    parser.add_argument('command', choices=['analyze', 'generate', 'validate', 'onboard'],
                       help='Command to execute')
    parser.add_argument('path', help='Repository path or config file path')
    parser.add_argument('--output', '-o', help='Output directory for generated files')
    parser.add_argument('--service-name', '-s', help='Service name override')
    parser.add_argument('--test-mode', '-t', action='store_true',
                       help='Run in test mode (no file writes)')
    parser.add_argument('--report', '-r', help='Save report to file')
    
    args = parser.parse_args()
    
    # Initialize agent
    agent = VIPEROnboardingAgent(test_mode=args.test_mode)
    
    try:
        if args.command == 'analyze':
            # Analyze repository only
            analysis = agent.analyze_repository(args.path)
            
            print("\n" + "="*60)
            print("📊 ANALYSIS COMPLETE")
            print("="*60)
            print(f"\nLanguage: {analysis.language}")
            print(f"Build Tool: {analysis.build_tool or 'Not detected'}")
            print(f"Has Tests: {analysis.has_tests}")
            
            if analysis.recommendations:
                print("\n⚠️  Recommendations:")
                for rec in analysis.recommendations:
                    print(f"  {rec}")
        
        elif args.command == 'generate':
            # Generate configuration files
            analysis = agent.analyze_repository(args.path)
            output_dir = args.output or args.path
            
            files = agent.create_viper_files(analysis, output_dir, args.service_name)
            
            print("\n" + "="*60)
            print("✅ FILES GENERATED")
            print("="*60)
            for filename in files.keys():
                print(f"  ✓ {filename}")
        
        elif args.command == 'validate':
            # Validate existing config
            is_valid, issues = agent.validate_config(args.path)
            
            print("\n" + "="*60)
            if is_valid:
                print("✅ VALIDATION PASSED")
            else:
                print("❌ VALIDATION FAILED")
                print("="*60)
                print("\nIssues found:")
                for issue in issues:
                    print(f"  - {issue}")
            print("="*60)
            
            return 0 if is_valid else 1
        
        elif args.command == 'onboard':
            # Full onboarding process
            analysis = agent.analyze_repository(args.path)
            output_dir = args.output or args.path
            
            files = agent.create_viper_files(analysis, output_dir, args.service_name)
            report = agent.generate_report(analysis, files)
            
            # Save report if requested
            if args.report:
                with open(args.report, 'w') as f:
                    f.write(report)
                agent.log(f"✓ Report saved to: {args.report}")
            else:
                print("\n" + "="*60)
                print(report)
            
            print("\n" + "="*60)
            print("✅ ONBOARDING COMPLETE")
            print("="*60)
        
        return 0
        
    except Exception as e:
        agent.log(f"❌ Error: {str(e)}", "ERROR")
        return 1


if __name__ == '__main__':
    exit(main())
