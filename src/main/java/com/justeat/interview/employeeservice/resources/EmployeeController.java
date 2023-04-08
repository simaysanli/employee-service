package com.justeat.interview.employeeservice.resources;

import com.justeat.interview.employeeservice.domain.model.Employee;
import com.justeat.interview.employeeservice.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/employee")
@Validated
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping
    @Valid
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    //TODO: add validation
    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @GetMapping(value = "{employeeId}")
    public Employee getEmployeeById(@PathVariable UUID employeeId) {
        return employeeService.getEmployeeById(employeeId);
    }

    @PutMapping(value = "{employeeId}")
    public Employee updateEmployee(@PathVariable UUID employeeId, @RequestBody Employee employee) {
        return employeeService.updateEmployeeById(employeeId, employee);
    }

    @DeleteMapping(value = "{employeeId}")
    public ResponseEntity<UUID> removeEmployeeById(@PathVariable UUID employeeId) {
        boolean isRemoved = employeeService.removeEmployeeById(employeeId);

        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(employeeId, HttpStatus.OK);
    }

}
