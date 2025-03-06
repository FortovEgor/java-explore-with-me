package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.validator.FutureCustom;

import java.time.LocalDateTime;

@Data
public class CreateEventRequest {
    @NotBlank(message = "У события должно быть название")
    @Size(min = 3, max = 120, message = "Название события должно быть не короче 3 и не длинее 120 символов")
    private String title;

    @NotBlank(message = "У события должна быть аннотация")
    @Size(min = 20, max = 2000, message = "Аннотация события должно быть не короче 20 и не длинее 2000 символов")
    private String annotation;

    @NotNull(message = "У события должен быть id")
    private Long category;

    @NotBlank(message = "У события должно быть описание")
    @Size(min = 20, max = 7000, message = "Описание события должно быть не короче 20 и не длинее 7000 символов")
    private String description;

    @FutureCustom(seconds = 2 * 60 * 60)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    private Boolean paid = false;

    @PositiveOrZero
    private Integer participantLimit = 0;

    private Boolean requestModeration = true;
}
