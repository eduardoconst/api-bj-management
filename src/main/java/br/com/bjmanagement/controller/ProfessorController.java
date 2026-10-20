
package br.com.bjmanagement.controller;

import br.com.bjmanagement.model.Professor;
import br.com.bjmanagement.service.ProfessorService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Professor create(@RequestBody Professor professor) {
        return professorService.create(professor);
    }

    @GetMapping
    public List<Professor> findAll() {
        return professorService.findAll();
    }

    @GetMapping("/{id}")
    public Professor findById(@PathVariable Long id) {
        return professorService.findById(id);
    }

    @PutMapping("/{id}")
    public Professor update(
            @PathVariable Long id,
            @RequestBody Professor professor) {
        return professorService.update(id, professor);
    }

    @PatchMapping("/{id}")
    public Professor patch(
            @PathVariable Long id,
            @RequestBody Professor professor) {
        return professorService.patch(id, professor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        professorService.delete(id);
    }
}
