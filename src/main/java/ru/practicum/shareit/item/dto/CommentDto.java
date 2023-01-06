package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Builder(toBuilder = true)
@Getter
public class CommentDto {
    private Long id;
    @NotBlank
    private String text;
    private String authorName;
}
