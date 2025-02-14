package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EmailAlreadyExistException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class UserRepositoryInMemoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> uniqueEmail = new HashSet<>();

    @Override
    public User getUserById(long id) {
        log.info("Найден пользователь с id {}", users.get(id));
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        log.info("Начало создания пользователя - {}", user);

        if (uniqueEmail.contains(user.getEmail())) {
            log.error("Указанный email уже существует, email={}", user.getEmail());
            throw new EmailAlreadyExistException("Указанный email уже существует.");
        }
        uniqueEmail.add(user.getEmail());

        if (user.getId()  == null) {
            user.setId(getNextId());
        }
        users.put(user.getId(), user);

        log.info("Конец создания пользователя - {}", user);
        log.info("users= {}", users.values());
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Начало обновления пользователя - {}", users.get(user.getId()));

        if (uniqueEmail.contains(user.getEmail())) {
            log.error("Указанный email уже существует, email={}", user.getEmail());
            throw new EmailAlreadyExistException("Указанный email уже существует.");
        }
        uniqueEmail.add(user.getEmail());

        User oldUser = users.get(user.getId());
        if (user.getEmail() == null) {
            user.setEmail(oldUser.getEmail());
        }
        if (user.getName() == null) {
            user.setName(oldUser.getName());
        }

        users.put(user.getId(), user);

        log.info("Конец обновления пользователя - {}", users.get(user.getId()));

        return users.get(user.getId());
    }

    @Override
    public void removeUser(long id) {
        users.remove(id);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
