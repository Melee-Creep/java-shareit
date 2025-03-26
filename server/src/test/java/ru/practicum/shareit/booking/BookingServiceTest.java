package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotTheOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest
public class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private Item item;
    private User owner;
    private User booker;
    private Booking booking;
    private Booking bookingByOwner;
    private User notOwner;
    private Item unavalibleItem;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .name("testUser")
                .email("testUser@email.com")
                .build();
        userRepository.save(owner);

        booker = User.builder()
                .id(2L)
                .name("testBooker")
                .email("testBooker@email.com")
                .build();
        userRepository.save(booker);

        notOwner = User.builder()
                .id(3L)
                .name("noOwner")
                .email("testnoOwner")
                .build();
        userRepository.save(notOwner);

        item = Item.builder()
                .id(1L)
                .name("ItemOwner")
                .description("ItemTestUser")
                .available(true)
                .request(1L)
                .owner(owner)
                .build();
        itemRepository.save(item);

        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2025, 10, 10, 10, 10))
                .end(LocalDateTime.of(2025,10,10,11,11))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);

        bookingByOwner = Booking.builder()
                .id(2L)
                .start(LocalDateTime.of(2025, 10, 10, 10, 10))
                .end(LocalDateTime.of(2025,10,10,11,11))
                .item(item)
                .booker(owner)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(bookingByOwner);

        unavalibleItem = Item.builder()
                .id(3L)
                .name("ItemOwner")
                .description("ItemTestUser")
                .available(false)
                .request(1L)
                .owner(owner)
                .build();

    }

    @Test
    void getAllBooking() {

        List<BookingRequestDto> result = bookingService.getAllBooking("ALL", booker.getId());


        assertThat(result, notNullValue());
        assertThat(result.iterator().next().getId(), is(booking.getId()));
        assertThat(result.iterator().next().getStart(), is(booking.getStart()));
        assertThat(result.iterator().next().getEnd(), is(booking.getEnd()));
        assertThat(result.iterator().next().getItemId(), is(booking.getItem().getId()));
        assertThat(result.iterator().next().getBooker().getId(), is(booking.getBooker().getId()));
        assertThat(result.iterator().next().getStatus(), is(booking.getStatus()));
    }

    @Test
    void getBookingByOwner() {

        List<BookingRequestDto> result = bookingService.getBookingByOwner("ALL", owner.getId());

        assertThat(result, notNullValue());
        assertThat(result.iterator().next().getId(), is(bookingByOwner.getId()));
        assertThat(result.iterator().next().getStart(), is(bookingByOwner.getStart()));
        assertThat(result.iterator().next().getEnd(), is(bookingByOwner.getEnd()));
        assertThat(result.iterator().next().getItemId(), is(bookingByOwner.getItem().getId()));
        assertThat(result.iterator().next().getBooker().getId(), is(bookingByOwner.getBooker().getId()));
        assertThat(result.iterator().next().getStatus(), is(bookingByOwner.getStatus()));
    }

    @Test
    void getBookingById() {

        Booking result = BookingMapper.toBooking(bookingService.getBookingById(bookingByOwner.getId(), owner.getId()));

        assertThat(result, notNullValue());
        assertThat(result.getId(), is(bookingByOwner.getId()));
        assertThat(result.getStart(), is(bookingByOwner.getStart()));
        assertThat(result.getEnd(), is(bookingByOwner.getEnd()));
        assertThat(result.getBooker().getId(), is(bookingByOwner.getBooker().getId()));
        assertThat(result.getItem().getId(), is(bookingByOwner.getItem().getId()));
        assertThat(result.getStatus(), is(bookingByOwner.getStatus()));
    }

    @Test
    void getBookingById_NotFoundException() {

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(10000L, owner.getId()));
    }

    @Test
    void getBookingById_NotTheOwnerException() {

        assertThrows(NotTheOwnerException.class, () -> bookingService.getBookingById(booking.getId(), notOwner.getId()));
    }

    @Test
    void createBooking() {

        Booking create = Booking.builder()
                .id(3L)
                .start(LocalDateTime.of(2025, 10, 10, 10, 10))
                .end(LocalDateTime.of(2025,10,10,11,11))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
        BookingRequestDto result = bookingService.createBooking(BookingMapper.toBookingDto(create), booker.getId());

        assertThat(result, notNullValue());
        assertThat(result.getId(), is(create.getId()));
        assertThat(result.getStart(), is(create.getStart()));
        assertThat(result.getEnd(), is(create.getEnd()));
        assertThat(result.getItemId(), is(create.getItem().getId()));
        assertThat(result.getBooker().getId(), is(create.getBooker().getId()));
        assertThat(result.getStatus(), is(create.getStatus()));
    }

    @Test
    void approveBooking() {
        BookingRequestDto create = BookingRequestDto.builder()
                .start(LocalDateTime.of(2025, 10, 10, 10, 10))
                .end(LocalDateTime.of(2025,10,10,11,11))
                .itemId(item.getId())
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
        BookingRequestDto createBooking = bookingService.createBooking(create, create.getBooker().getId());
        BookingRequestDto result = bookingService.approveBooking(owner.getId(), createBooking.getId(), true);

        assertThat(result, notNullValue());
        assertThat(result.getStart(), is(create.getStart()));
        assertThat(result.getEnd(), is(create.getEnd()));
        assertThat(result.getItemId(), is(create.getItem().getId()));
        assertThat(result.getBooker().getId(), is(create.getBooker().getId()));
        assertThat(result.getStatus(), is(BookingStatus.APPROVED));
    }

}
