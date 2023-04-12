package com.justeat.interview.employeeservice.resources;

import com.justeat.interview.employeeservice.domain.model.EmployeeDto;
import com.justeat.interview.employeeservice.entity.Employee;
import com.justeat.interview.employeeservice.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    private EmployeeService employeeService;
    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Get all employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all employees",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDto.class))}),
            @ApiResponse(responseCode = "404", description = "Employees not found",
                    content = @Content)})
    @GetMapping
    @Valid
    @SecurityRequirement(name = "Bearer Authentication")
    public List<EmployeeDto> getEmployees() {
        List<Employee> employees = employeeService.getEmployees();
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Create an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDto.class))}),
            @ApiResponse(responseCode = "500", description = "Employee not created",
                    content = @Content)})
    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public Employee addEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        employeeDto.setId(UUID.randomUUID());
        Employee employee = convertToEntity(employeeDto);
        return employeeService.addEmployee(employee);
    }

    @Operation(summary = "Get an employee by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found an employee",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content)})
    @GetMapping(value = "{employeeId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public EmployeeDto getEmployeeById(@PathVariable String employeeId) {
        return convertToDto(employeeService.getEmployeeById(employeeId));
    }

    @Operation(summary = "Update an existing employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content)})
    @PutMapping(value = "{employeeId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String employeeId, @Valid @RequestBody EmployeeDto employeeDto) {
        Employee updatedEmployee = convertToEntity(employeeDto);
        return employeeService.updateEmployeeById(String.valueOf(employeeId), updatedEmployee);
    }

    @Operation(summary = "Delete an existing employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content)})
    @DeleteMapping(value = "{employeeId}")
    @SecurityRequirement(name = "Bearer Authentication")
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
