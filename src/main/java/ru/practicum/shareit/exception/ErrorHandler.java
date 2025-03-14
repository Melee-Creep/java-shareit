package ru.practicum.shareit.exception;

import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictRequest(final EmailAlreadyExistException e) {
        return new ErrorResponse("Ошибка валидации данных", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundRequest(final NotFoundException e) {
        return new ErrorResponse("Ошибка валидации данных", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse validAnnotation(final MethodArgumentNotValidException e) {
        return new ErrorResponse("Ошибка валидации данных", e.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse notFoundRequestAnnotation(final MissingRequestHeaderException e) {
        return new ErrorResponse("Ошибка валидации данных", "Владелиц вещи должен быть указан");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validItemAnnotation(final UnexpectedTypeException e) {
        return new ErrorResponse("Ошибка валидации данных", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestBooking(final UnavailableBookingException e) {
        return new ErrorResponse("Ошибка валидации данных", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestNotOwner(final NotTheOwnerException e) {
        return new ErrorResponse("Ошибка валидации данных", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestIncompleteComment(final IncompleteCommentException e) {
        return new ErrorResponse("Ошибка валидации данных", e.getMessage());
    }
}
