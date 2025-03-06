package ru.practicum.category.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Valid
public class CreateCategoryRequest {
    @NotBlank(message = "Категория должна иметь имя")
    @Size(max = 50, message = "Имя категории не может быть длиннее 50 символов")
    private String name;
}
