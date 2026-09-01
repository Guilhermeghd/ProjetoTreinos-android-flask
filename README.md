# TreinosJC

[![CI](https://github.com/Guilhermeghd/ProjetoTreinos-android-flask/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/Guilhermeghd/ProjetoTreinos-android-flask/actions/workflows/ci.yml)

![Último commit](https://img.shields.io/github/last-commit/Guilhermeghd/ProjetoTreinos-android-flask)
![Tamanho do repo](https://img.shields.io/github/repo-size/Guilhermeghd/ProjetoTreinos-android-flask)
![Linguagem principal](https://img.shields.io/github/languages/top/Guilhermeghd/ProjetoTreinos-android-flask)



Um aplicativo Android completo para gerenciamento de treinos físicos, com integração a uma API REST em Python/Flask e banco de dados MySQL.

## 📋 Descrição do Projeto

TreinosJC é uma aplicação mobile que permite usuários criar, gerenciar e acompanhar seus treinos físicos. O aplicativo oferece funcionalidades de cadastro de exercícios, criação de planos de treino, monitoramento de progresso e gerenciamento de usuários através de um painel administrativo.

A aplicação segue uma arquitetura cliente-servidor, com o aplicativo Android se comunicando com uma API REST desenvolvida em Flask.

## 🛠️ Tecnologias

### Frontend (Aplicativo Android)
- **Linguagem:** Java 11
- **Framework:** Android 25 - 36 (API Level 25+)
- **IDE:** Android Studio
- **Bibliotecas principais:**
  - Retrofit 2.11.0 - Cliente HTTP para comunicação com API
  - Gson - Serialização/Desserialização de JSON
  - AndroidX - Componentes modernos do Android
  - Material Design - Interface visual

### Backend (API)
- **Linguagem:** Python
- **Framework:** Flask
- **Banco de Dados:** MySQL
- **Servidor:** Porta 5000 (Desenvolvimento)

## 📁 Estrutura de Pastas
## Estrutura do Projeto

```text
TreinosJC/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/treinosjc/
│   │   │   │   ├── adapter/                    # Adapters para RecyclerView
│   │   │   │   ├── api/                        # Configuração Retrofit e serviços
│   │   │   │   │   ├── RetrofitClient.java
│   │   │   │   │   └── ApiService.java
│   │   │   │   ├── dao/                        # Data Access Objects
│   │   │   │   ├── modelos/                    # Modelos (POJOs)
│   │   │   │   ├── views/                      # Componentes de UI customizados
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── LoginActivity.java
│   │   │   │   ├── CadastrarActivity.java
│   │   │   │   ├── IndexActivity.java
│   │   │   │   ├── IndexAdminActivity.java
│   │   │   │   ├── IndexUsuarioActivity.java
│   │   │   │   ├── GerenciarTreinosActivity.java
│   │   │   │   ├── TreinoDetalheActivity.java
│   │   │   │   ├── ExercicioActivity.java
│   │   │   │   ├── ExercicioDetalheActivity.java
│   │   │   │   ├── ProgressoTreinoActivity.java
│   │   │   │   ├── UsuarioDetalheActivity.java
│   │   │   │   ├── VincularUsuarioTreinoActivity.java
│   │   │   │   └── SelecionarUsuarioAtualActivity.java
│   │   │   └── res/                            # Layouts, drawables, strings, etc.
│   │   ├── androidTest/                        # Testes instrumentados
│   │   └── test/                               # Testes unitários
│   ├── build.gradle                            # Configuração do módulo app
│   └── proguard-rules.pro                      # Regras ProGuard
│
├── gradle/
│   └── libs.versions.toml                      # Versões das dependências
│
├── build.gradle                                # Configuração raiz do Gradle
├── settings.gradle                             # Configuração dos módulos
├── gradle.properties                           # Propriedades do Gradle
├── gradlew                                     # Gradle Wrapper (Linux/Mac)
├── gradlew.bat                                 # Gradle Wrapper (Windows)
└── local.properties                            # Configurações locais (não versionar)
```

## 🧪 Testes e Integração Contínua

A API Flask possui testes unitários (pytest) que rodam contra um banco SQLite em
memória, sem depender do MySQL.

```bash
cd api_flask_treinosjc_android
python -m venv .venv
.venv\Scripts\activate            # Windows (Linux/Mac: source .venv/bin/activate)
pip install -r requirements-dev.txt
pytest -v
```

O pipeline de CI (`.github/workflows/ci.yml`) roda automaticamente a cada push e
pull request para a branch `main` e executa:

1. **Instalação de dependências** – `pip install -r requirements-dev.txt`
2. **Execução dos testes** – `pytest -v`
3. **Build** – construção da imagem Docker da API (`Dockerfile`)
