package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    ItemDto getItem(long id);

    Collection<Item> getItemOwner(long userId);

    Collection<Item> findByText(String text);

    ItemDto createItem(ItemDto itemDto, long userId);

    Item updateItem(Item item, long userId);
}
