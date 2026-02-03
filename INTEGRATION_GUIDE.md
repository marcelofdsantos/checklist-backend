# 🔗 Guia de Integração Frontend-Backend

## Visão Geral

Este documento descreve como integrar completamente o frontend Angular com o backend Spring Boot.

## Arquitetura da Aplicação

```
┌─────────────────────────────────────────────────────────────┐
│                     FRONTEND (Angular)                      │
│                   http://localhost:4200                     │
│                                                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │   Login      │  │  Checklist   │  │   Guards     │    │
│  │  Component   │  │  Component   │  │              │    │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘    │
│         │                 │                  │             │
│  ┌──────▼─────────────────▼──────────────────▼───────┐    │
│  │              Services (HTTP Client)              │    │
│  │  • AuthService                                   │    │
│  │  • ChecklistService                              │    │
│  │  • EmpilhadeiraService                           │    │
│  └─────────────────────┬────────────────────────────┘    │
└────────────────────────┼─────────────────────────────────┘
                         │ HTTP/REST + JWT
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                     BACKEND (Spring Boot)                   │
│                   http://localhost:8080/api                 │
│                                                             │
│  ┌──────────────────────────────────────────────────┐     │
│  │          Security Filter (JWT)                   │     │
│  └────────────┬─────────────────────────────────────┘     │
│               │                                             │
│  ┌────────────▼─────────────────────────────────────┐     │
│  │              Controllers                         │     │
│  │  • AuthController                                │     │
│  │  • ChecklistController                           │     │
│  │  • EmpilhadeiraController                        │     │
│  │  • UsuarioController                             │     │
│  └────────────┬─────────────────────────────────────┘     │
│               │                                             │
│  ┌────────────▼─────────────────────────────────────┐     │
│  │               Services                           │     │
│  └────────────┬─────────────────────────────────────┘     │
│               │                                             │
│  ┌────────────▼─────────────────────────────────────┐     │
│  │             Repositories (JPA)                   │     │
│  └────────────┬─────────────────────────────────────┘     │
└───────────────┼──────────────────────────────────────────┘
                │
                ▼
┌─────────────────────────────────────────────────────────────┐
│                    PostgreSQL Database                      │
│                       checklist_db                          │
└─────────────────────────────────────────────────────────────┘
```

## Configuração do CORS

O backend já está configurado para aceitar requisições do frontend Angular:

```java
// SecurityConfig.java
configuration.setAllowedOrigins(Arrays.asList(
    "http://localhost:4200",  // Angular dev server
    "http://localhost:8080"   // Backend
));
```

Se você mudar a porta do frontend, atualize esta configuração.

## Fluxo de Autenticação

### 1. Login

**Frontend envia:**
```typescript
// auth.service.ts
login(request: LoginRequest): Observable<LoginResponse> {
  return this.http.post<LoginResponse>(
    `${this.API_URL}/auth/login`, 
    request
  );
}
```

**Backend responde:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "usuarioId": 1,
  "re": "OPR001",
  "nomeCompleto": "João Silva",
  "perfil": "OPERADOR"
}
```

### 2. Armazenamento do Token

```typescript
// auth.service.ts
private saveAuthData(response: LoginResponse): void {
  localStorage.setItem(this.TOKEN_KEY, response.token);
  localStorage.setItem(this.USER_KEY, JSON.stringify(response));
}
```

### 3. Interceptor JWT

Todas as requisições subsequentes incluem o token automaticamente:

```typescript
// jwt.interceptor.ts
intercept(req: HttpRequest<any>, next: HttpHandler) {
  const token = this.authService.getToken();
  
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }
  
  return next.handle(req);
}
```

## Endpoints Mapeados

### AuthService → AuthController

| Frontend Method | HTTP | Backend Endpoint |
|----------------|------|------------------|
| `login()` | POST | `/api/auth/login` |

### ChecklistService → ChecklistController

| Frontend Method | HTTP | Backend Endpoint |
|----------------|------|------------------|
| `criar()` | POST | `/api/checklists` |
| `listarTodos()` | GET | `/api/checklists` |
| `buscarPorId()` | GET | `/api/checklists/{id}` |
| `listarPorEmpilhadeira()` | GET | `/api/checklists/empilhadeira/{id}` |
| `listarPorOperador()` | GET | `/api/checklists/operador/{id}` |
| `listarPorData()` | GET | `/api/checklists/data/{data}` |
| `listarPorPeriodo()` | GET | `/api/checklists/periodo?dataInicio=...&dataFim=...` |

### EmpilhadeiraService → EmpilhadeiraController

| Frontend Method | HTTP | Backend Endpoint |
|----------------|------|------------------|
| `criar()` | POST | `/api/empilhadeiras` |
| `listarTodas()` | GET | `/api/empilhadeiras` |
| `listarAtivas()` | GET | `/api/empilhadeiras/ativas` |
| `listarDisponiveis()` | GET | `/api/empilhadeiras/disponiveis` |
| `listarBloqueadas()` | GET | `/api/empilhadeiras/bloqueadas` |
| `buscarPorId()` | GET | `/api/empilhadeiras/{id}` |
| `bloquear()` | PATCH | `/api/empilhadeiras/{id}/bloquear` |
| `desbloquear()` | PATCH | `/api/empilhadeiras/{id}/desbloquear` |
| `inativar()` | DELETE | `/api/empilhadeiras/{id}` |

## Tratamento de Erros

### Frontend

```typescript
// Exemplo em checklist.component.ts
this.checklistService.criar(request).subscribe({
  next: (response) => {
    this.snackBar.open('Checklist criado com sucesso!', 'OK', {
      duration: 3000
    });
  },
  error: (error) => {
    const message = error.error?.message || 'Erro ao criar checklist';
    this.snackBar.open(message, 'Fechar', {
      duration: 5000
    });
  }
});
```

### Backend

Retorna erros padronizados:

```json
{
  "timestamp": "2025-02-01T10:30:00",
  "status": 400,
  "error": "Erro de validação",
  "message": "Dados inválidos fornecidos",
  "details": {
    "re": "RE é obrigatório",
    "senha": "Senha deve ter no mínimo 6 caracteres"
  }
}
```

## Sincronização de Enums

Os enums devem estar sincronizados entre frontend e backend:

### Backend (Java)
```java
public enum Perfil {
    OPERADOR, SUPERVISOR, ADMIN
}
```

### Frontend (TypeScript)
```typescript
export enum Perfil {
  OPERADOR = 'OPERADOR',
  SUPERVISOR = 'SUPERVISOR',
  ADMIN = 'ADMIN'
}
```

## Guards de Rota

### Frontend

```typescript
// auth.guard.ts
canActivate(): boolean {
  if (!this.authService.isAuthenticated()) {
    this.router.navigate(['/login']);
    return false;
  }
  return true;
}
```

### Backend

```java
// ChecklistController.java
@PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'OPERADOR')")
public ResponseEntity<ChecklistResponse> criar(...) {
    // ...
}
```

## Passo a Passo de Integração

### 1. Iniciar o Backend

```bash
cd checklist-backend
./run.sh
```

Aguarde até ver:
```
Started ChecklistApplication in X.XXX seconds
```

### 2. Iniciar o Frontend

```bash
cd login
npm install
npm start
```

Aguarde até ver:
```
✔ Compiled successfully.
```

### 3. Testar a Integração

1. Acesse: `http://localhost:4200`
2. Faça login com:
   - RE: `ADMIN`
   - Senha: `admin123`
3. Navegue pelas funcionalidades

## Cenários de Teste

### Teste 1: Login e Logout

1. ✅ Login com credenciais corretas
2. ✅ Verificar token no localStorage
3. ✅ Acessar página protegida
4. ✅ Fazer logout
5. ✅ Verificar redirecionamento para login

### Teste 2: Criar Checklist

1. ✅ Login como OPERADOR
2. ✅ Selecionar empilhadeira disponível
3. ✅ Preencher formulário de checklist
4. ✅ Adicionar itens (OK e NÃO_CONFORME)
5. ✅ Enviar checklist
6. ✅ Verificar resultado (APROVADO/REPROVADO)
7. ✅ Verificar bloqueio automático se reprovado

### Teste 3: Gerenciar Empilhadeiras

1. ✅ Login como SUPERVISOR
2. ✅ Criar nova empilhadeira
3. ✅ Bloquear empilhadeira
4. ✅ Verificar que não aparece em "disponíveis"
5. ✅ Desbloquear empilhadeira
6. ✅ Verificar que volta a aparecer

## Troubleshooting

### Erro: "CORS policy"

**Problema:** Backend não aceita requisições do frontend

**Solução:**
1. Verifique o `SecurityConfig.java`
2. Certifique-se que a porta do frontend está nas origens permitidas
3. Limpe o cache do navegador

### Erro: "401 Unauthorized"

**Problema:** Token inválido ou expirado

**Solução:**
1. Faça logout e login novamente
2. Verifique se o token está sendo enviado no header
3. Verifique a validade do token (24h por padrão)

### Erro: "Connection refused"

**Problema:** Backend não está rodando

**Solução:**
1. Verifique se o backend está rodando: `curl http://localhost:8080/api`
2. Verifique os logs do backend
3. Reinicie o backend

### Erro: "Cannot read properties of undefined"

**Problema:** Resposta do backend diferente do esperado

**Solução:**
1. Verifique os tipos no TypeScript
2. Compare com os DTOs do backend
3. Adicione logs no service do Angular

## Monitoramento

### Logs do Backend

```bash
# Ver logs em tempo real
tail -f logs/application.log

# Filtrar por nível
grep "ERROR" logs/application.log
```

### DevTools do Angular

```typescript
// Habilitar debug mode
import { enableDebugTools } from '@angular/platform-browser';

// No main.ts
platformBrowserDynamic()
  .bootstrapModule(AppModule)
  .then(ref => enableDebugTools(ref.components[0]));
```

### Network Tab (Chrome DevTools)

1. F12 → Network
2. Filtrar por "XHR"
3. Verificar:
   - Request headers (Authorization)
   - Response status
   - Response body

## Performance

### Otimizações no Frontend

```typescript
// Usar trackBy em *ngFor
trackByFn(index: number, item: any): number {
  return item.id;
}
```

```html
<tr *ngFor="let checklist of checklists; trackBy: trackByFn">
```

### Otimizações no Backend

```java
// Usar FetchType.LAZY em relacionamentos
@ManyToOne(fetch = FetchType.LAZY)
private Usuario operador;

// Usar projeções quando não precisar de todos os dados
@Query("SELECT new com.deicmar...DTO(...) FROM Entity e")
```

## Segurança

### Checklist de Segurança

- ✅ Senhas encriptadas com BCrypt
- ✅ Tokens JWT com expiração
- ✅ HTTPS em produção
- ✅ Validação de entrada no backend
- ✅ SQL parametrizado (JPA)
- ✅ Headers de segurança (CORS, CSP)
- ✅ Rate limiting (implementar em produção)

## Deploy em Produção

### Backend

1. Configurar `application-prod.properties`
2. Usar variáveis de ambiente para credenciais
3. Gerar JAR: `mvn clean package -Pprod`
4. Executar: `java -jar -Dspring.profiles.active=prod target/checklist-backend.jar`

### Frontend

1. Build de produção: `ng build --configuration production`
2. Servir com Nginx/Apache
3. Configurar proxy reverso para o backend

## Suporte

Para mais informações:
- README principal do backend
- README do frontend
- Documentação do Spring Boot: https://spring.io/projects/spring-boot
- Documentação do Angular: https://angular.io/docs

---

**Desenvolvido para Deicmar Ltda**
