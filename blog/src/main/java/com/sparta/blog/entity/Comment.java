package com.sparta.blog.entity;

import com.sparta.blog.dto.request.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "post_id")
    private Post post;

    private String writer;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentLike> commentLikes = new ArrayList<>();


    ////////대댓글 로직처리//////

    @Column(nullable = false)
    private int ref;    //그룹번호

    @Column(nullable = false)
    private int deps;   //깊이

    @Column(nullable = false)
    private int refOrder; //정렬 인덱스

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();



    public Comment(CommentRequestDto commentRequest, User user, Post post,int deps, int refOrder, Comment parent) {
        this.writer = user.getUsername(); //코맨트 작성자 정보
        this.contents = commentRequest.getContents();
        this.post = post;   //ref
        this.deps = deps;
        this.refOrder = refOrder;
        this.parent = parent;
        this.ref = parent.getRef();


    }
    public Comment(CommentRequestDto commentRequest, User user, Post post,int deps, int refOrder,int ref) {
        this.writer = user.getUsername(); //코맨트 작성자 정보
        this.contents = commentRequest.getContents();
        this.post = post;   //ref
        this.deps = deps;
        this.refOrder = refOrder;
        this.parent = null;
        this.ref = ref;


    }

    public void updateComment(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }

    public boolean isCommentWriter(String inputUsername) {
        return this.writer.equals(inputUsername);
    }
}
