package ru.practicum.category.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryMapper;
import ru.practicum.category.CategoryService;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CreateCategoryRequest;
import ru.practicum.category.dto.UpdatedCategoryRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryService service;
    private final CategoryMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        Category category = service.createCategory(request);
        return mapper.toDto(category);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteCategory(@PathVariable(name = "catId") Long categoryId) {
        service.deleteCategoryById(categoryId);
        return null;
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable(name = "catId") Long categoryId,
                                      @Valid @RequestBody UpdatedCategoryRequest request) {
        Category category = service.updateCategory(categoryId, request);
        return mapper.toDto(category);
    }
}
