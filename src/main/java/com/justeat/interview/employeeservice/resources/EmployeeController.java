package com.justeat.interview.employeeservice.resources;

import com.justeat.interview.employeeservice.domain.model.Employee;
import com.justeat.interview.employeeservice.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
