package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.IncompleteCommentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDtos;
import ru.practicum.shareit.item.dto.ItemResponseComment;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/items")
public class ItemController {

    ItemService itemService;
    UserService userService;
    BookingService bookingService;

    @GetMapping("/{itemId}")
    public ItemResponseComment getItem(@PathVariable long itemId) {
        return itemService.getItemComment(itemId);
    }

    @GetMapping()
    public Collection<Item> getItemOwner(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return itemService.getItemOwner(ownerId);
    }

    @PostMapping
    public ItemRequestDtos createItem(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                                      @Valid @RequestBody ItemRequestDtos itemDto) {
        itemDto.setOwner(userService.getUserById(ownerId));
        return itemService.createItem(itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                           @PathVariable long itemId,
                           @RequestBody Item item) {

        userService.getUserById(ownerId);
        return itemService.updateItem(item, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentRequestDto createItemComment(@PathVariable long itemId,
                                               @RequestHeader(value = "X-Sharer-User-Id") Long authorId,
                                               @RequestBody CommentRequestDto commentRequestDto) {
        Collection<BookingRequestDto> bookings = bookingService.getAllBooking("ALL", authorId);
        BookingRequestDto booker = bookings.stream()
                .filter(BookingDto -> BookingDto.getBooker().getId().equals(authorId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Вы не бронировали вещь"));

        Comment comment = CommentMapper.toComment(commentRequestDto);
        if (booker.getEnd().isAfter(comment.getCreated())) {
            throw new IncompleteCommentException("Бронирование вещи ещё не завершено");
        }
        comment.setAuthor(UserMapper.toUser(userService.getUserById(authorId)));
        comment.setItem(ItemMapper.toItem(itemService.getItem(itemId)));
        return CommentMapper.toCommentDto(itemService.createItemComment(comment));
    }
}
