package com.sparta.blog.controller;

import com.sparta.blog.dto.category.CategoryRequestDto;
import com.sparta.blog.dto.category.CategoryResponseDto;
import com.sparta.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class CategoryController {

    private final CategoryService categoryService;
    @GetMapping("/api/categories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "View all category", description = "View all category list Page")
    public List<CategoryResponseDto> getAllCategory() {
        return categoryService.getAllCategory();
    }




    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @PostMapping("/api/categories")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create category", description = "Create category Page")
    public ResponseEntity<String> createParentCategory(@RequestBody CategoryRequestDto req) {
            categoryService.createParentCategory(req);
            return new ResponseEntity<>("Create parent category ", HttpStatus.CREATED);
    }



    @PostMapping("/api/categories/{categoryParentId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create category", description = "Create category Page")
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    public ResponseEntity<String> createChildrenCategory(@PathVariable("categoryParentId") long id, @RequestBody CategoryRequestDto req) {
        categoryService.createChildrenCategory(id, req);
        return new ResponseEntity<>("Create children category ", HttpStatus.CREATED);

    }



    @DeleteMapping("/api/categories/{id}")
    @Operation(summary = "Delete category",description = "Delete category Page")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("Create children category ", HttpStatus.OK);

    }
}