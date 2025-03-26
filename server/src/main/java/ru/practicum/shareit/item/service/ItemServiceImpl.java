package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDtos;
import ru.practicum.shareit.item.dto.ItemResponseComment;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemRequestDtos getItem(long id) {
        return ItemMapper.toItemDto(itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Предмет с id=" + id + " не найден")));
    }

    @Override
    public List<Item> getItemOwner(long userId) {
        return itemRepository.findAllItemByOwnerId(userId);
    }

    @Override
    public ItemResponseComment getItemComment(long id) {
        ItemResponseComment itemResponseComment = ItemMapper.toItemComment(itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Предмет с id=" + id + " не найден")));
        itemResponseComment.setComments(commentRepository.findByItem_Id(id));

        return itemResponseComment;
    }

    @Override
    public ItemRequestDtos createItem(ItemRequestDtos itemRequestDtos) {
        Item item = ItemMapper.toItem(itemRequestDtos);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public Item updateItem(Item item, long itemId) {
        Item oldItem = ItemMapper.toItem(getItem(itemId));
        Item updateItem = Item.builder()
                .id(itemId)
                .name(item.getName() != null ? item.getName() : oldItem.getName())
                .description(item.getDescription() != null ? item.getDescription() : oldItem.getDescription())
                .available(item.getAvailable() != null ? item.getAvailable() : oldItem.getAvailable())
                .request(item.getRequest() != null ? item.getRequest() : oldItem.getRequest())
                .owner(item.getOwner() != null ? item.getOwner() : oldItem.getOwner())
                .build();

        return itemRepository.save(updateItem);
    }

    @Override
    public Comment createItemComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Item> findByRequestId(long requestId) {
        return itemRepository.findByRequest(requestId);
    }
}
