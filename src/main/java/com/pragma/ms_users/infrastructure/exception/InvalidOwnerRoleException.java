package com.pragma.ms_users.infrastructure.exception;

public class InvalidOwnerRoleException extends RuntimeException {
    public InvalidOwnerRoleException(String message) {
        super(message);
    }
}
