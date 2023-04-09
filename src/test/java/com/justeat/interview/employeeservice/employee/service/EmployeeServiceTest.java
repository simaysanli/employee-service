package com.justeat.interview.employeeservice.employee.service;

import com.justeat.interview.employeeservice.domain.model.Hobby;
import com.justeat.interview.employeeservice.entity.Employee;
import com.justeat.interview.employeeservice.exception.DuplicateEmailException;
import com.justeat.interview.employeeservice.exception.EmployeeNotFoundException;
import com.justeat.interview.employeeservice.repository.EmployeeRepository;
import com.justeat.interview.employeeservice.services.EmployeeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void shouldReturnEmptyListIfNoEmployeeExists() {
        Mockito.when(employeeRepository.findAll()).thenReturn(Collections.emptyList());
        Assertions.assertThat(employeeService.getEmployees().isEmpty()).isTrue();
    }

    @Test
    void shouldReturnEmployeeIfExists() {
        Employee newEmployee = createEmployee();
        Mockito.when(employeeRepository.findAll()).thenReturn(Collections.singletonList(newEmployee));
        employeeService.addEmployee(newEmployee);
        Assertions.assertThat(Collections.singletonList(newEmployee)).isEqualTo(employeeService.getEmployees());
    }

    @Test
    void shouldNotFoundEmployeeByIdIfNotExist() {
        String uuid = UUID.randomUUID().toString();
        Mockito.when(employeeRepository.findById(uuid)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> employeeService.getEmployeeById(uuid)).isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("No employee found with id: " + uuid);
    }

    @Test
    void shouldReturnEmployeeByIdIfExists() {
        String id = UUID.randomUUID().toString();
        Employee newEmployee = createEmployee();
        newEmployee.setEmployeeId(id);
        Mockito.when(employeeRepository.findById(id)).thenReturn(Optional.of(newEmployee));
        Employee existingEmployee = employeeService.getEmployeeById(id);
        Assertions.assertThat(newEmployee.getEmployeeId()).isEqualTo(existingEmployee.getEmployeeId());
        Assertions.assertThat(newEmployee.getFirstName()).isEqualTo(existingEmployee.getFirstName());
        Assertions.assertThat(newEmployee.getLastName()).isEqualTo(existingEmployee.getLastName());
        Assertions.assertThat(newEmployee.getBirthday()).isEqualTo(existingEmployee.getBirthday());
        Assertions.assertThat(newEmployee.getEmail()).isEqualTo(existingEmployee.getEmail());
        Assertions.assertThat(newEmployee.getHobbies()).isEqualTo(existingEmployee.getHobbies());
    }

    @Test
    void shouldCreateEmployee() {
        Employee newEmployee = createEmployee();
        Mockito.when(employeeRepository.save(ArgumentMatchers.any(Employee.class))).thenReturn(newEmployee);
        Employee employee = employeeService.addEmployee(newEmployee);
        Assertions.assertThat(employee).usingRecursiveComparison().isEqualTo(newEmployee);
        Mockito.verify(employeeRepository, Mockito.times(1)).save(ArgumentMatchers.any(Employee.class));
    }

    @Test
    void shouldUpdateEmployeeIfExist() {
        Employee newEmployee = createEmployee();
        Employee updatedEmployee = createEmployee();
        updatedEmployee.setEmployeeId(newEmployee.getEmployeeId());
        updatedEmployee.setFirstName("John");
        updatedEmployee.setLastName("Wick");
        Mockito.when(employeeRepository.findById(newEmployee.getEmployeeId())).thenReturn(Optional.of(newEmployee));
        ResponseEntity<Employee> response = employeeService.updateEmployeeById(newEmployee.getEmployeeId(), updatedEmployee);
        Assertions.assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
        Assertions.assertThat(newEmployee.getFirstName()).isEqualTo(updatedEmployee.getFirstName());
        Assertions.assertThat(newEmployee.getLastName()).isEqualTo(updatedEmployee.getLastName());
    }

    @Test
    void shouldThrowErrorUpdateEmployeeIfNotExist() {
        Employee newEmployee = createEmployee();
        Employee updatedEmployee = createEmployee();
        updatedEmployee.setEmployeeId(newEmployee.getEmployeeId());
        updatedEmployee.setFirstName("John");
        updatedEmployee.setLastName("Wick");
        Mockito.when(employeeRepository.findById(newEmployee.getEmployeeId())).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> employeeService.updateEmployeeById(newEmployee.getEmployeeId(), updatedEmployee)).isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("No employee found with id: " + newEmployee.getEmployeeId());

    }

    @Test
    void shouldThrowErrorAddEmployeeEmailIfSameEmailExists() {
        Employee savedEmployee = createEmployee();
        Mockito.when(employeeRepository.existsByEmail(savedEmployee.getEmail())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> employeeService.addEmployee(savedEmployee)).isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("Email address already exists in database");

    }

    @Test
    void shouldDeleteEmployeeByIdIfExists() {
        Employee savedEmployee = createEmployee();
        Mockito.when(employeeRepository.findById(savedEmployee.getEmployeeId())).thenReturn(Optional.of(savedEmployee));

        ResponseEntity<Void> response = employeeService.removeEmployeeById(savedEmployee.getEmployeeId());
        Mockito.verify(employeeRepository).findById(savedEmployee.getEmployeeId());
        Mockito.verify(employeeRepository).delete(savedEmployee);
        Assertions.assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    }

    @Test
    void shouldThrowErrorForDeleteEmployeeByIdIfNotExist() {
        Employee savedEmployee = createEmployee();
        Mockito.when(employeeRepository.findById(savedEmployee.getEmployeeId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> employeeService.removeEmployeeById(savedEmployee.getEmployeeId())).isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("No employee found with id: " + savedEmployee.getEmployeeId());
    }

    private Employee createEmployee() {
        Employee newEmployee = Employee.builder()
                .employeeId(String.valueOf(UUID.randomUUID()))
                .firstName("Simay")
                .lastName("Sanli")
                .email("sanli.simay@gmail.com")
                .birthday(LocalDate.of(1995, 11, 29))
                .hobbies(Set.of(Hobby.SOCCER, Hobby.MUSIC))
                .build();
        return newEmployee;
    }


}
