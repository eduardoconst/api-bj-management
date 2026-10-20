# API de Gerenciamento Acadêmico

API REST em Java com Spring Boot para implementar CRUDs usando Spring Data JPA/Hibernate e banco H2 em memória.

## Tecnologias

- Java 21
- Spring Boot 4
- Maven
- Spring Web
- Spring Data JPA / Hibernate
- H2 Database
- JUnit / Spring Boot Test

## Modelo de domínio

- `Person`: nome, telefone, e-mail, status e relacionamento com endereços.
- `Student extends Person`: número de matrícula e foto.
- `Professor extends Person`: salário.
- `Address`: rua, cidade, estado, CEP e país; cada endereço pertence a uma pessoa.
- `Status`: `ACTIVE` ou `DISABLE`.

A herança usa `InheritanceType.JOINED`: os atributos comuns ficam em `PERSON`, e os atributos específicos ficam em `STUDENT` ou `PROFESSOR`, relacionados pelo mesmo ID.

### Interpretação do diagrama

O diagrama não esclarece se uma pessoa pode ser aluno e professor ao mesmo tempo, nem se é possível mudar de um tipo para outro. Para manter o escopo objetivo, esta implementação cria diretamente um `Student` ou um `Professor` por seus respectivos endpoints. Os tipos são tratados como cadastros distintos; conversão entre subtipos não foi implementada.

A tabela `PERSON` isoladamente não contém uma coluna discriminadora de tipo. O tipo pode ser identificado pela existência do mesmo ID em `STUDENT` ou `PROFESSOR`, ou por uma consulta com `LEFT JOIN`.

## Endpoints

### Pessoas

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/persons` | Criar pessoa |
| GET | `/api/persons` | Listar pessoas |
| GET | `/api/persons/{id}` | Buscar pessoa |
| PUT | `/api/persons/{id}` | Atualizar campos comuns |
| PATCH | `/api/persons/{id}` | Atualizar parcialmente |
| DELETE | `/api/persons/{id}` | Excluir pessoa |

### Alunos

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/students` | Criar aluno |
| GET | `/api/students` | Listar alunos |
| GET | `/api/students/{id}` | Buscar aluno |
| PUT | `/api/students/{id}` | Atualizar aluno |
| PATCH | `/api/students/{id}` | Atualizar parcialmente |
| DELETE | `/api/students/{id}` | Excluir aluno |

### Professores

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/professors` | Criar professor |
| GET | `/api/professors` | Listar professores |
| GET | `/api/professors/{id}` | Buscar professor |
| PUT | `/api/professors/{id}` | Atualizar professor |
| PATCH | `/api/professors/{id}` | Atualizar parcialmente |
| DELETE | `/api/professors/{id}` | Excluir professor |

### Endereços

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/addresses` | Criar endereço associado a uma pessoa |
| GET | `/api/addresses` | Listar endereços |
| GET | `/api/addresses/{id}` | Buscar endereço |
| PUT | `/api/addresses/{id}` | Atualizar endereço |
| PATCH | `/api/addresses/{id}` | Atualizar parcialmente |
| DELETE | `/api/addresses/{id}` | Excluir endereço |

### Saúde

- `GET /api/health`: verifica se a API está respondendo.

## Exemplos de requisições

### Criar aluno — `POST /api/students`

```json
{
  "name": "Eduardo Constantino",
  "phoneNumber": "41999999999",
  "emailAddress": "ducontageral@email.com",
  "status": "ACTIVE",
  "studentNumber": "2026001",
  "photo": "du.jpg"
}
```

### Criar professor — `POST /api/professors`

```json
{
  "name": "Maria Antonieta",
  "phoneNumber": "41988887777",
  "emailAddress": "maria@universidade.com",
  "status": "ACTIVE",
  "salary": 8500.00
}
```

### Criar endereço — `POST /api/addresses`

Substitua o ID abaixo pelo ID real de uma pessoa, aluno ou professor existente.

```json
{
  "street": "Rua das Flores, 100",
  "city": "Curitiba",
  "state": "PR",
  "zipCode": "80010-000",
  "country": "Brasil",
  "person": { "id": 1 }
}
```

O serviço verifica se a pessoa existe antes de associar o endereço.

## Banco H2

Console: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:bjdb`
- User: `bj`
- Password: em branco

O banco é em memória; os dados são perdidos quando a aplicação é encerrada ou reiniciada.

## Executar

Requisitos: JDK 21 e Maven. Também é possível executar a classe `ApiBjManagementApplication` pelo IntelliJ IDEA.

```bash
mvn spring-boot:run
mvn test
```

No Windows, se o Maven Wrapper estiver configurado:

```powershell
.\mvnw.cmd spring-boot:run
.\mvnw.cmd test
```

## Consultar pessoas e tipos no H2

Esta consulta lista todas as pessoas e exibe os dados específicos de aluno ou professor quando existirem:

```sql
SELECT
    p.ID,
    p.NAME,
    p.PHONENUMBER,
    p.EMAILADDRESS,
    p.STATUS,
    s.STUDENTNUMBER,
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

Os `LEFT JOIN`s mantêm na consulta pessoas que não possuem registro em `STUDENT` ou `PROFESSOR`.

## Dados de teste

É possível cadastrar dados manualmente pelos endpoints ou executar SQL no console H2. Como o banco é recriado ao iniciar a aplicação, dados inseridos manualmente precisam ser cadastrados novamente após reiniciar.

Os testes automatizados devem preparar seus próprios dados e verificar os resultados sem depender de registros inseridos manualmente. Testes verificam comportamento; não substituem as instruções de configuração do ambiente.

## Decisões e melhorias futuras

- Alunos e professores recebem os atributos comuns herdados de `Person` e os atributos específicos em uma única requisição.
- `Address` recebe a referência à pessoa pelo campo `person.id`.
- Para evitar serialização JSON recursiva entre `Person.addresses` e `Address.person`, a lista de endereços é ignorada na serialização direta de `Person`. Uma resposta agregada poderá ser implementada futuramente com DTOs.
- Próximas melhorias: tratamento padronizado de exceções, validação de entrada, testes automatizados de CRUD, Docker e upload real de foto.
