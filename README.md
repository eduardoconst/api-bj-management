# API de Gerenciamento Acadêmico

API REST para gerenciamento de pessoas em um contexto acadêmico,
desenvolvida como parte de um teste técnico com Java e Spring Boot.

> **Status atual:** modelagem JPA implementada e operações de criação e
> consulta de pessoas disponíveis. Atualização, exclusão, validações,
> tratamento padronizado de erros e testes automatizados de CRUD ainda
> precisam ser concluídos.

## Tecnologias

-   Java 21
-   Spring Boot 4.1.1
-   Maven
-   Spring Data JPA / Hibernate
-   H2 Database em memória
-   Jakarta Persistence (JPA)

## Como executar

No Windows PowerShell, na raiz do projeto:

``` powershell
.\mvnw.cmd spring-boot:run
```

Para executar os testes:

``` powershell
.\mvnw.cmd test
```

A aplicação fica disponível, por padrão, em `http://localhost:8080`.

## Endpoints disponíveis

### Health check

`GET /api/health`

URL local: `http://localhost:8080/api/health`

Resposta esperada: `API Academic Management está funcionando!`

### Criar pessoa

`POST /api/persons`

Exemplo de corpo JSON:

``` json
{
  "name": "Eduardo",
  "phoneNumber": "41999999999",
  "emailAddress": "teste@gmail.com",
  "status": "ACTIVE"
}
```

Em caso de sucesso, retorna o objeto criado com o identificador gerado e
HTTP `201 Created`.

### Listar pessoas

`GET /api/persons`

Retorna uma lista JSON com as pessoas cadastradas.

### Consultar pessoa por ID

`GET /api/persons/{id}`

Exemplo: `GET http://localhost:8080/api/persons/1`

Retorna a pessoa correspondente ao ID informado, se encontrada. O
tratamento específico para IDs inexistentes será aprimorado para
retornar HTTP `404 Not Found`.

O endpoint de criação recebe uma pessoa por requisição; o cadastro de
várias pessoas em uma única chamada ainda não foi implementado.

## Banco de dados H2

A aplicação utiliza H2 em memória, configurado em
`src/main/resources/application.yaml`.

``` yaml
spring:
  application:
    name: api-bj-management

  datasource:
    url: jdbc:h2:mem:academicdb
    driver-class-name: org.h2.Driver
    username: sa
    password:

  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
    properties:
      hibernate:
        format_sql: true

  h2:
    console:
      enabled: true
      path: /h2-console
```

Com `ddl-auto: create`, o Hibernate recria o esquema a partir das
entidades ao iniciar. Como o banco é em memória, os dados são perdidos
ao encerrar a aplicação.

Para abrir o console, acesse `http://localhost:8080/h2-console` com:

  Campo       Valor
  ----------- --------------------------
  JDBC URL    `jdbc:h2:mem:academicdb`
  User Name   `sa`
  Password    deixar vazio

## Modelagem implementada

As entidades estão no pacote `br.com.bjmanagement.model`.

### Person

Entidade-base com `id`, `name`, `phoneNumber`, `emailAddress`, `status`
e `addresses`.

O enum `Status` contém `ACTIVE` e `DISABLE`.
`@Enumerated(EnumType.STRING)` persiste o nome do valor. A estratégia
`@Inheritance(strategy = InheritanceType.JOINED)` mantém os campos
comuns na tabela `PERSON` e os específicos das subclasses em tabelas
próprias.

### Student

Herda de `Person` e adiciona `studentNumber` e `photo`.

### Professor

Herda de `Person` e adiciona `salary`, representado por `BigDecimal`.

### Address

Contém `id`, `street`, `city`, `state`, `zipCode`, `country` e uma
referência a `Person`. O relacionamento usa `@ManyToOne` em
`Address.person` e `@OneToMany(mappedBy = "person")` em
`Person.addresses`.

## Organização em camadas

-   **Controller:** recebe requisições HTTP e retorna respostas.
-   **Service:** coordena a lógica da aplicação.
-   **Repository:** utiliza Spring Data JPA para persistência.
-   **Model:** contém entidades e enumerações de domínio.

Estrutura principal atual:

``` text
src/
└── main/
    ├── java/
    │   └── br/com/bjmanagement/
    │       ├── ApiBjManagementApplication.java
    │       ├── controller/
    │       │   ├── HealthController.java
    │       │   └── PersonController.java
    │       ├── model/
    │       │   ├── Address.java
    │       │   ├── Person.java
    │       │   ├── Professor.java
    │       │   ├── Status.java
    │       │   └── Student.java
    │       ├── repository/
    │       │   └── PersonRepository.java
    │       └── service/
    │           └── PersonService.java
    └── resources/
        └── application.yaml
```

## Verificações manuais realizadas

-   [x] Aplicação Spring Boot inicia localmente.
-   [x] `GET /api/health` responde.
-   [x] Conexão com H2 em memória.
-   [x] Hibernate cria `PERSON`, `STUDENT`, `PROFESSOR` e `ADDRESS`.
-   [x] `POST /api/persons` cria pessoa e retorna `201 Created`.
-   [x] Cadastro confirmado no console H2.
-   [x] `GET /api/persons` lista pessoas.
-   [x] `GET /api/persons/{id}` consulta uma pessoa por identificador.

Essas verificações foram feitas manualmente; os testes automatizados
ainda precisam ser implementados.

## Próximas etapas

1.  Implementar atualização (`PUT`) e exclusão (`DELETE`).
2.  Criar tratamento adequado para recursos não encontrados.
3.  Adicionar validações de entrada.
4.  Criar testes automatizados para as operações CRUD.
5.  Avaliar DTOs para separar o modelo de persistência do contrato HTTP.
6.  Preparar Dockerfile e verificar a execução em contêiner.
7.  Finalizar a documentação e os vídeos solicitados no teste técnico.
