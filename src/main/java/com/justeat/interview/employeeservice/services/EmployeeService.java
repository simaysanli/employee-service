package com.justeat.interview.employeeservice.services;

import com.justeat.interview.employeeservice.domain.model.EmployeeActionsDto;
import com.justeat.interview.employeeservice.domain.model.EmployeeDto;
import com.justeat.interview.employeeservice.entity.Employee;
import com.justeat.interview.employeeservice.exception.DuplicateEmailException;
import com.justeat.interview.employeeservice.exception.EmployeeNotFoundException;
import com.justeat.interview.employeeservice.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KafkaTemplate<String, EmployeeActionsDto> kafkaTemplate;

    public List<EmployeeDto> getEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Employee addEmployee(EmployeeDto employeeDto) {
        Employee newEmployee = convertToEntity(employeeDto);
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

    public EmployeeDto getEmployeeById(String employeeId) {
        return convertToDto(employeeRepository.findById(String.valueOf(employeeId))
                .orElseThrow(() -> new EmployeeNotFoundException("No employee found with id: " + employeeId)));
    }

    public ResponseEntity<Employee> updateEmployeeById(String id, EmployeeDto employeeDto) {
        Employee updatedEmployee = convertToEntity(employeeDto);
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

    private Employee convertToEntity(EmployeeDto employeeDto) {
        return modelMapper.map(employeeDto, Employee.class);
    }

    private EmployeeDto convertToDto(Employee employee) {
        return modelMapper.map(employee, EmployeeDto.class);
    }
}
