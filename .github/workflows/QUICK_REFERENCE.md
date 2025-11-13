# 🎯 Pipeline Quick Reference

## 📁 File Structure

```
.github/workflows/
├── ci-cd.yml          # 🚀 Main pipeline orchestrator
├── test.yml           # 🧪 Tests + Build
├── sonar.yml          # 📊 SonarCloud Analysis
├── docker.yml         # 🐳 Docker Build & Push
├── README.md          # 📖 Complete documentation
├── PIPELINE_FLOW.md   # 📊 Visual flow diagrams
└── old_workflows/     # 🗄️ Archived old files
    ├── build.yml
    ├── test-sonar.yml
    └── deploy.yaml
```

## 🔑 Key Features

### ✅ What Changed?

| Before | After |
|--------|-------|
| 3 separate workflows | 1 main + 3 reusable workflows |
| Sequential with `workflow_run` | Direct job dependencies |
| No caching | Maven, SonarCloud, Docker caching |
| Simple tagging | Smart tagging strategy |
| No artifact reuse | JAR built once, reused |

### ⚡ Performance Improvements

- **Maven Cache**: ⬇️ 30-60s per build
- **Sonar Cache**: ⬇️ 10-20s per analysis
- **Docker Cache**: ⬇️ 1-2min per build
- **Artifact Reuse**: JAR built once instead of 3 times

## 🚀 How to Use

### Run Complete Pipeline
```bash
git add .
git commit -m "feat: your feature"
git push origin main
```

### Test Without Deployment
```bash
git push origin develop
# or create a PR
```

### View Pipeline Status
```bash
# In GitHub UI
https://github.com/USERNAME/REPO/actions

# Or via CLI
gh run list
gh run view [run-id]
```

## 🔧 Required Secrets

Set these in: **Settings → Secrets and variables → Actions**

```bash
DOCKERHUB_USERNAME=your_username
DOCKERHUB_TOKEN=your_token
SONAR_TOKEN=your_sonar_token
```

### How to Get Docker Hub Token
```bash
1. Go to hub.docker.com
2. Account Settings → Security → New Access Token
3. Copy and save to GitHub Secrets
```

### How to Get Sonar Token
```bash
1. Go to sonarcloud.io
2. My Account → Security → Generate Token
3. Copy and save to GitHub Secrets
```

## 📊 Pipeline Stages

```
1️⃣ Tests (Always runs)
   ├─ Checkout code
   ├─ Setup Java 21
   ├─ Run tests
   ├─ Build JAR
   └─ Upload artifacts

2️⃣ SonarCloud (After tests)
   ├─ Download coverage
   ├─ Run analysis
   └─ Check quality gate

3️⃣ Docker (main branch only)
   ├─ Download JAR
   ├─ Build image
   └─ Push to Docker Hub
```

## 🐛 Troubleshooting

### Pipeline Not Running?
```bash
# Check if workflow file is valid
cat .github/workflows/ci-cd.yml

# Check Actions are enabled
# Go to Settings → Actions → Allow all actions
```

### Docker Push Fails?
```bash
# Verify secrets are set
gh secret list

# Check Docker Hub login
docker login -u $DOCKERHUB_USERNAME

# Verify image name in docker.yml matches your username
```

### SonarCloud Fails?
```bash
# Check token is valid
# Verify project key: mariomendonca_learning-pipelines
# Check organization: mariomendonca
```

### Tests Fail?
```bash
# Run locally first
mvn clean test

# Check test reports
target/surefire-reports/
```

## 📈 Monitoring

### View Test Results
```
Actions → [Your Run] → test → Test Results
```

### View Coverage
```
Actions → [Your Run] → test → Artifacts → jacoco-report
```

### View Docker Image
```bash
docker pull USERNAME/learning-pipes-app:latest
docker run -p 8080:8080 USERNAME/learning-pipes-app:latest
```

### View SonarCloud Report
```
https://sonarcloud.io/dashboard?id=mariomendonca_learning-pipelines
```

## 🎨 Best Practices Applied

- ✅ **DRY**: Reusable workflows, no duplication
- ✅ **Fast Feedback**: Tests run first, fail fast
- ✅ **Caching**: Multiple levels of caching
- ✅ **Security**: Secrets properly managed
- ✅ **Artifacts**: Built once, used everywhere
- ✅ **Tagging**: Smart Docker tag strategy
- ✅ **Documentation**: Self-documenting workflows
- ✅ **Modularity**: Easy to modify/extend

## 🔄 Workflow Dependencies

```
ci-cd.yml (Main Orchestrator)
    ↓
    ├─→ test.yml (produces artifacts)
    │       ↓
    │   ├─→ application-jar
    │   └─→ jacoco-report
    │
    ├─→ sonar.yml (consumes jacoco-report)
    │       ↓
    │   └─→ Quality report
    │
    └─→ docker.yml (consumes application-jar)
            ↓
        └─→ Docker image
```

## 💡 Tips

1. **Always test locally first**: `mvn clean test`
2. **Check workflows before pushing**: YAML syntax is strict
3. **Monitor Actions quota**: Free tier has limits
4. **Use descriptive commit messages**: Helps tracking
5. **Review SonarCloud regularly**: Improve code quality

## 📞 Support

- GitHub Actions: https://docs.github.com/en/actions
- Docker: https://docs.docker.com/
- SonarCloud: https://docs.sonarcloud.io/
- Maven: https://maven.apache.org/guides/

---

Pipeline created with ❤️ following industry best practices

