package com.sparta.blog.repository;

import com.sparta.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByModifiedAtDesc(); //내림차순 정렬
    Optional<Post> findByIdAndUsername(Long id, String username);
}
