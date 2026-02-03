# 📊 Documentação do Banco de Dados

## Diagrama de Entidades (ER)

```
┌──────────────────────┐
│      USUARIOS        │
├──────────────────────┤
│ PK id (BIGSERIAL)    │
│    re (VARCHAR 20)   │◄──┐
│    nome_completo     │   │
│    senha             │   │
│    perfil (ENUM)     │   │
│    ativo (BOOLEAN)   │   │
│    criado_em         │   │
│    atualizado_em     │   │
└──────────────────────┘   │
                           │
                           │
┌──────────────────────┐   │
│   EMPILHADEIRAS      │   │
├──────────────────────┤   │
│ PK id (BIGSERIAL)    │   │
│    modelo            │◄──┼──┐
│    tipo              │   │  │
│    capacidade        │   │  │
│    bloqueada         │   │  │
│    motivo_bloqueio   │   │  │
│    ativa             │   │  │
│    criado_em         │   │  │
│    atualizado_em     │   │  │
└──────────────────────┘   │  │
                           │  │
                           │  │
┌──────────────────────┐   │  │
│     CHECKLISTS       │   │  │
├──────────────────────┤   │  │
│ PK id (BIGSERIAL)    │   │  │
│ FK operador_id       │───┘  │
│ FK empilhadeira_id   │──────┘
│    data (DATE)       │
│    hora_vistoria     │
│    turno (ENUM)      │
│    horimetro_inicial │
│    horimetro_final   │
│    resultado (ENUM)  │
│    observacao_geral  │
│    criado_em         │
└──────┬───────────────┘
       │
       │ 1:N
       │
┌──────▼───────────────┐
│  ITENS_CHECKLIST     │
├──────────────────────┤
│ PK id (BIGSERIAL)    │
│ FK checklist_id      │
│    descricao         │
│    tipo (ENUM)       │
│    status (ENUM)     │
│    observacao        │
└──────────────────────┘
```

## Tabelas

### 1. USUARIOS

Armazena informações dos usuários do sistema.

| Coluna | Tipo | Restrições | Descrição |
|--------|------|------------|-----------|
| id | BIGSERIAL | PK, NOT NULL | Identificador único |
| re | VARCHAR(20) | UNIQUE, NOT NULL | Registro de Empregado |
| nome_completo | VARCHAR(100) | NOT NULL | Nome completo do usuário |
| senha | VARCHAR(255) | NOT NULL | Senha encriptada (BCrypt) |
| perfil | VARCHAR(20) | NOT NULL | OPERADOR, SUPERVISOR, ADMIN |
| ativo | BOOLEAN | NOT NULL, DEFAULT true | Indica se está ativo |
| criado_em | TIMESTAMP | NOT NULL | Data de criação |
| atualizado_em | TIMESTAMP | | Data da última atualização |

**Índices:**
- PK em `id`
- UNIQUE em `re`
- INDEX em `perfil`
- INDEX em `ativo`

**Exemplo de registro:**
```sql
INSERT INTO usuarios (re, nome_completo, senha, perfil, ativo, criado_em) 
VALUES ('OPR001', 'João Silva', '$2a$10$...', 'OPERADOR', true, NOW());
```

### 2. EMPILHADEIRAS

Armazena informações das empilhadeiras.

| Coluna | Tipo | Restrições | Descrição |
|--------|------|------------|-----------|
| id | BIGSERIAL | PK, NOT NULL | Identificador único |
| modelo | VARCHAR(50) | NOT NULL | Modelo da empilhadeira |
| tipo | VARCHAR(50) | NOT NULL | Elétrica, GLP, Diesel |
| capacidade | INTEGER | NOT NULL | Capacidade em kg |
| bloqueada | BOOLEAN | NOT NULL, DEFAULT false | Indica se está bloqueada |
| motivo_bloqueio | VARCHAR(500) | | Motivo do bloqueio |
| ativa | BOOLEAN | NOT NULL, DEFAULT true | Indica se está ativa |
| criado_em | TIMESTAMP | NOT NULL | Data de criação |
| atualizado_em | TIMESTAMP | | Data da última atualização |

**Índices:**
- PK em `id`
- INDEX em `bloqueada`
- INDEX em `ativa`
- INDEX em `modelo`

**Exemplo de registro:**
```sql
INSERT INTO empilhadeiras (modelo, tipo, capacidade, bloqueada, ativa, criado_em) 
VALUES ('YALE GP030', 'Elétrica', 3000, false, true, NOW());
```

### 3. CHECKLISTS

Armazena os checklists realizados.

| Coluna | Tipo | Restrições | Descrição |
|--------|------|------------|-----------|
| id | BIGSERIAL | PK, NOT NULL | Identificador único |
| operador_id | BIGINT | FK, NOT NULL | Referência ao operador |
| empilhadeira_id | BIGINT | FK, NOT NULL | Referência à empilhadeira |
| data | DATE | NOT NULL | Data do checklist |
| hora_vistoria | TIME | NOT NULL | Hora da vistoria |
| turno | VARCHAR(20) | NOT NULL | A, B, C |
| horimetro_inicial | INTEGER | NOT NULL | Leitura inicial |
| horimetro_final | INTEGER | | Leitura final |
| resultado | VARCHAR(20) | NOT NULL | APROVADO, REPROVADO |
| observacao_geral | VARCHAR(1000) | | Observações gerais |
| criado_em | TIMESTAMP | NOT NULL | Data de criação |

**Índices:**
- PK em `id`
- FK em `operador_id` → usuarios(id)
- FK em `empilhadeira_id` → empilhadeiras(id)
- INDEX em `data`
- INDEX em `resultado`
- INDEX em `(empilhadeira_id, data)`

**Exemplo de registro:**
```sql
INSERT INTO checklists (operador_id, empilhadeira_id, data, hora_vistoria, 
                        turno, horimetro_inicial, resultado, criado_em) 
VALUES (1, 1, '2025-02-01', '08:30:00', 'A', 15000, 'APROVADO', NOW());
```

### 4. ITENS_CHECKLIST

Armazena os itens individuais de cada checklist.

| Coluna | Tipo | Restrições | Descrição |
|--------|------|------------|-----------|
| id | BIGSERIAL | PK, NOT NULL | Identificador único |
| checklist_id | BIGINT | FK, NOT NULL | Referência ao checklist |
| descricao | VARCHAR(200) | NOT NULL | Descrição do item |
| tipo | VARCHAR(20) | NOT NULL | CONFORME, IMPEDITIVO |
| status | VARCHAR(20) | NOT NULL | OK, NAO_CONFORME |
| observacao | VARCHAR(500) | | Observação do item |

**Índices:**
- PK em `id`
- FK em `checklist_id` → checklists(id) ON DELETE CASCADE
- INDEX em `tipo`
- INDEX em `status`

**Exemplo de registro:**
```sql
INSERT INTO itens_checklist (checklist_id, descricao, tipo, status, observacao) 
VALUES (1, 'Verificação de freios', 'IMPEDITIVO', 'OK', 'Freios em perfeito estado');
```

## Enumerações (ENUMS)

### Perfil
```sql
'OPERADOR'   -- Operador de empilhadeira
'SUPERVISOR' -- Supervisor de operações
'ADMIN'      -- Administrador do sistema
```

### Turno
```sql
'A' -- Turno A (Manhã)
'B' -- Turno B (Tarde)
'C' -- Turno C (Noite)
```

### TipoItem
```sql
'CONFORME'   -- Item de verificação normal
'IMPEDITIVO' -- Item crítico (reprova se não conforme)
```

### StatusItem
```sql
'OK'            -- Item conforme
'NAO_CONFORME'  -- Item não conforme
```

### ResultadoChecklist
```sql
'APROVADO'  -- Todos itens impeditivos estão OK
'REPROVADO' -- Pelo menos um item impeditivo não conforme
```

## Relacionamentos

### 1. Usuario (1) → (N) Checklist
- Um usuário pode realizar múltiplos checklists
- Um checklist pertence a um único operador
- CASCADE: Não deleta (protect)

### 2. Empilhadeira (1) → (N) Checklist
- Uma empilhadeira pode ter múltiplos checklists
- Um checklist é feito em uma única empilhadeira
- CASCADE: Não deleta (protect)

### 3. Checklist (1) → (N) ItemChecklist
- Um checklist tem múltiplos itens
- Um item pertence a um único checklist
- CASCADE: DELETE (quando deleta checklist, deleta itens)

## Queries Úteis

### Estatísticas Gerais

```sql
-- Total de checklists por resultado
SELECT resultado, COUNT(*) as total
FROM checklists
GROUP BY resultado;

-- Empilhadeiras mais usadas
SELECT 
    e.modelo,
    e.tipo,
    COUNT(c.id) as total_checklists,
    COUNT(CASE WHEN c.resultado = 'APROVADO' THEN 1 END) as aprovados,
    COUNT(CASE WHEN c.resultado = 'REPROVADO' THEN 1 END) as reprovados
FROM empilhadeiras e
LEFT JOIN checklists c ON e.id = c.empilhadeira_id
GROUP BY e.id, e.modelo, e.tipo
ORDER BY total_checklists DESC;

-- Operadores mais ativos
SELECT 
    u.nome_completo,
    u.re,
    COUNT(c.id) as total_checklists
FROM usuarios u
LEFT JOIN checklists c ON u.id = c.operador_id
WHERE u.perfil = 'OPERADOR'
GROUP BY u.id, u.nome_completo, u.re
ORDER BY total_checklists DESC;
```

### Relatórios

```sql
-- Checklists do mês atual
SELECT 
    c.data,
    c.turno,
    u.nome_completo as operador,
    e.modelo as empilhadeira,
    c.resultado
FROM checklists c
JOIN usuarios u ON c.operador_id = u.id
JOIN empilhadeiras e ON c.empilhadeira_id = e.id
WHERE c.data >= DATE_TRUNC('month', CURRENT_DATE)
ORDER BY c.data DESC, c.hora_vistoria DESC;

-- Empilhadeiras bloqueadas
SELECT 
    e.modelo,
    e.tipo,
    e.motivo_bloqueio,
    c.data as data_ultimo_checklist,
    c.resultado
FROM empilhadeiras e
LEFT JOIN checklists c ON e.id = c.empilhadeira_id
WHERE e.bloqueada = true
ORDER BY c.data DESC;

-- Itens não conformes mais frequentes
SELECT 
    ic.descricao,
    ic.tipo,
    COUNT(*) as total_ocorrencias
FROM itens_checklist ic
WHERE ic.status = 'NAO_CONFORME'
GROUP BY ic.descricao, ic.tipo
ORDER BY total_ocorrencias DESC
LIMIT 10;
```

### Auditoria

```sql
-- Histórico de uma empilhadeira
SELECT 
    c.data,
    c.hora_vistoria,
    c.turno,
    u.nome_completo as operador,
    c.resultado,
    c.horimetro_inicial,
    c.horimetro_final
FROM checklists c
JOIN usuarios u ON c.operador_id = u.id
WHERE c.empilhadeira_id = 1
ORDER BY c.data DESC, c.hora_vistoria DESC;

-- Checklists reprovados com detalhes
SELECT 
    c.id,
    c.data,
    e.modelo as empilhadeira,
    u.nome_completo as operador,
    ic.descricao as item_problema,
    ic.observacao
FROM checklists c
JOIN empilhadeiras e ON c.empilhadeira_id = e.id
JOIN usuarios u ON c.operador_id = u.id
JOIN itens_checklist ic ON c.id = ic.checklist_id
WHERE c.resultado = 'REPROVADO'
  AND ic.tipo = 'IMPEDITIVO'
  AND ic.status = 'NAO_CONFORME'
ORDER BY c.data DESC;
```

## Backup e Restore

### Backup Completo

```bash
# Backup do banco inteiro
pg_dump -U postgres -d checklist_db -f backup_$(date +%Y%m%d).sql

# Backup compactado
pg_dump -U postgres -d checklist_db | gzip > backup_$(date +%Y%m%d).sql.gz
```

### Restore

```bash
# Restore de backup SQL
psql -U postgres -d checklist_db < backup_20250201.sql

# Restore de backup compactado
gunzip -c backup_20250201.sql.gz | psql -U postgres -d checklist_db
```

### Backup Apenas de Dados

```bash
# Apenas dados (sem estrutura)
pg_dump -U postgres -d checklist_db --data-only -f data_backup.sql

# Apenas estrutura (sem dados)
pg_dump -U postgres -d checklist_db --schema-only -f schema_backup.sql
```

## Manutenção

### Otimização

```sql
-- Analisar tabelas para otimizar queries
ANALYZE usuarios;
ANALYZE empilhadeiras;
ANALYZE checklists;
ANALYZE itens_checklist;

-- Vacuum para liberar espaço
VACUUM ANALYZE;

-- Reindexar se necessário
REINDEX TABLE checklists;
```

### Limpeza de Dados Antigos

```sql
-- Arquivar checklists antigos (>1 ano)
-- ATENÇÃO: Execute com cuidado!

-- 1. Criar tabela de arquivo
CREATE TABLE checklists_arquivo AS 
SELECT * FROM checklists 
WHERE data < CURRENT_DATE - INTERVAL '1 year';

-- 2. Verificar quantidade
SELECT COUNT(*) FROM checklists_arquivo;

-- 3. Deletar da tabela principal
DELETE FROM checklists 
WHERE data < CURRENT_DATE - INTERVAL '1 year';
```

## Segurança

### Permissões

```sql
-- Criar usuário de aplicação
CREATE USER checklist_app WITH PASSWORD 'senha_segura';

-- Conceder permissões
GRANT CONNECT ON DATABASE checklist_db TO checklist_app;
GRANT USAGE ON SCHEMA public TO checklist_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO checklist_app;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO checklist_app;
```

### SSL/TLS

Para produção, habilite SSL no PostgreSQL:

```
# postgresql.conf
ssl = on
ssl_cert_file = 'server.crt'
ssl_key_file = 'server.key'
```

## Monitoramento

### Queries Lentas

```sql
-- Habilitar log de queries lentas
-- postgresql.conf:
-- log_min_duration_statement = 1000  # em ms

-- Ver queries ativas
SELECT 
    pid,
    usename,
    application_name,
    client_addr,
    state,
    query,
    query_start
FROM pg_stat_activity
WHERE state != 'idle'
ORDER BY query_start;
```

### Tamanho das Tabelas

```sql
SELECT 
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
```

---

**Documentação do Banco de Dados - Sistema de Checklist de Empilhadeiras**
