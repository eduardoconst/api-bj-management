
package br.com.bjmanagement.repository;

import br.com.bjmanagement.model.Address;
import br.com.bjmanagement.model.Person;
import br.com.bjmanagement.model.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AddressRepositoryIntegrationTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PersonRepository personRepository;

    @Test
    void shouldSaveAndRetrieveAddressWithPersonFromH2() {
        Person person = new Person();
        person.setName("Pessoa do Endereco H2");
        person.setPhoneNumber("41966665555");
        person.setEmailAddress("endereco.h2@email.com");
        person.setStatus(Status.ACTIVE);

        Person savedPerson = personRepository.saveAndFlush(person);

        Address address = new Address();
        address.setStreet("Rua de Teste");
        address.setCity("Curitiba");
        address.setState("PR");
        address.setZipCode("80000-000");
        address.setCountry("Brasil");
        address.setPerson(savedPerson);

        Address savedAddress = addressRepository.saveAndFlush(address);

        Optional<Address> result =
                addressRepository.findById(savedAddress.getId());

        assertThat(savedAddress.getId()).isNotNull();
        assertThat(result).isPresent();

        Address foundAddress = result.orElseThrow();

        assertThat(foundAddress.getStreet())
                .isEqualTo("Rua de Teste");
        assertThat(foundAddress.getCity())
                .isEqualTo("Curitiba");
        assertThat(foundAddress.getState())
                .isEqualTo("PR");
        assertThat(foundAddress.getZipCode())
                .isEqualTo("80000-000");
        assertThat(foundAddress.getCountry())
                .isEqualTo("Brasil");

        assertThat(foundAddress.getPerson()).isNotNull();
        assertThat(foundAddress.getPerson().getId())
                .isEqualTo(savedPerson.getId());
        assertThat(foundAddress.getPerson().getName())
                .isEqualTo("Pessoa do Endereco H2");
    }
}
