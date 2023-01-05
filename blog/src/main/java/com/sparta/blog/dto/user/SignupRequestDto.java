package com.sparta.blog.dto.user;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {

    @Size(min = 4, max = 10, message = "최소 4자 이상 10자 이하여야 함")
    @Pattern(regexp = "^[a-z0-9]*$", message = "알파벳 소문자와 숫자로만 구성되야 함")
    private String username;

    @Size(min = 8, max = 15, message = "최소 8자 이상 15자 이하여야 함")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "알파벳 대소문자와 숫자로만 구성되야 함")
    private String password;

    private boolean admin = false;
    private String adminToken = "";
}
