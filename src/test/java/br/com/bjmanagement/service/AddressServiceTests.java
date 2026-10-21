
package br.com.bjmanagement.service;


import br.com.bjmanagement.model.Address;
import br.com.bjmanagement.model.Person;
import br.com.bjmanagement.model.Status;
import br.com.bjmanagement.repository.AddressRepository;
import br.com.bjmanagement.repository.PersonRepository;
import org.springframework.test.util.ReflectionTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTests {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private AddressService addressService;

    @Test
    void shouldCreateAddress() {
        Person person = createPerson(1L);
        Address address = createAddress(person);

        when(personRepository.findById(1L))
                .thenReturn(Optional.of(person));
        when(addressRepository.save(address)).thenReturn(address);

        Address result = addressService.create(address);

        assertEquals("Rua das Flores, 100", result.getStreet());
        assertEquals("Curitiba", result.getCity());
        assertEquals("PR", result.getState());
        assertEquals("80010-000", result.getZipCode());
        assertEquals("Brasil", result.getCountry());
        assertEquals(1L, result.getPerson().getId());

        verify(personRepository).findById(1L);
        verify(addressRepository).save(address);
    }

    @Test
    void shouldFindAllAddresses() {
        List<Address> addresses = List.of(
                createAddress(createPerson(1L)),
                createAddress(createPerson(2L))
        );

        when(addressRepository.findAll()).thenReturn(addresses);

        List<Address> result = addressService.findAll();

        assertEquals(2, result.size());
        assertEquals("Curitiba", result.get(0).getCity());

        verify(addressRepository).findAll();
    }

    @Test
    void shouldFindAddressById() {
        Address address = createAddress(createPerson(1L));

        when(addressRepository.findById(10L))
                .thenReturn(Optional.of(address));

        Address result = addressService.findById(10L);

        assertEquals("Rua das Flores, 100", result.getStreet());
        assertEquals(1L, result.getPerson().getId());

        verify(addressRepository).findById(10L);
    }

    @Test
    void shouldThrowExceptionWhenAddressDoesNotExist() {
        when(addressRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> addressService.findById(99L)
        );
    }

    @Test
    void shouldUpdateAddress() {
        Person person = createPerson(1L);
        Address existing = createAddress(person);

        Address updated = new Address();
        updated.setStreet("Avenida Brasil, 200");
        updated.setCity("São Paulo");
        updated.setState("SP");
        updated.setZipCode("01000-000");
        updated.setCountry("Brasil");
        updated.setPerson(createPersonReference(2L));

        Person newPerson = createPerson(2L);

        when(addressRepository.findById(10L))
                .thenReturn(Optional.of(existing));
        when(personRepository.findById(2L))
                .thenReturn(Optional.of(newPerson));
        when(addressRepository.save(existing)).thenReturn(existing);

        Address result = addressService.update(10L, updated);

        assertEquals("Avenida Brasil, 200", result.getStreet());
        assertEquals("São Paulo", result.getCity());
        assertEquals("SP", result.getState());
        assertEquals("01000-000", result.getZipCode());
        assertEquals("Brasil", result.getCountry());
        assertEquals(2L, result.getPerson().getId());

        verify(addressRepository).save(existing);
    }

    @Test
    void shouldPatchOnlyProvidedFields() {
        Person person = createPerson(1L);
        Address existing = createAddress(person);

        Address changes = new Address();
        changes.setCity("Pinhais");

        when(addressRepository.findById(10L))
                .thenReturn(Optional.of(existing));
        when(addressRepository.save(existing)).thenReturn(existing);

        Address result = addressService.patch(10L, changes);

        assertEquals("Pinhais", result.getCity());
        assertEquals("Rua das Flores, 100", result.getStreet());
        assertEquals("PR", result.getState());
        assertEquals("80010-000", result.getZipCode());
        assertEquals("Brasil", result.getCountry());
        assertEquals(1L, result.getPerson().getId());

        verify(personRepository, never()).findById(anyLong());
        verify(addressRepository).save(existing);
    }

    @Test
    void shouldPatchAddressPersonWhenProvided() {
        Person originalPerson = createPerson(1L);
        Person newPerson = createPerson(2L);
        Address existing = createAddress(originalPerson);

        Address changes = new Address();
        changes.setPerson(createPersonReference(2L));

        when(addressRepository.findById(10L))
                .thenReturn(Optional.of(existing));
        when(personRepository.findById(2L))
                .thenReturn(Optional.of(newPerson));
        when(addressRepository.save(existing)).thenReturn(existing);

        Address result = addressService.patch(10L, changes);

        assertEquals(2L, result.getPerson().getId());
        assertEquals("Rua das Flores, 100", result.getStreet());

        verify(personRepository).findById(2L);
        verify(addressRepository).save(existing);
    }

    @Test
    void shouldDeleteAddress() {
        Address address = createAddress(createPerson(1L));

        when(addressRepository.findById(10L))
                .thenReturn(Optional.of(address));

        addressService.delete(10L);

        verify(addressRepository).findById(10L);
        verify(addressRepository).deleteById(10L);
    }

    @Test
    void shouldNotDeleteAddressWhenIdDoesNotExist() {
        when(addressRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> addressService.delete(99L)
        );

        verify(addressRepository, never()).deleteById(anyLong());
    }

    @Test
    void shouldNotCreateAddressWithoutPersonId() {
        Address address = createAddress(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> addressService.create(address)
        );

        verify(addressRepository, never()).save(any(Address.class));
        verify(personRepository, never()).findById(anyLong());
    }

    @Test
    void shouldNotCreateAddressWhenPersonDoesNotExist() {
        Address address = createAddress(createPersonReference(99L));

        when(personRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> addressService.create(address)
        );

        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void shouldNotUpdateAddressWhenIdDoesNotExist() {
        Address changes = createAddress(createPersonReference(1L));

        when(addressRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> addressService.update(99L, changes)
        );

        verify(addressRepository, never()).save(any(Address.class));
        verify(personRepository, never()).findById(anyLong());
    }

    @Test
    void shouldNotPatchAddressWhenIdDoesNotExist() {
        Address changes = new Address();
        changes.setCity("Pinhais");

        when(addressRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> addressService.patch(99L, changes)
        );

        verify(addressRepository, never()).save(any(Address.class));
    }

    private Person createPerson(Long id) {
        Person person = createPersonReference(id);
        person.setName("Ana Souza");
        person.setPhoneNumber("41912345678");
        person.setEmailAddress("ana@email.com");
        person.setStatus(Status.ACTIVE);
        return person;
    }

    private Person createPersonReference(Long id) {
        Person person = new Person();

        // Define o ID apenas no cenário de teste.
        // A aplicação continua gerando o ID automaticamente.
        ReflectionTestUtils.setField(person, "id", id);

        return person;
    }

    private Address createAddress(Person person) {
        Address address = new Address();
        address.setStreet("Rua das Flores, 100");
        address.setCity("Curitiba");
        address.setState("PR");
        address.setZipCode("80010-000");
        address.setCountry("Brasil");
        address.setPerson(person);
        return address;
    }
}
