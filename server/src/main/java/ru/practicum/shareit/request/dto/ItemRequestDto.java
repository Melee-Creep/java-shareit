package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequestDto {

    private long id;
    private String description;
    private long requestorId;
    private LocalDateTime created;
}
