package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.util.Collection;

@Getter
@Setter
@Builder
public class ItemResponseComment {

    private long id;
    private String name;
    private String description;
    private Boolean available;
    private BookItemRequestDto lastBooking;
    private BookItemRequestDto nextBooking;
    private Collection<CommentRequestDto> comments;
}
