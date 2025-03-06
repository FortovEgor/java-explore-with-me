package ru.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateCompilationRequest {

    private List<Long> events = new ArrayList<>();

    private Boolean pinned = false;

    @NotBlank
    @Length(min = 1, max = 50, message = "Название подборки должен быть не короче 1 и не длинее 50 символов")
    private String title;
}
