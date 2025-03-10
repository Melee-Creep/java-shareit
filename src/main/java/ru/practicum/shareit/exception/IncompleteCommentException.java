package ru.practicum.shareit.exception;

public class IncompleteCommentException extends RuntimeException {
    public IncompleteCommentException(String message) {
        super(message);
    }
}
