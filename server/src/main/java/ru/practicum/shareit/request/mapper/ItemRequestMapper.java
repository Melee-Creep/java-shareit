package ru.practicum.shareit.request.mapper;


import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestorDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;

public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {

        if (itemRequest == null) {
            return null;
        }

        return ItemRequestDto.builder()
                .id(itemRequest.getRequestId())
                .description(itemRequest.getDescription())
                .requestorId(itemRequest.getRequestor().getId())
                .created(itemRequest.getCreated())
                .build();

    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {

        if (itemRequestDto == null) {
            return null;
        }
        return ItemRequest.builder()
                .requestId(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .created(itemRequestDto.getCreated() != null ? itemRequestDto.getCreated() : LocalDateTime.now())
                .build();
    }

    public static ItemRequestorDto toItemRequestorDto(ItemRequest itemRequest) {

        if (itemRequest == null) {
            return null;
        }
        return ItemRequestorDto.builder()
                .id(itemRequest.getRequestId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }
}
