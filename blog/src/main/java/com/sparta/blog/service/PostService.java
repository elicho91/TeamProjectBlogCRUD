package com.sparta.blog.service;

import com.sparta.blog.dto.page.PageResponseDto;
import com.sparta.blog.dto.post.PostRequestDto;
import com.sparta.blog.dto.post.PostResponseDto;
import com.sparta.blog.entity.Category;
import com.sparta.blog.entity.Post;
import com.sparta.blog.entity.PostLike;
import com.sparta.blog.repository.CategoryRepository;
import com.sparta.blog.repository.PostLikeRepository;
import com.sparta.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, String usernmae) {
        Category category = categoryRepository.findByName(postRequestDto.getCategory()).orElseThrow();
        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        Post post = postRepository.saveAndFlush(new Post(postRequestDto, usernmae,category));
        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPost() {
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> postResponseDto = new ArrayList<>();
        for (Post post : posts){
            postResponseDto.add(new PostResponseDto(post));
        }
        return postResponseDto;
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPagingPost(PageResponseDto dto) {
        int page = dto.getPage()-1;
        int size = dto.getSize();
        String sortBy = dto.getSortBy();
        boolean isAsc = dto.isAsc();
        // 페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Post> posts = postRepository.findAll(pageable);
        List<PostResponseDto> postResponseDto = new ArrayList<>();
        for(Post post : posts){
            PostResponseDto postListDTO = new PostResponseDto(post);
            postResponseDto.add(postListDTO);
        }
        return postResponseDto;

    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Not found post")
        );
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, String username) {
        Post post = postRepository.findByIdAndUsername(id, username).orElseThrow(
                () -> new IllegalArgumentException("Not found post")
        );
        post.update(requestDto);
        return new PostResponseDto(post);
    }

    @Transactional
    public ResponseEntity<String> deletePost(Long id, String username) {
        Post post = postRepository.findByIdAndUsername(id, username).orElseThrow(
                () -> new IllegalArgumentException("Not found post")
        );
        postRepository.deleteById(id);
        return new ResponseEntity<>("Not found post", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> likePost(Long id, String username) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Not found post")
        );
        postLikeRepository.save(new PostLike(username, post));
        return new ResponseEntity<>("Success like post", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> cancelLikedPost(Long id, String username) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Not found post")
        );
        postLikeRepository.deleteByPostIdAndUsername(id, username);
        return new ResponseEntity<>("Cancel like post", HttpStatus.OK);
    }
}