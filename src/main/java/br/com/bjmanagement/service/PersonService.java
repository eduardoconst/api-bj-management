package br.com.bjmanagement.service;

import br.com.bjmanagement.exception.ResourceNotFoundException;
import br.com.bjmanagement.model.Person;
import br.com.bjmanagement.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    //encapsulamento imutavel PersonRepository
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person create(Person person) {
        return personRepository.save(person);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Person not found by ID: " + id
                ));
    }

    public Person update(Long id, Person person) {
        Person exist = findById(id);
        exist.setName(person.getName());
        exist.setPhoneNumber(person.getPhoneNumber());
        exist.setEmailAddress(person.getEmailAddress());
        exist.setStatus(person.getStatus());
        return personRepository.save(exist);
    }

    public Person patch(Long id, Person person) {
        Person exist = findById(id);
        if (person.getName() != null) {exist.setName(person.getName());}
        if (person.getPhoneNumber() != null) {exist.setPhoneNumber(person.getPhoneNumber());}
        if (person.getEmailAddress() != null) {exist.setEmailAddress(person.getEmailAddress());}
        if (person.getStatus() != null) {exist.setStatus(person.getStatus());}
        return personRepository.save(exist);
    }

    public void delete(Long id) {
        findById(id);
        personRepository.deleteById(id);
    }
}