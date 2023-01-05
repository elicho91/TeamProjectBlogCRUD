package com.sparta.blog.controller;

import com.sparta.blog.dto.jwt.TokenResponseDto;
import com.sparta.blog.dto.user.SigninRequestDto;
import com.sparta.blog.dto.user.SignupRequestDto;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")

public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "Get SignUp", description = "Get SignUp Page")
    public String signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return "success";
    }
    @ResponseBody
    @PostMapping("/signin")
    @Operation(summary = "Post signin", description = "Post signIn page")
    public TokenResponseDto signin(@RequestBody SigninRequestDto signinRequestDto) {
        return userService.signin(signinRequestDto);
    }


    /**
     * Delete user
     * Writer by Park
     */
    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete User", description = "Delete User page")
    public String resign(@PathVariable("id") Long id,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userService.deleteUser(id, userDetails.getUser())) {
            return "Success delete user";
        } else {
            throw new IllegalArgumentException("Failed delete user");
        }
    }
}
