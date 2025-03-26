package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDtos;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestorDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemService itemService;


    @Override
    public List<ItemRequestDto> getUserIdRequest(long userId) {
        checkUser(userId);
        List<ItemRequest> itemRequest = itemRequestRepository.findAllRequestByRequestorId(userId);
        List<ItemRequestDto> itemRequestDto = itemRequest.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
        return itemRequestDto;

    }

    @Override
    public ItemRequestorDto getRequestId(long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id="+ requestId + " не найден!"));
        List<Item> items = itemService.findByRequestId(requestId);
        List<ItemRequestDtos> itemRequestDtos = items.stream()
                .map(ItemMapper::toItemDto).toList();
        ItemRequestorDto itemRequestorDto = ItemRequestMapper.toItemRequestorDto(itemRequest);
        itemRequestorDto.setItems(itemRequestDtos);
        return itemRequestorDto;
    }

    @Override
    public ItemRequestDto createRequest(long userId, ItemRequestDto itemRequestDto) {
        checkUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(UserMapper.toUser(userService.getUserById(userId)));
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    private void checkUser(long userId) {
        userService.isExist(userId);
    }
}
