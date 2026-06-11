package com.aivle.bookapp.service;

import java.util.List;

import com.aivle.bookapp.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.domain.Like;
import com.aivle.bookapp.domain.User;
import com.aivle.bookapp.dto.LikeStatusResponse;
import com.aivle.bookapp.exception.BookNotFoundException;
import com.aivle.bookapp.repository.BookRepository;
import com.aivle.bookapp.repository.LikeRepository;
import com.aivle.bookapp.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final BookRepository bookRepository;
    private final UsersRepository usersRepository;

    /**
     * 특정 도서에 좋아요를 추가합니다.
     */
    @Transactional
    public void likeBook(Long bookId, String email) {
        User user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        if (likeRepository.existsByBookIdAndUserId(bookId, user.getId())) {
            // 변경: 중복 좋아요 방지 (409 Conflict)
            throw new RuntimeException("Like already exists: bookId=" + bookId);
            // (참고: LikeAlreadyExistsException 생성자 파라미터에 맞게 수정하셔도 좋습니다)
        }

        Like like = Like.builder()
                .book(book)
                .user(user)
                .build();
        
        likeRepository.save(like);
        book.increaseLikeCount();
    }

    /**
     * 특정 도서의 좋아요를 취소합니다.
     */
    @Transactional
    public void unlikeBook(Long bookId, String email) {
        User user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        Like like = likeRepository.findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new RuntimeException("좋아요 기록을 찾을 수 없습니다."));

        likeRepository.delete(like);
        book.decreaseLikeCount();
    }

    /**
     * 특정 도서의 좋아요 여부 및 총 좋아요 수를 반환합니다.
     */
    @Transactional(readOnly = true)
    public LikeStatusResponse getLikeStatus(Long bookId, String email) {
        User user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        boolean liked = likeRepository.existsByBookIdAndUserId(bookId, user.getId());

        return LikeStatusResponse.builder()
                .liked(liked)
                .likeCount(book.getLikeCount())
                .build();
    }

    /**
     * 사용자가 좋아요를 누른 도서 목록을 반환합니다.
     */
    @Transactional(readOnly = true)
    public List<Book> getLikedBooks(String email) {
        User user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        List<Like> likesList = likeRepository.findByUserId(user.getId());
        
        return likesList.stream()
                .map(Like::getBook)
                .toList();
    }

    // 특정 도서 좋아요 개수 조회
    @Transactional(readOnly = true)
    public long getLikeCount(Long bookId) {

        bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        return likeRepository.countByBookId(bookId);
    }

    // 특정 사용자의 특정 도서 좋아요 취소
    @Transactional
    public void cancelLike(Long bookId, Long userId) {

        if (!likeRepository.existsByBookIdAndUserId(bookId, userId)) {
            throw new RuntimeException("좋아요 기록이 존재하지 않습니다.");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        likeRepository.deleteByBookIdAndUserId(bookId, userId);

        book.decreaseLikeCount();
    }
}
