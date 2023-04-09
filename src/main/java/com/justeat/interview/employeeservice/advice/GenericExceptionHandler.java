package com.justeat.interview.employeeservice.advice;

import com.justeat.interview.employeeservice.exception.DuplicateEmailException;
import com.justeat.interview.employeeservice.exception.EmployeeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    ProblemDetail handleDuplicateEmailException(DuplicateEmailException e) {
        //TODO:check status code for duplicate
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setDetail("Email address already exists.");
        return problemDetail;
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    ProblemDetail handleEmployeeNotFoundException(EmployeeNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setDetail("Employee not found.");
        return problemDetail;
    }
}
