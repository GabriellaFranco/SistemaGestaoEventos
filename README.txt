O Sistema de Gestão de Eventos e Inscrições é uma aplicação desenvolvida para facilitar a administração de eventos e a
inscrição de participantes. O projeto foi desenvolvido com Spring Boot, seguindo as melhores práticas de arquitetura e
desenvolvimento, garantindo escalabilidade e segurança.

📌Tecnologias Utilizadas:
- Java 17
- Spring Boot (Spring Web, Spring Data JPA);
- JPA/Hibernate para persistência de dados;
- Banco de Dados PostgresSQL;
- Lombok para redução de código boilerplate;
- JUnit 5 & Mockito para testes unitários;
- Jackson para serialização/deserialização de JSON;
- Maven para gerenciamento de dependências.

🚀Funcionalidades Implementadas:
1. Modelagem de entidades(Evento, Inscrição, Usuário e Pagamento);
2. DTOs para comunicação entre camadas;
3. Camada Service com regras de negócio iniciais;
4. Mappers para conversão entre entidades e DTOs;
5. Controllers e endpoints REST (CRUD);
6. Tratamento de exceções com @ControllerAdvice;
7. Testes Automatizados com JUnit, Mockito e MockMvc.

------------------------------------------------------------------------------------------------------------------------

Próximos passos:
- Implementar Spring Security e autenticação JWT;

