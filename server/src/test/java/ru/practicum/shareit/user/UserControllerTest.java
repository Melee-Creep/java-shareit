package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = ShareItServer.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequestDto userRequestDto;

    @BeforeEach
    void createTestUser() {
        userRequestDto = UserRequestDto.builder()
                .id(1L)
                .name("test")
                .email("test@gmail.com")
                .build();
    }

    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(1L))
                .thenReturn(userRequestDto);

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"));
    }

    @Test
    void createUser() throws Exception {

        UserRequestDto create  = UserRequestDto.builder()
                        .id(2L)
                        .name("test")
                        .email("email@email.com").build();
        when(userService.createUser(any(UserRequestDto.class)))
                .thenReturn(create);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("email@email.com"));
    }

    @Test
    void updateUser() throws Exception {
        User create  = User.builder()
                .id(3L)
                .name("test")
                .email("email@email.com").build();

        User update  = User.builder()
                .id(3L)
                .name("update")
                .email("update@email.com").build();

        when(userService.updateUser(any(User.class)))
                .thenReturn(update);

        mockMvc.perform(patch("/users/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("update"))
                .andExpect(jsonPath("$.email").value("update@email.com"));

    }

    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).removeUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}
