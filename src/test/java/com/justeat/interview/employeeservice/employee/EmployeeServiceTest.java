package com.justeat.interview.employeeservice.employee;

import com.justeat.interview.employeeservice.domain.model.Hobby;
import com.justeat.interview.employeeservice.entity.Employee;
import com.justeat.interview.employeeservice.exception.EmployeeNotFoundException;
import com.justeat.interview.employeeservice.repository.EmployeeRepository;
import com.justeat.interview.employeeservice.services.EmployeeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    void shouldNotFoundEmployeeByIdIfNotExist() {
        String uuid = UUID.randomUUID().toString();
        Mockito.when(employeeRepository.findById(uuid)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> employeeService.getEmployeeById(uuid)).isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("No employee found with id: " + uuid);
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
