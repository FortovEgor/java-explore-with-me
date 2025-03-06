package ru.practicum.category;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CreateCategoryRequest;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CategoryMapper {
    Category toCategory(CreateCategoryRequest request);

    CategoryDto toDto(Category category);

    List<CategoryDto> toDto(List<Category> categories);
}
