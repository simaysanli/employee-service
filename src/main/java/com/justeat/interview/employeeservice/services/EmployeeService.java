package com.justeat.interview.employeeservice.services;

import com.justeat.interview.employeeservice.domain.model.EmployeeActionsDto;
import com.justeat.interview.employeeservice.entity.Employee;
import com.justeat.interview.employeeservice.exception.DuplicateEmailException;
import com.justeat.interview.employeeservice.exception.EmployeeNotFoundException;
import com.justeat.interview.employeeservice.repository.EmployeeRepository;
import com.justeat.interview.employeeservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KafkaTemplate<String, EmployeeActionsDto> kafkaTemplate;

    public List<Employee> getEmployees() {
        //kafkaTemplate.send("employee-actions", "UUID", new EmployeeActionsDto("Simay", "Sanli"));
        return employeeRepository.findAll();
    }

    public Employee addEmployee(Employee newEmployee) {
        if (employeeRepository.existsByEmail(newEmployee.getEmail())) {
            throw new DuplicateEmailException("Email address already exists in database");
        }
        EmployeeActionsDto employeeAction = new EmployeeActionsDto();
        employeeAction.setId(newEmployee.getEmployeeId());
        employeeAction.setFirstName(newEmployee.getFirstName());
        employeeAction.setLastName(newEmployee.getLastName());
        employeeAction.setEmail(newEmployee.getEmail());
        kafkaTemplate.send("employee-actions", employeeAction);
        return employeeRepository.save(newEmployee);
    }

    public Employee getEmployeeById(String employeeId) {
        return employeeRepository.findById(String.valueOf(employeeId))
                .orElseThrow(() -> new EmployeeNotFoundException("No employee found with id: " + employeeId));
    }

    public ResponseEntity<Employee> updateEmployeeById(String id, Employee updatedEmployee) {
        Employee employee = employeeRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new EmployeeNotFoundException("No employee found with id: " + id));
        if (employeeRepository.existsByEmail(updatedEmployee.getEmail()) && (!updatedEmployee.getEmail().equals(employee.getEmail()))) {
            throw new DuplicateEmailException("Email address already exists in database");
        }
        employee.setFirstName(Optional.ofNullable(updatedEmployee.getFirstName()).orElse(employee.getFirstName()));
        employee.setLastName(Optional.ofNullable(updatedEmployee.getLastName()).orElse(employee.getLastName()));
        employee.setBirthday(Optional.ofNullable(updatedEmployee.getBirthday()).orElse(employee.getBirthday()));
        employee.setEmail(Optional.ofNullable(updatedEmployee.getEmail()).orElse(employee.getEmail()));
        employee.setHobbies(Optional.ofNullable(updatedEmployee.getHobbies()).orElse(employee.getHobbies()));

        EmployeeActionsDto employeeAction = new EmployeeActionsDto();
        employeeAction.setId(employee.getEmployeeId());
        employeeAction.setFirstName(employee.getFirstName());
        employeeAction.setLastName(employee.getLastName());
        employeeAction.setEmail(employee.getEmail());

        kafkaTemplate.send("employee-actions", employeeAction);
        return ResponseEntity.ok(employeeRepository.save(employee));
    }

    public ResponseEntity<Void> removeEmployeeById(String employeeId) {
        Employee employee = employeeRepository.findById(String.valueOf(employeeId)).orElseThrow(
                () -> new EmployeeNotFoundException("No employee found with id: " + employeeId));
        employeeRepository.delete(employee);
        return ResponseEntity.ok().build();
    }
}
