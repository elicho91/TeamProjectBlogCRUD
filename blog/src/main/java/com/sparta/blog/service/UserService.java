package com.sparta.blog.service;

import com.sparta.blog.dto.request.SigninRequestDto;
import com.sparta.blog.dto.request.SignupRequestDto;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.dto.response.TokenResponseDto;
import com.sparta.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {

        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        //회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 role 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀렸습니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, role);
        userRepository.save(user);
    }

    /**
     * Writer by Park
     *
     * @param signinRequestDto
     * @return AccessToken, Refresh Token
     */
    @Transactional(readOnly = true)
    public TokenResponseDto signin(SigninRequestDto signinRequestDto) {
        String username = signinRequestDto.getUsername();
        String password = signinRequestDto.getPassword();
        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("미등록 사용자입니다.")
        );
        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String accessToken = jwtUtil.createToken(user.getUsername(), user.getRole());
        String refreshToken1 = jwtUtil.refreshToken(user.getUsername(), user.getRole());
        return new TokenResponseDto(accessToken, refreshToken1);
    }


    /**
     * Writer by Park
     *
     * @param username, role
     * @return Reissue AccessToken, RefreshToken
     */
    @Transactional
    public TokenResponseDto reissue(String username, UserRoleEnum role) {
        String newCreatedToken = jwtUtil.createToken(username, role);
        String refreshToken1 = jwtUtil.refreshToken(username, role);
        return new TokenResponseDto(newCreatedToken, refreshToken1);
    }

    @Transactional
    public boolean deleteUser(Long id, User user) {
        //포스트 존재 여부 확인
        User users = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid User"));
        if (users.isWriter(user.getUsername()) || UserRoleEnum.ADMIN == user.getRole()) {
            userRepository.deleteById(id);
            return true;
        } else {
            throw new IllegalArgumentException("Invalid Writer");
        }
    }

    public User findByUsername(String name) {
        return userRepository.findByUsername(name).orElseThrow();
    }
}
