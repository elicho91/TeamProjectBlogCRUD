package com.sparta.blog.entity;

import com.sparta.blog.dto.post.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    private String username;

    @OneToMany (mappedBy = "post", cascade = CascadeType.REMOVE)
    // 1.그룹순 2.깊이순 3.reforder순으로 정렬필요
    @OrderBy("ref asc , deps asc, refOrder asc")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany (mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostLike> postLikes  = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    public Post(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public Post(PostRequestDto requestDto, String username,Category category) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.category = category;
        this.username = username;
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }



}
