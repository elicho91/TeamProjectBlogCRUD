package com.sparta.blog.controller;

import com.sparta.blog.dto.comment.CommentRequestDto;
import com.sparta.blog.dto.comment.CommentResponseDto;
import com.sparta.blog.dto.post.PostRequestDto;
import com.sparta.blog.dto.post.PostResponseDto;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {

    private final AdminService adminService;
    private final JwtUtil jwtUtil;


    @PutMapping("/posts/{id}")
    @Operation(summary = "Update post by admin", description = "Update post by admin Page")
    public PostResponseDto updatePostByAdmin(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.updatePostByAdmin(id, requestDto);

    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "Delete post by admin", description = "Delete post by admin Page")
    public ResponseEntity<String> deletePostByAdmin(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.deletePostByAdmin(postId);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    @Operation(summary = "Update comment by admin", description = "Update comment by admin Page")
    public CommentResponseDto updateCommentByAdmin(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.updateCommentByAdmin(postId, commentId, requestDto);
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    @Operation(summary = "Delete comment by admin", description = "Delete comment by admin Page")
    public ResponseEntity<String> deleteCommentByAdmin(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        adminService.deleteCommentByAdmin(postId, commentId);
        return new ResponseEntity<>("댓글 삭제 완료", HttpStatus.OK);

    }

}
