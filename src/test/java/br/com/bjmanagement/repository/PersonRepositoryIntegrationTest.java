package br.com.bjmanagement.repository;

import br.com.bjmanagement.model.Person;
import br.com.bjmanagement.model.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PersonRepositoryIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void shouldSaveAndRetrievePersonFromH2() {
        // Arrange: preparar uma pessoa
        Person person = new Person();
        person.setName("Pessoa Teste H2");
        person.setPhoneNumber("41999999999");
        person.setEmailAddress("teste.h2@email.com");
        person.setStatus(Status.ACTIVE);

        // Act: salvar no banco H2
        Person savedPerson = personRepository.saveAndFlush(person);

        // Consultar o registro persistido
        Optional<Person> result =
                personRepository.findById(savedPerson.getId());

        // Assert: verificar os dados recuperados
        assertThat(savedPerson.getId()).isNotNull();
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Pessoa Teste H2");
        assertThat(result.get().getEmailAddress())
                .isEqualTo("teste.h2@email.com");
        assertThat(result.get().getStatus()).isEqualTo(Status.ACTIVE);
    }
}