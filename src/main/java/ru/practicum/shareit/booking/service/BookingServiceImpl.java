package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotTheOwnerException;
import ru.practicum.shareit.exception.UnavailableBookingException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;

    @Override
    public Collection<BookingDto> getAllBooking(String text, Long userId) {
        List<Booking> bookings = bookingRepository.findByBooker_IdOrderByStartDesc(userId);
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> getBookingByOwner(String text, Long userId) {
        List<Booking> bookings = bookingRepository.findByBooker_IdOrderByStartDesc(userId);
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public BookingDto getBookingById(long bookingId, long userId) {

        log.info("userId={}", userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        Item item = booking.getItem();
        if (booking.getBooker().getId() == userId) {
            return BookingMapper.toBookingDto(bookingRepository.findById(bookingId).get());
        }
        if (item.getOwner().getId() == userId) {
            return BookingMapper.toBookingDto(bookingRepository.findById(bookingId).get());
        }
        log.error("пользователь userId={}, не как не относиться к вещи item={}", userId, booking.getItem());
        log.error("item.getOwner={}", item.getOwner().getId());
        throw new NotTheOwnerException("Просмотр бронирования не доступен");
    }

    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        Item item = ItemMapper.toItem(itemService.getItem(bookingDto.getItemId()));
        if (!item.getAvailable()) {
            throw new UnavailableBookingException("Предмет: " + item.getName() + " не доступен для бронирования");
        }
        bookingDto.setStatus(BookingStatus.WAITING);
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setItem(ItemMapper.toItem(itemService.getItem(bookingDto.getItemId())));
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto approveBooking(long userId, long bookingId, BookingStatus state) {
        Booking booking = BookingMapper.toBooking(getBookingById(bookingId, userId));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotTheOwnerException("Пользователь с id=" + userId + " не является владельцев вещи");
        }
        booking.setStatus(state);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }
}
