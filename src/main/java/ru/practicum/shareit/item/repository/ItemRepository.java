package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {

    Item getItem(long id);

    Collection<Item> getItemOwner(long userId);

    Collection<Item> findByText(String text);

    Item createItem(Item item, long userId);

    Item updateItem(Item item, long userId);
}
