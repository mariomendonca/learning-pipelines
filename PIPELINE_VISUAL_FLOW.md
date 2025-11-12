# 🎨 Visualização do Pipeline CI/CD

## 📊 Fluxo Completo do Pipeline

```
┌─────────────────────────────────────────────────────────────────┐
│                         GITHUB REPOSITORY                        │
│                                                                  │
│  Developer faz: git push origin main  OU  abre Pull Request     │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                    🎯 TRIGGER (Gatilho)                          │
│                                                                  │
│  on:                                                             │
│    push: branches [main]                                         │
│    pull_request: branches [main]                                 │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                   🖥️  RUNNER (Ubuntu VM)                         │
│                                                                  │
│  GitHub cria uma máquina virtual limpa (ubuntu-latest)          │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                    📋 JOB: build                                 │
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  Step 1: 🧾 Checkout do código                            │  │
│  │  └─> Clona seu repositório para o runner                 │  │
│  │      Resultado: Código disponível na VM                   │  │
│  └───────────────────────────────────────────────────────────┘  │
│                             │                                    │
│                             ▼                                    │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  Step 2: ☕ Configurar Java 21                            │  │
│  │  └─> Instala JDK 21 (Temurin)                            │  │
│  │      Resultado: Java pronto para usar                     │  │
│  └───────────────────────────────────────────────────────────┘  │
│                             │                                    │
│                             ▼                                    │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  Step 3: 💾 Cache Maven dependencies                      │  │
│  │  └─> Restaura dependências do cache (se existir)         │  │
│  │      Resultado: Build 70% mais rápido!                    │  │
│  └───────────────────────────────────────────────────────────┘  │
│                             │                                    │
│                             ▼                                    │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  Step 4: 📦 Build com Maven                               │  │
│  │  └─> mvn clean package -DskipTests                        │  │
│  │      Resultado: target/learning-pipelines.jar             │  │
│  └───────────────────────────────────────────────────────────┘  │
│                             │                                    │
│                             ▼                                    │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  Step 5: 🧪 Rodar testes                                  │  │
│  │  └─> mvn test                                             │  │
│  │      ❓ Todos os testes passaram?                         │  │
│  └───────────────────────────────────────────────────────────┘  │
│                             │                                    │
│                   ┌─────────┴─────────┐                          │
│                   │                   │                          │
│                   ▼                   ▼                          │
│              ✅ SIM                ❌ NÃO                         │
│         (Continua)           (Pipeline FALHA)                    │
│                   │                   │                          │
│                   │                   └──> ❌ Marca commit       │
│                   │                        como falho            │
│                   │                                              │
│                   ▼                                              │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  Step 6: 📤 Gerar artefato                                │  │
│  │  └─> Copia JAR para pasta artifacts/                     │  │
│  │      Resultado: artifacts/learning-pipelines.jar          │  │
│  └───────────────────────────────────────────────────────────┘  │
│                             │                                    │
│                             ▼                                    │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  Step 7: 🔼 Upload do artefato                            │  │
│  │  └─> Salva no GitHub Actions                             │  │
│  │      Resultado: Download disponível por 90 dias          │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                    ✅ PIPELINE CONCLUÍDO                         │
│                                                                  │
│  • Commit marcado com ✅ (verde)                                 │
│  • Artefato disponível para download                            │
│  • Notificação enviada (se configurado)                         │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Ciclo de Vida do Pipeline

```
   Developer              GitHub               Runner              Resultado
      │                     │                    │                    │
      │  git push           │                    │                    │
      ├────────────────────>│                    │                    │
      │                     │  Cria VM Ubuntu    │                    │
      │                     ├───────────────────>│                    │
      │                     │                    │  Clona código      │
      │                     │                    ├──────┐             │
      │                     │                    │<─────┘             │
      │                     │                    │  Instala Java      │
      │                     │                    ├──────┐             │
      │                     │                    │<─────┘             │
      │                     │                    │  Baixa deps Maven  │
      │                     │                    ├──────┐             │
      │                     │                    │<─────┘             │
      │                     │                    │  Compila (build)   │
      │                     │                    ├──────┐             │
      │                     │                    │<─────┘             │
      │                     │                    │  Roda testes       │
      │                     │                    ├──────┐             │
      │                     │                    │<─────┘             │
      │                     │                    │  Cria artifact     │
      │                     │                    ├──────┐             │
      │                     │                    │<─────┘             │
      │                     │  Salva artifact    │                    │
      │                     │<───────────────────┤                    │
      │                     │  Destroi VM        │                    │
      │                     ├───────────────────>│  [VM deletada]     │
      │                     │                    │                    │
      │                     │  Marca commit ✅   │                    │
      │                     ├────────────────────────────────────────>│
      │  Notificação ✅     │                    │                    │
      │<────────────────────┤                    │                    │
      │                     │                    │                    │
```

---

## 📂 Estrutura de Arquivos

```
seu-repositorio/
│
├── .github/                          ← Pasta especial do GitHub
│   └── workflows/                    ← Aqui ficam os pipelines
│       └── ci.yml                    ← SEU PIPELINE! 🎯
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/example/...      ← Código da aplicação
│   └── test/
│       └── java/
│           └── com/example/...      ← Testes unitários
│
├── target/                           ← Gerado pelo Maven (build)
│   └── learning-pipelines.jar       ← Artefato final
│
└── pom.xml                           ← Configuração Maven
```

---

## 🕐 Timeline de Execução (Exemplo Real)

```
00:00 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━ Pipeline iniciado
00:02 ✅ Checkout do código        (2 segundos)
00:10 ✅ Configurar Java           (8 segundos)
00:11 ✅ Cache Maven               (1 segundo - cache hit!)
00:56 ✅ Build com Maven           (45 segundos)
01:08 ✅ Rodar testes              (12 segundos)
01:09 ✅ Gerar artefato            (1 segundo)
01:12 ✅ Upload do artefato        (3 segundos)
01:12 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━ Pipeline concluído ✅

TEMPO TOTAL: 1 minuto e 12 segundos
```

**⚡ Com cache:**  ~1 min
**🐌 Sem cache:** ~3-4 min (tem que baixar todas as dependências)

---

## 🎯 Estados do Pipeline

### ✅ Success (Sucesso)
```
✅ CI/CD - Spring Boot
   └── build (1m 12s)
```
- **Todos os steps passaram**
- Commit marcado com ✅ verde
- Artefato disponível
- Pode fazer merge do PR

---

### ❌ Failure (Falha)
```
❌ CI/CD - Spring Boot
   └── build (45s)
       ├── ✅ Checkout do código
       ├── ✅ Configurar Java
       ├── ✅ Cache Maven
       ├── ✅ Build com Maven
       └── ❌ Rodar testes  ← FALHOU AQUI!
           (cancelou steps seguintes)
```
- **Algum step falhou**
- Commit marcado com ❌ vermelho
- Email de notificação enviado
- **NÃO pode fazer merge** (se branch protection estiver ativo)

---

### 🟡 In Progress (Executando)
```
🟡 CI/CD - Spring Boot
   └── build (30s)
       ├── ✅ Checkout do código
       ├── ✅ Configurar Java
       ├── ✅ Cache Maven
       └── ⏳ Build com Maven  ← RODANDO AGORA
```
- Pipeline ainda está executando
- Aguarde a conclusão

---

## 🔍 Como Ler os Logs

Quando você abre um step no GitHub Actions, verá algo assim:

```bash
Run mvn test
[INFO] Scanning for projects...
[INFO] 
[INFO] ----------------< com.example:learning-pipelines >-----------------
[INFO] Building learning-pipelines 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-surefire-plugin:3.0.0:test (default-test) @ learning-pipelines ---
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.learning_pipelines.LearningPipelinesApplicationTests
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.345 s
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0     ← ✅ PASSOU!
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS                                         ← ✅ SUCESSO!
[INFO] ------------------------------------------------------------------------
```

---

## 💰 Custos do GitHub Actions

### Planos Gratuitos (Open Source)

| Plano | Minutos Grátis | Armazenamento |
|-------|----------------|---------------|
| **Public Repo** | ♾️ Ilimitado | 500 MB |
| **Private Repo** | 2,000 min/mês | 500 MB |

### ⏱️ Tempo de Execução por Runner

| Runner | Multiplicador |
|--------|---------------|
| Linux (ubuntu) | 1x (recomendado) |
| Windows | 2x (conta em dobro) |
| macOS | 10x (conta 10x mais!) |

**Dica:** Use `ubuntu-latest` sempre que possível! 💚

---

## 🎓 Exercícios para Praticar

### Nível 1: Básico ⭐
- [ ] Criar o pipeline e fazer push
- [ ] Ver o pipeline executar no GitHub
- [ ] Baixar o artefato gerado
- [ ] Fazer um teste falhar propositalmente
- [ ] Corrigir e ver o pipeline passar

### Nível 2: Intermediário ⭐⭐
- [ ] Adicionar badge do status no README
- [ ] Configurar cache para PostgreSQL (se usar)
- [ ] Adicionar step de linting (Checkstyle)
- [ ] Configurar notificações de falha

### Nível 3: Avançado ⭐⭐⭐
- [ ] Adicionar matriz de testes (Java 17, 21)
- [ ] Implementar deploy automático
- [ ] Adicionar análise de segurança (Snyk/Dependabot)
- [ ] Criar workflow de release com changelog

---

## 🏆 Badge de Status

Adicione ao seu README.md:

```markdown
![CI/CD Status](https://github.com/SEU-USUARIO/SEU-REPO/workflows/CI%2FCD%20-%20Spring%20Boot/badge.svg)
```

Resultado:
![CI/CD Status](https://img.shields.io/badge/build-passing-brightgreen)

---

## 📚 Próximos Passos Recomendados

1. **Dominar o básico** (você está aqui! ✅)
2. Adicionar análise de código (SonarQube)
3. Implementar deploy automático (Heroku/Railway)
4. Configurar ambientes (dev/staging/prod)
5. Adicionar testes de integração
6. Implementar GitFlow com pipelines específicos

---

**Pronto para começar?** 🚀

Execute os comandos abaixo para ativar seu pipeline:

```bash
git add .github/workflows/ci.txt
git commit -m "ci: adiciona pipeline básico de CI/CD"
git push origin main
```

Depois, vá para: `https://github.com/SEU-USUARIO/SEU-REPO/actions` e veja a mágica acontecer! ✨

