package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookItemRequestDto {
	private long itemId;
	@FutureOrPresent
	@NotNull(message = "Время начала бронирования не указано")
	private LocalDateTime start;
	@Future
	@NotNull(message = "Время окончания бронирования не указано")
	private LocalDateTime end;
}
