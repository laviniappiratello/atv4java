# Automanager — Atividade 4

> Microserviço Java (Spring Boot) para cadastro de clientes e usuários. Tecnologias principais: Java 17, Spring Boot, Maven (com wrapper), H2 para desenvolvimento e suporte a JWT para autenticação.

Pré-requisitos
- Java 17 (JDK)
- Git
- (Opcional) Maven (use o `mvnw`/`mvnw.cmd` incluído)

Execução (Windows)
- Compilar:
  ```
  mvnw.cmd clean package
  ```
- Rodar em desenvolvimento:
   ```
  mvnw.cmd spring-boot:run
    ```
- Ou executar o JAR em `target/` depois do `package`.

Configuração rápida
- Arquivo: `src/main/resources/application.properties`
- Porta padrão: `8080` (altere `server.port` ou use `SERVER_PORT`)

Segurança
- JWT implementado nas classes em `src/main/java/com/autobots/automanager/jwt/`.
- Configurações de segurança em `src/main/java/com/autobots/automanager/configuracao/`.

Testes
- Executar:
  ```
  mvnw.cmd test
   ```
