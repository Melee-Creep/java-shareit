package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserRequestDto toUserDto(User user) {

        if (user == null) {
            return null;
        }
        return UserRequestDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserRequestDto userRequestDto) {

        if (userRequestDto == null) {
            return null;
        }
        return User.builder()
                .id(userRequestDto.getId())
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .build();
    }
}
