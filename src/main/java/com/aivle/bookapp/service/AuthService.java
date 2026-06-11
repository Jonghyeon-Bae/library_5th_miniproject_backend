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

        String currentDbPassword = user.getPassword();
        boolean isMatched = false;

        // BCrypt 해시 형식인 경우 matches 사용, 그렇지 않은 경우 평문 비교 (하이브리드 대응)
        if (currentDbPassword != null && (currentDbPassword.startsWith("$2a$") || currentDbPassword.startsWith("$2b$") || currentDbPassword.startsWith("$2y$"))) {
            isMatched = passwordEncoder.matches(request.getPassword(), currentDbPassword);
        } else {
            isMatched = request.getPassword().equals(currentDbPassword);
        }

        if (!isMatched) {
            throw new RuntimeException("Invalid email or password");
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

    @Transactional
    public User updateProfile(String email, com.aivle.bookapp.dto.UpdateProfileRequest request) {
        User user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("[DEBUG] email: " + email);
        System.out.println("[DEBUG] request name: " + request.getName());
        System.out.println("[DEBUG] request currentPassword: [" + request.getCurrentPassword() + "]");
        System.out.println("[DEBUG] request newPassword: [" + request.getNewPassword() + "]");
        System.out.println("[DEBUG] DB password: [" + user.getPassword() + "]");

        // 이름 변경 (입력값이 있을 때만 변경)
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            user.updateProfile(request.getName(), user.getAvatar(), user.getEmailVisibility());
        }

        // 비밀번호 변경
        if (request.getNewPassword() != null && !request.getNewPassword().trim().isEmpty()) {
            if (request.getCurrentPassword() == null || request.getCurrentPassword().trim().isEmpty()) {
                throw new RuntimeException("Current password is required to change password");
            }
            
            String currentDbPassword = user.getPassword();
            boolean isMatched = false;

            // BCrypt 해시 형식인 경우 matches 사용, 그렇지 않은 경우 평문 비교 (하이브리드 대응)
            if (currentDbPassword != null && (currentDbPassword.startsWith("$2a$") || currentDbPassword.startsWith("$2b$") || currentDbPassword.startsWith("$2y$"))) {
                isMatched = passwordEncoder.matches(request.getCurrentPassword(), currentDbPassword);
            } else {
                isMatched = request.getCurrentPassword().equals(currentDbPassword);
            }

            System.out.println("[DEBUG] matches result: " + isMatched);
            if (!isMatched) {
                throw new RuntimeException("Current password does not match");
            }
            user.changePassword(passwordEncoder.encode(request.getNewPassword()));
        }

        return user;
    }
}
