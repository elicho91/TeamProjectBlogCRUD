package com.sparta.blog.dto.response;

import com.sparta.blog.entity.PostLike;
import lombok.Getter;

@Getter
public class PostLikeResponseDto {
    private String username;

    public PostLikeResponseDto(PostLike postLike) {
        this.username = postLike.getUsername();
    }
}
