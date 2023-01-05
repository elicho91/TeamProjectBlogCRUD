package com.sparta.blog.repository;

import com.sparta.blog.entity.Post;
import com.sparta.blog.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    PostLike findByUsernameAndPostId(String username, Long postId);
    List<PostLike> deleteByPostIdAndUsername(Long postId, String username);
}