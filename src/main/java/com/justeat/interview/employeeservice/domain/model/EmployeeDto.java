package com.justeat.interview.employeeservice.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.UUID;

//TODO: Implement with @Value
@Data
@Builder(toBuilder = true)
public class EmployeeDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    private String firstName;
    private String lastName;
    private EnumSet<Hobby> hobbies;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    private String email;
}
