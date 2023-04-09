package com.justeat.interview.employeeservice.resources;

import com.justeat.interview.employeeservice.domain.model.EmployeeDto;
import com.justeat.interview.employeeservice.entity.Employee;
import com.justeat.interview.employeeservice.services.EmployeeService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/v1/employee")
@Validated
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @Valid
    public List<EmployeeDto> getEmployees() {
        List<Employee> employees = employeeService.getEmployees();
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public Employee addEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        Employee employee = convertToEntity(employeeDto);
        return employeeService.addEmployee(employee);
    }

    @GetMapping(value = "{employeeId}")
    public EmployeeDto getEmployeeById(@PathVariable String employeeId) {
        return convertToDto(employeeService.getEmployeeById(employeeId));
    }

    @PutMapping(value = "{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable UUID employeeId, @RequestBody EmployeeDto employeeDto) {
        Employee updatedEmployee = convertToEntity(employeeDto);
        return employeeService.updateEmployeeById(String.valueOf(employeeId), updatedEmployee);
    }

    @DeleteMapping(value = "{employeeId}")
    public ResponseEntity<Void> removeEmployeeById(@PathVariable UUID employeeId) {
        return employeeService.removeEmployeeById(String.valueOf(employeeId));
    }

    private Employee convertToEntity(EmployeeDto employeeDto) {
        return modelMapper.map(employeeDto, Employee.class);
    }

    private EmployeeDto convertToDto(Employee employee) {
        return modelMapper.map(employee, EmployeeDto.class);
    }

}
