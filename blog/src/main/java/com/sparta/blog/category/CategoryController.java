package com.sparta.blog.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "View all category", description = "View all category list Page")
    public List<CategoryResponse> readAll() {
        return categoryService.readAll();
    }
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create category", description = "Create category Page")
    public ResponseEntity<String> createParent(@RequestBody CategoryRequest req) {
            categoryService.createParentCategory(req);
            return new ResponseEntity<>("Create parent category ", HttpStatus.CREATED);
    }

    @PostMapping("/{categoryParentId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create category", description = "Create category Page")
    public ResponseEntity<String> createChildren(@PathVariable("categoryParentId") long id, @RequestBody CategoryRequest req) {
        categoryService.createChildrenCategory(id, req);
        return new ResponseEntity<>("Create children category ", HttpStatus.CREATED);

    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Delete category",description = "Delete category Page")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
        return new ResponseEntity<>("Create children category ", HttpStatus.OK);

    }
}