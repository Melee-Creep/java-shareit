package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping()
    public List<BookingRequestDto> getAllUserBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                     @RequestParam(name = "text", defaultValue = "ALL") String text) {

        return bookingService.getAllBooking(text, userId);
    }

    @GetMapping("/owner")
    public Collection<BookingRequestDto> getBookingByOwner(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                    @RequestParam(defaultValue = "ALL") String text) {

        return bookingService.getBookingByOwner(text, userId);
    }

    @GetMapping("/{id}")
    public BookingRequestDto getBookingById(@PathVariable long id,
                                     @RequestHeader(value = "X-Sharer-User-Id") Long userId) {

        return bookingService.getBookingById(id, userId);
    }

    @PostMapping
    public BookingRequestDto createBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                    @Validated @RequestBody BookingRequestDto bookingDto) {

        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingRequestDto approveBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestBody boolean approved) {

        return bookingService.approveBooking(userId, bookingId, approved);
    }
}
