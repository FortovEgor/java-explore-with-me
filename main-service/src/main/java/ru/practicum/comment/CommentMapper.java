package ru.practicum.comment;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.user.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {UserMapper.class})
public interface CommentMapper {

    CommentDto toDto(Comment comment);

    List<CommentDto> toDto(List<Comment> comments);
}