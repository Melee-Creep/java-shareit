package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDtos;
import ru.practicum.shareit.item.dto.ItemResponseComment;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    private Item item;
    private User owner;
    private User booker;
    private Comment comment;

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

        item = Item.builder()
                .id(1L)
                .name("testItem")
                .description("testDescription")
                .available(true)
                .request(1L)
                .owner(owner)
                .build();
        itemRepository.save(item);

        comment = Comment.builder()
                .id(1L)
                .text("testComment")
                .author(booker)
                .item(item)
                .created(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
    }

    @Test
    void getItemById() {
        ItemRequestDtos getItem = itemService.getItem(1L);

        assertThat(getItem, notNullValue());
        assertThat(getItem.getId(), is(item.getId()));
        assertThat(getItem.getName(), is(item.getName()));
        assertThat(getItem.getDescription(), is(item.getDescription()));
        assertThat(getItem.getAvailable(), is(item.getAvailable()));
        assertThat(getItem.getRequestId(), is(item.getRequest()));
        assertThat(getItem.getOwner().getId(), is(item.getOwner().getId()));
        assertThat(getItem.getOwner().getName(), is(item.getOwner().getName()));
        assertThat(getItem.getOwner().getEmail(), is(item.getOwner().getEmail()));
    }

    @Test
    void getItemById_NotFoundException() {
        assertThrows(NotFoundException.class, () -> itemService.getItem(10000L));
    }

    @Test
    void getItemByOwnerId() {
        List<Item> result = itemService.getItemOwner(owner.getId());

        assertThat(result, notNullValue());
        assertThat(result, hasSize(List.of(item).size()));

        for (int i = 0; i < List.of(item).size(); i++) {
            assertThat(result.get(i), hasProperty("id", is(List.of(item).get(i).getId())));
            assertThat(result.get(i), hasProperty("name", is(List.of(item).get(i).getName())));
            assertThat(result.get(i), hasProperty("description", is(List.of(item).get(i).getDescription())));
            assertThat(result.get(i), hasProperty("available", is(List.of(item).get(i).getAvailable())));
            assertThat(result.get(i), hasProperty("request", is(List.of(item).get(i).getRequest())));
            assertThat(result.get(i), hasProperty("owner", hasProperty("id", is(1L))));
        }
    }

    @Test
    void getItemComment() {
        ItemResponseComment itemResponseComment = itemService.getItemComment(1L);
        List<Comment> comments = itemResponseComment.getComments().stream()
                        .map(CommentMapper::toComment)
                                .toList();

        assertThat(itemResponseComment, notNullValue());
        for (int i = 0; i < List.of(comment).size(); i++) {
            assertThat(comments.get(i), hasProperty("id", is(comment.getId())));
            assertThat(comments.get(i), hasProperty("text", is(comment.getText())));
        }
    }

    @Test
    void getItemComment_NotFoundException() {
        assertThrows(NotFoundException.class, () -> itemService.getItemComment(10000L));
    }

    @Test
    void createItem() {
        Item create = Item.builder()
                .id(2L)
                .name("testItem")
                .description("testDescription")
                .available(true)
                .request(1L)
                .owner(owner)
                .build();

        ItemRequestDtos result = itemService.createItem(ItemMapper.toItemDto(create));

        assertThat(result, notNullValue());
        assertThat(result.getId(), is(create.getId()));
        assertThat(result.getName(), is(create.getName()));
        assertThat(result.getDescription(), is(create.getDescription()));
        assertThat(result.getAvailable(), is(create.getAvailable()));
        assertThat(result.getRequestId(), is(create.getRequest()));
        assertThat(result.getOwner().getId(), is(create.getOwner().getId()));
        assertThat(result.getOwner().getName(), is(create.getOwner().getName()));
        assertThat(result.getOwner().getEmail(), is(create.getOwner().getEmail()));
    }

    @Test
    void updateItem() {
        Item update = Item.builder()
                .id(1L)
                .name("updateTestItem")
                .description("updateTestDescription")
                .available(true)
                .request(1L)
                .owner(owner)
                .build();

        Item result = itemService.updateItem(update, owner.getId());

        assertThat(result, notNullValue());
        assertThat(result.getId(), is(update.getId()));
        assertThat(result.getName(), is(update.getName()));
        assertThat(result.getDescription(), is(update.getDescription()));
        assertThat(result.getAvailable(), is(update.getAvailable()));
        assertThat(result.getRequest(), is(update.getRequest()));
        assertThat(result.getOwner().getId(), is(update.getOwner().getId()));
        assertThat(result.getOwner().getName(), is(update.getOwner().getName()));
        assertThat(result.getOwner().getEmail(), is(update.getOwner().getEmail()));
    }

    @Test
    void createItemComment() {
        Comment createComment = Comment.builder()
                .id(2L)
                .text("createComment")
                .author(booker)
                .item(item)
                .created(LocalDateTime.now())
                .build();

        CommentMapper.toCommentDto(createComment);
        Comment result = itemService.createItemComment(createComment);

        assertThat(result, notNullValue());
        assertThat(result.getId(), is(createComment.getId()));
        assertThat(result.getText(), is(createComment.getText()));
        assertThat(result.getAuthor().getName(), is(createComment.getAuthor().getName()));
        assertThat(result.getItem().getName(), is(createComment.getItem().getName()));
    }

    @Test
    void findByRequestId() {
        List<Item> result = itemService.findByRequestId(item.getRequest());

        assertThat(result, notNullValue());
        for (int i = 0; i < List.of(item).size(); i++) {
            assertThat(result.get(i), hasProperty("id", is(item.getId())));
            assertThat(result.get(i), hasProperty("name", is(item.getName())));
            assertThat(result.get(i), hasProperty("description", is(item.getDescription())));
        }
    }
}
