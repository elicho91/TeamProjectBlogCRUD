package com.sparta.blog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Writer By Park
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String access_token;
    private String refresh_token;


}
