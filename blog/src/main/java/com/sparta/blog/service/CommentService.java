package com.sparta.blog.service;

import com.sparta.blog.dto.request.CommentRequestDto;
import com.sparta.blog.dto.response.CommentResponseDto;
import com.sparta.blog.entity.*;
import com.sparta.blog.repository.CommentLikeRepository;
import com.sparta.blog.repository.PostRepository;
import com.sparta.blog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;
    static int group_num = 0;//코맨트 그룹화에 사용

    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto, User user) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        Comment comments = null;
        //코멘트 번호 유효성 검사
        if (!commentRequestDto.isZeroId()) {  //id값이 0이 아니라면
            comments = commentRepository.findById(commentRequestDto.getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 코멘트가 존재하지 않습니다.")
            );
        }
        Comment comment = null;
        //3.댓글 저장
        int deps;
        int refOrder;
        if (commentRequestDto.isZeroId()) { //id값이 0이면 즉, 대댓글이 아니라 댓글이라면
            deps = 0;
            refOrder = 0;
            comment = new Comment(commentRequestDto, user, post, deps, refOrder, group_num);
            group_num++;
            commentRepository.save(comment);
        } else {  //대댓글
            deps = comments.getDeps() + 1;
            //포스트에서 가장 높은 refOrder값을 찾는다
            //int max = commentRepository.findMaxReforder(posts);
            //코코맨트를 작성할 코맨트의 refOrder값을 찾는다
            int min = comments.getRefOrder();
            //기준 코맨트보다 refOrder가 큰 수들은 +1씩 올린다
            commentRepository.updateReforder(min, post);
            refOrder = comments.getRefOrder() + 1;
            comment = new Comment(commentRequestDto, user, post, deps, refOrder, comments);
            commentRepository.save(comment);
        }
        //반환
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long postId, Long commentId, CommentRequestDto requestDto, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );
        if (comment.isCommentWriter(user.getUsername())) { // 댓글의 작성자가 필요하네!!!!
            comment.updateComment(requestDto);
            return new CommentResponseDto(comment);
        } else {
            throw new IllegalArgumentException("작성자만 수정이 가능합니다.");
        }
    }

    @Transactional
    public ResponseEntity<String> deleteComment(Long postId, Long commentId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );
        if (comment.isCommentWriter(user.getUsername())) { // 댓글의 작성자가 필요하네!!!!
            commentRepository.delete(comment);
            return new ResponseEntity<>("해당 댓글이 삭제되었습니다.", HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("작성자만 삭제가 가능합니다.");
        }
    }

    @Transactional
    public ResponseEntity<String> likeComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        commentLikeRepository.save(new CommentLike(username, comment));
        return new ResponseEntity<>("You liked the comment", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> cancelLikedComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        commentLikeRepository.deleteByCommentIdAndUsername(commentId, username);
        return new ResponseEntity<>("You canceled the like on the post", HttpStatus.OK);
    }

}
