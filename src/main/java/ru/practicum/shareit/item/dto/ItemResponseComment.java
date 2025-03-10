package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingResponseComment;

import java.util.Collection;

@Getter
@Setter
@Builder
public class ItemResponseComment {

    private long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingResponseComment lastBooking;
    private BookingResponseComment nextBooking;
    private Collection<CommentDto> comments;
}
