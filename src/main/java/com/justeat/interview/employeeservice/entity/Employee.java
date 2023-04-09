package com.justeat.interview.employeeservice.entity;

import com.justeat.interview.employeeservice.domain.model.Hobby;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String employeeId;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "hobbies")
    @Enumerated(EnumType.STRING)
    private Set<Hobby> hobbies;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "email", unique = true)
    private String email;

}

