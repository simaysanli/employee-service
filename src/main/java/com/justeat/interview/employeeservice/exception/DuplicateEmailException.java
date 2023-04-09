package com.justeat.interview.employeeservice.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String msg) {
        super(msg);
    }

}
