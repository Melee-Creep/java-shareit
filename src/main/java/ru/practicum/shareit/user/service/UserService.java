package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {

    UserDto getUserById(long id);

    UserDto createUser(UserDto userDto);

    User updateUser(User user);

    void removeUser(long id);
}
