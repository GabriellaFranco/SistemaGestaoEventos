🛠️ Tecnologias Utilizadas:
- Java 17
- Spring Boot 3
- Spring Security (Autenticação baseada em sessão, sem JWT)
- Spring Data JPA
- Banco de Dados PostgreSQL
- Lombok
- Swagger (Springdoc OpenAPI)
- JUnit & Mockito

🚀 Notas da Release:

1 - Autenticação e Segurança:
- Configurado o Spring Security, utilizando autenticação baseada em sessão;
- Adicionada injeção de dependência do usuário autenticado nos controllers usando @AuthenticationPrincipal;
- Definidos papéis de acesso (ADMIN, ORGANIZADOR, PARTICIPANTE) e regras para acesso a endpoints;
- Criptografia de senhas utilizando BCrypt.

2 - Integração entre Entidades
- Injetado o usuário autenticado (@AuthenticationPrincipal) para recuperar automaticamente os dados do usuário logado
nos endpoints;
- Ajustadas relações entre entidades (Usuario, Evento, Inscricao) para garantir que cada ação seja associada ao usuário
correto;
- Corrigidos mapeamentos JPA para melhorar a consistência do banco de dados;

3 - Testes Automatizados
- Criados novos testes para endpoints protegidos, garantindo que usuários não autorizados não possam acessá-los.

------------------------------------------------------------------------------------------------------------------------
