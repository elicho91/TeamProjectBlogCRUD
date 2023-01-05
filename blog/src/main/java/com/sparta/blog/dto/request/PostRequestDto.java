package com.sparta.blog.dto.request;

import lombok.Getter;

@Getter
public class PostRequestDto {
    private String title;
    private String contents;
    private String category;
}
