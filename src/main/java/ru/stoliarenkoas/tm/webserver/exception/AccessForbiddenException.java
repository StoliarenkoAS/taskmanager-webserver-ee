package ru.stoliarenkoas.tm.webserver.exception;

public class AccessForbiddenException extends RuntimeException {

    public AccessForbiddenException() {
        super("access forbidden");
    }

    public AccessForbiddenException(String message) {
        super(message);
    }

}
