package br.com.bjmanagement.service;

import br.com.bjmanagement.model.Student;
import br.com.bjmanagement.model.Status;
import br.com.bjmanagement.repository.StudentRepository;
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
class StudentServiceTests {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void shouldCreateStudent() {
        Student student = createStudent("Ana Souza");

        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.create(student);

        assertEquals("Ana Souza", result.getName());
        assertEquals("41912345678", result.getPhoneNumber());
        assertEquals("ana@email.com", result.getEmailAddress());
        assertEquals(Status.ACTIVE, result.getStatus());
        assertEquals("2026001", result.getStudentNumber());
        assertEquals("ana.jpg", result.getPhoto());

        verify(studentRepository).save(student);
    }

    @Test
    void shouldFindAllStudents() {
        List<Student> students = List.of(
                createStudent("Ana Souza"),
                createStudent("Bruno Lima")
        );

        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.findAll();

        assertEquals(2, result.size());
        assertEquals("Ana Souza", result.get(0).getName());
        assertEquals("Bruno Lima", result.get(1).getName());

        verify(studentRepository).findAll();
    }

    @Test
    void shouldFindStudentById() {
        Student student = createStudent("Ana Souza");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        Student result = studentService.findById(1L);

        assertEquals("Ana Souza", result.getName());
        assertEquals("2026001", result.getStudentNumber());

        verify(studentRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenStudentDoesNotExist() {
        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> studentService.findById(99L)
        );
    }

    @Test
    void shouldUpdateStudent() {
        Student existing = createStudent("Ana Souza");

        Student updated = createStudent("Ana Silva");
        updated.setPhoneNumber("41999990000");
        updated.setEmailAddress("ana.silva@email.com");
        updated.setStatus(Status.ACTIVE);
        updated.setStudentNumber("2026002");
        updated.setPhoto("ana-silva.jpg");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(existing));
        when(studentRepository.save(existing)).thenReturn(existing);

        Student result = studentService.update(1L, updated);

        assertEquals("Ana Silva", result.getName());
        assertEquals("41999990000", result.getPhoneNumber());
        assertEquals("ana.silva@email.com", result.getEmailAddress());
        assertEquals(Status.ACTIVE, result.getStatus());
        assertEquals("2026002", result.getStudentNumber());
        assertEquals("ana-silva.jpg", result.getPhoto());

        verify(studentRepository).save(existing);
    }

    @Test
    void shouldPatchOnlyProvidedFields() {
        Student existing = createStudent("Ana Souza");
        existing.setStudentNumber("2026001");
        existing.setPhoto("ana.jpg");

        Student changes = new Student();
        changes.setName("Ana Silva");
        changes.setPhoto("ana-atualizada.jpg");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(existing));
        when(studentRepository.save(existing)).thenReturn(existing);

        Student result = studentService.patch(1L, changes);

        assertEquals("Ana Silva", result.getName());
        assertEquals("ana-atualizada.jpg", result.getPhoto());

        // Campos não enviados devem permanecer iguais.
        assertEquals("2026001", result.getStudentNumber());
        assertEquals("41912345678", result.getPhoneNumber());
        assertEquals("ana@email.com", result.getEmailAddress());

        verify(studentRepository).save(existing);
    }

    @Test
    void shouldDeleteStudent() {
        Student existing = createStudent("Ana Souza");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        studentService.delete(1L);

        verify(studentRepository).findById(1L);
        verify(studentRepository).deleteById(1L);
    }

    @Test
    void shouldNotUpdateStudentWhenIdDoesNotExist() {
        Student updated = createStudent("Ana Silva");

        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> studentService.update(99L, updated)
        );

        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void shouldNotPatchStudentWhenIdDoesNotExist() {
        Student changes = new Student();
        changes.setName("Ana Silva");

        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> studentService.patch(99L, changes)
        );

        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void shouldNotDeleteStudentWhenIdDoesNotExist() {
        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> studentService.delete(99L)
        );

        verify(studentRepository, never()).deleteById(anyLong());
    }

    private Student createStudent(String name) {
        Student student = new Student();
        student.setName(name);
        student.setPhoneNumber("41912345678");
        student.setEmailAddress("ana@email.com");
        student.setStatus(Status.ACTIVE);
        student.setStudentNumber("2026001");
        student.setPhoto("ana.jpg");
        return student;
    }
}
