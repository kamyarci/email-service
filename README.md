# Sobre a API

Trata-se de uma API REST simples para envio de e-mails, com suporte à implementação de múltiplos provedores por meio de abstração. Neste projeto, foram implementados dois provedores: o AWS SES como serviço principal e o SendGrid como fallback. A presente aplicação foi desenvolvida com foco em aprendizado, priorizando a arquitetura e a organização do código, seguindo os princípios de Clean Architecture para garantir uma estrutura modular, desacoplada e de fácil manutenção e testabilidade.

## Stack utilizada

- **Java 21**
- **Spring Boot 4.0.6**
- **AWS SES v2** (provedor primário)
- **SendGrid** (provedor de fallback)

## Arquitetura

O projeto segue os princípios de **Clean Architecture**, organizado em quatro camadas:

```
src/main/java/com/kamyla/email_service/
├── core/           # Domínio: interfaces de caso de uso, modelo de request, exceções
├── adapters/       # Porta: interface EmailSenderGateway
├── application/    # Orquestração: EmailSenderService (lógica de failover)
├── infra/          # Implementações concretas dos provedores (SES, SendGrid, etc.)
└── controllers/    # Entrypoint REST
```

A interface EmailSenderGateway desacopla a aplicação de qualquer provedor específico. Adicionar ou trocar um provedor exige apenas uma nova implementação em infra, sem alterações nas camadas core ou application.

## API
### Enviar e-mail

```
POST /api/email
Content-Type: application/json
```

**Body da requisição:**

```json
{
  "to": "destinatario@exemplo.com",
  "subject": "Assunto",
  "body": "Conteúdo do e-mail"
}
```
## Como rodar localmente

**Pré-requisitos:** Java 21, Maven, conta AWS com SES configurado e também conta na Twilio SendGrid com o remetente verificado via Single Sender Verification.

1. Clone o repositório:
   ```bash
   git clone https://github.com/kamyarci/email-service.git
   cd email-service
   ```

2. Configure as variáveis de ambiente:
   ```bash
   export AWS_ACCESS_KEY_ID=sua_access_key
   export AWS_SECRET_ACCESS_KEY=sua_secret_key
   export AWS_DEFAULT_REGION=us-east-1
   export SENDGRID_API_KEY=sua_sendgrid_api_key
   ```

3. Execute:
   ```bash
   ./mvnw spring-boot:run
   ```
   
A API estará disponível em `http://localhost:8080`.

