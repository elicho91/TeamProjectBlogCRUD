package com.sparta.blog.dto.page;

import lombok.Getter;

@Getter
public class PageResponseDto {

    private int page;
    private int size;
    private String sortBy;
    private boolean isAsc;
}
