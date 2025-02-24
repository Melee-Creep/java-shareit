package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
    public Collection<Item> getItemOwner(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return itemService.getItemOwner(ownerId);
    }

    @GetMapping("/search")
    public Collection<Item> findByText(@RequestParam(name = "text", required = false) String text) {
       return itemService.findByText(text);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
          @Valid @RequestBody ItemDto itemDto) {

        itemDto.setOwner(userService.getUserById(ownerId));
        return itemService.createItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                           @PathVariable long itemId,
                           @RequestBody Item item) {

        userService.getUserById(ownerId);
        return itemService.updateItem(item, itemId);
    }
}
