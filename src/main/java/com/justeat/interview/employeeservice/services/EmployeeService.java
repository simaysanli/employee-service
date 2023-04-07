package com.justeat.interview.employeeservice.services;

import com.justeat.interview.employeeservice.domain.model.Employee;
import com.justeat.interview.employeeservice.domain.model.Hobby;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {
    List<Employee> employees = List.of(Employee.builder()
            .id(UUID.randomUUID())
            .firstName("Simay")
            .lastName("Sanli")
            .birthday(LocalDate.of(1995,11,29))
            .email("sanli.simay@gmail.com")
            .hobbies(EnumSet.of(Hobby.SOCCER, Hobby.MUSIC))
            .build());

    public List<Employee> getEmployees() {
        return employees;
    }


}
