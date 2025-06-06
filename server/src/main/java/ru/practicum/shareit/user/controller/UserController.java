package ru.practicum.shareit.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@RequiredArgsConstructor
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(path = "/users")
public class UserController {

     UserService userService;

    @GetMapping("/{id}")
    public UserRequestDto getUser(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserRequestDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable long id,
                           @RequestBody User user) {
        user.setId(id);
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable long id) {
        userService.removeUser(id);
    }
}
