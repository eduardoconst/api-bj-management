package br.com.bjmanagement.repository;

import br.com.bjmanagement.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}