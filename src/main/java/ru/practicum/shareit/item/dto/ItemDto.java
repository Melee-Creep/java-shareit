package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {

    private long id;
    @NotBlank(message = "Название предмета должно быть указано")
    private String name;
    @NotBlank(message = "Описание предмета должно быть указано")
    private String description;
    @NotNull(message = "Поле доступности должно быть указано")
    private Boolean available;
    private ItemRequest request;
    private UserDto owner;
}
