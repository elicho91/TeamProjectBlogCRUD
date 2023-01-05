package com.sparta.blog.controller;

import com.sparta.blog.dto.request.SigninRequestDto;
import com.sparta.blog.dto.request.SignupRequestDto;
import com.sparta.blog.entity.User;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.dto.request.TokenRequestDto;
import com.sparta.blog.dto.response.TokenResponseDto;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    @Operation(summary = "Get SignUp", description = "Get SignUp Page")
    public String signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return "success";
    }
    @ResponseBody
    @PostMapping("/signin")
    @Operation(summary = "Post signin", description = "Post signIn page")
    public TokenResponseDto login(@RequestBody SigninRequestDto signinRequestDto) {
        return userService.signin(signinRequestDto);
    }

    /**
     * Delete user
     * Writer by Park
     */
    @PreAuthorize("isAuthenticated() and (( #userDetails.username == principal.username ) or hasRole('ADMIN'))")
    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete User", description = "Delete User page")
    public String deleteUser(@PathVariable Long userId,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userService.deleteUser(userId, userDetails.getUser())) {
            return "Success delete user";
        } else {
            throw new IllegalArgumentException("Failed delete user");
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/reissue")
    @Operation(summary = "Reissue refresh token", description = "Reissue refresh token Page")
    public TokenResponseDto reissue(HttpServletRequest request, @RequestBody TokenRequestDto tokenRequestDto) {
        String resolvedAccessToken = jwtUtil.resolveAccessToken(tokenRequestDto.getAccessToken());
        //Access 토큰 username가져오기
        Authentication authenticationAccessToken = jwtUtil.getAuthentication(resolvedAccessToken);
        User accessUser = userService.findByUsername(authenticationAccessToken.getName());
        //Refrest 토큰 username가져오기
        String refreshToken = request.getHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER);
        String resolvedRefreshToken = jwtUtil.resolveRefreshToken(refreshToken);
        Authentication authenticationFreshToken = jwtUtil.getAuthentication(resolvedRefreshToken);
        User refreshUser = userService.findByUsername(authenticationFreshToken.getName());
        //두개 비교 후 맞으면 재발행
        if (accessUser == refreshUser) {
            return userService.reissue(refreshUser.getUsername(), refreshUser.getRole());
        }
        throw new IllegalStateException("Vaild Error");
    }
}
