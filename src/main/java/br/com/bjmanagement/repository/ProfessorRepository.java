package br.com.bjmanagement.repository;

import br.com.bjmanagement.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}