package com.sparta.blog.dto.request;

import lombok.Getter;

@Getter
public class PageRequestDTO {

    private int page;
    private int size;
    private String sortBy;
    private boolean isAsc;
}
