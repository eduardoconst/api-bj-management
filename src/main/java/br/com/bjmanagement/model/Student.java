package br.com.bjmanagement.model;

import jakarta.persistence.Entity;

@Entity
public class Student extends Person {

    private String studentNumber;

    private String photo;

    public Student() {
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}