package br.com.bjmanagement.service;

import br.com.bjmanagement.model.Person;
import br.com.bjmanagement.model.Status;
import br.com.bjmanagement.repository.PersonRepository;
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
class PersonServiceTests {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    void shouldCreatePerson() {
        // Arrange
        Person person = new Person();
        person.setName("Ana Souza");
        person.setPhoneNumber("41988883422");
        person.setEmailAddress("anasouza@gmail.com");
        person.setStatus(Status.ACTIVE);

        when(personRepository.save(person)).thenReturn(person);

        // Act
        Person result = personService.create(person);

        // Assert
        assertEquals("Ana Souza", result.getName());
        assertEquals("41988883422", result.getPhoneNumber());
        assertEquals("anasouza@gmail.com", result.getEmailAddress());
        assertEquals(Status.ACTIVE, result.getStatus());

        verify(personRepository).save(person);
    }

    @Test
    void shouldFindAllPersons() {
        //Arrange
        List<Person> persons = List.of(
                createPerson("Ana Souza"),
                createPerson("Bruno Lima")
        );

        when(personRepository.findAll()).thenReturn(persons);

        //Act
        List<Person> result = personService.findAll();

        //Assert
        assertEquals(2, result.size());
        assertEquals("Ana Souza", result.get(0).getName());
        verify(personRepository).findAll();
    }

    @Test
    void shouldFindPersonById() {
        //Arrange
        Person person = createPerson("Ana Souza");

        when(personRepository.findById(1L))
                .thenReturn(Optional.of(person));

        //Act
        Person result = personService.findById(1L);

        //Assert
        assertEquals("Ana Souza", result.getName());
        verify(personRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenPersonDoesNotExist() {
        when(personRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> personService.findById(99L)
        );
    }

    @Test
    void shouldUpdatePerson() {
        Person existing = createPerson("Ana Souza");

        Person updated = createPerson("Ana Silva");
        updated.setPhoneNumber("41999990000");
        updated.setEmailAddress("ana.silva@email.com");
        updated.setStatus(Status.ACTIVE);

        when(personRepository.findById(1L))
                .thenReturn(Optional.of(existing));
        when(personRepository.save(existing)).thenReturn(existing);

        Person result = personService.update(1L, updated);

        assertEquals("Ana Silva", result.getName());
        assertEquals("41999990000", result.getPhoneNumber());
        assertEquals("ana.silva@email.com", result.getEmailAddress());
        assertEquals(Status.ACTIVE, result.getStatus());

        verify(personRepository).save(existing);
    }

    @Test
    void shouldPatchOnlyProvidedFields() {
        Person existing = createPerson("Ana Souza");
        existing.setPhoneNumber("41911112222");

        Person changes = new Person();
        changes.setName("Ana Silva");

        when(personRepository.findById(1L))
                .thenReturn(Optional.of(existing));
        when(personRepository.save(existing)).thenReturn(existing);

        Person result = personService.patch(1L, changes);

        assertEquals("Ana Silva", result.getName());
        assertEquals("41911112222", result.getPhoneNumber());

        verify(personRepository).save(existing);
    }

    @Test
    void shouldDeletePerson() {

        //Arrange
        Person existing = createPerson("Ana Souza");

        when(personRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        //Act
        personService.delete(1L);

        //Assert
        verify(personRepository).findById(1L);
        verify(personRepository).deleteById(1L);
    }

    private Person createPerson(String name) {
        Person person = new Person();
        person.setName(name);
        person.setPhoneNumber("41912345678");
        person.setEmailAddress("teste@email.com");
        person.setStatus(Status.ACTIVE);
        return person;
    }


    @Test
    void shouldNotUpdatePersonWhenIdDoesNotExist() {
        Person updated = createPerson("Ana Silva");

        when(personRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> personService.update(99L, updated)
        );

        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    void shouldNotPatchPersonWhenIdDoesNotExist() {
        Person changes = new Person();
        changes.setName("Ana Silva");

        when(personRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> personService.patch(99L, changes)
        );

        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    void shouldNotDeletePersonWhenIdDoesNotExist() {
        when(personRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> personService.delete(99L)
        );

        verify(personRepository, never()).deleteById(anyLong());
    }
}
