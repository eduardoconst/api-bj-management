
package br.com.bjmanagement.repository;

import br.com.bjmanagement.model.Professor;
import br.com.bjmanagement.model.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProfessorRepositoryIntegrationTest {

    @Autowired
    private ProfessorRepository professorRepository;

    @Test
    void shouldSaveAndRetrieveProfessorFromH2() {
        Professor professor = new Professor();
        professor.setName("Professor Teste H2");
        professor.setPhoneNumber("41977776666");
        professor.setEmailAddress("professor.h2@email.com");
        professor.setStatus(Status.ACTIVE);
        professor.setSalary(new BigDecimal("7500.00"));

        Professor savedProfessor =
                professorRepository.saveAndFlush(professor);

        Optional<Professor> result =
                professorRepository.findById(savedProfessor.getId());

        assertThat(savedProfessor.getId()).isNotNull();
        assertThat(result).isPresent();

        Professor foundProfessor = result.orElseThrow();

        // Campos herdados de Person
        assertThat(foundProfessor.getName())
                .isEqualTo("Professor Teste H2");
        assertThat(foundProfessor.getEmailAddress())
                .isEqualTo("professor.h2@email.com");
        assertThat(foundProfessor.getStatus())
                .isEqualTo(Status.ACTIVE);

        // Campo específico de Professor
        assertThat(foundProfessor.getSalary())
                .isEqualByComparingTo("7500.00");
    }
}
