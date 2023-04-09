package com.justeat.interview.employeeservice.resources;

import com.justeat.interview.employeeservice.domain.model.EmployeeDto;
import com.justeat.interview.employeeservice.domain.model.Employee;
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
        Employee employee = modelMapper.map(employeeDto, Employee.class);

        employee.setEmployeeId(String.valueOf(employeeDto.getId()));
        employee.setBirthday(employeeDto.getBirthday());
        employee.setEmail(employeeDto.getEmail());
        employee.setHobbies(employeeDto.getHobbies());
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());

        return employee;
    }

    private EmployeeDto convertToDto(Employee employee) {
        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);

        employeeDto.setId(UUID.fromString(employee.getEmployeeId()));
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setHobbies(employee.getHobbies());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setBirthday(employee.getBirthday());

        return employeeDto;
    }


}
