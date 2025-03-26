package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Set<String> uniqueEmail = new HashSet<>();

    @Override
    public UserRequestDto getUserById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserRequestDto createUser(UserRequestDto userRequestDto) {
        log.info("Начало создания пользователя - {}", userRequestDto);

        if (uniqueEmail.contains(userRequestDto.getEmail())) {
            log.error("Указанный email уже существует, email={}", userRequestDto.getEmail());
            throw new EmailAlreadyExistException("Указанный email уже существует.");
        }
        uniqueEmail.add(userRequestDto.getEmail());
        User user = UserMapper.toUser(userRequestDto);
        log.info("Конец создания пользователя - {}", userRequestDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public User updateUser(User user) {

        if (uniqueEmail.contains(user.getEmail())) {
            log.error("Указанный email уже существует, email={}", user.getEmail());
            throw new EmailAlreadyExistException("Указанный email уже существует.");
        }
        uniqueEmail.add(user.getEmail());
        User oldUser = UserMapper.toUser(getUserById(user.getId()));
        User updateUser = User.builder()
                .id(user.getId())
                .name(user.getName() != null ? user.getName() : oldUser.getName())
                .email(user.getEmail() != null ? user.getEmail() : oldUser.getEmail())
                .build();
        return userRepository.save(updateUser);
    }

    @Override
    public void removeUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean isExist(long userId) {
        return userRepository.existsById(userId);
    }
}
