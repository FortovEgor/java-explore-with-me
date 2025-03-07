package ru.practicum.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {
    @Size(max = 5000, message = "Комментарий не может быть длинее 5000 символов")
    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;
}
