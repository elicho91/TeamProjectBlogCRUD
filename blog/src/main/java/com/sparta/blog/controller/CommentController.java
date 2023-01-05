package com.sparta.blog.controller;

import com.sparta.blog.dto.request.CommentRequestDto;
import com.sparta.blog.dto.response.AuthenticatedUser;
import com.sparta.blog.dto.response.CommentResponseDto;
import com.sparta.blog.entity.CommentLike;
import com.sparta.blog.entity.PostLike;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.repository.CommentLikeRepository;
import com.sparta.blog.repository.PostRepository;
import com.sparta.blog.repository.UserRepository;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@SecurityRequirement(name = "Bearer Authentication")
public class CommentController {

    private final JwtUtil jwtUtil;
    private final CommentService commentService;
    private final CommentLikeRepository commentLikeRepository;

    @PostMapping("/{postId}/comments")
    @Operation(summary = "Create Comment", description = "Create Comment Page")

    public CommentResponseDto createComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, commentRequest, userDetails.getUser());
    }

    @PutMapping("/{postId}/comments/{commentId}")
    @Operation(summary = "Update Comment", description = "Update Comment Page")
    public CommentResponseDto updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(postId, commentId, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    @Operation(summary = "Delete Comment", description = "Delete Comment Page")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(postId, commentId, userDetails.getUser());
    }

@PostMapping("/{postId}/comments/{commentId}/like")
@Operation(summary = "Like Comment", description = "Like Comment Page")
public ResponseEntity<String> likePost(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    String username = userDetails.getUsername();
    CommentLike commentLike = commentLikeRepository.findByUsernameAndCommentId(username, commentId);
    if (commentLike == null) {
        return commentService.likeComment(commentId, username);
    } else {
        throw new IllegalArgumentException("You already did like on the comment");
    }
}

    @DeleteMapping("/{postId}/comments/{commentId}/like")
    @Operation(summary = "Cancel Liked Comment", description = "Cancel Liked Comment Page")
    public ResponseEntity<String> cancelLikedComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        CommentLike commentLike = commentLikeRepository.findByUsernameAndCommentId(username, commentId);
        if (commentLike != null) {
            return commentService.cancelLikedComment(commentId, username);
        } else {
            throw new IllegalArgumentException("You didn't like on the comment");
        }
    }

}
