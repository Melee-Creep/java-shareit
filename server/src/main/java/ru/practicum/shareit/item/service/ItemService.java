package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemRequestDtos;
import ru.practicum.shareit.item.dto.ItemResponseComment;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemRequestDtos getItem(long id);

    List<Item> getItemOwner(long userId);

    ItemResponseComment getItemComment(long id);

    ItemRequestDtos createItem(ItemRequestDtos itemRequestDtos);

    Item updateItem(Item item, long userId);

    Comment createItemComment(Comment comment);

    List<Item> findByRequestId(long requestId);
}
