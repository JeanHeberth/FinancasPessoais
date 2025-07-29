# 💰 Financas Pessoais

Projeto para controle de gastos pessoais, com backend em **Spring Boot** e banco de dados **MongoDB**, incluindo autenticação via **JWT**, testes com **JUnit 5** e integração contínua com **GitHub Actions**.

---

## 📦 Tecnologias utilizadas

- Java 21
- Spring Boot 3.5.x
    - Spring Web
    - Spring Security
    - Spring Data MongoDB
    - Spring Validation
- JWT (via `jjwt`)
- Lombok
- MapStruct
- MongoDB (local ou em memória via Flapdoodle para testes)
- JUnit 5 + Mockito
- GitHub Actions para CI

---

## 📁 Estrutura do projeto

```
src
├── main
│   ├── java/com/br/financaspessoais
│   │   ├── controller          # Endpoints REST
│   │   ├── service             # Regras de negócio
│   │   ├── repository          # Interfaces de acesso ao MongoDB
│   │   ├── model               # Entidades (Usuario, Lancamento)
│   │   ├── config              # JWT, segurança e MongoConfig
│   │   └── mapper              # MapStruct DTO ↔ Model
│   └── resources
│       └── application.yml     # Configurações da aplicação
└── test
    ├── controller              # Testes de integração dos controllers
    ├── service                 # Testes unitários de serviços
    └── repository              # Testes de integração dos repositórios
```

---

## ✅ Funcionalidades principais

- Cadastro de usuários
- Login com geração de token JWT
- Lançamento de despesas e receitas
- Filtro de lançamentos por usuário
- Dashboard com resumo mensal
- Validações de dados
- Testes automatizados por camada
- Banco de dados em memória nos testes

---

## 🧪 Testes

Executar todos os testes:

```bash
./gradlew test
```

Executar testes por camada:

```bash
./gradlew test --tests "*Controller*"
./gradlew test --tests "*Service*"
./gradlew test --tests "*Repository*"
```

---

## 🧪 Integração Contínua (CI)

A pipeline está definida no arquivo `.github/workflows/gradle.yml` e realiza:

- Build com Gradle
- Execução de testes por camada (`Controller`, `Service`, `Repository`)
- Upload de relatórios de falhas se existirem

---

## 🛡️ Segurança

A autenticação é feita via JWT:

- Após login, o token deve ser enviado no header `Authorization: Bearer <token>`.
- Endpoints protegidos utilizam filtros com `OncePerRequestFilter`.

---

## 🧪 Testes com MongoDB em memória

Utilizamos **Flapdoodle Embedded MongoDB** (`de.flapdoodle.embed.mongo`) para rodar os testes sem depender de um Mongo real.

O `application-test.yml` já configura a porta como `0` para gerar uma porta aleatória:

```yaml
spring:
  data:
    mongodb:
      port: 0
      database: financas-testes
```

Além disso, a task de teste ajusta a versão do Mongo para Windows ou Linux:

```groovy
test {
    def version = System.getProperty("os.name").toLowerCase().contains("windows") ? "5.0.5" : "4.6.3"
    systemProperty "custom.mongodb.embedded.version", version
}
```

---

## 🚀 Como rodar localmente

1. Certifique-se de ter o Java 21 e MongoDB rodando localmente (ou use apenas os testes com o banco em memória).
2. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/FinancasPessoais.git
   cd FinancasPessoais
   ```
3. Rode o projeto:
   ```bash
   ./gradlew bootRun
   ```
4. Acesse em: [http://localhost:8080](http://localhost:8080)

---

## 📌 TODO

- [ ] Adicionar testes de integração para autenticação
- [ ] Implementar funcionalidades de exportação
- [ ] Criar documentação Swagger/OpenAPI
- [ ] Implementar Apache Kafka (em andamento)

---

## 👨‍💼 Autor

Jean Heberth Souza Vieira  
📧 jeanheberth19@gmail.com 
www.linkedin.com/in/jeanheberth
📍 Brasil
