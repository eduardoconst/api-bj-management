
package br.com.bjmanagement.service;

import br.com.bjmanagement.model.Professor;
import br.com.bjmanagement.model.Status;
import br.com.bjmanagement.repository.ProfessorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceTests {

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private ProfessorService professorService;

    @Test
    void shouldCreateProfessor() {
        Professor professor = createProfessor("Maria Oliveira");

        when(professorRepository.save(professor)).thenReturn(professor);

        Professor result = professorService.create(professor);

        assertEquals("Maria Oliveira", result.getName());
        assertEquals("41912345678", result.getPhoneNumber());
        assertEquals("maria@email.com", result.getEmailAddress());
        assertEquals(Status.ACTIVE, result.getStatus());
        assertEquals(0, new BigDecimal("8500.00").compareTo(result.getSalary()));

        verify(professorRepository).save(professor);
    }

    @Test
    void shouldFindAllProfessors() {
        List<Professor> professors = List.of(
                createProfessor("Maria Oliveira"),
                createProfessor("Carlos Santos")
        );

        when(professorRepository.findAll()).thenReturn(professors);

        List<Professor> result = professorService.findAll();

        assertEquals(2, result.size());
        assertEquals("Maria Oliveira", result.get(0).getName());
        assertEquals("Carlos Santos", result.get(1).getName());

        verify(professorRepository).findAll();
    }

    @Test
    void shouldFindProfessorById() {
        Professor professor = createProfessor("Maria Oliveira");

        when(professorRepository.findById(1L))
                .thenReturn(Optional.of(professor));

        Professor result = professorService.findById(1L);

        assertEquals("Maria Oliveira", result.getName());
        assertEquals(0, new BigDecimal("8500.00").compareTo(result.getSalary()));

        verify(professorRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenProfessorDoesNotExist() {
        when(professorRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> professorService.findById(99L)
        );
    }

    @Test
    void shouldUpdateProfessor() {
        Professor existing = createProfessor("Maria Oliveira");

        Professor updated = createProfessor("Maria Silva");
        updated.setPhoneNumber("41999990000");
        updated.setEmailAddress("maria.silva@email.com");
        updated.setStatus(Status.ACTIVE);
        updated.setSalary(new BigDecimal("9500.00"));

        when(professorRepository.findById(1L))
                .thenReturn(Optional.of(existing));
        when(professorRepository.save(existing)).thenReturn(existing);

        Professor result = professorService.update(1L, updated);

        assertEquals("Maria Silva", result.getName());
        assertEquals("41999990000", result.getPhoneNumber());
        assertEquals("maria.silva@email.com", result.getEmailAddress());
        assertEquals(Status.ACTIVE, result.getStatus());
        assertEquals(0, new BigDecimal("9500.00").compareTo(result.getSalary()));

        verify(professorRepository).save(existing);
    }

    @Test
    void shouldPatchOnlyProvidedFields() {
        Professor existing = createProfessor("Maria Oliveira");

        Professor changes = new Professor();
        changes.setName("Maria Silva");
        changes.setSalary(new BigDecimal("9000.00"));

        when(professorRepository.findById(1L))
                .thenReturn(Optional.of(existing));
        when(professorRepository.save(existing)).thenReturn(existing);

        Professor result = professorService.patch(1L, changes);

        assertEquals("Maria Silva", result.getName());
        assertEquals(0, new BigDecimal("9000.00").compareTo(result.getSalary()));

        // Os campos que não foram enviados devem permanecer iguais.
        assertEquals("41912345678", result.getPhoneNumber());
        assertEquals("maria@email.com", result.getEmailAddress());
        assertEquals(Status.ACTIVE, result.getStatus());

        verify(professorRepository).save(existing);
    }

    @Test
    void shouldDeleteProfessor() {
        Professor existing = createProfessor("Maria Oliveira");

        when(professorRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        professorService.delete(1L);

        verify(professorRepository).findById(1L);
        verify(professorRepository).deleteById(1L);
    }

    @Test
    void shouldNotUpdateProfessorWhenIdDoesNotExist() {
        Professor updated = createProfessor("Maria Silva");

        when(professorRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> professorService.update(99L, updated)
        );

        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void shouldNotPatchProfessorWhenIdDoesNotExist() {
        Professor changes = new Professor();
        changes.setName("Maria Silva");

        when(professorRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> professorService.patch(99L, changes)
        );

        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void shouldNotDeleteProfessorWhenIdDoesNotExist() {
        when(professorRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> professorService.delete(99L)
        );

        verify(professorRepository, never()).deleteById(anyLong());
    }

    private Professor createProfessor(String name) {
        Professor professor = new Professor();
        professor.setName(name);
        professor.setPhoneNumber("41912345678");
        professor.setEmailAddress("maria@email.com");
        professor.setStatus(Status.ACTIVE);
        professor.setSalary(new BigDecimal("8500.00"));
        return professor;
    }
}
