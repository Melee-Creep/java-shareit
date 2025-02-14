package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public ItemDto getItem(long id) {

        return ItemMapper.toItemDto(itemRepository.getItem(id));
    }

    @Override
    public Collection<Item> getItemOwner(long userId) {
        return itemRepository.getItemOwner(userId);
    }

    @Override
    public Collection<Item> findByText(String text) {
        return itemRepository.findByText(text);
    }

    @Override
    public Item createItem(Item item, long userId) {
        return itemRepository.createItem(item, userId);
    }

    @Override
    public Item updateItem(Item item, long itemId) {
        return itemRepository.updateItem(item, itemId);
    }
}
