package com.justeat.interview.employeeservice.services.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String msg) {
        super(msg);
    }

}
