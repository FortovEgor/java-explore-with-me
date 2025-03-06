package ru.practicum.compilation.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class UpdateCompilationRequest {

    private List<Long> events;

    private Boolean pinned;

    @Length(min = 1, max = 50, message = "Название подборки должен быть не короче 1 и не длинее 50 символов")
    private String title;
}
