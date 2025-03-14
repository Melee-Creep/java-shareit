package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseComment;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {

        if (item == null) {
            return null;
        }
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest())
                .owner(item.getOwner())
                .build();

    }

    public static Item toItem(ItemDto itemDto) {

        if (itemDto == null) {
            return null;
        }
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .request(itemDto.getRequest())
                .owner(itemDto.getOwner())
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
