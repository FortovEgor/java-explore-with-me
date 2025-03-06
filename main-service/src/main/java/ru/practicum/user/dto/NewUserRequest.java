package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewUserRequest {
    @NotBlank
    @Size(min = 2, max = 250, message = "Имя пользователя должно быть не короче 2 и не длинеее 250 символов")
    private String name;

    @NotNull
    @Email
    @Size(min = 6, max = 254, message = "Почта пользователя должна быть не короче 6 и не длинее 254 символов")
    private String email;
}
