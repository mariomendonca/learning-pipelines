# 🚀 Guia Completo: GitHub Actions para Iniciantes

## 📖 O que é GitHub Actions?

GitHub Actions é uma ferramenta de **CI/CD (Continuous Integration/Continuous Deployment)** que permite automatizar tarefas como:
- ✅ Rodar testes automaticamente
- 🔨 Compilar seu código
- 📦 Criar pacotes/artefatos
- 🚀 Fazer deploy para servidores

---

## 🎯 Conceitos Fundamentais

### 1. **Workflow** (Fluxo de Trabalho)
- É um arquivo YAML que define o processo automatizado
- Localização: `.github/workflows/nome-do-arquivo.yml`
- Pode ter múltiplos workflows no mesmo repositório

### 2. **Event** (Evento)
- Gatilho que inicia o workflow
- Exemplos: `push`, `pull_request`, `schedule`, `workflow_dispatch`

### 3. **Job** (Trabalho)
- Conjunto de **steps** que rodam na mesma máquina virtual
- Podem rodar em **paralelo** ou **sequencial**

### 4. **Step** (Passo)
- Tarefa individual dentro de um job
- Pode ser um **comando** ou uma **action** pré-construída

### 5. **Runner** (Executor)
- Máquina virtual que executa o workflow
- GitHub fornece gratuitamente: `ubuntu-latest`, `windows-latest`, `macos-latest`

---

## 📋 Anatomia do Pipeline Básico

Vamos analisar **LINHA POR LINHA** o pipeline que criamos:

```yaml
name: CI/CD - Spring Boot
```
**O que faz?** Define o nome do workflow que aparece na aba "Actions" do GitHub.

---

### 🎯 Seção: TRIGGERS (Quando executar)

```yaml
on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]
```

**Explicação:**
- **`on:`** → Define os eventos que ativam o pipeline
- **`push:`** → Executa quando você faz um commit/push
  - **`branches: [ main ]`** → Só executa se o push for na branch `main`
- **`pull_request:`** → Executa quando alguém abre um PR para a `main`

**Por que isso é útil?**
- Garante que todo código que vai para `main` está funcionando
- Testa Pull Requests antes de aprovar
- Evita que código quebrado vá para produção

---

### 🛠️ Seção: JOBS

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
```

**Explicação:**
- **`jobs:`** → Inicia a seção de trabalhos
- **`build:`** → Nome do job (você pode chamar como quiser)
- **`runs-on: ubuntu-latest`** → Usa uma VM Ubuntu (grátis no GitHub)

**Alternativas de runners:**
- `windows-latest` → Para projetos .NET/Windows
- `macos-latest` → Para apps iOS/macOS
- `ubuntu-22.04` → Versão específica do Ubuntu

---

### 📝 Seção: STEPS (Passos)

#### **Step 1: Checkout do código** ✅

```yaml
- name: 🧾 Checkout do código
  uses: actions/checkout@v4
```

**O que faz?**
- Baixa (faz clone) do seu repositório para o runner
- **SEM ISSO** → O runner não teria acesso ao seu código!

**`uses:`** → Usa uma "action" pré-construída (criada pela comunidade/GitHub)
- `actions/checkout@v4` → Action oficial do GitHub (versão 4)

---

#### **Step 2: Configurar Java** ☕

```yaml
- name: ☕ Configurar Java
  uses: actions/setup-java@v4
  with:
    distribution: 'temurin'
    java-version: '21'
```

**O que faz?**
- Instala o Java Development Kit (JDK) no runner
- Configura a variável `JAVA_HOME`

**Parâmetros:**
- **`distribution:`** → Qual distribuição do OpenJDK usar
  - `temurin` → Eclipse Temurin (recomendado)
  - `zulu` → Azul Zulu
  - `adopt` → AdoptOpenJDK
- **`java-version:`** → Versão do Java (deve ser a mesma do `pom.xml`)

**⚠️ IMPORTANTE:** Se seu `pom.xml` usa Java 21, o pipeline também deve usar!

---

#### **Step 3: Cache Maven** 💾

```yaml
- name: 💾 Cache Maven dependencies
  uses: actions/cache@v3
  with:
    path: ~/.m2/repository
    key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
    restore-keys: |
      ${{ runner.os }}-maven-
```

**O que faz?**
- Armazena as dependências baixadas pelo Maven
- Acelera o build em **50-80%** nas próximas execuções!

**Como funciona:**
1. Primeira execução: Baixa todas as dependências (~2-5 min)
2. Próximas execuções: Restaura do cache (~10-30 seg)
3. Se o `pom.xml` mudar → Atualiza o cache

**Parâmetros:**
- **`path:`** → Onde ficam as dependências do Maven
- **`key:`** → Identificador único baseado no hash do `pom.xml`
- **`restore-keys:`** → Chave alternativa se o cache exato não for encontrado

---

#### **Step 4: Build com Maven** 📦

```yaml
- name: 📦 Build com Maven
  run: mvn clean package -DskipTests
```

**O que faz?**
- **`mvn clean`** → Remove arquivos compilados anteriores
- **`mvn package`** → Compila o código e cria o JAR
- **`-DskipTests`** → Pula os testes (vamos rodar no próximo step)

**Por que pular testes aqui?**
- Separa a fase de **compilação** da fase de **testes**
- Se os testes falharem, você saberá que é um problema de teste, não de compilação

**Resultado:**
- Cria o arquivo `target/learning-pipelines-0.0.1-SNAPSHOT.jar`

---

#### **Step 5: Rodar Testes** 🧪

```yaml
- name: 🧪 Rodar testes
  run: mvn test
```

**O que faz?**
- Executa todos os testes unitários do projeto
- Se **UM teste falhar** → O pipeline **para** e marca como ❌

**Por que separar do build?**
- Logs mais claros: você vê exatamente onde falhou
- Pode adicionar relatórios de cobertura depois

**⚠️ Se os testes falharem:**
- O pipeline para AQUI
- Os steps seguintes NÃO são executados
- GitHub marca o commit com um ❌ vermelho

---

#### **Step 6: Gerar Artefato** 📤

```yaml
- name: 📤 Gerar artefato (JAR)
  run: mkdir -p artifacts && cp target/*.jar artifacts/
```

**O que faz?**
- Cria uma pasta `artifacts/`
- Copia o JAR compilado para essa pasta

**Por que?**
- Organiza os arquivos para upload
- Evita fazer upload de toda a pasta `target/`

---

#### **Step 7: Upload do Artefato** 🔼

```yaml
- name: 🔼 Upload do artefato
  uses: actions/upload-artifact@v4
  with:
    name: app-springboot
    path: artifacts/*.jar
```

**O que faz?**
- Salva o JAR como um **artifact** no GitHub Actions
- Fica disponível para download por **90 dias** (padrão)

**Para acessar:**
1. Vá na aba **Actions** do seu repositório
2. Clique no workflow executado
3. Seção **Artifacts** → Download do `app-springboot`

**Uso prático:**
- Compartilhar o JAR com a equipe
- Usar em um job de deploy posterior
- Testar o build localmente

---

## 🎬 Como Testar o Pipeline

### 1️⃣ Commit e Push

```bash
cd /Users/mariomendonca/Desktop/dev/fiap/fase4/learning-pipelines
git add .github/workflows/ci.txt
git commit -m "ci: adiciona pipeline básico de CI/CD"
git push origin main
```

### 2️⃣ Ver Execução

1. Vá para o GitHub: `https://github.com/SEU-USUARIO/SEU-REPO`
2. Clique na aba **Actions**
3. Verá o workflow rodando em tempo real

### 3️⃣ Interpretar Resultados

- ✅ **Verde (Success)** → Tudo passou!
- ❌ **Vermelho (Failed)** → Algo deu errado (clique para ver logs)
- 🟡 **Amarelo (In Progress)** → Ainda rodando

---

## 🔍 Troubleshooting: Problemas Comuns

### ❌ Erro: "No tests were found"

**Causa:** Não existem testes no projeto

**Solução:**
```yaml
- name: 🧪 Rodar testes
  run: mvn test || echo "Nenhum teste encontrado"
```

---

### ❌ Erro: "Java version mismatch"

**Causa:** `pom.xml` usa Java 21, mas o pipeline usa Java 17

**Solução:** Alinhar as versões:
```yaml
java-version: '21'  # Deve ser igual ao pom.xml
```

---

### ❌ Erro: "Permission denied"

**Causa:** Script Maven não tem permissão de execução

**Solução:** Adicione antes do build:
```yaml
- name: 🔧 Dar permissão ao mvnw
  run: chmod +x mvnw
```

---

## 📊 Visualizando o Pipeline

Quando você fizer push, verá algo assim no GitHub:

```
CI/CD - Spring Boot
└── build (ubuntu-latest)
    ├── ✅ Checkout do código (2s)
    ├── ✅ Configurar Java (8s)
    ├── ✅ Cache Maven dependencies (1s)
    ├── ✅ Build com Maven (45s)
    ├── ✅ Rodar testes (12s)
    ├── ✅ Gerar artefato (1s)
    └── ✅ Upload do artefato (3s)
    
Total: ~1min 12s
```

---

## 🚀 Próximos Passos (Melhorias Futuras)

Depois de dominar esse pipeline básico, você pode adicionar:

### 1. **Code Coverage** (Cobertura de Código)
```yaml
- name: 📊 Gerar relatório de cobertura
  run: mvn jacoco:report
```

### 2. **SonarQube** (Análise de Qualidade)
```yaml
- name: 🔍 Análise SonarQube
  run: mvn sonar:sonar -Dsonar.token=${{ secrets.SONAR_TOKEN }}
```

### 3. **Docker Build** (Criar imagem Docker)
```yaml
- name: 🐳 Build Docker image
  run: docker build -t meuapp:latest .
```

### 4. **Deploy Automático** (Heroku/AWS/Azure)
```yaml
- name: 🚀 Deploy para Heroku
  uses: akhileshns/heroku-deploy@v3.12.12
  with:
    heroku_api_key: ${{ secrets.HEROKU_API_KEY }}
    heroku_app_name: "meu-app"
```

### 5. **Notificações** (Slack/Discord)
```yaml
- name: 💬 Notificar no Slack
  if: failure()
  uses: slackapi/slack-github-action@v1
```

---

## 🎓 Conceitos Importantes para Lembrar

### ✅ Boas Práticas

1. **Use cache** → Economiza tempo e recursos
2. **Separe jobs** → Build, Test, Deploy em jobs diferentes
3. **Use versões fixas** → `@v4` ao invés de `@latest`
4. **Adicione emojis** → Facilita visualização nos logs
5. **Documente** → Explique steps complexos com comentários

### ⚠️ O que EVITAR

1. ❌ Não coloque **senhas** no código → Use `secrets`
2. ❌ Não ignore falhas de teste → Não use `-DskipTests` sempre
3. ❌ Não rode builds desnecessários → Configure branches específicas
4. ❌ Não esqueça do cache → Desperdiça tempo e recursos

---

## 🆘 Recursos Úteis

- 📚 [Documentação Oficial](https://docs.github.com/en/actions)
- 🏪 [Marketplace de Actions](https://github.com/marketplace?type=actions)
- 💡 [Actions Starter Workflows](https://github.com/actions/starter-workflows)
- 🎥 [GitHub Actions Tutorial (Video)](https://www.youtube.com/watch?v=R8_veQiYBjI)

---

## 🎯 Checklist: Meu primeiro Pipeline

- [ ] Criei a pasta `.github/workflows/`
- [ ] Criei o arquivo `ci.yml`
- [ ] Fiz commit e push
- [ ] Vi o pipeline rodar na aba Actions
- [ ] Todos os steps passaram ✅
- [ ] Baixei o artefato gerado
- [ ] Entendi cada step do pipeline

---

## 📝 Glossário Rápido

| Termo | Significado |
|-------|-------------|
| **CI** | Continuous Integration (Integração Contínua) |
| **CD** | Continuous Deployment (Entrega Contínua) |
| **Workflow** | Arquivo YAML com o pipeline |
| **Job** | Conjunto de steps que rodam juntos |
| **Step** | Tarefa individual (comando ou action) |
| **Runner** | VM que executa o workflow |
| **Artifact** | Arquivo gerado (JAR, ZIP, etc.) |
| **Cache** | Armazenamento temporário para acelerar builds |
| **Action** | Código reutilizável (como uma função) |

---

## 🎉 Conclusão

Parabéns! Agora você tem:
- ✅ Um pipeline funcional de CI/CD
- ✅ Testes automatizados
- ✅ Artefatos sendo gerados
- ✅ Base para adicionar mais funcionalidades

**Lembre-se:** Esse é apenas o começo! CI/CD é uma jornada de melhoria contínua. 🚀

---

**Dúvidas?** Consulte a [documentação oficial](https://docs.github.com/en/actions) ou abra uma issue no repositório!

