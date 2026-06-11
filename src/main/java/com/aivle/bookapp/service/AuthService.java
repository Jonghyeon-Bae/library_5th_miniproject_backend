package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.User;
import com.aivle.bookapp.dto.LoginRequest;
import com.aivle.bookapp.dto.SignUpRequest;
import com.aivle.bookapp.dto.TokenResponse;
import com.aivle.bookapp.config.security.JwtTokenProvider;
import com.aivle.bookapp.exception.EmailAlreadyExistsException;
import com.aivle.bookapp.exception.InvalidCredentialsException;
import com.aivle.bookapp.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public User signUp(SignUpRequest request) {
        //수정 :  커스텀 예외 사용 (GlobalExceptionHandler가 409 Conflict로 처리함)
        if (usersRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .avatar(request.getAvatar())
                .emailVisibility(true)
                .verified(false)
                .build();

        return usersRepository.save(user);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        User user = usersRepository.findByEmail(request.getEmail())
                // 수정 : 401 Unauthorized 처리
                .orElseThrow(() -> new InvalidCredentialsException());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // 수정
            throw new InvalidCredentialsException();
        }

        String token = jwtTokenProvider.createToken(user.getEmail());

        return TokenResponse.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .avatar(user.getAvatar())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
