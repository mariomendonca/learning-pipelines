# 🚀 CI/CD Pipeline Structure

This repository uses a modular GitHub Actions pipeline following best practices.

## 📋 Pipeline Overview

The main pipeline is orchestrated by `ci-cd.yml` and consists of three stages:

```
┌─────────────┐      ┌──────────────┐      ┌────────────────┐
│  🧪 Tests   │ ───> │ 📊 SonarCloud │ ───> │ 🐳 Docker Push │
│             │      │   Analysis    │      │  (main only)   │
└─────────────┘      └──────────────┘      └────────────────┘
```

## 📁 Workflow Files

### Main Pipeline
- **`ci-cd.yml`**: Main orchestration file that calls all reusable workflows

### Reusable Workflows
- **`test.yml`**: Runs unit tests, generates coverage reports, and builds artifacts
- **`sonar.yml`**: Performs SonarCloud static code analysis with quality gate
- **`docker.yml`**: Builds and pushes Docker images to Docker Hub

## 🔄 Workflow Execution

### On Pull Requests
1. ✅ Tests run
2. ✅ SonarCloud analysis runs
3. ❌ Docker deployment is **skipped**

### On Push to Main
1. ✅ Tests run
2. ✅ SonarCloud analysis runs
3. ✅ Docker image is built and pushed to Docker Hub

## 🎯 Best Practices Implemented

### ✨ Modularity
- Separated concerns into individual reusable workflows
- Each workflow can be maintained and tested independently
- Easy to add new stages or modify existing ones

### ⚡ Performance
- **Maven caching**: Reduces build time by caching dependencies
- **SonarCloud caching**: Speeds up analysis
- **Docker layer caching**: Optimizes image builds
- **Artifact sharing**: JAR is built once and reused

### 🔒 Security
- Secrets are properly managed via GitHub Secrets
- Docker Hub credentials stored securely
- SonarCloud token protected

### 📊 Quality Gates
- Automated testing before any deployment
- Code quality analysis with SonarCloud
- Coverage reports generated with JaCoCo
- Quality gate check (non-blocking)

### 🏷️ Docker Tagging Strategy
- `latest`: Latest version from main branch
- `<branch>-<sha>`: Unique tag for each commit
- `<branch>`: Latest version for each branch

## 🔧 Required Secrets

Configure these secrets in your GitHub repository settings:

| Secret | Description |
|--------|-------------|
| `DOCKERHUB_USERNAME` | Your Docker Hub username |
| `DOCKERHUB_TOKEN` | Docker Hub access token |
| `SONAR_TOKEN` | SonarCloud authentication token |
| `GITHUB_TOKEN` | Automatically provided by GitHub Actions |

## 📦 Artifacts Generated

Each pipeline run produces:
- 📊 Test results (Surefire reports)
- 📈 JaCoCo coverage reports
- 📦 Application JAR file
- 🐳 Docker image (on main branch)

## 🚀 Usage

### Trigger on Push
```bash
git push origin main
```

### Trigger on Pull Request
```bash
gh pr create --base main --head feature-branch
```

### Manual Trigger
Go to Actions → CI/CD Pipeline → Run workflow

## 📖 Documentation

For more details on specific workflows:
- See inline comments in each workflow file
- Check the GitHub Actions documentation: https://docs.github.com/en/actions

## 🔄 Migrating from Old Workflows

Old workflows have been moved to `old_workflows/` directory:
- `build.yml` → Replaced by `test.yml`
- `test-sonar.yml` → Split into `test.yml` and `sonar.yml`
- `deploy.yaml` → Replaced by `docker.yml`

## 🎓 Benefits of This Structure

1. **Maintainability**: Easy to update individual stages
2. **Reusability**: Workflows can be called from other pipelines
3. **Clarity**: Clear separation of concerns
4. **Efficiency**: Parallel execution where possible, artifact reuse
5. **Scalability**: Easy to add new stages (e.g., staging deployment, integration tests)

---

Made with ❤️ following GitHub Actions best practices

