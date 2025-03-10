package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingStatus;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @GetMapping()
    public Collection<BookingDto> getAllUserBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                    @RequestParam(name = "text", defaultValue = "ALL") String text) {

        UserMapper.toUser(userService.getUserById(userId));
        return bookingService.getAllBooking(text, userId);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getBookingByOwner(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                    @RequestParam(defaultValue = "ALL") String text) {

        UserMapper.toUser(userService.getUserById(userId));
        return bookingService.getBookingByOwner(text, userId);
    }


    @GetMapping("/{id}")
    public BookingDto getBookingById(@PathVariable long id,
                                     @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingById(id, userId);
    }

    @PostMapping
    public BookingDto createBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
          @Validated @RequestBody BookingDto bookingDto) {

        bookingDto.setBooker(UserMapper.toUser(userService.getUserById(userId)));
        return bookingService.createBooking(bookingDto);
    }

    @PatchMapping("/{booking_Id}")
    public BookingDto approveBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                               @PathVariable Long bookingId,
                               @RequestParam boolean approved) {

        BookingStatus state = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        return bookingService.approveBooking(userId, bookingId, state);
    }
}
