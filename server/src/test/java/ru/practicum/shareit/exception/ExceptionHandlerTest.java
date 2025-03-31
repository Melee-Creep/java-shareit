package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExceptionHandlerTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void handleNotFoundException_404() {
        NotFoundException notFoundException = new NotFoundException("Объект не найден");

        ErrorResponse errorResponse = errorHandler.notFoundRequest(notFoundException);

        assertEquals("Объект не найден", errorResponse.getDescription());
    }

    @Test
    void badRequestIncompleteComment() {
        IncompleteCommentException incompleteCommentException = new IncompleteCommentException("комментарий");

        ErrorResponse errorResponse = errorHandler.badRequestIncompleteComment(incompleteCommentException);

        assertEquals("комментарий", errorResponse.getDescription());
    }

    @Test
    void badRequestNotOwner() {
        NotTheOwnerException notTheOwnerException = new NotTheOwnerException("владелиц");

        ErrorResponse errorResponse = errorHandler.badRequestNotOwner(notTheOwnerException);

        assertEquals("владелиц", errorResponse.getDescription());
    }

    @Test
    void badRequestBooking() {
        UnavailableBookingException unavailableBookingException = new UnavailableBookingException("бронь");

        ErrorResponse errorResponse = errorHandler.badRequestBooking(unavailableBookingException);

        assertEquals("бронь", errorResponse.getDescription());
    }

    @Test
    void handleConflictRequest() {
        EmailAlreadyExistException emailAlreadyExistException = new EmailAlreadyExistException("дубль");

        ErrorResponse errorResponse = errorHandler.handleConflictRequest(emailAlreadyExistException);

        assertEquals("дубль", errorResponse.getDescription());
    }
}
