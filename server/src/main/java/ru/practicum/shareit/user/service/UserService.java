package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {

    UserRequestDto getUserById(long id);

    UserRequestDto createUser(UserRequestDto userRequestDto);

    User updateUser(User user);

    void removeUser(long id);

    boolean isExist(long userId);
}
