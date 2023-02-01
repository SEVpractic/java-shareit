package ru.practicum.shareit.util;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
}
