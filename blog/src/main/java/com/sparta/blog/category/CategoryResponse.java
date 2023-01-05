package com.sparta.blog.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private int layer;
    private List<CategoryResponse> children;

    public CategoryResponse(Category parentCategory, List<Category> listAllcategory) {
        this.id = parentCategory.getId();
        this.name = parentCategory.getName();
        this.layer = parentCategory.getLayer();

        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category ca : listAllcategory){
            if (parentCategory.getLayer() + 1 == ca.getLayer() && ca.getParent().equals(parentCategory.getName())) {
                categoryResponses.add(new CategoryResponse(ca,listAllcategory));
            }
            this.children = categoryResponses;
        }
    }
}