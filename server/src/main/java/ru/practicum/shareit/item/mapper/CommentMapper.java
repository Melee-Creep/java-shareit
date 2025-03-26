package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

public class CommentMapper {

    public  static CommentRequestDto toCommentDto(Comment comment) {

        if (comment == null) {
            return null;
        }
        return CommentRequestDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toComment(CommentRequestDto commentDto) {

        if (commentDto == null) {
            return null;
        }
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .created(commentDto.getCreated() != null ? commentDto.getCreated() : LocalDateTime.now())
                .build();

    }
}
