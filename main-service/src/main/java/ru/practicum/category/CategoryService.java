package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CreateCategoryRequest;
import ru.practicum.category.dto.UpdatedCategoryRequest;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repo;
    private final CategoryMapper mapper;

    @Transactional
    public Category createCategory(CreateCategoryRequest request) {
        log.info("creating category: {}", request);
        Category category = mapper.toCategory(request);
        return repo.save(category);
    }

    @Transactional
    public void deleteCategoryById(Long categoryId) {
        log.info("deleting category with id = {}", categoryId);
        repo.deleteById(categoryId);
    }

    @Transactional
    public Category updateCategory(Long categoryId, UpdatedCategoryRequest request) {
        log.info("updating category with id = {}, new category data: {}", categoryId, request);
        Category category = getCategoryById(categoryId);
        category.setName(request.getName());
        return repo.save(category);
    }

    public List<Category> getCategories(Long from, Long size) {
        log.info("getting {} categories from {}", size, from);
        return repo.getCategoriesWithFromAndOfSize(from, size);
    }

    public Category getCategoryById(Long categoryId) {
        log.info("getting category with id = {}", categoryId);
        return repo.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + categoryId + " не найдена"));
    }

    public List<Category> getCategoriesById(List<Long> ids) {
        log.info("getting categories with ids = {}", ids);
        return repo.findAllById(ids);
    }
}
