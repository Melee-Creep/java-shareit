package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    List<BookingDto> getAllBooking(String text, Long userId);

    List<BookingDto> getBookingByOwner(String text, Long userId);

    BookingDto getBookingById(long bookingId, long userId);

    BookingDto createBooking(BookingDto bookingDto, long userId);

    BookingDto approveBooking(long userId, long bookingId, boolean approved);
}
