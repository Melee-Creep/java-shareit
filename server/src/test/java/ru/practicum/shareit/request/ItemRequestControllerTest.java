package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestorDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.List;

@AutoConfigureMockMvc
@WebMvcTest(ItemRequestController.class)
@ContextConfiguration(classes = ShareItServer.class)
public class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemRequestDto itemRequestDto;
    private ItemRequestorDto itemRequestorDto;

    @Test
    void getUserIdRequest() throws Exception {
        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("test")
                .created(LocalDateTime.of(2025,10,10,10,10))
                .requestor_id(2L)
                .build();

        when(itemRequestService.getUserIdRequest(anyLong())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests")
                .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].description", is("test")));
    }

    @Test
    void getRequestId() throws Exception {
        itemRequestorDto = ItemRequestorDto.builder()
                .id(1L)
                .description("test")
                .created(LocalDateTime.of(2025,10,10,10,10))
                .items(List.of())
                .build();

        when(itemRequestService.getRequestId(anyLong())).thenReturn(itemRequestorDto);

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("test")));
    }

    @Test
    void createRequest() throws Exception {
        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("test")
                .created(LocalDateTime.of(2025,10,10,10,10))
                .requestor_id(2L)
                .build();

        when(itemRequestService.createRequest(anyLong(), any(ItemRequestDto.class)))
                .thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", "1")
                .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("test")));
    }
}
