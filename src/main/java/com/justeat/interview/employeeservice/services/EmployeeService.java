package com.justeat.interview.employeeservice.services;

import com.justeat.interview.employeeservice.domain.model.Employee;
import com.justeat.interview.employeeservice.repository.EmployeeRepository;
import com.justeat.interview.employeeservice.services.exception.DuplicateEmailException;
import com.justeat.interview.employeeservice.services.exception.EmployeeNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional
    public Employee addEmployee(Employee newEmployee) {
        if (employeeRepository.existsByEmail(newEmployee.getEmail())) {
            throw new DuplicateEmailException("Email address already exists in database");
        }
        return employeeRepository.save(newEmployee);
    }

    public Employee getEmployeeById(String employeeId) {
        return employeeRepository.findById(String.valueOf(employeeId))
                .orElseThrow(() -> new EmployeeNotFoundException("No employee found with id: " + employeeId));
    }

    @Transactional
    public ResponseEntity<Employee> updateEmployeeById(String id, Employee updatedEmployee) {
        Employee employee = employeeRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new NoSuchElementException("No employee found with id: " + id));
        employee.setFirstName(Optional.ofNullable(updatedEmployee.getFirstName()).orElse(employee.getFirstName()));
        employee.setLastName(Optional.ofNullable(updatedEmployee.getLastName()).orElse(employee.getLastName()));
        employee.setBirthday(Optional.ofNullable(updatedEmployee.getBirthday()).orElse(employee.getBirthday()));
        employee.setEmail(Optional.ofNullable(updatedEmployee.getEmail()).orElse(employee.getEmail()));
        employee.setHobbies(Optional.ofNullable(updatedEmployee.getHobbies()).orElse(employee.getHobbies()));
        return ResponseEntity.ok(employeeRepository.save(employee));
    }

    public ResponseEntity<Void> removeEmployeeById(String employeeId) {
        Employee employee = employeeRepository.findById(String.valueOf(employeeId)).orElseThrow(
                () -> new EmployeeNotFoundException("Employee not found" + employeeId));
        employeeRepository.delete(employee);
        return ResponseEntity.ok().build();
    }

}
