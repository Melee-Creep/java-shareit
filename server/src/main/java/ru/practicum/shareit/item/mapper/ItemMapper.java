package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemRequestDtos;
import ru.practicum.shareit.item.dto.ItemResponseComment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

public class ItemMapper {

    public static ItemRequestDtos toItemDto(Item item) {

        if (item == null) {
            return null;
        }
        return ItemRequestDtos.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest())
                .owner(UserMapper.toUserDto(item.getOwner()))
                .build();

    }

    public static Item toItem(ItemRequestDtos itemRequestDtos) {

        if (itemRequestDtos == null) {
            return null;
        }
        return Item.builder()
                .id(itemRequestDtos.getId())
                .name(itemRequestDtos.getName())
                .description(itemRequestDtos.getDescription())
                .available(itemRequestDtos.getAvailable())
                .request(itemRequestDtos.getRequestId())
                .owner(UserMapper.toUser(itemRequestDtos.getOwner()))
                .build();
    }

    public static ItemResponseComment toItemComment(Item item) {
        return ItemResponseComment.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }
}
