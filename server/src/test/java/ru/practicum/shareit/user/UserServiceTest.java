package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.EmailAlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void createTestUser() {
        user = User.builder()
                .id(1L)
                .name("test")
                .email("test@gmail.com")
                .build();
        userRepository.save(user);
    }

    @Test
    void getUserById() {
        UserRequestDto testUser = userService.getUserById(user.getId());

        assertThat(testUser, notNullValue());
        assertThat(testUser.getEmail(), is(user.getEmail()));
        assertThat(testUser.getName(), is(user.getName()));
        assertThat(testUser.getId(), is(user.getId()));
    }

    @Test
    void getUserById_NotFoundException() {
        assertThrows(NotFoundException.class, () -> userService.getUserById(1000000L));
    }

    @Test
    void createUser() {
        UserRequestDto createUser = UserRequestDto.builder()
                .name("test")
                .email("test@mail.com")
                .build();

        UserRequestDto result = userService.createUser(createUser);

        assertThat(result, notNullValue());
        assertThat(result.getEmail(), is(createUser.getEmail()));
        assertThat(result.getName(), is(createUser.getName()));
    }

    @Test
    void createUser_EmailAlreadyExistException() {
        UserRequestDto createUser = UserRequestDto.builder()
                .name("test")
                .email("test@gmail.com")
                .build();
        userService.createUser(UserMapper.toUserDto(user));
        assertThrows(EmailAlreadyExistException.class, () -> userService.createUser(createUser));
    }

    @Test
    void updateUser() {
        UserRequestDto updateUser = UserRequestDto.builder()
                .id(1L)
                .name("updated")
                .email("updated@email.com")
                .build();
        User result = userService.updateUser(UserMapper.toUser(updateUser));

        assertThat(result, notNullValue());
        assertThat(result.getEmail(), is(updateUser.getEmail()));
        assertThat(result.getName(), is(updateUser.getName()));
    }

    @Test
    void updateUser_EmailAlreadyExistException() {
        UserRequestDto updateUser = UserRequestDto.builder()
                .id(1L)
                .name("test")
                .email("test@gmail.com")
                .build();

        assertThrows(EmailAlreadyExistException.class, () -> userService.updateUser(UserMapper.toUser(updateUser)));
    }

    @Test
    void deleteUser() {
        User create = User.builder()
                .id(2L)
                .name("name")
                .email("email@email.com")
                .build();
        userService.createUser(UserMapper.toUserDto(create));
        assertThat(create, notNullValue());
        userService.removeUser(create.getId());
        assertThrows(NotFoundException.class, () -> userService.getUserById(create.getId()));
    }

    @Test
    void isExist() {
        assertThat(userService.isExist(user.getId()), is(true));
    }

}
