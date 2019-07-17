package ru.stoliarenkoas.tm.webserver.exception;

public class IncorrectDataException extends Exception {

    public IncorrectDataException() {
        super("incorrect input");
    }

    public IncorrectDataException(String message) {
        super(message);
    }

}
