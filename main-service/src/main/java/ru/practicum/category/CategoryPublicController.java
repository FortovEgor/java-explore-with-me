package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryPublicController {

    private final CategoryService service;
    private final CategoryMapper mapper;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") Long from,
                                                           @RequestParam(defaultValue = "10") Long size) {

        List<Category> categories = service.getCategories(from, size);
        return mapper.toDto(categories);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        Category category = service.getById(catId);
        return mapper.toDto(category);
    }
}
