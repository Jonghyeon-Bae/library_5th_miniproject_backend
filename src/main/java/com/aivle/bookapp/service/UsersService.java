package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Users;
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
    public Users findById(Long id){
        return usersRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(id + "를 가진 유저를 찾을 수 없습니다."));
    }

    // 전체 회원 조회
    @Transactional(readOnly = true)
    public List<Users> findAll(){
        return usersRepository.findAll();
    }

    // 회원가입
    @Transactional
    public Users createUser(Users user){
        if(usersRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        return usersRepository.save(user);
    }

    // 이메일로 회원 찾기
    @Transactional(readOnly = true)
    public Users findByEmail(String email){
        return usersRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(email + "를 가진 유저를 찾을 수 없습니다."));
    }

    // 프로필 수정
    @Transactional
    public Users updateProfile(
            Long userId,
            String name,
            String avatar,
            Boolean emailVisibility){

        Users user = findById(userId);

        user.updateProfile(
                name,
                avatar,
                emailVisibility
        );

        return user;
    }

    // 비밀번호 변경
    @Transactional
    public Users changePassword(
            Long userId,
            String newPassword){

        Users user = findById(userId);

        user.changePassword(newPassword);

        return user;
    }

    // 이메일 인증
    @Transactional
    public Users verifyEmail(Long userId){

        Users user = findById(userId);

        user.changeVerified();

        return user;
    }

    // 프로필 사진 변경
    @Transactional
    public Users updateAvatar(
            Long userId,
            String avatarUrl){

        Users user = findById(userId);

        user.updateAvatar(avatarUrl);

        return user;
    }

    // 이메일 공개 여부 변경
    @Transactional
    public Users toggleEmailVisibility(Long userId){

        Users user = findById(userId);

        user.changeEmailVisibility();

        return user;
    }

    // 회원 삭제
    @Transactional
    public Users deleteUser(Long id){

        Users user = findById(id);

        usersRepository.delete(user);

        return user;
    }

    // 회원 수 조회
    @Transactional(readOnly = true)
    public String getCount(){

        return String.valueOf(usersRepository.count());
    }
}