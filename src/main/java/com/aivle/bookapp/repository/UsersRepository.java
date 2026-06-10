// Users 엔티티를 위한 리포지토리 인터페이스
package com.aivle.bookapp.repository;

import com.aivle.bookapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    // 회원가입 시 이메일 중복 여부 확인
    boolean existsByEmail(String email);

    // 로그인 시 이메일을 기준으로 사용자 조회
    Optional<User> findByEmail(String email);
}