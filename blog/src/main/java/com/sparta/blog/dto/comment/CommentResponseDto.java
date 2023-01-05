package com.sparta.blog.dto.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.CommentLike;
import com.sparta.blog.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponseDto {
    private Long id;
    private String username;
    private String contents;
    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;

    private int commentLikesCount;
    private List<CommentLikeResponseDto> likedUsers;
    private List<CommentResponseDto> commentList = new ArrayList<>();


    //새롭게 추가된 부분중 CommentResponseDto가 필요할땐 이걸 사용(ex-PostResponseDto 34라인)
    public CommentResponseDto(){

    }

    //기존로직에서 CommentResponseDto가 필요할땐 이걸 사용(ex-AdminService 68라인)
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }


    @JsonIgnore
    private int max_index;  //최대깊이 인덱스를 구한다
    @JsonIgnore
    private int ref;//그룹번호
    @JsonIgnore
    private int deps;//깊이\




    public void addDTO(Post post, int i){
        //그대로 삽입
        this.id = post.getComments().get(i).getId();
        this.username = post.getComments().get(i).getUsername();
        this.contents = post.getComments().get(i).getContents();

        this.commentLikesCount = post.getComments().get(i).getCommentLikes().size();
        List<CommentLikeResponseDto> likedUserList = new ArrayList<>();
        for (CommentLike commentLike : post.getComments().get(i).getCommentLikes()) {
            likedUserList.add(new CommentLikeResponseDto(commentLike));
        }
        this.likedUsers = likedUserList;

        this.max_index = i;
        this.ref = post.getComments().get(i).getRef();
        this.deps = post.getComments().get(i).getDeps();
    }


    public List<CommentResponseDto> recursionDTO(Post post,int i,int level){


        List<CommentResponseDto> dtoList = new ArrayList<>();
        while(post.getComments().size()>i) {
            CommentResponseDto dto = new CommentResponseDto();
            dto.addDTO(post, i);
            //작성해야하는 코멘트가 적절한 깊이에 있다면
            if(dto.getDeps() >= level){
                //마지막 노드가 아니면
                if (post.getComments().size() > i + 1) {

                    // 하위노드,동일노드,상위노드 판별
                    if (post.getComments().get(i + 1).getDeps() > post.getComments().get(i).getDeps()) {
                        //다음 노드가 하위노드
                        i++;
                        dto.commentList = dto.recursionDTO(post, i, level + 1);
                        //dto의 하위 리스트의 크기를 구한후 마지막에 나온 id값(하위 id중 최대값);
                        i = dto.commentList.get(dto.commentList.size() - 1).getMax_index() + 1;
                        dto.max_index = i;
                        dtoList.add(dto);
                    } else if (post.getComments().get(i + 1).getDeps() == post.getComments().get(i).getDeps()) {
                        //다음 노드가 동일노드
                        dtoList.add(dto);
                        i++;
                    } else {
                        //다음 노드가 상위노드
                        dtoList.add(dto);
                        return dtoList;
                    }
                } else {
                    dtoList.add(dto);
                    return dtoList;
                }
            }else{
                dtoList.get((dtoList.size()-1)).max_index -=1;
                return dtoList;
            }
        }
        return dtoList;
    }









}
