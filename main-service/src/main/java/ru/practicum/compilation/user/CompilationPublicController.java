package ru.practicum.compilation.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.Compilation;
import ru.practicum.compilation.CompilationMapper;
import ru.practicum.compilation.CompilationService;
import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

@Valid
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationPublicController {

    private final CompilationService service;
    private final CompilationMapper mapper;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") boolean pinned,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") long from,
                                                @Positive @RequestParam(defaultValue = "10") long size) {
        List<Compilation> compilations = service.getCompilations(pinned, from, size);
        return mapper.toDto(compilations);

    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable(name = "compId") Long compilationId) {
        Compilation compilation = service.getCompilationById(compilationId);
        return mapper.toDto(compilation);
    }
}
