package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {

    List<BookingRequestDto> getAllBooking(String text, Long userId);

    List<BookingRequestDto> getBookingByOwner(String text, Long userId);

    BookingRequestDto getBookingById(long bookingId, long userId);

    BookingRequestDto createBooking(BookingRequestDto bookingDto, long userId);

    BookingRequestDto approveBooking(long userId, long bookingId, boolean approved);
}
