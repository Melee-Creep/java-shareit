package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentDto {

    private long id;

    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;

    private String authorName;

    private LocalDateTime created;


}
