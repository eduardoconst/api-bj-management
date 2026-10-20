package br.com.bjmanagement.controller;

import br.com.bjmanagement.model.Person;
import br.com.bjmanagement.service.PersonService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person create(@RequestBody Person person) {
        return personService.create(person);
    }

    @GetMapping
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public Person findById(@PathVariable Long id) {
        return personService.findById(id);
    }

}