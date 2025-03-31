package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDtos;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getItem(long id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> getItemOwner(long ownerId) {
        return get("/" + ownerId);
    }

    public ResponseEntity<Object> createItem(long ownerId, ItemRequestDtos itemRequestDtos) {
        return post("", ownerId, itemRequestDtos);
    }

    public ResponseEntity<Object> updateItem(long id, long ownerId, ItemRequestDtos itemRequestDtos) {
        return patch("/" + id, ownerId, itemRequestDtos);
    }

    public ResponseEntity<Object> createItemComment(long itemId, long userId, CommentRequestDto commentRequestDto) {
        return post("/" + itemId + "/comment", userId, commentRequestDto);
    }
}
