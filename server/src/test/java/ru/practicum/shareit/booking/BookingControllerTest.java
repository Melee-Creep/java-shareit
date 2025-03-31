package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.is;

@AutoConfigureMockMvc
@WebMvcTest(BookingController.class)
@ContextConfiguration(classes = ShareItServer.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingRequestDto bookingRequestDto;
    private User booker;
    private User owner;
    private Item item;

    @BeforeEach
    void setUp() {
        booker = User.builder()
                .id(2L)
                .name("booker")
                .email("booker@email.com")
                .build();

        owner = User.builder()
                .id(1L)
                .name("owner")
                .email("owner@email.com")
                .build();

        item = Item.builder()
                .id(1L)
                .name("test")
                .description("test")
                .request(null)
                .owner(owner)
                .build();

        bookingRequestDto = BookingRequestDto.builder()
                .id(2L)
                .start(LocalDateTime.of(2025, 10, 10, 10, 10))
                .end(LocalDateTime.of(2025,10,10,11,11))
                .booker(booker)
                .status(BookingStatus.WAITING)
                .itemId(1L)
                .item(item)
                .build();
    }

    @Test
    void getAllUserBooking() throws Exception {
        List<BookingRequestDto> bookingRequestDtoList = List.of(bookingRequestDto);

        when(bookingService.getAllBooking(anyString(), anyLong())).thenReturn(bookingRequestDtoList);

        mockMvc.perform(get("/bookings?state=ALL")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    void getBookingByOwner() throws Exception {
        List<BookingRequestDto> bookingRequestDtoList = List.of(bookingRequestDto);

        when(bookingService.getBookingByOwner(anyString(), anyLong())).thenReturn(bookingRequestDtoList);

        mockMvc.perform(get("/bookings/owner?state=ALL")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    void getBookingById() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(bookingRequestDto);

        mockMvc.perform(get("/bookings/{bookingId}", 2)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingRequestDto)));
    }

    @Test
    void createBooking() throws Exception {
        bookingRequestDto = BookingRequestDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2025, 10, 10, 10, 10))
                .end(LocalDateTime.of(2025,10,10,11,11))
                .booker(booker)
                .status(BookingStatus.WAITING)
                .itemId(1L)
                .item(item)
                .build();

        when(bookingService.createBooking(any(BookingRequestDto.class), anyLong())).thenReturn(bookingRequestDto);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(bookingRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

    @Test
    void approveBooking() throws Exception {
        bookingRequestDto = BookingRequestDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2025, 10, 10, 10, 10))
                .end(LocalDateTime.of(2025,10,10,11,11))
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .itemId(1L)
                .item(item)
                .build();

        when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingRequestDto);

        mockMvc.perform(patch("/bookings/1")
                        .content("true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }
}
