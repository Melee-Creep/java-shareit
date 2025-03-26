package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemRequestDtos;
import ru.practicum.shareit.item.dto.ItemResponseComment;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;


@Slf4j
@AutoConfigureMockMvc
@WebMvcTest(ItemController.class)
@ContextConfiguration(classes = ShareItServer.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;
    @MockBean
    private UserService userService;
    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemRequestDtos itemRequestDtos;
    private UserRequestDto userRequestDto;
    private Comment comment;
    private User commentAuthor;
    private BookingRequestDto booking;

    @BeforeEach
    void setUp() {

        userRequestDto = UserRequestDto.builder()
                .id(1L)
                .name("testUser")
                .email("test@email.com")
                .build();

        commentAuthor = User.builder()
                .id(2L)
                .name("commentUser")
                .email("comment@email.com")
                .build();

        itemRequestDtos = ItemRequestDtos.builder()
                .id(1L)
                .name("test")
                .description("test")
                .available(true)
                .requestId(1L)
                .owner(userRequestDto)
                .build();

        booking = BookingRequestDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2025, 10, 10, 10, 10))
                .end(LocalDateTime.of(2025, 10, 10, 11, 10))
                .booker(commentAuthor)
                .status(BookingStatus.APPROVED)
                .itemId(itemRequestDtos.getId())
                .item(ItemMapper.toItem(itemRequestDtos))
                .build();

        comment = Comment.builder()
                .id(1L)
                .text("comment")
                .author(commentAuthor)
                .item(ItemMapper.toItem(itemRequestDtos))
                .created(LocalDateTime.of(2025,10,10,12,10))
                .build();

    }

    @Test
    void getItem() throws Exception {

        Item item = ItemMapper.toItem(itemRequestDtos);
        ItemResponseComment responseComment = ItemMapper.toItemComment(item);
        when(itemService.getItemComment(anyLong())).thenReturn(responseComment);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("test")));
    }

    @Test
    void getItemOwner() throws Exception {
        List<Item> items = List.of(itemRequestDtos).stream()
                .map(ItemMapper::toItem)
                .toList();

        when(itemService.getItemOwner(anyLong())).thenReturn(items);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    void createItem() throws Exception {
        when(itemService.createItem(any(ItemRequestDtos.class))).thenReturn(itemRequestDtos);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(itemRequestDtos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("test")));
    }

    @Test
    void updateItem() throws Exception {
        Item update = Item.builder()
                .id(1L)
                .name("UpdateTest")
                .description("UpdateTest")
                .available(true)
                .request(1L)
                .owner(UserMapper.toUser(userRequestDto))
                .build();

        when(itemService.updateItem(any(Item.class), anyLong())).thenReturn(update);

        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("UpdateTest")));
    }

    @Test
    void createItemComment() throws Exception {

        Item item = Item.builder()
                .id(2L)
                .name("test")
                .description("test")
                .available(true)
                .request(null)
                .owner(UserMapper.toUser(userRequestDto))
                .build();

        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                .id(2L)
                .start(LocalDateTime.of(2025, 10, 10, 10, 10))
                .end(LocalDateTime.of(2025, 10, 10, 11, 10))
                .booker(commentAuthor)
                .status(BookingStatus.APPROVED)
                .itemId(item.getId())
                .item(item)
                .build();

        Comment create = Comment.builder()
                .id(2L)
                .text("comment")
                .author(commentAuthor)
                .item(item)
                .created(LocalDateTime.of(2025,10,10,12,10))
                .build();

        when(bookingService.getAllBooking(any(), eq(commentAuthor.getId())))
                .thenReturn(List.of(bookingRequestDto));


        mockMvc.perform(post("/items/{itemId}/comment", 2L)
                        .header("X-Sharer-User-Id", commentAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isOk());
    }
}

