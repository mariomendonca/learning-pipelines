# 📝 GitHub Actions - Cheat Sheet

## 🚀 Quick Start

### Estrutura Básica de um Workflow

```yaml
name: Nome do Pipeline            # Nome que aparece no GitHub
on: [push, pull_request]          # Quando executar
jobs:                              # Trabalhos
  nome-do-job:                     # Nome customizável
    runs-on: ubuntu-latest         # Qual máquina usar
    steps:                         # Passos
      - name: Nome do step         # Descrição
        run: comando               # Comando a executar
```

---

## 🎯 Triggers (Eventos)

### Push em branch específica
```yaml
on:
  push:
    branches: [ main, develop ]
```

### Pull Request
```yaml
on:
  pull_request:
    branches: [ main ]
```

### Schedule (CRON)
```yaml
on:
  schedule:
    - cron: '0 0 * * *'  # Todo dia à meia-noite
```

### Manual (Workflow Dispatch)
```yaml
on:
  workflow_dispatch:  # Botão "Run workflow" no GitHub
```

### Múltiplos triggers
```yaml
on: [push, pull_request, workflow_dispatch]
```

---

## 🖥️ Runners

```yaml
runs-on: ubuntu-latest     # Linux (mais usado)
runs-on: windows-latest    # Windows
runs-on: macos-latest      # macOS
runs-on: ubuntu-22.04      # Versão específica
```

---

## 📋 Steps: Comandos vs Actions

### Comando Shell
```yaml
- name: Executar comando
  run: echo "Hello World"
```

### Action pré-construída
```yaml
- name: Checkout código
  uses: actions/checkout@v4
```

### Múltiplos comandos
```yaml
- name: Vários comandos
  run: |
    npm install
    npm test
    npm run build
```

---

## 🔧 Actions Essenciais

### Checkout (baixar código)
```yaml
- uses: actions/checkout@v4
```

### Setup Java
```yaml
- uses: actions/setup-java@v4
  with:
    distribution: 'temurin'
    java-version: '21'
```

### Setup Node.js
```yaml
- uses: actions/setup-node@v4
  with:
    node-version: '20'
```

### Setup Python
```yaml
- uses: actions/setup-python@v4
  with:
    python-version: '3.11'
```

### Cache (Maven)
```yaml
- uses: actions/cache@v3
  with:
    path: ~/.m2/repository
    key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
```

### Upload Artifact
```yaml
- uses: actions/upload-artifact@v4
  with:
    name: my-artifact
    path: target/*.jar
```

### Download Artifact
```yaml
- uses: actions/download-artifact@v4
  with:
    name: my-artifact
    path: ./downloads
```

---

## 🔐 Secrets (Variáveis Secretas)

### Definir no GitHub
1. Settings → Secrets and variables → Actions
2. New repository secret
3. Nome: `API_KEY`, Valor: `seu-valor-secreto`

### Usar no workflow
```yaml
- name: Usar secret
  run: echo "Token: ${{ secrets.API_KEY }}"
  env:
    API_TOKEN: ${{ secrets.API_KEY }}
```

---

## 🌍 Environment Variables

### Definir para todo o job
```yaml
jobs:
  build:
    env:
      NODE_ENV: production
      DB_HOST: localhost
```

### Definir para um step
```yaml
- name: Build
  env:
    API_URL: https://api.example.com
  run: npm run build
```

### Usar variável
```yaml
run: echo "Ambiente: ${{ env.NODE_ENV }}"
```

---

## 🔀 Condicionais

### Executar apenas se...
```yaml
- name: Deploy
  if: github.ref == 'refs/heads/main'  # Só na main
  run: ./deploy.sh
```

### Executar em caso de falha
```yaml
- name: Notificar falha
  if: failure()
  run: echo "Pipeline falhou!"
```

### Executar sempre (mesmo se falhar)
```yaml
- name: Cleanup
  if: always()
  run: rm -rf temp/
```

### Executar apenas em sucesso
```yaml
- name: Deploy
  if: success()
  run: ./deploy.sh
```

---

## 🎯 Estratégias de Matrix

### Testar múltiplas versões
```yaml
jobs:
  test:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        java: [17, 21]
        os: [ubuntu-latest, windows-latest]
    steps:
      - uses: actions/setup-java@v4
        with:
          java-version: ${{ matrix.java }}
```

---

## 📦 Jobs com Dependências

### Jobs em sequência
```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - run: mvn package
  
  test:
    needs: build  # Só roda depois do build
    runs-on: ubuntu-latest
    steps:
      - run: mvn test
  
  deploy:
    needs: [build, test]  # Precisa de build E test
    runs-on: ubuntu-latest
    steps:
      - run: ./deploy.sh
```

---

## 🐳 Docker

### Build de imagem
```yaml
- name: Build Docker image
  run: docker build -t myapp:latest .
```

### Login no Docker Hub
```yaml
- name: Login Docker Hub
  uses: docker/login-action@v2
  with:
    username: ${{ secrets.DOCKER_USERNAME }}
    password: ${{ secrets.DOCKER_PASSWORD }}
```

### Push de imagem
```yaml
- name: Push image
  run: docker push myapp:latest
```

---

## 📊 Outputs

### Definir output
```yaml
- id: step1
  run: echo "result=success" >> $GITHUB_OUTPUT

- name: Usar output
  run: echo "Resultado: ${{ steps.step1.outputs.result }}"
```

---

## 🛠️ Comandos Maven Úteis

```yaml
# Build sem testes
- run: mvn clean package -DskipTests

# Rodar testes
- run: mvn test

# Verificar código
- run: mvn verify

# Instalar no repositório local
- run: mvn install

# Limpar
- run: mvn clean

# Build com profile específico
- run: mvn package -Pprod
```

---

## 🎨 Emojis para Steps

```yaml
- name: 🧾 Checkout       # Checkout
- name: ☕ Setup Java     # Java
- name: 🐍 Setup Python   # Python
- name: 📦 Build          # Build/Package
- name: 🧪 Testes         # Testes
- name: 🔍 Lint/Quality   # Análise
- name: 🐳 Docker         # Docker
- name: 🚀 Deploy         # Deploy
- name: 📤 Upload         # Upload
- name: 📥 Download       # Download
- name: 💾 Cache          # Cache
- name: 🔐 Security       # Segurança
- name: 📊 Report         # Relatórios
- name: 🔔 Notificar      # Notificações
- name: 🧹 Cleanup        # Limpeza
```

---

## 📈 Análise de Código

### SonarQube
```yaml
- name: SonarQube Scan
  run: mvn sonar:sonar -Dsonar.token=${{ secrets.SONAR_TOKEN }}
```

### Checkstyle
```yaml
- name: Checkstyle
  run: mvn checkstyle:check
```

### JaCoCo (Code Coverage)
```yaml
- name: Code Coverage
  run: mvn jacoco:report
```

---

## 🔔 Notificações

### Slack
```yaml
- name: Slack Notification
  uses: slackapi/slack-github-action@v1.24.0
  with:
    payload: |
      {
        "text": "Build ${{ job.status }}"
      }
  env:
    SLACK_WEBHOOK_URL: ${{ secrets.SLACK_WEBHOOK }}
```

### Discord
```yaml
- name: Discord Notification
  uses: sarisia/actions-status-discord@v1
  with:
    webhook: ${{ secrets.DISCORD_WEBHOOK }}
```

---

## 🎯 Contextos Úteis

```yaml
${{ github.repository }}      # owner/repo
${{ github.ref }}             # refs/heads/main
${{ github.sha }}             # commit SHA
${{ github.actor }}           # usuário que triggou
${{ github.event_name }}      # push, pull_request, etc
${{ runner.os }}              # Linux, Windows, macOS
${{ job.status }}             # success, failure, cancelled
```

---

## 🚦 Status Checks

### Branch Protection
No GitHub:
1. Settings → Branches
2. Add rule → Branch name pattern: `main`
3. ✅ Require status checks to pass
4. Selecione seu workflow

Agora PRs só podem ser merged se o pipeline passar! ✅

---

## 📝 Exemplos Completos

### Java + Maven + PostgreSQL
```yaml
name: Java CI
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    
    services:
      postgres:
        image: postgres:15
        env:
          POSTGRES_PASSWORD: postgres
        ports:
          - 5432:5432
    
    steps:
      - uses: actions/checkout@v4
      
      - uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '21'
      
      - name: Build e Test
        env:
          DB_HOST: localhost
          DB_PORT: 5432
        run: |
          mvn clean package
          mvn test
```

---

### Node.js + NPM
```yaml
name: Node.js CI
on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v4
      
      - uses: actions/setup-node@v4
        with:
          node-version: '20'
          cache: 'npm'
      
      - run: npm ci
      - run: npm test
      - run: npm run build
```

---

### Python + Pytest
```yaml
name: Python CI
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v4
      
      - uses: actions/setup-python@v4
        with:
          python-version: '3.11'
          cache: 'pip'
      
      - run: pip install -r requirements.txt
      - run: pytest
```

---

## 🐛 Debug

### Ativar debug logs
No repositório, crie secrets:
- `ACTIONS_RUNNER_DEBUG` = `true`
- `ACTIONS_STEP_DEBUG` = `true`

### Usar tmate (SSH interativo)
```yaml
- name: Setup tmate session
  uses: mxschmitt/action-tmate@v3
```

---

## ⚡ Performance Tips

1. ✅ Use cache sempre que possível
2. ✅ Paralelizar jobs independentes
3. ✅ Use `ubuntu-latest` (mais rápido e barato)
4. ✅ `-DskipTests` no build, testes em step separado
5. ✅ Limitar `pull_request` para branches específicas

---

## 📚 Links Rápidos

- [Docs Oficiais](https://docs.github.com/en/actions)
- [Marketplace](https://github.com/marketplace?type=actions)
- [Awesome Actions](https://github.com/sdras/awesome-actions)
- [Action Examples](https://github.com/actions/starter-workflows)

---

## 🎯 Template Pronto para Copiar

```yaml
name: CI/CD Pipeline
on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
      - name: 🧾 Checkout
        uses: actions/checkout@v4
      
      - name: ☕ Setup Java
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '21'
      
      - name: 💾 Cache Maven
        uses: actions/cache@v3
        with:
          path: ~/.m2/repository
          key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
      
      - name: 📦 Build
        run: mvn clean package -DskipTests
      
      - name: 🧪 Test
        run: mvn test
      
      - name: 🔼 Upload Artifact
        uses: actions/upload-artifact@v4
        with:
          name: app-jar
          path: target/*.jar
```

---

**Copie, cole e adapte para seu projeto! 🚀**

