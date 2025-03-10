package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseComment;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    ItemDto getItem(long id);

    ItemResponseComment getItemComment(long id);

    Comment createItemComment(Comment comment);

    Collection<Item> getItemOwner(long userId);

    Collection<Item> findByText(String text);

    ItemDto createItem(ItemDto itemDto);

    Item updateItem(Item item, long userId);
}
