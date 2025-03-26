package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class CommentRequestDto {

    private long id;
    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;
    private String authorName;
    private LocalDateTime created;

}
