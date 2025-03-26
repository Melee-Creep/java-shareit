package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestorDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @GetMapping
    public List<ItemRequestDto> getUserIdRequest(@RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return itemRequestService.getUserIdRequest(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestorDto getRequestId(@PathVariable(name = "requestId") long requestId) {
        return itemRequestService.getRequestId(requestId);
    }
    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                        @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.createRequest(userId, itemRequestDto);
    }
}
