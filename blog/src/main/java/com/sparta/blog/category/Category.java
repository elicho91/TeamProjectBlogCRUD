package com.sparta.blog.category;

import com.sparta.blog.entity.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Category Package Writer By Park
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(length = 30, nullable = false)
    private String name;
    private String parent;
    private int layer = 0;

    public Category(String name, String parent, int layer) {
        this.name = name;
        this.parent = parent;
        this.layer = layer;
    }

    public Category(CategoryRequest categoryRequest) {
        this.name = categoryRequest.getName();
    }

}