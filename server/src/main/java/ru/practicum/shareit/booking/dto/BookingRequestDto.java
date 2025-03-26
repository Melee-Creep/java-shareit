package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {

        private Long id;
        @NotNull(message = "Время начала бронирования не указано")
        private LocalDateTime start;
        @NotNull(message = "Время окончания бронирования не указано")
        private LocalDateTime end;
        private User booker;
        private BookingStatus status;
        private Long itemId;
        private Item item;
}
