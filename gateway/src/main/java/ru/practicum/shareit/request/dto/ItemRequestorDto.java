package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemRequestDtos;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestorDto {

    private long id;
    private String description;
    private LocalDateTime created;
    private List<ItemRequestDtos> items;
}
