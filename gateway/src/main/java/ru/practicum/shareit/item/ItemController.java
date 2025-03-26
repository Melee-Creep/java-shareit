package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDtos;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@PathVariable long id) {
        return itemClient.getItem(id);
    }

    @GetMapping
    public ResponseEntity<Object> getItemOwner(@RequestHeader(value = "X-Sharer-User-Id") long ownerId) {
        return itemClient.getItemOwner(ownerId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                                             @Validated @RequestBody ItemRequestDtos itemRequestDtos) {
        return itemClient.createItem(ownerId, itemRequestDtos);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                                             @PathVariable long id,
                                             @RequestBody ItemRequestDtos itemRequestDtos) {
        return itemClient.updateItem(id, ownerId, itemRequestDtos);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createItemComment(@PathVariable long itemId,
                                                    @RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                    @RequestBody CommentRequestDto commentRequestDto) {
        log.info(String.valueOf(commentRequestDto.getCreated()));
       return itemClient.createItemComment(itemId, userId, commentRequestDto);
    }
}
