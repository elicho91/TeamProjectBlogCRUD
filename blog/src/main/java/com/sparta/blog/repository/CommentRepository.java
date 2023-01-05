package com.sparta.blog.repository;


import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Modifying
    @Transactional
    @Query("update Comment c set c.refOrder = c.refOrder+1 where c.refOrder > :num and c.post = :post")
    int updateReforder(@Param("num")int num, Post post);

}
