package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.service.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class BookingResponseComment {

    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private User booker;
}
