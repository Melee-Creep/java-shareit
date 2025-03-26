package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> getAllUserBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {

		return bookingClient.getAllUserBooking(userId);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingByOwner(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
													@RequestParam(defaultValue = "ALL", required = false) String text) {

		BookingState state = BookingState.valueOf(text);
		return bookingClient.getBookingByOwner(userId, state);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getBookingById(@PathVariable long id,
											@RequestHeader(value = "X-Sharer-User-Id") Long userId) {

		return bookingClient.getBookingById(id, userId);
	}

	@PostMapping
	public ResponseEntity<Object> createBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
										   @Validated @RequestBody BookItemRequestDto bookingDto) {

		return bookingClient.createBooking(userId, bookingDto);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approveBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
											@PathVariable Long bookingId,
											@RequestParam boolean approved) {

		log.info("approved={}", approved);
		return bookingClient.approveBooking(userId, bookingId, approved);
	}
}
