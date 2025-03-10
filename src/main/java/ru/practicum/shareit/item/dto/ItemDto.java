package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@Builder
public class ItemDto {

    private long id;
    @NotBlank(message = "Название предмета должно быть указано")
    private String name;
    @NotBlank(message = "Описание предмета должно быть указано")
    private String description;
    @NotNull(message = "Поле доступности должно быть указано")
    private Boolean available;
    private Long request;
    private User owner;
}
