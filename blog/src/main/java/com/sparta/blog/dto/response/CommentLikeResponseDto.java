package com.sparta.blog.dto.response;

import com.sparta.blog.entity.CommentLike;
import lombok.Getter;

@Getter
public class CommentLikeResponseDto {
    private String username;

    public CommentLikeResponseDto(CommentLike commentLike) {
        this.username = commentLike.getUsername();
    }
}
