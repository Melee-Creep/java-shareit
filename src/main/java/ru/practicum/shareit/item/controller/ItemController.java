package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable long itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping()
    public Collection<Item> getItemOwner(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId) {
        validateOwner(ownerId);
        return itemService.getItemOwner(ownerId);
    }

    @GetMapping("/search")
    public Collection<Item> findByText(@RequestParam(name = "text", required = false) String text) {
       return itemService.findByText(text);
    }

    @PostMapping
    public Item createItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
            @RequestBody Item item) {

        validateItem(item);
        validateOwner(ownerId);
        item.setOwner(userService.getUserById(ownerId));
        return itemService.createItem(item, ownerId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                           @PathVariable long itemId,
                           @RequestBody Item item) {


        validateOwner(ownerId);
        userService.getUserById(ownerId);
        return itemService.updateItem(item, itemId);
    }




    private void validateOwner(Long ownerId) {
        if (ownerId == null) {
            log.error("Отсутствует заголовок X-Sharer-User-Id={}", ownerId);
            throw new ValidateException("Владелец вещи должен быть указан");
        }
    }

    private void validateItem(Item item) {

        if (item.getName() == null || item.getName().isBlank()) {
            log.error("Не указано название предмета item.name={}", item.getName());
            throw new ValidateException("Название предмета должно быть указано");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            log.error("Не указано описание предмета item.description={}", item.getDescription());
            throw new ValidateException("Описание предмета должно быть указано");
        }
        if (item.getAvailable() == null) {
            log.error("Не указано поле доступности available={}", item.getAvailable());
            throw new ValidateException("Не указано поле доступности");
        }
    }
}
