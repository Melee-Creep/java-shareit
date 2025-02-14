package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserRepository {

    User getUserById(long id);

    User createUser(User user);

    User updateUser(User user);

    void removeUser(long id);

}
