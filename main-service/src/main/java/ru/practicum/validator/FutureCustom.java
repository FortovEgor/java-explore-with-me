package ru.practicum.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureCustomValidator.class)
public @interface FutureCustom {
    String message() default "Invalid date";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    long seconds() default 0;
}
