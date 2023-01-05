package com.sparta.blog.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> readAll() {
        List<Category> categoriesAllList = categoryRepository.findAllByOrderByNameAsc();
        List<Category> parentCateogryList = categoryRepository.findCategoriesByLayer(0);
        return parentCateogryList.stream().map(parentCateogryList1 -> new CategoryResponse(parentCateogryList1, categoriesAllList)).collect(Collectors.toList());
    }

    @Transactional
    public void createParentCategory(CategoryRequest req) {
        Optional<Category> checkCategoryIsPresent = categoryRepository.findByName(req.getName());
        if (checkCategoryIsPresent.isPresent()) {
            throw new IllegalArgumentException("Exist category name");
        }
        Category category = new Category(req);
        categoryRepository.save(category);
    }

    @Transactional
    public void createChildrenCategory(long id, CategoryRequest req) {
        Category category = categoryRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        int layer = category.getLayer() + 1;
        Category category1 = new Category(req.getName(), category.getName(), layer);
        categoryRepository.save(category1);
    }

    @Transactional
    public void delete(Long id) {
        if (notExistsCategory(id)) throw new IllegalStateException("ERORR");
        categoryRepository.deleteById(id);
    }

    private boolean notExistsCategory(Long id) {
        return !categoryRepository.existsById(id);
    }
}