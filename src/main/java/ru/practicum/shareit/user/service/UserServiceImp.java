package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

   private final UserRepository userRepository;
   private final Set<String> uniqueEmail = new HashSet<>();

    @Override
    public UserDto getUserById(long id) {
       User user = userRepository.findById(id)
               .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
       return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        log.info("Начало создания пользователя - {}", userDto);

        if (uniqueEmail.contains(userDto.getEmail())) {
            log.error("Указанный email уже существует, email={}", userDto.getEmail());
            throw new EmailAlreadyExistException("Указанный email уже существует.");
        }
        uniqueEmail.add(userDto.getEmail());
        User user = UserMapper.toUser(userDto);

        log.info("Конец создания пользователя - {}", user);
        return UserMapper.toUserDto(userRepository.save(user));
    }

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
}
