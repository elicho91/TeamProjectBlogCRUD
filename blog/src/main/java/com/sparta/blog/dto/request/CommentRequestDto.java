package com.sparta.blog.dto.request;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private String contents;
    private Long id;


    public boolean isZeroId(){
        if(this.id==0){
            return true;
        }
        return false;
    }

}


