
package br.com.bjmanagement.service;

import br.com.bjmanagement.exception.ResourceNotFoundException;
import br.com.bjmanagement.model.Professor;
import br.com.bjmanagement.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public Professor create(Professor professor) {
        return professorRepository.save(professor);
    }

    public List<Professor> findAll() {
        return professorRepository.findAll();
    }

    public Professor findById(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Professor not found by ID: " + id));
    }

    public Professor update(Long id, Professor professor) {
        Professor existing = findById(id);

        existing.setName(professor.getName());
        existing.setPhoneNumber(professor.getPhoneNumber());
        existing.setEmailAddress(professor.getEmailAddress());
        existing.setStatus(professor.getStatus());
        existing.setSalary(professor.getSalary());

        return professorRepository.save(existing);
    }

    public Professor patch(Long id, Professor professor) {
        Professor existing = findById(id);

        if (professor.getName() != null) {
            existing.setName(professor.getName());
        }
        if (professor.getPhoneNumber() != null) {
            existing.setPhoneNumber(professor.getPhoneNumber());
        }
        if (professor.getEmailAddress() != null) {
            existing.setEmailAddress(professor.getEmailAddress());
        }
        if (professor.getStatus() != null) {
            existing.setStatus(professor.getStatus());
        }
        if (professor.getSalary() != null) {
            existing.setSalary(professor.getSalary());
        }

        return professorRepository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        professorRepository.deleteById(id);
    }
}
