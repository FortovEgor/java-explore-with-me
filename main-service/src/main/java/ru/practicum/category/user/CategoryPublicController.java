package ru.practicum.category.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryMapper;
import ru.practicum.category.CategoryService;
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
    public CategoryDto getCategory(@PathVariable(name = "catId") Long categoryId) {
        Category category = service.getCategoryById(categoryId);
        return mapper.toDto(category);
    }
}
