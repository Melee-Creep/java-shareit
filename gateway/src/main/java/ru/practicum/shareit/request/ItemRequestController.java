package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    ItemRequestClient itemRequestClient;

    @GetMapping
    public ResponseEntity<Object> getUserIdRequest(@RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return itemRequestClient.getUserIdRequest(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestId(@PathVariable(name = "requestId") long requestId) {
        return itemRequestClient.getRequestId(requestId);
    }

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                @Validated @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.createRequest(userId, itemRequestDto);
    }
}
