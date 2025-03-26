package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequestDto {

    private long id;
    @NotNull(message = "Запрос не может быть пустым")
    private String description;
    private long requestorId;
    private LocalDateTime created;
}
