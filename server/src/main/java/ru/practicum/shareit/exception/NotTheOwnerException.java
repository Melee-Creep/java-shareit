package ru.practicum.shareit.exception;

public class NotTheOwnerException extends RuntimeException {
    public NotTheOwnerException(String message) {
        super(message);
    }
}
