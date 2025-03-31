package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestorDto;

import java.util.List;

public interface ItemRequestService {

    List<ItemRequestDto> getRequestByUserId(long userId);

    ItemRequestorDto getRequestId(long requestId);

    ItemRequestDto createRequest(long userId, ItemRequestDto itemRequestDto);
}
