-- ==========================================
-- 1. PESSOAS
-- ==========================================

INSERT INTO PERSON (ID, NAME, PHONE_NUMBER, EMAIL_ADDRESS, STATUS)
VALUES
    (101, 'Ana Souza', '41999991001', 'ana.souza@email.com', 'ACTIVE');

INSERT INTO PERSON (ID, NAME, PHONE_NUMBER, EMAIL_ADDRESS, STATUS)
VALUES
    (102, 'Bruno Lima', '41999991002', 'bruno.lima@email.com', 'ACTIVE');

INSERT INTO PERSON (ID, NAME, PHONE_NUMBER, EMAIL_ADDRESS, STATUS)
VALUES
    (103, 'Carla Mendes', '41999991003', 'carla.mendes@email.com', 'DISABLED');

INSERT INTO PERSON (ID, NAME, PHONE_NUMBER, EMAIL_ADDRESS, STATUS)
VALUES
    (104, 'Daniel Costa', '41999991004', 'daniel.costa@email.com', 'ACTIVE');

INSERT INTO PERSON (ID, NAME, PHONE_NUMBER, EMAIL_ADDRESS, STATUS)
VALUES
    (105, 'Elisa Martins', '41999991005', 'elisa.martins@email.com', 'ACTIVE');


-- ==========================================
-- 2. ALUNOS
-- O ID é compartilhado com PERSON
-- ==========================================

INSERT INTO STUDENT (ID, STUDENT_NUMBER, PHOTO)
VALUES (101, '2026001', 'ana.jpg');

INSERT INTO STUDENT (ID, STUDENT_NUMBER, PHOTO)
VALUES (102, '2026002', 'bruno.jpg');

INSERT INTO STUDENT (ID, STUDENT_NUMBER, PHOTO)
VALUES (103, '2026003', 'carla.jpg');


-- ==========================================
-- 3. PROFESSORES
-- O ID é compartilhado com PERSON
-- ==========================================

INSERT INTO PROFESSOR (ID, SALARY)
VALUES (104, 8500.00);

INSERT INTO PROFESSOR (ID, SALARY)
VALUES (105, 9200.00);


-- ==========================================
-- 4. ENDEREÇOS
-- Cada endereço pertence a uma pessoa
-- ==========================================

INSERT INTO ADDRESS
(STREET, CITY, STATE, ZIP_CODE, COUNTRY, PERSON_ID)
VALUES
    ('Rua das Flores, 100', 'Curitiba', 'PR', '80010-000', 'Brasil', 101);

INSERT INTO ADDRESS
(STREET, CITY, STATE, ZIP_CODE, COUNTRY, PERSON_ID)
VALUES
    ('Rua XV de Novembro, 250', 'Curitiba', 'PR', '80020-310', 'Brasil', 102);

INSERT INTO ADDRESS
(STREET, CITY, STATE, ZIP_CODE, COUNTRY, PERSON_ID)
VALUES
    ('Avenida Paraná, 500', 'Colombo', 'PR', '83405-000', 'Brasil', 103);

INSERT INTO ADDRESS
(STREET, CITY, STATE, ZIP_CODE, COUNTRY, PERSON_ID)
VALUES
    ('Rua Marechal Deodoro, 700', 'Curitiba', 'PR', '80060-010', 'Brasil', 104);

INSERT INTO ADDRESS
(STREET, CITY, STATE, ZIP_CODE, COUNTRY, PERSON_ID)
VALUES
    ('Rua João Gualberto, 1200', 'Curitiba', 'PR', '80030-000', 'Brasil', 105);