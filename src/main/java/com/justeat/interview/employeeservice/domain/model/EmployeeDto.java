package com.justeat.interview.employeeservice.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

//TODO: Implement with @Value
@Data
@NoArgsConstructor
public class EmployeeDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotEmpty
    @Size(min = 2, message = "employee first name should have at least 2 characters")
    private String firstName;

    @NotEmpty
    @Size(min = 2, message = "employee last name should have at least 2 characters")
    private String lastName;

    private Set<Hobby> hobbies;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(type = "string", pattern = "yyyy-MM-dd", example = "2023-04-10")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotEmpty
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    private String email;
}
