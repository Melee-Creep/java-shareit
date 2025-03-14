package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {

    public  static CommentDto toCommentDto(Comment comment) {

        if (comment == null) {
            return null;
        }
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toComment(CommentDto commentDto) {

        if (commentDto == null) {
            return null;
        }
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .created(commentDto.getCreated())
                .build();

    }
}
