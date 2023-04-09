package com.justeat.interview.employeeservice.repository;

import com.justeat.interview.employeeservice.domain.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    @Query("SELECT COUNT(e) > 0 FROM Employee e WHERE e.email = :email")
    boolean existsByEmail(@Param("email") String email);

}
