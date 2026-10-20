package br.com.bjmanagement.model;

import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class Professor extends Person {

    private BigDecimal salary;

    public Professor() {
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}