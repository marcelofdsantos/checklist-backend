# 📋 Sistema de Checklist Digital de Empilhadeiras

## 📖 Sumário

- [Visão Geral](#visão-geral)
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Modelo de Dados](#modelo-de-dados)
- [API REST](#api-rest)
- [Autenticação e Segurança](#autenticação-e-segurança)
- [Regras de Negócio](#regras-de-negócio)
- [Testes](#testes)
- [Deploy](#deploy)
- [Troubleshooting](#troubleshooting)
- [Contribuindo](#contribuindo)

---

## 🎯 Visão Geral

Sistema web full-stack para gerenciamento digital de checklists de inspeção de empilhadeiras, substituindo o processo manual em papel por uma solução moderna, sustentável e eficiente.

### Objetivos do Projeto

- ✅ **Digitalização:** Eliminar checklists em papel
- ✅ **Sustentabilidade:** Reduzir desperdício de papel e recursos
- ✅ **Rastreabilidade:** Histórico completo de inspeções
- ✅ **Automação:** Bloqueio automático de equipamentos reprovados
- ✅ **Compliance:** Atendimento às normas de segurança NR-11

### Impacto Esperado

- 🌱 Redução de 95% no uso de papel
- ⚡ Diminuição de 70% no tempo de preenchimento
- 🔒 100% de rastreabilidade das inspeções
- 📊 Geração automática de relatórios e métricas

---

## 🏗️ Arquitetura

### Visão Geral da Arquitetura

```
┌─────────────────────────────────────────────────────────────┐
│                     FRONTEND (Angular)                      │
│  ┌─────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │  Login      │  │  Checklist   │  │  Guards &        │  │
│  │  Component  │  │  Component   │  │  Interceptors    │  │
│  └─────────────┘  └──────────────┘  └──────────────────┘  │
│         │                │                     │            │
│         └────────────────┴─────────────────────┘            │
│                          │                                   │
│              ┌───────────▼──────────┐                       │
│              │   HTTP Client (JWT)  │                       │
│              └───────────┬──────────┘                       │
└────────────────────────────┬────────────────────────────────┘
                             │ REST API
                             │ (JSON over HTTP)
┌────────────────────────────▼────────────────────────────────┐
│                    BACKEND (Spring Boot)                    │
│  ┌──────────────────────────────────────────────────────┐  │
│  │            Security Layer (JWT + Spring)             │  │
│  └──────────────────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Controllers → Services → Repositories → Entities    │  │
│  └──────────────────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │      Exception Handling & Validation Layer           │  │
│  └──────────────────────────────────────────────────────┘  │
└────────────────────────────┬────────────────────────────────┘
                             │ JPA/Hibernate
                             │
┌────────────────────────────▼────────────────────────────────┐
│                  DATABASE (PostgreSQL 16)                   │
│  ┌──────────┐  ┌──────────────┐  ┌────────────────────┐   │
│  │ usuarios │  │ empilhadeiras│  │ checklists         │   │
│  └──────────┘  └──────────────┘  └────────────────────┘   │
│                                   │                         │
│                          ┌────────▼─────────┐              │
│                          │ itens_checklist  │              │
│                          └──────────────────┘              │
└─────────────────────────────────────────────────────────────┘
```

### Padrões Arquiteturais

- **MVC (Model-View-Controller):** Separação de responsabilidades
- **REST API:** Comunicação stateless via HTTP
- **Repository Pattern:** Abstração de acesso a dados
- **DTO Pattern:** Transfer objects entre camadas
- **Service Layer:** Lógica de negócio centralizada
- **Dependency Injection:** Inversão de controle com Spring

---

## 🛠️ Tecnologias

### Backend

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Java** | 21 LTS | Linguagem de programação |
| **Spring Boot** | 3.2.2 | Framework de aplicação |
| **Spring Data JPA** | 3.2.2 | Persistência de dados |
| **Spring Security** | 6.2.1 | Autenticação e autorização |
| **Hibernate** | 6.4.1 | ORM (Object-Relational Mapping) |
| **PostgreSQL** | 16 | Banco de dados relacional |
| **Maven** | 3.9.12 | Gerenciamento de dependências |
| **Lombok** | 1.18.30 | Redução de boilerplate code |
| **JJWT** | 0.12.5 | Implementação JWT |
| **Jakarta Validation** | 3.0.2 | Validação de dados |

### Frontend

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Angular** | 21 | Framework SPA |
| **TypeScript** | 5.7.2 | Superset do JavaScript |
| **RxJS** | 7.8.1 | Programação reativa |
| **Angular Material** | 21 | Biblioteca de componentes UI |

### DevOps & Tools

| Ferramenta | Versão | Descrição |
|------------|--------|-----------|
| **Docker** | Latest | Containerização |
| **Git** | 2.x | Controle de versão |
| **Postman** | Latest | Testes de API |
| **pgAdmin** | 4.x | Administração PostgreSQL |

---

## 📋 Pré-requisitos

### Desenvolvimento

- **Java Development Kit (JDK):** 21 ou superior
  ```bash
  java -version
  # openjdk version "21.0.8" 2025-01-16 LTS
  ```

- **Maven:** 3.9.12 ou superior
  ```bash
  mvn -version
  # Apache Maven 3.9.12
  ```

- **PostgreSQL:** 16 ou superior
  ```bash
  psql --version
  # psql (PostgreSQL) 16.x
  ```

- **Node.js:** 20.x ou superior (para o frontend)
  ```bash
  node -version
  # v20.x.x
  ```

### Ambiente

- **Sistema Operacional:** Windows 10/11, Linux, macOS
- **RAM:** Mínimo 4GB (recomendado 8GB)
- **Disco:** 2GB de espaço livre
- **IDE Recomendada:** 
  - Backend: IntelliJ IDEA, Eclipse, VS Code
  - Frontend: VS Code, WebStorm

---

## 🚀 Instalação

### 1. Clonar o Repositório

```bash
# Backend
git clone https://github.com/seu-usuario/checklist-backend.git
cd checklist-backend

# Frontend (em outro terminal)
git clone https://github.com/seu-usuario/checklist-frontend.git
cd checklist-frontend
```

### 2. Configurar Banco de Dados

```sql
-- Conectar ao PostgreSQL como superuser
psql -U postgres

-- Criar banco de dados
CREATE DATABASE checklist_db;

-- Criar usuário (opcional)
CREATE USER checklist_user WITH PASSWORD 'sua_senha_segura';
GRANT ALL PRIVILEGES ON DATABASE checklist_db TO checklist_user;

-- Verificar
\l checklist_db
```

### 3. Configurar Backend

```bash
cd checklist-backend

# Copiar arquivo de configuração
cp src/main/resources/application.properties.example src/main/resources/application.properties

# Editar configurações (opcional)
nano src/main/resources/application.properties
```

**Configurações mínimas:**
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/checklist_db
spring.datasource.username=postgres
spring.datasource.password=******

# JWT (gerar uma chave segura para produção)
jwt.secret=sua-chave-secreta-muito-longa-e-segura-com-pelo-menos-256-bits
jwt.expiration=86400000
```

### 4. Compilar e Executar Backend

```bash
# Limpar e compilar
mvn clean install

# Executar
mvn spring-boot:run

# Ou executar o JAR
java -jar target/checklist-backend-1.0.0.jar
```

**Saída esperada:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.2)

[INFO] ChecklistApplication : Started ChecklistApplication in 8.532 seconds
[INFO] Tomcat started on port(s): 8080 (http)
```

### 5. Configurar e Executar Frontend

```bash
cd checklist-frontend

# Instalar dependências
npm install

# Executar em modo de desenvolvimento
ng serve

# Ou
npm start
```

**Acessar:** http://localhost:4200

### 6. Dados Iniciais

O backend cria automaticamente dados de teste na primeira execução:

**Usuários:**
- **ADMIN** / admin123 (Administrador)
- **SUP001** / senha123 (Supervisor)
- **OPR001** / senha123 (Operador)
- **OPR002** / senha123 (Operador)
- **OPR003** / senha123 (Operador)

**Empilhadeiras:** 6 empilhadeiras pré-cadastradas

---

## ⚙️ Configuração

### Variáveis de Ambiente

#### Backend

```bash
# Linux/Mac
export DB_USERNAME=postgres
export DB_PASSWORD=*****
export JWT_SECRET=sua-chave-secreta

# Windows
set DB_USERNAME=postgres
set DB_PASSWORD=*****
set JWT_SECRET=sua-chave-secreta
```

#### Arquivo application.properties

```properties
# ========================================
# SERVER
# ========================================
server.port=8080
server.servlet.context-path=/api

# ========================================
# DATABASE
# ========================================
spring.datasource.url=jdbc:postgresql://localhost:5432/checklist_db
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:*****}
spring.datasource.driver-class-name=org.postgresql.Driver

# ========================================
# JPA/HIBERNATE
# ========================================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# ========================================
# TIMEZONE
# ========================================
spring.jpa.properties.hibernate.jdbc.time_zone=America/Sao_Paulo
spring.jackson.time-zone=America/Sao_Paulo

# ========================================
# JWT
# ========================================
jwt.secret=${JWT_SECRET:deicmar-checklist-empilhadeiras-secret-key-2025}
jwt.expiration=86400000

# ========================================
# LOGGING
# ========================================
logging.level.root=INFO
logging.level.com.deicmar=DEBUG
logging.level.org.springframework.security=DEBUG

# ========================================
# CORS
# ========================================
cors.allowed-origins=http://localhost:4200,http://localhost:8080
```

### Perfis de Execução

#### Development (padrão)
```bash
mvn spring-boot:run
```

#### Production
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod

# Ou
java -jar target/checklist-backend-1.0.0.jar --spring.profiles.active=prod
```

**Arquivo:** `application-prod.properties`
```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
logging.level.root=WARN
logging.level.com.deicmar=INFO
```

---

## 📁 Estrutura do Projeto

### Backend (Spring Boot)

```
checklist-backend/
│
├── src/
│   ├── main/
│   │   ├── java/com/deicmar/checklist/
│   │   │   ├── ChecklistApplication.java          # Classe principal
│   │   │   │
│   │   │   ├── config/                            # Configurações
│   │   │   │   ├── DataInitializer.java          # Dados iniciais
│   │   │   │   └── CorsConfig.java                # CORS
│   │   │   │
│   │   │   ├── controller/                        # Camada de API
│   │   │   │   ├── AuthController.java           # Autenticação
│   │   │   │   ├── UsuarioController.java        # Usuários
│   │   │   │   ├── EmpilhadeiraController.java   # Empilhadeiras
│   │   │   │   └── ChecklistController.java      # Checklists
│   │   │   │
│   │   │   ├── service/                           # Lógica de negócio
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── UsuarioService.java
│   │   │   │   ├── EmpilhadeiraService.java
│   │   │   │   └── ChecklistService.java
│   │   │   │
│   │   │   ├── repository/                        # Acesso a dados
│   │   │   │   ├── UsuarioRepository.java
│   │   │   │   ├── EmpilhadeiraRepository.java
│   │   │   │   └── ChecklistRepository.java
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── entity/                        # Entidades JPA
│   │   │   │   │   ├── Usuario.java
│   │   │   │   │   ├── Empilhadeira.java
│   │   │   │   │   ├── Checklist.java
│   │   │   │   │   └── ItemChecklist.java
│   │   │   │   │
│   │   │   │   └── enums/                         # Enumerações
│   │   │   │       ├── Perfil.java
│   │   │   │       ├── Turno.java
│   │   │   │       ├── TipoItem.java
│   │   │   │       ├── StatusItem.java
│   │   │   │       └── ResultadoChecklist.java
│   │   │   │
│   │   │   ├── dto/                               # Data Transfer Objects
│   │   │   │   ├── auth/
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   └── LoginResponse.java
│   │   │   │   ├── usuario/
│   │   │   │   ├── empilhadeira/
│   │   │   │   ├── checklist/
│   │   │   │   └── error/
│   │   │   │
│   │   │   ├── mapper/                            # Conversores
│   │   │   │   ├── UsuarioMapper.java
│   │   │   │   ├── EmpilhadeiraMapper.java
│   │   │   │   └── ChecklistMapper.java
│   │   │   │
│   │   │   ├── security/                          # Segurança
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JwtUtil.java
│   │   │   │   └── JwtAuthenticationFilter.java
│   │   │   │
│   │   │   └── exception/                         # Exceções
│   │   │       ├── GlobalExceptionHandler.java
│   │   │       ├── ResourceNotFoundException.java
│   │   │       └── BusinessException.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       └── init.sql
│   │
│   └── test/                                      # Testes (a implementar)
│       └── java/com/deicmar/checklist/
│
├── pom.xml                                        # Maven
├── docker-compose.yml                             # Docker
├── .gitignore
└── README.md
```

### Frontend (Angular)

```
checklist-frontend/
│
├── src/
│   ├── app/
│   │   ├── core/                                  # Serviços core
│   │   │   ├── guards/
│   │   │   │   └── auth.guard.ts
│   │   │   ├── interceptors/
│   │   │   │   └── auth.interceptor.ts
│   │   │   └── services/
│   │   │       ├── auth.service.ts
│   │   │       ├── checklist.service.ts
│   │   │       └── empilhadeira.service.ts
│   │   │
│   │   ├── features/                              # Componentes
│   │   │   ├── login/
│   │   │   │   ├── login.component.ts
│   │   │   │   ├── login.component.html
│   │   │   │   └── login.component.css
│   │   │   │
│   │   │   └── checklist/
│   │   │       ├── checklist.component.ts
│   │   │       ├── checklist.component.html
│   │   │       └── checklist.component.css
│   │   │
│   │   ├── shared/                                # Compartilhado
│   │   │   ├── models/
│   │   │   └── components/
│   │   │
│   │   ├── app.component.ts
│   │   ├── app.routes.ts
│   │   └── app.config.ts
│   │
│   ├── assets/
│   ├── environments/
│   │   ├── environment.ts
│   │   └── environment.prod.ts
│   │
│   ├── index.html
│   └── styles.css
│
├── angular.json
├── package.json
├── tsconfig.json
└── README.md
```

---

## 🗄️ Modelo de Dados

### Diagrama ER

```
┌──────────────────┐
│    USUARIOS      │
├──────────────────┤
│ • id (PK)        │
│ • re (UNIQUE)    │
│ • nome_completo  │
│ • senha          │
│ • perfil (ENUM)  │
│ • ativo          │
│ • criado_em      │
└────────┬─────────┘
         │ 1
         │
         │ N
┌────────▼─────────┐         ┌──────────────────┐
│   CHECKLISTS     │ N     1 │  EMPILHADEIRAS   │
├──────────────────┤◄────────┤──────────────────┤
│ • id (PK)        │         │ • id (PK)        │
│ • data           │         │ • modelo         │
│ • hora_vistoria  │         │ • tipo           │
│ • turno (ENUM)   │         │ • capacidade     │
│ • horimetro_ini  │         │ • bloqueada      │
│ • horimetro_fim  │         │ • motivo_bloq    │
│ • operador_id(FK)│         │ • ativa          │
│ • empilh_id (FK) │         │ • criado_em      │
│ • resultado(ENUM)│         └──────────────────┘
│ • observacao     │
│ • criado_em      │
└────────┬─────────┘
         │ 1
         │
         │ N
┌────────▼─────────┐
│ ITENS_CHECKLIST  │
├──────────────────┤
│ • id (PK)        │
│ • checklist_id(FK│
│ • descricao      │
│ • tipo (ENUM)    │
│ • status (ENUM)  │
│ • observacao     │
└──────────────────┘
```

### Entidades

#### Usuario
```sql
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    re VARCHAR(20) UNIQUE NOT NULL,
    nome_completo VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(20) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP
);
```

**Perfis:**
- `ADMIN`: Administrador total do sistema
- `SUPERVISOR`: Gerencia empilhadeiras e visualiza relatórios
- `OPERADOR`: Cria e visualiza seus próprios checklists

#### Empilhadeira
```sql
CREATE TABLE empilhadeiras (
    id BIGSERIAL PRIMARY KEY,
    modelo VARCHAR(50) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    capacidade INTEGER NOT NULL,
    bloqueada BOOLEAN DEFAULT FALSE,
    motivo_bloqueio VARCHAR(500),
    ativa BOOLEAN DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP
);
```

**Tipos:** Elétrica, GLP, Diesel

#### Checklist
```sql
CREATE TABLE checklists (
    id BIGSERIAL PRIMARY KEY,
    data DATE NOT NULL,
    hora_vistoria TIME NOT NULL,
    turno VARCHAR(10) NOT NULL,
    horimetro_inicial INTEGER NOT NULL,
    horimetro_final INTEGER,
    operador_id BIGINT NOT NULL,
    empilhadeira_id BIGINT NOT NULL,
    resultado VARCHAR(20) NOT NULL,
    observacao_geral VARCHAR(1000),
    criado_em TIMESTAMP NOT NULL,
    FOREIGN KEY (operador_id) REFERENCES usuarios(id),
    FOREIGN KEY (empilhadeira_id) REFERENCES empilhadeiras(id)
);
```

**Turnos:** A, B, C  
**Resultados:** APROVADO, REPROVADO

#### ItemChecklist
```sql
CREATE TABLE itens_checklist (
    id BIGSERIAL PRIMARY KEY,
    checklist_id BIGINT NOT NULL,
    descricao VARCHAR(200) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    observacao VARCHAR(500),
    FOREIGN KEY (checklist_id) REFERENCES checklists(id) ON DELETE CASCADE
);
```

**Tipos:**
- `CONFORME`: Item de verificação normal
- `IMPEDITIVO`: Item crítico de segurança

**Status:**
- `OK`: Conforme/Aprovado
- `NAO_CONFORME`: Não conforme/Problema detectado

### Relacionamentos

1. **Usuario → Checklist:** 1:N (Um operador pode ter vários checklists)
2. **Empilhadeira → Checklist:** 1:N (Uma empilhadeira pode ter vários checklists)
3. **Checklist → ItemChecklist:** 1:N (Um checklist tem vários itens)

### Índices

```sql
-- Performance em consultas frequentes
CREATE INDEX idx_checklist_data ON checklists(data);
CREATE INDEX idx_checklist_operador ON checklists(operador_id);
CREATE INDEX idx_checklist_empilhadeira ON checklists(empilhadeira_id);
CREATE INDEX idx_empilhadeira_bloqueada ON empilhadeiras(bloqueada);
CREATE INDEX idx_usuario_perfil ON usuarios(perfil);
CREATE INDEX idx_usuario_ativo ON usuarios(ativo);
```

---

## 🔌 API REST

### Base URL
```
http://localhost:8080/api
```

### Autenticação

#### POST /auth/login
Autentica usuário e retorna token JWT.

**Request:**
```json
{
  "re": "OPR001",
  "senha": "senha123"
}
```

**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "usuarioId": 3,
  "re": "OPR001",
  "nomeCompleto": "João da Silva",
  "perfil": "OPERADOR"
}
```

**Errors:**
- `400 Bad Request`: Dados inválidos
- `401 Unauthorized`: Credenciais incorretas

### Usuários

#### GET /usuarios
Lista todos os usuários (requer ADMIN ou SUPERVISOR).

**Headers:**
```
Authorization: Bearer {token}
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "re": "ADMIN",
    "nomeCompleto": "Administrador",
    "perfil": "ADMIN",
    "ativo": true,
    "criadoEm": "2025-02-01T10:00:00"
  }
]
```

#### POST /usuarios
Cria novo usuário (requer ADMIN).

**Request:**
```json
{
  "re": "OPR004",
  "nomeCompleto": "Carlos Souza",
  "senha": "senha123",
  "perfil": "OPERADOR"
}
```

**Response:** `201 Created`

#### GET /usuarios/{id}
Busca usuário por ID.

#### DELETE /usuarios/{id}
Inativa usuário (requer ADMIN).

#### PATCH /usuarios/{id}/senha
Atualiza senha do usuário.

### Empilhadeiras

#### GET /empilhadeiras/disponiveis
Lista empilhadeiras disponíveis (ativas e não bloqueadas).

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "modelo": "YALE GP030",
    "tipo": "Elétrica",
    "capacidade": 3000,
    "bloqueada": false,
    "ativa": true
  }
]
```

#### POST /empilhadeiras
Cria nova empilhadeira (requer ADMIN ou SUPERVISOR).

**Request:**
```json
{
  "modelo": "HYSTER H80FT",
  "tipo": "GLP",
  "capacidade": 8000
}
```

#### PATCH /empilhadeiras/{id}/bloquear
Bloqueia empilhadeira (requer ADMIN ou SUPERVISOR).

**Request:**
```json
{
  "motivo": "Manutenção preventiva programada"
}
```

#### PATCH /empilhadeiras/{id}/desbloquear
Desbloqueia empilhadeira (requer ADMIN ou SUPERVISOR).

### Checklists

#### POST /checklists
Cria novo checklist.

**Request:**
```json
{
  "data": "2025-02-02",
  "horaVistoria": "08:30:00",
  "turno": "A",
  "horimetroInicial": 15000,
  "horimetroFinal": 15100,
  "operadorId": 3,
  "empilhadeiraId": 1,
  "itens": [
    {
      "descricao": "Verificação de freios",
      "tipo": "IMPEDITIVO",
      "status": "OK",
      "observacao": "Freios em perfeito estado"
    },
    {
      "descricao": "Nível de óleo",
      "tipo": "CONFORME",
      "status": "OK"
    },
    {
      "descricao": "Estado dos pneus",
      "tipo": "IMPEDITIVO",
      "status": "NAO_CONFORME",
      "observacao": "Pneu traseiro direito desgastado"
    }
  ],
  "observacaoGeral": "Empilhadeira necessita troca de pneu"
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "data": "2025-02-02",
  "horaVistoria": "08:30:00",
  "diaSemana": "Domingo",
  "turno": "A",
  "horimetroInicial": 15000,
  "horimetroFinal": 15100,
  "resultado": "REPROVADO",
  "operador": {
    "id": 3,
    "re": "OPR001",
    "nomeCompleto": "João da Silva"
  },
  "empilhadeira": {
    "id": 1,
    "modelo": "YALE GP030",
    "tipo": "Elétrica",
    "bloqueada": true
  },
  "itens": [...],
  "observacaoGeral": "Empilhadeira necessita troca de pneu",
  "criadoEm": "2025-02-02T08:35:00"
}
```

**Regras:**
- Empilhadeira é bloqueada automaticamente se resultado = REPROVADO
- Resultado = REPROVADO se houver item IMPEDITIVO com status NAO_CONFORME

#### GET /checklists
Lista todos os checklists (requer ADMIN ou SUPERVISOR).

#### GET /checklists/{id}
Busca checklist por ID.

#### GET /checklists/empilhadeira/{id}
Lista checklists de uma empilhadeira.

#### GET /checklists/operador/{id}
Lista checklists de um operador.

#### GET /checklists/data/{data}
Lista checklists de uma data específica.

**Formato:** `YYYY-MM-DD`

#### GET /checklists/periodo?dataInicio=YYYY-MM-DD&dataFim=YYYY-MM-DD
Lista checklists em um período.

### Códigos de Status HTTP

| Código | Descrição |
|--------|-----------|
| `200` | OK - Sucesso |
| `201` | Created - Recurso criado |
| `204` | No Content - Operação sem retorno |
| `400` | Bad Request - Dados inválidos |
| `401` | Unauthorized - Não autenticado |
| `403` | Forbidden - Sem permissão |
| `404` | Not Found - Recurso não encontrado |
| `500` | Internal Server Error - Erro no servidor |

### Formato de Erro

```json
{
  "timestamp": "2025-02-02T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Empilhadeira não encontrada com ID: 999",
  "details": null
}
```

---

## 🔐 Autenticação e Segurança

### JWT (JSON Web Token)

#### Estrutura do Token
```
eyJhbGciOiJIUzI1NiJ9.eyJwZXJmaWwiOiJPUEVSQURPUiIsInN1YiI6Ik9QUjAwMSIsImlhdCI6MTczODQ4MTIwMCwiZXhwIjoxNzM4NTY3NjAwfQ.signature
│──────────────────│ │──────────────────────────────────────────────────────────────────────────────────────│ │─────────│
     HEADER                                            PAYLOAD                                                 SIGNATURE
```

#### Payload
```json
{
  "sub": "OPR001",           // Subject: RE do usuário
  "perfil": "OPERADOR",      // Perfil do usuário
  "iat": 1738481200,         // Issued At: Timestamp de criação
  "exp": 1738567600          // Expiration: Timestamp de expiração (24h)
}
```

#### Algoritmo
- **HS256** (HMAC with SHA-256)
- Chave secreta de 256 bits

#### Validação
1. Verificar assinatura
2. Verificar expiração
3. Extrair claims (sub, perfil)
4. Carregar usuário do banco

### Spring Security

#### Configuração

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/empilhadeiras/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/checklists").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

#### Filtro JWT

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) {
        String token = extractTokenFromRequest(request);
        
        if (token != null && jwtUtil.validateToken(token)) {
            String username = jwtUtil.extractUsername(token);
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
    }
}
```

### Autorização

#### Anotações de Segurança

```java
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")  // Apenas ADMIN
    public ResponseEntity<UsuarioResponse> criar(@RequestBody UsuarioRequest request) {
        // ...
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")  // ADMIN ou SUPERVISOR
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        // ...
    }
}
```

### Senha

#### Algoritmo
- **BCrypt** com salt aleatório
- Fator de custo: 10 (2^10 = 1024 iterações)

#### Exemplo
```java
String rawPassword = "senha123";
String encodedPassword = passwordEncoder.encode(rawPassword);
// $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
// true
```

### CORS (Cross-Origin Resource Sharing)

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(
        "http://localhost:4200",
        "http://localhost:8080"
    ));
    configuration.setAllowedMethods(Arrays.asList(
        "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
    ));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```

### Segurança de Produção

#### Checklist
- [✅] Usar HTTPS/TLS em produção
- [✅] Gerar chave JWT segura (>= 256 bits)
- [✅] Rotacionar chaves periodicamente
- [✅] Implementar rate limiting
- [✅] Implementar refresh tokens
- [✅] Validar entrada de dados
- [✅] Sanitizar saída de dados
- [✅] Implementar auditoria
- [✅] Monitorar tentativas de login
- [✅] Implementar bloqueio após falhas
- [✅] Usar variáveis de ambiente
- [✅] Não commitar segredos no Git
- [✅] Implementar HTTPS-only cookies
- [✅] Configurar headers de segurança
- [✅] Implementar CSP (Content Security Policy)

---

## 📊 Regras de Negócio

### Checklist

#### Criação
1. **Validações:**
   - Operador deve estar ativo
   - Empilhadeira deve estar ativa e não bloqueada
   - Data não pode ser futura
   - Horímetro final >= horímetro inicial (se informado)
   - Mínimo 1 item obrigatório

2. **Cálculo de Resultado:**
   ```
   SE existe algum item IMPEDITIVO com status NAO_CONFORME
   ENTÃO resultado = REPROVADO
   SENÃO resultado = APROVADO
   ```

3. **Bloqueio Automático:**
   ```
   SE resultado = REPROVADO
   ENTÃO
     empilhadeira.bloqueada = TRUE
     empilhadeira.motivoBloqueio = "Checklist reprovado em [data]"
   FIM SE
   ```

#### Exemplo de Fluxo

```java
@Transactional
public ChecklistResponse criar(ChecklistRequest request) {
    // 1. Validar operador
    Usuario operador = validarOperador(request.getOperadorId());
    
    // 2. Validar empilhadeira
    Empilhadeira empilhadeira = validarEmpilhadeira(request.getEmpilhadeiraId());
    
    // 3. Validar horímetros
    validarHorimetros(request);
    
    // 4. Calcular resultado
    ResultadoChecklist resultado = calcularResultado(request.getItens());
    
    // 5. Criar checklist
    Checklist checklist = criarChecklist(request, operador, empilhadeira, resultado);
    
    // 6. Salvar
    Checklist salvo = checklistRepository.save(checklist);
    
    // 7. Bloquear empilhadeira se reprovado
    if (resultado == ResultadoChecklist.REPROVADO) {
        bloquearEmpilhadeira(empilhadeira, salvo.getData());
    }
    
    return checklistMapper.toResponse(salvo);
}
```

### Empilhadeira

#### Bloqueio
- Apenas ADMIN e SUPERVISOR podem bloquear/desbloquear
- Empilhadeira bloqueada não pode ser usada em novos checklists
- Bloqueio automático ao reprovar checklist

#### Inativação
- Apenas ADMIN pode inativar
- Empilhadeira inativa não aparece em listagens
- Mantém histórico de checklists

### Usuário

#### Criação
- RE deve ser único
- Senha mínima de 6 caracteres
- Senha é criptografada com BCrypt
- Apenas ADMIN pode criar usuários

#### Perfis e Permissões

| Operação | ADMIN | SUPERVISOR | OPERADOR |
|----------|-------|------------|----------|
| Criar usuário | ✅ | ❌ | ❌ |
| Ver usuários | ✅ | ✅ | ❌ |
| Inativar usuário | ✅ | ❌ | ❌ |
| Criar empilhadeira | ✅ | ✅ | ❌ |
| Bloquear empilhadeira | ✅ | ✅ | ❌ |
| Desbloquear empilhadeira | ✅ | ✅ | ❌ |
| Ver todas empilhadeiras | ✅ | ✅ | ✅ |
| Criar checklist | ✅ | ✅ | ✅ |
| Ver todos checklists | ✅ | ✅ | ❌ |
| Ver seus checklists | ✅ | ✅ | ✅ |

---

## 🧪 Testes

### Estrutura de Testes

```
src/test/java/com/deicmar/checklist/
├── unit/
│   ├── service/
│   │   ├── AuthServiceTest.java
│   │   ├── ChecklistServiceTest.java
│   │   └── EmpilhadeiraServiceTest.java
│   └── util/
│       └── JwtUtilTest.java
├── integration/
│   ├── controller/
│   │   ├── AuthControllerIntegrationTest.java
│   │   └── ChecklistControllerIntegrationTest.java
│   └── repository/
│       └── ChecklistRepositoryIntegrationTest.java
└── e2e/
    └── ChecklistFlowE2ETest.java
```

### Testes Unitários

#### Exemplo: ChecklistServiceTest

```java
@ExtendWith(MockitoExtension.class)
class ChecklistServiceTest {
    
    @Mock
    private ChecklistRepository checklistRepository;
    
    @Mock
    private UsuarioRepository usuarioRepository;
    
    @Mock
    private EmpilhadeiraRepository empilhadeiraRepository;
    
    @InjectMocks
    private ChecklistService checklistService;
    
    @Test
    void deveCriarChecklistComSucesso() {
        // Given
        ChecklistRequest request = criarRequestValido();
        Usuario operador = criarOperadorValido();
        Empilhadeira empilhadeira = criarEmpilhadeiraValida();
        
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(operador));
        when(empilhadeiraRepository.findById(anyLong())).thenReturn(Optional.of(empilhadeira));
        when(checklistRepository.save(any())).thenReturn(criarChecklistMock());
        
        // When
        ChecklistResponse response = checklistService.criar(request);
        
        // Then
        assertNotNull(response);
        assertEquals(ResultadoChecklist.APROVADO, response.getResultado());
        verify(checklistRepository).save(any());
    }
    
    @Test
    void deveBloquearEmpilhadeiraQuandoChecklistReprovado() {
        // Given
        ChecklistRequest request = criarRequestComItemImpeditivo();
        // ...
        
        // When
        checklistService.criar(request);
        
        // Then
        verify(empilhadeiraRepository).save(argThat(emp -> 
            emp.getBloqueada() == true
        ));
    }
}
```

### Testes de Integração

#### Exemplo: ChecklistControllerIntegrationTest

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ChecklistControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private String token;
    
    @BeforeEach
    void setUp() {
        token = obterTokenDeAutenticacao();
    }
    
    @Test
    void deveCriarChecklistComSucesso() throws Exception {
        ChecklistRequest request = criarRequestValido();
        
        mockMvc.perform(post("/api/checklists")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.resultado").value("APROVADO"));
    }
    
    @Test
    void deveRetornar403SemAutenticacao() throws Exception {
        mockMvc.perform(post("/api/checklists")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isForbidden());
    }
}
```

### Executar Testes

```bash
# Todos os testes
mvn test

# Testes de uma classe específica
mvn test -Dtest=ChecklistServiceTest

# Testes com cobertura
mvn clean test jacoco:report

# Ver relatório de cobertura
open target/site/jacoco/index.html
```

### Cobertura de Testes (Meta)

- **Cobertura de código:** >= 80%
- **Testes unitários:** >= 90%
- **Testes de integração:** >= 70%
- **Testes E2E:** Fluxos críticos

---

## 🚀 Deploy

### Ambientes

#### Development
```bash
mvn spring-boot:run
```

#### Staging
```bash
mvn clean package -Pstaging
java -jar target/checklist-backend-1.0.0.jar --spring.profiles.active=staging
```

#### Production
```bash
mvn clean package -Pprod
java -jar target/checklist-backend-1.0.0.jar --spring.profiles.active=prod
```

### Docker

#### Dockerfile

```dockerfile
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/checklist-backend-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Build e Run

```bash
# Build da imagem
docker build -t checklist-backend:1.0.0 .

# Executar container
docker run -d \
  --name checklist-backend \
  -p 8080:8080 \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=admin \
  -e JWT_SECRET=sua-chave-secreta \
  checklist-backend:1.0.0
```

#### Docker Compose

```yaml
version: '3.8'

services:
  db:
    image: postgres:16
    container_name: checklist-db
    environment:
      POSTGRES_DB: checklist_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: admin
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - checklist-network

  backend:
    build: .
    container_name: checklist-backend
    environment:
      DB_USERNAME: postgres
      DB_PASSWORD: admin
      JWT_SECRET: sua-chave-secreta
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/checklist_db
    ports:
      - "8080:8080"
    depends_on:
      - db
    networks:
      - checklist-network

  frontend:
    image: nginx:alpine
    container_name: checklist-frontend
    volumes:
      - ./dist/checklist-frontend:/usr/share/nginx/html
    ports:
      - "80:80"
    depends_on:
      - backend
    networks:
      - checklist-network

volumes:
  postgres_data:

networks:
  checklist-network:
    driver: bridge
```

**Executar:**
```bash
docker-compose up -d
```

### Cloud Deploy

#### Heroku

```bash
# Login
heroku login

# Criar app
heroku create checklist-backend

# Adicionar PostgreSQL
heroku addons:create heroku-postgresql:hobby-dev

# Configurar variáveis
heroku config:set JWT_SECRET=sua-chave-secreta

# Deploy
git push heroku main

# Ver logs
heroku logs --tail
```

#### AWS (Elastic Beanstalk)

```bash
# Instalar EB CLI
pip install awsebcli

# Inicializar
eb init -p java-17 checklist-backend

# Criar ambiente
eb create checklist-prod

# Deploy
eb deploy

# Ver logs
eb logs
```

#### Google Cloud (App Engine)

**app.yaml:**
```yaml
runtime: java21
instance_class: F2

env_variables:
  DB_USERNAME: "postgres"
  JWT_SECRET: "sua-chave-secreta"

automatic_scaling:
  min_instances: 1
  max_instances: 10
```

**Deploy:**
```bash
gcloud app deploy
```

### Monitoramento

#### Spring Boot Actuator

**Adicionar dependência:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**Configurar:**
```properties
management.endpoints.web.exposure.include=health,info,metrics,env
management.endpoint.health.show-details=always
```

**Endpoints:**
- `/actuator/health` - Status da aplicação
- `/actuator/metrics` - Métricas
- `/actuator/info` - Informações da aplicação

#### Logs

```properties
# application.properties
logging.file.name=logs/checklist.log
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

---

## 🔧 Troubleshooting

### Problemas Comuns

#### 1. Erro de Conexão com Banco

**Sintoma:**
```
Unable to connect to database: Connection refused
```

**Solução:**
```bash
# Verificar se PostgreSQL está rodando
sudo systemctl status postgresql

# Iniciar PostgreSQL
sudo systemctl start postgresql

# Verificar conectividade
psql -U postgres -d checklist_db -c "SELECT 1;"
```

#### 2. Erro de Compilação

**Sintoma:**
```
package lombok does not exist
```

**Solução:**
```bash
# Limpar e recompilar
mvn clean install -U

# Forçar download de dependências
mvn dependency:purge-local-repository
```

#### 3. Erro 403 ao Salvar Checklist

**Sintoma:**
```
POST /api/checklists 403 (Forbidden)
```

**Solução:**
- Verificar se token JWT está sendo enviado
- Verificar se token não expirou
- Verificar configuração de CORS
- Ver arquivo `FIX_403_CHECKLIST.md`

#### 4. Token JWT Inválido

**Sintoma:**
```
JWT signature does not match locally computed signature
```

**Solução:**
- Verificar se a chave JWT é a mesma no servidor
- Regenerar token fazendo novo login
- Verificar configuração `jwt.secret`

#### 5. Erro ao Salvar Checklist (NullPointerException)

**Sintoma:**
```
Cannot invoke "List.add()" because "this.itens" is null
```

**Solução:**
- Verificar arquivo `FIX_SALVAR_CHECKLIST.md`
- Atualizar para versão corrigida do backend

### Debug

#### Habilitar Logs Detalhados

```properties
# application.properties
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
logging.level.com.deicmar.checklist=TRACE
```

#### Debugar no IntelliJ IDEA

1. Colocar breakpoint no código
2. Executar em modo debug: `Run → Debug 'ChecklistApplication'`
3. Fazer requisição que aciona o breakpoint
4. Inspecionar variáveis e fluxo

#### Debugar Remotamente

```bash
# Executar com debug remoto
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar target/checklist-backend-1.0.0.jar
```

**No IntelliJ:**
1. `Run → Edit Configurations`
2. `+ → Remote JVM Debug`
3. Host: `localhost`, Port: `5005`
4. `Run → Debug 'Remote'`

---

## 🤝 Contribuindo

### Processo de Contribuição

1. **Fork** o repositório
2. **Clone** seu fork
   ```bash
   git clone https://github.com/seu-usuario/checklist-backend.git
   ```
3. **Crie** uma branch para sua feature
   ```bash
   git checkout -b feature/minha-feature
   ```
4. **Faça** suas alterações
5. **Commit** suas mudanças
   ```bash
   git commit -m "feat: adiciona nova funcionalidade"
   ```
6. **Push** para o branch
   ```bash
   git push origin feature/minha-feature
   ```
7. **Abra** um Pull Request

### Convenções de Código

#### Java
- Seguir [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Usar Lombok para reduzir boilerplate
- Documentar métodos públicos com Javadoc
- Máximo 120 caracteres por linha

#### Git Commits
Seguir [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

**Tipos:**
- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Documentação
- `style`: Formatação
- `refactor`: Refatoração
- `test`: Testes
- `chore`: Tarefas de build


### Recursos Externos

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/index.html)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [JWT.io](https://jwt.io/) - JWT Debugger
- [Angular Documentation](https://angular.dev/)

---
## 👥 Equipe

**Desenvolvedor:** Marcelo florindo dos santos   
**Empresa:** MarceloDev013  
**Contato:** marcelorpc13@gmail.com 
