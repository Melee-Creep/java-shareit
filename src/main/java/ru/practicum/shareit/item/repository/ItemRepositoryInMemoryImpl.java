package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ItemRepositoryInMemoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item getItem(long id) {
        return items.get(id);
    }

    @Override
    public Collection<Item> getItemOwner(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> findByText(String text) {

        log.info("Начало поиска доступных предметов по тексту text={}", text);
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        String lowerCaseText = text.toLowerCase();

        return items.values().stream()
                .filter(item -> (
                                item.getName().toLowerCase().contains(lowerCaseText)
                                        || item.getDescription().toLowerCase().contains(lowerCaseText)
                        ) && item.getAvailable()
                ).collect(Collectors.toList());
    }

    @Override
    public Item createItem(Item item, long userId) {
        log.info("Начало добавления предмета item={}", item);

        item.setId(getNextId());
        items.put(item.getId(), item);

        log.info("Конец добавления предмета item={}", item);
        return item;
    }

    @Override
    public Item updateItem(Item item, long itemId) {
        log.info("Начало обновления предмета item={}", item);
        Item oldItem = items.get(itemId);

        if (item.getOwner() != null) {
            oldItem.setOwner(item.getOwner());
        }
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        if (item.getRequest() != null) {
            oldItem.setRequest(item.getRequest());
        }

        items.replace(itemId, oldItem);
        log.info("Конец обновления предмета item={}", item);
        return items.get(itemId);
    }

    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
