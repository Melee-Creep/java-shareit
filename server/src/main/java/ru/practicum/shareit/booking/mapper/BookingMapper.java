package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {

    public static BookingRequestDto toBookingDto(Booking booking) {

        if (booking == null) {
            return null;
        }
        return BookingRequestDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .itemId(booking.getItem().getId())
                .item(booking.getItem())
                .build();
    }

    public static Booking toBooking(BookingRequestDto bookingDto) {

        if (bookingDto == null) {
            return null;
        }
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .booker(bookingDto.getBooker())
                .item(bookingDto.getItem())
                .status(bookingDto.getStatus())
                .build();
    }
}
