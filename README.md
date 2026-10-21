# API BJ Management

API REST desenvolvida em Java com Spring Boot para gerenciamento de pessoas, alunos, professores e endereços. Utiliza Spring Data JPA/Hibernate para persistência e H2 como banco de dados em memória.

## Tecnologias

- Java 21
- Spring Boot 4.1.1
- Maven
- Spring Web
- Spring Data JPA / Hibernate
- H2 Database
- JUnit, Mockito e Spring Boot Test

## Modelo de domínio

- **Person:** nome, telefone, e-mail, status e relacionamento com endereços.
- **Student:** herda os atributos de `Person` e adiciona número de matrícula e foto.
- **Professor:** herda os atributos de `Person` e adiciona salário.
- **Address:** rua, cidade, estado, CEP e país; cada endereço pertence a uma pessoa.
- **Status:** `ACTIVE` ou `DISABLED`.

A herança utiliza `InheritanceType.JOINED`: os atributos comuns ficam na tabela `PERSON`, enquanto os atributos específicos ficam nas tabelas `STUDENT` e `PROFESSOR`, relacionadas pelo mesmo ID.

Uma pessoa pode possuir vários endereços, mas cada endereço pertence a uma única pessoa.

## Endpoints

Todos os endpoints utilizam o prefixo `/api`.

### Saúde da aplicação

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/health` | Verificar se a API está respondendo |

### Pessoas

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/person` | Cadastrar pessoa |
| GET | `/api/person` | Listar pessoas |
| GET | `/api/person/{id}` | Buscar pessoa por ID |
| PUT | `/api/person/{id}` | Atualizar pessoa |
| PATCH | `/api/person/{id}` | Atualizar parcialmente |
| DELETE | `/api/person/{id}` | Excluir pessoa |

### Alunos

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/students` | Cadastrar aluno |
| GET | `/api/students` | Listar alunos |
| GET | `/api/students/{id}` | Buscar aluno por ID |
| PUT | `/api/students/{id}` | Atualizar aluno |
| PATCH | `/api/students/{id}` | Atualizar parcialmente |
| DELETE | `/api/students/{id}` | Excluir aluno |

### Professores

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/professors` | Cadastrar professor |
| GET | `/api/professors` | Listar professores |
| GET | `/api/professors/{id}` | Buscar professor por ID |
| PUT | `/api/professors/{id}` | Atualizar professor |
| PATCH | `/api/professors/{id}` | Atualizar parcialmente |
| DELETE | `/api/professors/{id}` | Excluir professor |

### Endereços

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/addresses` | Cadastrar endereço associado a uma pessoa |
| GET | `/api/addresses` | Listar endereços |
| GET | `/api/addresses/{id}` | Buscar endereço por ID |
| PUT | `/api/addresses/{id}` | Atualizar endereço |
| PATCH | `/api/addresses/{id}` | Atualizar parcialmente |
| DELETE | `/api/addresses/{id}` | Excluir endereço |

As operações de criação retornam HTTP 201 e as exclusões bem-sucedidas retornam HTTP 204.

## Exemplos de requisições

### Cadastrar aluno

`POST /api/students`

```json
{
  "name": "João Silva",
  "phoneNumber": "41999999999",
  "emailAddress": "joao@email.com",
  "status": "ACTIVE",
  "studentNumber": "2026001",
  "photo": "joao.jpg"
}
```

### Cadastrar professor

`POST /api/professors`

```json
{
  "name": "Maria Oliveira",
  "phoneNumber": "41988887777",
  "emailAddress": "maria@universidade.com",
  "status": "ACTIVE",
  "salary": 8500.00
}
```

### Cadastrar endereço

`POST /api/addresses`

A pessoa referenciada precisa existir previamente.

```json
{
  "street": "Rua das Flores, 100",
  "city": "Curitiba",
  "state": "PR",
  "zipCode": "80010-000",
  "country": "Brasil",
  "person": {
    "id": 101
  }
}
```

Substitua o ID pelo identificador de uma pessoa existente.

## Executar a aplicação

### Pré-requisitos

- JDK 21
- Maven ou Maven Wrapper incluído no projeto

Abra um terminal na raiz do projeto e execute:

```powershell
.\mvnw.cmd spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

Para verificar se está respondendo, acesse `http://localhost:8080/api/health`.

## Banco de dados H2

A aplicação utiliza H2 em memória. O banco é recriado quando a aplicação é reiniciada, e os dados iniciais definidos em `data.sql` são carregados novamente.

### Acessar o console H2

Com a aplicação em execução, acesse:

`http://localhost:8080/h2-console`

Utilize as seguintes configurações:

| Campo | Valor |
|---|---|
| Driver Class | `org.h2.Driver` |
| JDBC URL | `jdbc:h2:mem:bjdb` |
| User Name | `BJ` |
| Password | Conforme a configuração da aplicação |

Confira a senha no arquivo de configuração do projeto. Se estiver vazia, deixe o campo Password em branco e clique em **Connect**.

### Consultar as pessoas cadastradas

No console H2, execute a seguinte consulta para listar pessoas e identificar se cada registro possui dados de aluno ou professor:

```sql
SELECT
    p.ID,
    p.NAME,
    p.PHONE_NUMBER,
    p.EMAIL_ADDRESS,
    p.STATUS,
    s.STUDENT_NUMBER,
    s.PHOTO,
    pr.SALARY,
    CASE
        WHEN s.ID IS NOT NULL THEN 'STUDENT'
        WHEN pr.ID IS NOT NULL THEN 'PROFESSOR'
        ELSE 'PERSON'
    END AS PERSON_TYPE
FROM PERSON p
LEFT JOIN STUDENT s ON s.ID = p.ID
LEFT JOIN PROFESSOR pr ON pr.ID = p.ID
ORDER BY p.ID;
```

Os `LEFT JOIN` permitem que a consulta retorne todas as pessoas, inclusive aquelas que não possuem registro nas tabelas de alunos ou professores.

A coluna `PERSON_TYPE` facilita a identificação do tipo de registro:

- `PERSON`: pessoa sem registro correspondente nas tabelas de aluno ou professor.
- `STUDENT`: pessoa com registro na tabela de alunos.
- `PROFESSOR`: pessoa com registro na tabela de professores.

### Consultar endereços

Para listar os endereços e os respectivos proprietários:

```sql
SELECT
    a.ID,
    a.STREET,
    a.CITY,
    a.STATE,
    a.ZIP_CODE,
    a.COUNTRY,
    a.PERSON_ID,
    p.NAME AS PERSON_NAME
FROM ADDRESS a
JOIN PERSON p ON p.ID = a.PERSON_ID
ORDER BY a.ID;
```

### Importante sobre os dados

O H2 está configurado em memória. Portanto:

- Os dados cadastrados pela API são mantidos enquanto a aplicação estiver em execução.
- Ao reiniciar a aplicação, o banco é recriado.
- Os dados iniciais definidos em `data.sql` são carregados novamente.
- Dados inseridos manualmente pelo console não permanecem após o reinício.

## Executar os testes

Para executar toda a suíte automatizada:

```powershell
.\mvnw.cmd test
```

Para executar individualmente um teste de integração, por exemplo:

```powershell
.\mvnw.cmd -Dtest=PersonRepositoryIntegrationTest test
```

A suíte atual possui **48 testes aprovados**, incluindo testes unitários dos serviços e testes de integração dos repositórios com H2.

Os testes de integração verificam a persistência de pessoas, alunos, professores e endereços. Os testes unitários verificam os comportamentos dos serviços de forma isolada.

## Estrutura da aplicação

- **Model:** entidades e enumerações do domínio.
- **Repository:** acesso e persistência dos dados usando Spring Data JPA.
- **Service:** regras de negócio e operações sobre as entidades.
- **Controller:** endpoints REST.
- **Exception:** tratamento global e padronizado de erros.
- **Test:** testes unitários e de integração.

## Tratamento de erros

A API utiliza `@RestControllerAdvice` para padronizar as respostas de erro.

- **HTTP 404 — Not Found:** recurso não encontrado.
- **HTTP 400 — Bad Request:** requisição inválida, como uma referência de pessoa ausente no cadastro de endereço.

As respostas incluem informações como data e hora, código HTTP, descrição do erro e mensagem explicativa.

## Decisões técnicas e melhorias futuras

- Alunos e professores recebem os atributos comuns de `Person` e os atributos específicos em uma única requisição.
- Endereços são associados a pessoas por meio de `person.id`.
- A lista de endereços é ignorada na serialização direta de `Person` para evitar recursão JSON. Uma resposta agregada poderá ser implementada futuramente com DTOs.
- O campo `photo` representa uma referência textual; o upload de arquivos não está implementado.
- Docker e frontend são etapas futuras do projeto.
