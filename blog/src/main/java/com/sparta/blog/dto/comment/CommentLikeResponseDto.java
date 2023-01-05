package com.sparta.blog.dto.comment;

import com.sparta.blog.entity.CommentLike;
import lombok.Getter;

@Getter
public class CommentLikeResponseDto {
    private String username;

    public CommentLikeResponseDto(CommentLike commentLike) {
        this.username = commentLike.getUsername();
    }
}
