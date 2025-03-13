package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @GetMapping()
    public List<BookingDto> getAllUserBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                              @RequestParam(name = "text", defaultValue = "ALL") String text) {

        return bookingService.getAllBooking(text, userId);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getBookingByOwner(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                    @RequestParam(defaultValue = "ALL") String text) {

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

        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                               @PathVariable Long bookingId,
                               @RequestParam boolean approved) {

        return bookingService.approveBooking(userId, bookingId, approved);
    }
}
