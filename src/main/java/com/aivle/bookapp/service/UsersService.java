package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.User;
import com.aivle.bookapp.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    // 회원 조회
    @Transactional(readOnly = true)
    public User findById(Long id){
        return usersRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(id + "를 가진 유저를 찾을 수 없습니다."));
    }

    // 전체 회원 조회
    @Transactional(readOnly = true)
    public List<User> findAll(){
        return usersRepository.findAll();
    }

    // 회원가입
    @Transactional
    public User createUser(User user){
        if(usersRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        return usersRepository.save(user);
    }

    // 이메일로 회원 찾기
    @Transactional(readOnly = true)
    public User findByEmail(String email){
        return usersRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(email + "를 가진 유저를 찾을 수 없습니다."));
    }

    // 프로필 수정
    @Transactional
    public User updateProfile(
            Long userId,
            String name,
            String avatar,
            Boolean emailVisibility){

        User user = findById(userId);

        user.updateProfile(
                name,
                avatar,
                emailVisibility
        );

        return user;
    }

    // 비밀번호 변경
    @Transactional
    public User changePassword(
            Long userId,
            String newPassword){

        User user = findById(userId);

        user.changePassword(newPassword);

        return user;
    }

    // 이메일 인증
    @Transactional
    public User verifyEmail(Long userId){

        User user = findById(userId);

        user.verifyAccount();

        return user;
    }

    // 프로필 사진 변경
    @Transactional
    public User updateAvatar(
            Long userId,
            String avatarUrl){

        User user = findById(userId);

        user.updateAvatar(avatarUrl);

        return user;
    }

    // 이메일 공개 여부 변경
    @Transactional
    public User toggleEmailVisibility(Long userId){

        User user = findById(userId);

        user.changeEmailVisibility();

        return user;
    }

    // 회원 삭제
    @Transactional
    public User deleteUser(Long id){

        User user = findById(id);

        usersRepository.delete(user);

        return user;
    }

    // 회원 수 조회
    @Transactional(readOnly = true)
    public String getCount(){

        return String.valueOf(usersRepository.count());
    }
}