package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestorDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class RequestServiceTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;


    private User owner;
    private User requestor;
    private Item item;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .name("testUser")
                .email("testUser@email.com")
                .build();
        userRepository.save(owner);

        requestor = User.builder()
                .id(2L)
                .name("requestor")
                .email("requestor@email.com")
                .build();
        userRepository.save(requestor);

        item = Item.builder()
                .id(1L)
                .name("ItemOwner")
                .description("ItemTestUser")
                .available(true)
                .request(1L)
                .owner(owner)
                .build();
        itemRepository.save(item);

        itemRequest = ItemRequest.builder()
                .request_id(1L)
                .description("testItemRequest")
                .requestor(requestor)
                .created(LocalDateTime.of(2025, 10, 10, 10, 10))
                .build();
        itemRequestRepository.save(itemRequest);
    }

    @Test
    void getUserIdRequest() {
        List<ItemRequestDto> result = itemRequestService.getUserIdRequest(requestor.getId());

        assertThat(result, notNullValue());
        assertThat(result.iterator().next().getId(), is(itemRequest.getRequest_id()));
        assertThat(result.iterator().next().getDescription(), is(itemRequest.getDescription()));
        assertThat(result.iterator().next().getRequestor_id(), is(itemRequest.getRequestor().getId()));
    }

    @Test
    void getRequestId() {
        ItemRequestorDto result = itemRequestService.getRequestId(itemRequest.getRequest_id());

        assertThat(result, notNullValue());
        assertThat(result.getId(), is(itemRequest.getRequest_id()));
        assertThat(result.getDescription(), is(itemRequest.getDescription()));
        assertThat(result.getCreated(), is(itemRequest.getCreated()));
    }

    @Test
    void createRequest() {
        ItemRequestDto create = ItemRequestDto.builder()
                .id(2L)
                .description("test")
                .created(LocalDateTime.of(2025, 10, 10, 10, 10))
                .requestor_id(requestor.getId())
                .build();
        ItemRequestDto result = itemRequestService.createRequest(requestor.getId(), create);

        assertThat(result, notNullValue());
        assertThat(result.getId(), is(create.getId()));
        assertThat(result.getDescription(), is(create.getDescription()));
    }
}
