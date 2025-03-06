package ru.practicum.compilation.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.Compilation;
import ru.practicum.compilation.CompilationMapper;
import ru.practicum.compilation.CompilationService;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CreateCompilationRequest;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationService service;
    private final CompilationMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody CreateCompilationRequest request) {
        Compilation compilation = service.createCompilation(request);
        return mapper.toDto(compilation);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable(name = "compId") Long compilationId,
                                            @Valid @RequestBody UpdateCompilationRequest request) {
        Compilation compilation = service.updateCompilation(compilationId, request);
        return mapper.toDto(compilation);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Void deleteCompilation(@PathVariable(name = "compId") Long compilationId) {
        service.deleteCompilationById(compilationId);
        return null;
    }
}
