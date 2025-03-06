package ru.practicum.exception;

public class IncorrectInputDataException extends RuntimeException {
    public IncorrectInputDataException(String message) {
        super(message);
    }
}