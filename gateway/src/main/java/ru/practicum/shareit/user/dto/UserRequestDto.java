package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserRequestDto {

    private long id;
    private String name;
    @Email(message = "Почта должна содержать сивол @")
    @NotBlank(message = "Email не может быть пустым")
    private String email;
}
