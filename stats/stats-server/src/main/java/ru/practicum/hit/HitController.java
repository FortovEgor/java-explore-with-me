package ru.practicum.hit;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.hit.model.Hit;

@Validated
@RestController
@RequestMapping("/hit")
@RequiredArgsConstructor
public class HitController {

    private final HitService service;
    private final HitMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto hit(@Valid @RequestBody CreateHitRequest request) {
        Hit hit = service.save(request);
        return mapper.toDto(hit);
    }
}
