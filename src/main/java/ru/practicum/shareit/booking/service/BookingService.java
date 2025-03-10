package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

public interface BookingService {

    Collection<BookingDto> getAllBooking(String text, Long userId);

    Collection<BookingDto> getBookingByOwner(String text, Long userId);

    BookingDto getBookingById(long bookingId, long userId);

    BookingDto createBooking(BookingDto bookingDto);

    BookingDto approveBooking(long userId, long bookingId, BookingStatus state);
}
