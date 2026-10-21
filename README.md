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

O modelo representa pessoas, alunos, professores e endereços.

![Diagrama do modelo de domínio](docs/diagrama-modelo.png)

- **Person:** nome, telefone, e-mail, status e relacionamento com endereços.
- **Student:** herda os atributos de `Person` e adiciona número de matrícula e foto.
- **Professor:** herda os atributos de `Person` e adiciona salário.
- **Address:** rua, cidade, estado, CEP e país; cada endereço pertence a uma pessoa.
- **Status:** `ACTIVE` ou `DISABLED`.

A herança utiliza `InheritanceType.JOINED`: os atributos comuns ficam na tabela `PERSON`, enquanto os atributos específicos ficam nas tabelas `STUDENT` e `PROFESSOR`, relacionadas pelo mesmo ID. Uma pessoa pode possuir vários endereços, mas cada endereço pertence a uma única pessoa.

**Imagem do diagrama:** coloque `diagrama-modelo.png` dentro da pasta `docs`, na raiz do repositório. O caminho esperado é `docs/diagrama-modelo.png`.

## Estrutura da aplicação

O projeto segue uma organização em camadas, separando as responsabilidades de acesso HTTP, regras de negócio, persistência e representação dos dados.

```text
src/
├── main/
│   ├── java/br/com/bjmanagement/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   └── exception/
│   └── resources/
│       ├── application.properties
│       └── data.sql
└── test/
    └── java/br/com/bjmanagement/
        ├── repository/
        └── service/
```

- **Controller:** recebe as requisições HTTP, direciona as operações para os serviços e retorna as respostas da API.
- **Service:** concentra a lógica de negócio e coordena as operações antes de acessar os repositórios.
- **Repository:** utiliza Spring Data JPA para consultar, salvar e excluir registros no banco de dados.
- **Model:** contém `Person`, `Student`, `Professor` e `Address`, além do enum `Status`.
- **Exception:** reúne exceções personalizadas e o tratamento global de erros, padronizando respostas para recursos inexistentes e requisições inválidas.
- **Resources:** contém as configurações da aplicação e o script `data.sql`, responsável pela carga inicial de dados.
- **Test:** contém testes unitários dos serviços e testes de integração dos repositórios.

Essa separação facilita a manutenção, a leitura do código e os testes isolados das regras de negócio e da persistência.

## Endpoints

Todos os endpoints utilizam o prefixo `/api`.

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
  "zipCode": "80000-000",
  "country": "Brasil",
  "person": {
    "id": 101
  }
}
```

Substitua o ID pelo identificador de uma pessoa existente.

## Configuração e execução

### Pré-requisitos

- JDK 21
- Maven ou Maven Wrapper incluído no projeto

### Executar a aplicação

Abra um terminal na raiz do projeto e execute:

```powershell
.\mvnw.cmd spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

## Banco de dados H2

A aplicação utiliza H2 em memória. O banco é recriado quando a aplicação é reiniciada, e os dados iniciais definidos em `data.sql` são carregados novamente.

### Acessar o console H2

Com a aplicação em execução, acesse `http://localhost:8080/h2-console`.

| Campo | Valor |
|---|---|
| Driver Class | `org.h2.Driver` |
| JDBC URL | `jdbc:h2:mem:bjdb` |
| User Name | `BJ` |
| Password | "Null" |

Confira a senha no arquivo de configuração. Está vazio, deixe o campo Password em branco e clique em **Connect**.

### Consultar pessoas

Se quiser consultar Persons criadas, vá no console H2, execute:

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

Os `LEFT JOIN` permitem retornar todas as pessoas, inclusive as que não possuem registro nas tabelas de alunos ou professores. A coluna `PERSON_TYPE` identifica o tipo de registro.

### Consultar endereços

Se quiser consultar Address criados, vá no console H2, execute:

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

- Os dados cadastrados pela API são mantidos enquanto a aplicação estiver em execução.
- Ao reiniciar a aplicação, o banco é recriado.
- Os dados iniciais definidos em `data.sql` são carregados novamente.
- Dados inseridos manualmente pelo console não permanecem após o reinício.

## Testes automatizados

Para executar toda a suíte de testes:

```powershell
.\mvnw.cmd test
```

Para executar individualmente um teste de integração:

```powershell
.\mvnw.cmd -Dtest=PersonRepositoryIntegrationTest test
```

A suíte contempla testes unitários dos serviços com JUnit e Mockito e testes de integração dos repositórios com H2.

### Cobertura dos testes

- **PersonServiceTests:** criação, listagem, busca por ID, atualização completa e parcial, exclusão e cenários com ID inexistente.
- **StudentServiceTests:** operações CRUD, atualização parcial e validação dos atributos herdados e específicos de aluno.
- **ProfessorServiceTests:** operações CRUD, atualização parcial e validação dos atributos herdados e do salário.
- **AddressServiceTests:** operações CRUD, atualização da pessoa associada, validação da referência à pessoa e cenários com endereço ou pessoa inexistente.
- **PersonRepositoryIntegrationTest:** persistência e recuperação de uma pessoa no H2.
- **StudentRepositoryIntegrationTest:** persistência dos dados herdados e dos atributos específicos de aluno.
- **ProfessorRepositoryIntegrationTest:** persistência dos dados herdados e do salário.
- **AddressRepositoryIntegrationTest:** persistência do endereço e recuperação da pessoa associada.

Os testes de serviço usam repositórios simulados para verificar a lógica da camada de negócio. Os testes de integração usam `@DataJpaTest` para validar a persistência via JPA e H2. Essa suíte não representa, por si só, testes automatizados de ponta a ponta dos endpoints HTTP.

## Tratamento de erros

A API utiliza `@RestControllerAdvice` para padronizar as respostas de erro.

- **HTTP 404 — Not Found:** recurso não encontrado.
- **HTTP 400 — Bad Request:** requisição inválida, como uma referência de pessoa ausente no cadastro de endereço.

As respostas incluem data e hora, código HTTP, descrição do erro e mensagem explicativa.

## Decisões técnicas

- **Herança com `InheritanceType.JOINED`:** os atributos comuns são armazenados em `PERSON`, enquanto os campos específicos ficam nas tabelas `STUDENT` e `PROFESSOR`.
- **Relacionamento entre pessoa e endereço:** cada endereço referencia uma pessoa por meio de `person.id`.
- **Serialização JSON:** a lista de endereços de `Person` é ignorada na serialização direta para evitar recursão JSON.
- **Referência da foto:** o campo `photo` armazena uma referência textual; o upload de arquivos não está implementado.
- **Banco H2 em memória:** facilita a execução local sem exigir a instalação de um banco externo. Os dados são recriados ao reiniciar a aplicação.

## Melhorias futuras

- Adicionar Docker e Docker Compose para facilitar a configuração e a execução do ambiente.
- Desenvolver uma interface frontend para consumir a API.
- Avaliar o uso de DTOs para controlar os dados de entrada e saída e permitir respostas agregadas com informações de pessoas e endereços.
- Ampliar a cobertura automatizada com testes dos controllers e das respostas HTTP.
- Avaliar melhorias de validação dos dados de entrada conforme os requisitos do projeto.
