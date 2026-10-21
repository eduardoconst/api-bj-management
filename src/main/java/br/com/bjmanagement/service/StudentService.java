package br.com.bjmanagement.service;

import br.com.bjmanagement.exception.ResourceNotFoundException;
import br.com.bjmanagement.model.Student;
import br.com.bjmanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    //encapsulamento imutavel StudentRepository
    private final StudentRepository studentRepository;

    //Atribui studentRepository para a variavel studentRepository dentro da classe
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

 // create service
    public Student create(Student student) {
        return studentRepository.save(student);
    }

    //get all items
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    //get by id
    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found by ID: " + id));
    }

    //update all items
    public Student update(Long id, Student student) {
        Student existing = findById(id);

        existing.setName(student.getName());
        existing.setPhoneNumber(student.getPhoneNumber());
        existing.setEmailAddress(student.getEmailAddress());
        existing.setStatus(student.getStatus());
        existing.setStudentNumber(student.getStudentNumber());
        existing.setPhoto(student.getPhoto());

        return studentRepository.save(existing);
    }

    //update selected items
    public Student patch(Long id, Student student) {
        Student existing = findById(id);

        if (student.getName() != null) {
            existing.setName(student.getName());
        }
        if (student.getPhoneNumber() != null) {
            existing.setPhoneNumber(student.getPhoneNumber());
        }
        if (student.getEmailAddress() != null) {
            existing.setEmailAddress(student.getEmailAddress());
        }
        if (student.getStatus() != null) {
            existing.setStatus(student.getStatus());
        }
        if (student.getStudentNumber() != null) {
            existing.setStudentNumber(student.getStudentNumber());
        }
        if (student.getPhoto() != null) {
            existing.setPhoto(student.getPhoto());
        }

        return studentRepository.save(existing);
    }
    // Delete item
    public void delete(Long id) {
        findById(id);
        studentRepository.deleteById(id);
    }
}
