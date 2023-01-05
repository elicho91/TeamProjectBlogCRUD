package com.sparta.blog.dto.post;

import lombok.Getter;

@Getter
public class PostRequestDto {
    private String title;
    private String contents;
    private String category;
}
