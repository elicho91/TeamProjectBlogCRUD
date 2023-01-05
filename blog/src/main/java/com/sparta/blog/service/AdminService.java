package com.sparta.blog.service;

import com.sparta.blog.dto.request.CommentRequestDto;
import com.sparta.blog.dto.request.PostRequestDto;
import com.sparta.blog.dto.response.CommentResponseDto;
import com.sparta.blog.dto.response.PostResponseDto;
import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.Post;
import com.sparta.blog.repository.CommentRepository;
import com.sparta.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostResponseDto updatePostByAdmin(Long postId, PostRequestDto requestDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        post.update(requestDto);
        return new PostResponseDto(post);
    }

    public ResponseEntity<String> deletePostByAdmin(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        postRepository.deleteById(id);
        return new ResponseEntity<>("삭제 성공!", HttpStatus.OK);
    }

    @Transactional
    public void deleteCommentByAdmin(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );
        commentRepository.delete(comment);
    }

    @Transactional
    public CommentResponseDto updateCommentByAdmin(Long postId, Long commentId, CommentRequestDto requestDto) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );
        comment.updateComment(requestDto);
        return new CommentResponseDto(comment);
    }
}
