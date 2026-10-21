
package br.com.bjmanagement.repository;

import br.com.bjmanagement.model.Status;
import br.com.bjmanagement.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentRepositoryIntegrationTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void shouldSaveAndRetrieveStudentFromH2() {
        Student student = new Student();
        student.setName("Estudante Teste H2");
        student.setPhoneNumber("41988887777");
        student.setEmailAddress("estudante.h2@email.com");
        student.setStatus(Status.ACTIVE);
        student.setStudentNumber("STU-H2-001");
        student.setPhoto("estudante.jpg");

        Student savedStudent = studentRepository.saveAndFlush(student);

        Optional<Student> result =
                studentRepository.findById(savedStudent.getId());

        assertThat(savedStudent.getId()).isNotNull();
        assertThat(result).isPresent();

        Student foundStudent = result.orElseThrow();

        // Campos herdados de Person
        assertThat(foundStudent.getName())
                .isEqualTo("Estudante Teste H2");
        assertThat(foundStudent.getEmailAddress())
                .isEqualTo("estudante.h2@email.com");
        assertThat(foundStudent.getStatus())
                .isEqualTo(Status.ACTIVE);

        // Campos específicos de Student
        assertThat(foundStudent.getStudentNumber())
                .isEqualTo("STU-H2-001");
        assertThat(foundStudent.getPhoto())
                .isEqualTo("estudante.jpg");
    }
}
