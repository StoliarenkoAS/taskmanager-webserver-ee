package ru.stoliarenkoas.tm.webserver.exception;

public class IncorrectDataException extends Exception {

    public IncorrectDataException() {
        super("incorrect incoming data");
    }

    public IncorrectDataException(String message) {
        super(message);
    }

}
