package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.IncompleteCommentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseComment;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
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
    private final BookingService bookingService;

    @GetMapping("/{itemId}")
    public ItemResponseComment getItem(@PathVariable long itemId) {
        return itemService.getItemComment(itemId);
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
        itemDto.setOwner(UserMapper.toUser(userService.getUserById(ownerId)));
        return itemService.createItem(itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto CreateItemComment(@PathVariable long itemId,
                                        @RequestHeader(value = "X-Sharer-User-Id") Long authorId,
                                        @RequestBody Comment comment) {
        Collection<BookingDto> bookings = bookingService.getAllBooking("ALL", authorId);
        BookingDto booker = bookings.stream()
                        .filter(BookingDto -> BookingDto.getBooker().getId().equals(authorId))
                                .findFirst()
                                        .orElseThrow(() -> new NotFoundException("Вы не бронировали вещь"));

        if (booker.getEnd().isAfter(comment.getCreated())) {
            throw new IncompleteCommentException("Бронирование вещи ещё не завершено");
        }
        comment.setAuthor(UserMapper.toUser(userService.getUserById(authorId)));
        comment.setItem(ItemMapper.toItem(itemService.getItem(itemId)));
        return CommentMapper.toCommentDto(itemService.createItemComment(comment));
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                           @PathVariable long itemId,
                           @RequestBody Item item) {

        userService.getUserById(ownerId);
        return itemService.updateItem(item, itemId);
    }
}
