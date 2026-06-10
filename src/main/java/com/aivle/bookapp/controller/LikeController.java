package com.aivle.bookapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.dto.LikeStatusResponse;
import com.aivle.bookapp.service.LikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    private String getUsername(UserDetails userDetails) {
        if (userDetails == null) {
            return "testuser@example.com";
            // throw new RuntimeException("로그인이 필요한 서비스입니다. Authorization Bearer 토큰이 누락되었거나 유효하지 않습니다.");
        }
        return userDetails.getUsername();
    }

    /**
     * 특정 도서에 좋아요를 누릅니다.
     */
    @PostMapping("/api/books/{bookId}/like")
    public ResponseEntity<Void> likeBook(
            @PathVariable("bookId") Long bookId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        likeService.likeBook(bookId, getUsername(userDetails));
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 도서의 좋아요를 취소합니다.
     */
    @DeleteMapping("/api/books/{bookId}/like")
    public ResponseEntity<Void> unlikeBook(
            @PathVariable("bookId") Long bookId,
            @AuthenticationPrincipal UserDetails userDetails) {

        likeService.unlikeBook(bookId, getUsername(userDetails));
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 도서의 좋아요 상태(여부 및 총 개수)를 조회합니다.
     */
    @GetMapping("/api/books/{bookId}/like")
    public ResponseEntity<LikeStatusResponse> getLikeStatus(
            @PathVariable("bookId") Long bookId,
            @AuthenticationPrincipal UserDetails userDetails) {

        LikeStatusResponse status = likeService.getLikeStatus(bookId, getUsername(userDetails));
        return ResponseEntity.ok(status);
    }

    /**
     * 현재 사용자가 좋아요를 누른 도서 목록을 조회합니다.
     */
    @GetMapping("/api/books/liked")
    public ResponseEntity<List<Book>> getLikedBooks(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<Book> likedBooks = likeService.getLikedBooks(getUsername(userDetails));
        return ResponseEntity.ok(likedBooks);
    }
}
