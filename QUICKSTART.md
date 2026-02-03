# 🚀 Guia de Início Rápido

## Requisitos Mínimos

✅ Java 21 instalado
✅ Maven 3.9.12 instalado  
✅ PostgreSQL rodando
✅ Banco de dados `checklist_db` criado

## Passo a Passo

### 1. Verificar Instalações

```bash
java -version
# Deve mostrar: openjdk version "21.0.8" ou superior

mvn -version
# Deve mostrar: Apache Maven 3.9.12 ou superior

psql --version
# Deve mostrar: psql (PostgreSQL) 16.x ou superior
```

### 2. Criar o Banco de Dados

```bash
# Conectar ao PostgreSQL
psql -U postgres

# Criar o banco
CREATE DATABASE checklist_db;

# Sair
\q
```

### 3. Configurar Credenciais (Opcional)

Se suas credenciais forem diferentes de postgres/admin, edite:
`src/main/resources/application.properties`

```properties
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 4. Compilar e Executar

```bash
# Na pasta do projeto
cd checklist-backend

# Limpar e compilar
mvn clean install

# Executar
mvn spring-boot:run
```

### 5. Verificar se está Funcionando

Abra o navegador e acesse:
```
http://localhost:8080/api
```

Se ver uma página de erro 401/403, está funcionando! (É esperado, pois precisa de autenticação)

## 🔐 Primeiro Login

Um usuário ADMIN é criado automaticamente na primeira execução:

```
RE: ADMIN
Senha: admin123
```

### Teste o Login

Use uma ferramenta como Postman, Insomnia ou curl:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "re": "ADMIN",
    "senha": "admin123"
  }'
```

Você receberá um token JWT. Use-o nas próximas requisições:

```bash
curl -X GET http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

## 📱 Integração com Frontend Angular

O frontend já está configurado para conectar em `http://localhost:8080/api`.

1. Certifique-se de que o backend está rodando
2. Inicie o frontend Angular:
   ```bash
   cd login
   npm install
   npm start
   ```
3. Acesse: `http://localhost:4200`

## 🐛 Problemas Comuns

### Erro: "Connection refused"
- Verifique se o PostgreSQL está rodando
- Verifique as credenciais no application.properties

### Erro: "Port 8080 already in use"
- Mude a porta no application.properties:
  ```properties
  server.port=8081
  ```
- Ou mate o processo na porta 8080:
  ```bash
  # Linux/Mac
  lsof -ti:8080 | xargs kill -9
  
  # Windows
  netstat -ano | findstr :8080
  taskkill /PID <PID> /F
  ```

### Erro de compilação Maven
- Limpe o cache do Maven:
  ```bash
  mvn clean
  rm -rf ~/.m2/repository
  mvn install
  ```

### Erro de autenticação JWT
- Verifique se o token não expirou (validade: 24h)
- Certifique-se de incluir "Bearer " antes do token
- Formato correto: `Authorization: Bearer eyJhbGc...`

## 📊 Populando Dados de Teste

Para popular o banco com dados de teste, execute:

```bash
psql -U postgres -d checklist_db -f src/main/resources/init.sql
```

Isso criará:
- 5 usuários de teste (senha: senha123)
- 6 empilhadeiras de teste

## 🔧 Comandos Úteis

```bash
# Compilar sem executar testes
mvn clean install -DskipTests

# Executar em modo debug
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# Gerar JAR executável
mvn clean package

# Executar o JAR
java -jar target/checklist-backend-1.0.0.jar

# Ver logs em tempo real
tail -f logs/application.log
```

## 📝 Próximos Passos

1. ✅ Login como ADMIN
2. ✅ Criar usuários SUPERVISOR e OPERADOR
3. ✅ Cadastrar empilhadeiras
4. ✅ Criar seu primeiro checklist
5. ✅ Explorar os relatórios

## 🆘 Suporte

Se precisar de ajuda:
1. Consulte o README.md completo
2. Verifique os logs em: `logs/application.log`
3. Entre em contato com a equipe de desenvolvimento

---

**Boa sorte! 🎉**
