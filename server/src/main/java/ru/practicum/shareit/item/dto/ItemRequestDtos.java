package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserRequestDto;

@Builder
@Data
public class ItemRequestDtos {

    private long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private UserRequestDto owner;
}