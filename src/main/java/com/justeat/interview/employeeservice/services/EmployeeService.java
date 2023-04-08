package com.justeat.interview.employeeservice.services;

import com.justeat.interview.employeeservice.domain.model.Employee;
import com.justeat.interview.employeeservice.domain.model.Hobby;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class EmployeeService {
    List<Employee> employees = new ArrayList<>();
    Employee employee = Employee.builder()
            .id(UUID.randomUUID())
            .firstName("Simay")
            .lastName("Sanli")
            .birthday(LocalDate.of(1995,11,29))
            .email("sanli.simay@gmail.com")
            .hobbies(EnumSet.of(Hobby.SOCCER, Hobby.MUSIC))
            .build();

    public List<Employee> getEmployees() {
        employees.add(employee);
        return employees;
    }

    public ResponseEntity<Employee> addEmployee(Employee employee) {
        employee = employee.toBuilder().id(UUID.randomUUID()).build();
        employees.add(employee);
        return ResponseEntity.ok(employee);
    }

    //TODO: write own exception
    public Employee getEmployeeById(UUID id) {
        return employees.stream()
                .filter(emp -> emp.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No employee found with id: " + id));
    }

    public Employee updateEmployeeById(UUID id, Employee updatedEmployee) {
        for(Employee emp : employees) {
            if(emp.getId().equals(id)) {
                emp.setFirstName(Optional.ofNullable(updatedEmployee.getFirstName()).orElse(emp.getFirstName()));
                emp.setLastName(Optional.ofNullable(updatedEmployee.getLastName()).orElse(emp.getLastName()));
                emp.setBirthday(Optional.ofNullable(updatedEmployee.getBirthday()).orElse(emp.getBirthday()));
                emp.setEmail(Optional.ofNullable(updatedEmployee.getEmail()).orElse(emp.getEmail()));
                emp.setHobbies(Optional.ofNullable(updatedEmployee.getHobbies()).orElse(emp.getHobbies()));
            }
        }
        return getEmployeeById(id);
    }

    public boolean removeEmployeeById(UUID id) {
        return employees.removeIf(emp -> emp.getId().equals(id));
    }

}
