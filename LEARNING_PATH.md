# 📚 Índice de Documentação - GitHub Actions

Este repositório contém documentação completa sobre GitHub Actions e CI/CD para iniciantes.

## 🎯 Por onde começar?

### 1️⃣ **Iniciante Absoluto** → Comece aqui!
📖 **[GITHUB_ACTIONS_BEGINNER_GUIDE.md](./GITHUB_ACTIONS_BEGINNER_GUIDE.md)**

Este guia explica:
- ✅ O que é GitHub Actions
- ✅ Conceitos fundamentais (workflow, job, step, runner)
- ✅ Explicação LINHA POR LINHA do pipeline
- ✅ Glossário de termos
- ✅ Troubleshooting de erros comuns
- ✅ Próximos passos e melhorias

**Tempo de leitura:** 20-30 minutos

---

### 2️⃣ **Visual Learner** → Entenda o fluxo
🎨 **[PIPELINE_VISUAL_FLOW.md](./PIPELINE_VISUAL_FLOW.md)**

Contém:
- ✅ Diagramas visuais do fluxo do pipeline
- ✅ Timeline de execução
- ✅ Estados do pipeline (success, failure, in progress)
- ✅ Como ler logs
- ✅ Explicação de custos e limites
- ✅ Exercícios práticos

**Tempo de leitura:** 15 minutos

---

### 3️⃣ **Referência Rápida** → Para consultas
📝 **[GITHUB_ACTIONS_CHEATSHEET.md](./GITHUB_ACTIONS_CHEATSHEET.md)**

Uma folha de cola com:
- ✅ Sintaxe YAML de workflows
- ✅ Actions mais usadas
- ✅ Exemplos de código prontos
- ✅ Comandos Maven úteis
- ✅ Templates para copiar e colar

**Tempo de leitura:** 5 minutos (consulta rápida)

---

### 4️⃣ **O Pipeline em Ação** → Código real
⚙️ **[.github/workflows/ci.yml](ci.txt)**

O arquivo real do pipeline que:
- ✅ Faz checkout do código
- ✅ Configura Java 21
- ✅ Usa cache para acelerar builds
- ✅ Compila o projeto
- ✅ Roda os testes
- ✅ Gera e faz upload do artefato JAR

**Este arquivo está PRONTO para usar!**

---

## 🚀 Quick Start (5 minutos)

### Passo 1: Commit o pipeline
```bash
git add .github/workflows/ci.txt
git commit -m "ci: adiciona pipeline básico de CI/CD"
git push origin main
```

### Passo 2: Ver execução
1. Vá para: `https://github.com/SEU-USUARIO/SEU-REPO`
2. Clique na aba **Actions**
3. Veja o workflow executando em tempo real! 🎉

### Passo 3: Baixar artefato
1. Após a conclusão, clique no workflow
2. Seção **Artifacts** → Download `app-springboot`
3. Você terá o JAR compilado! 📦

---

## 📖 Ordem Recomendada de Leitura

### Para quem nunca usou CI/CD:
```
1. GITHUB_ACTIONS_BEGINNER_GUIDE.md  ← Entenda os conceitos
2. PIPELINE_VISUAL_FLOW.md           ← Veja como funciona
3. Faça o push do pipeline           ← Pratique!
4. GITHUB_ACTIONS_CHEATSHEET.md      ← Consulte quando precisar
```

### Para quem já conhece o básico:
```
1. .github/workflows/ci.yml          ← Veja o código
2. GITHUB_ACTIONS_CHEATSHEET.md      ← Adapte para seu caso
3. PIPELINE_VISUAL_FLOW.md           ← Entenda detalhes avançados
```

---

## 🎯 O que você vai aprender

- ✅ Configurar um pipeline CI/CD do zero
- ✅ Automatizar testes a cada push
- ✅ Gerar artefatos (JARs) automaticamente
- ✅ Usar cache para acelerar builds
- ✅ Entender logs e debugar falhas
- ✅ Evoluir o pipeline com novas features

---

## 📊 Estrutura do Pipeline Atual

```
┌─────────────────────────────────────────┐
│  Evento: Push/Pull Request na main      │
└──────────────────┬──────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────┐
│  Job: build (ubuntu-latest)             │
│                                          │
│  1. 🧾 Checkout do código               │
│  2. ☕ Configurar Java 21                │
│  3. 💾 Cache Maven dependencies         │
│  4. 📦 Build com Maven                  │
│  5. 🧪 Rodar testes                     │
│  6. 📤 Gerar artefato (JAR)             │
│  7. 🔼 Upload do artefato               │
└─────────────────────────────────────────┘
```

---

## 🔄 Próximas Evoluções Possíveis

Após dominar o básico, você pode adicionar:

1. **Análise de Qualidade**
   - SonarQube/SonarCloud
   - Checkstyle
   - SpotBugs

2. **Cobertura de Código**
   - JaCoCo
   - Codecov/Coveralls

3. **Security Scanning**
   - Dependabot
   - Snyk
   - OWASP Dependency Check

4. **Deploy Automático**
   - Heroku
   - Railway
   - AWS/Azure/GCP

5. **Notificações**
   - Slack
   - Discord
   - Email

6. **Matrix Strategy**
   - Testar múltiplas versões de Java
   - Testar em diferentes SOs

---

## 🆘 Precisa de Ajuda?

### Documentação Oficial
- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [GitHub Actions Marketplace](https://github.com/marketplace?type=actions)

### Comunidade
- [GitHub Community Forum](https://github.community/)
- [Stack Overflow - github-actions tag](https://stackoverflow.com/questions/tagged/github-actions)

### Exemplos
- [Actions Starter Workflows](https://github.com/actions/starter-workflows)
- [Awesome Actions](https://github.com/sdras/awesome-actions)

---

## 📝 Checklist de Aprendizado

- [ ] Li o guia para iniciantes completo
- [ ] Entendi todos os conceitos fundamentais
- [ ] Fiz push do pipeline e vi executar
- [ ] Baixei o artefato gerado
- [ ] Fiz um teste falhar propositalmente
- [ ] Corrigi e vi o pipeline passar novamente
- [ ] Adicionei badge de status no README
- [ ] Explorei a aba Actions no GitHub
- [ ] Entendi como ler os logs
- [ ] Sei como debugar falhas básicas

---

## 🎓 Certificado de Conclusão

Quando você completar todos os itens do checklist acima, você terá dominado:

✅ **Fundamentos de CI/CD**
✅ **GitHub Actions Workflow Syntax**
✅ **Maven Build Automation**
✅ **Artifact Management**
✅ **Basic Troubleshooting**

**Próximo nível:** Implementar análise de código e deploy automático! 🚀

---

## 📂 Estrutura dos Arquivos

```
learning-pipelines/
│
├── .github/
│   └── workflows/
│       └── ci.yml                           ← Pipeline ativo
│
├── docs/  (ou raiz do projeto)
│   ├── GITHUB_ACTIONS_BEGINNER_GUIDE.md    ← Guia completo
│   ├── PIPELINE_VISUAL_FLOW.md             ← Diagramas visuais
│   ├── GITHUB_ACTIONS_CHEATSHEET.md        ← Referência rápida
│   └── LEARNING_PATH.md                     ← Este arquivo!
│
└── src/...                                  ← Código da aplicação
```

---

## 💡 Dica Final

**Aprenda fazendo!** 🛠️

A melhor forma de aprender GitHub Actions é:
1. Começar simples (você já tem isso! ✅)
2. Ver funcionando
3. Fazer modificações pequenas
4. Observar o resultado
5. Iterar e melhorar

Não tenha medo de errar - cada falha é uma oportunidade de aprendizado! 💪

---

## 🎉 Parabéns!

Você agora tem:
- ✅ Um pipeline funcional
- ✅ Documentação completa
- ✅ Conhecimento para evoluir

**Happy coding and happy automating!** 🚀

---

**Criado com ❤️ para estudantes da FIAP - Fase 4**

*Última atualização: Novembro 2025*

