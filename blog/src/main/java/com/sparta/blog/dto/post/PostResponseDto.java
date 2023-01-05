package com.sparta.blog.dto.post;

import com.sparta.blog.dto.comment.CommentResponseDto;
import com.sparta.blog.entity.Post;
import com.sparta.blog.entity.PostLike;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;

    private List<CommentResponseDto> comments;

    private int postLIkesCount;
    private List<PostLikeResponseDto> likedUsers;

    private String category;


    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUsername();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();

        this.postLIkesCount = post.getPostLikes().size();
        List<PostLikeResponseDto> likedUserList = new ArrayList<>();
        for (PostLike postLike : post.getPostLikes()) {
            likedUserList.add(new PostLikeResponseDto(postLike));
        }
        this.likedUsers = likedUserList;

        CommentResponseDto commentResponseDto = new CommentResponseDto();
        this.comments = commentResponseDto.recursionDTO(post,0,0);

        this.category = post.getCategory().getName();
    }
}
