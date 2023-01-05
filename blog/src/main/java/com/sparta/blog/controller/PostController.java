package com.sparta.blog.controller;

import com.sparta.blog.dto.request.PageRequestDTO;
import com.sparta.blog.dto.request.PostRequestDto;
import com.sparta.blog.dto.response.PostResponseDto;
import com.sparta.blog.entity.PostLike;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.repository.PostLikeRepository;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
public class PostController {
    private final PostService postService;
    private final JwtUtil jwtUtil;
    private final PostLikeRepository postLikeRepository;

    @PostMapping("/posts")
    @Operation(summary = "Create Post", description = "Create post Page")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto, userDetails.getUser().getUsername());
    }

    @GetMapping("/posts")
    @Operation(summary = "View all post list", description = "View all post list Page")
    public List<PostResponseDto> getAllPost() {
        return postService.getAllPost();
    }

    @GetMapping("/posts/page")
    @Operation(summary = "Get paging post ", description = "Get paging post Page")
    public List<PostResponseDto> getPagingPost(@RequestBody PageRequestDTO pageRequestDTO){
        return postService.getPagingPost(pageRequestDTO);
    }

    @GetMapping("/posts/{postId}")
    @Operation(summary = "View only one post ", description = "View only one post Page")
    public PostResponseDto getPost(@PathVariable Long postId) {
        return postService.getPosts(postId);
    }

    @PutMapping("/posts/{postId}")
    @Operation(summary = "Update Post", description = "Update post Page")
    public PostResponseDto updatePost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(postId, requestDto, userDetails.getUser().getUsername());
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "Delete Post", description = "Delete post Page")
    public ResponseEntity<String> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(postId, userDetails.getUser().getUsername());
    }

    @PostMapping("/posts/{postId}/like")
    @Operation(summary = "Like post", description = "Like post Page")
    public ResponseEntity<String> likePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        PostLike postLike = postLikeRepository.findByUsernameAndPostId(username, postId);
        if (postLike == null) {
            return postService.likePost(postId, username);
        } else {
            throw new IllegalArgumentException("You already did like on the post");
        }
    }

    @DeleteMapping("/posts/{postId}/like")
    @Operation(summary = "Cancel liked post", description = "Cancel liked post Page")
    public ResponseEntity<String> cancelLikedPost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        PostLike postLike = postLikeRepository.findByUsernameAndPostId(username, postId);
        if (postLike != null) {
            return postService.cancelLikedPost(postId, username);
        } else {
            throw new IllegalArgumentException("You didn't like on the post");
        }
    }
}
