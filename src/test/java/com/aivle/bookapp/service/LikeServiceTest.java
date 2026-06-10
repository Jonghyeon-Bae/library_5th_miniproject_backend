// package com.aivle.bookapp.service;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.junit.jupiter.api.Assertions.assertThrows;

// import java.util.List;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.transaction.annotation.Transactional;

// import com.aivle.bookapp.domain.Books;
// import com.aivle.bookapp.domain.Users;
// import com.aivle.bookapp.dto.LikeStatusResponse;
// import com.aivle.bookapp.repository.BookRepository;
// import com.aivle.bookapp.repository.LikeRepository;
// import com.aivle.bookapp.repository.UsersRepository;

// @SpringBootTest
// @Transactional
// class LikeServiceTest {

//     @Autowired
//     private LikeService likeService;

//     @Autowired
//     private UsersRepository usersRepository;

//     @Autowired
//     private BookRepository bookRepository;

//     @Autowired
//     private LikeRepository likeRepository;

//     private Users testUser;
//     private Books testBook;

//     @BeforeEach
//     void setUp() {
//         // 기존 데이터 정리
//         likeRepository.deleteAll();
//         bookRepository.deleteAll();
//         usersRepository.deleteAll();

//         // 테스트 유저 생성 및 저장
//         testUser = new Users();
//         testUser.setEmail("testuser@example.com");
//         testUser.setPassword("password123");
//         testUser.setName("테스트유저");
//         testUser.setAvatar("avatar.png");
//         testUser = usersRepository.save(testUser);

//         // 등록자 유저 생성 및 저장 (책 등록자용)
//         Users creator = new Users();
//         creator.setEmail("creator@example.com");
//         creator.setPassword("password123");
//         creator.setName("등록자");
//         creator = usersRepository.save(creator);

//         // 테스트 도서 생성 및 저장
//         testBook = new Books();
//         testBook.setUser(creator);
//         testBook.setTitle("테스트 책 제목");
//         testBook.setAuthor("테스트 저자");
//         testBook.setContents("테스트 내용");
//         testBook.setPublisher("테스트 출판사");
//         testBook.setLikeCount(0);
//         testBook = bookRepository.save(testBook);
//     }

//     @Test
//     void 좋아요_등록_및_취소_성공() {
//         // Given
//         Long bookId = testBook.getId();
//         String email = testUser.getEmail();

//         // When (좋아요 등록)
//         likeService.likeBook(bookId, email);

//         // Then (검증)
//         boolean isLiked = likeRepository.existsByBook_IdAndUsers_Id(bookId, testUser.getId());
//         assertThat(isLiked).isTrue();

//         Books updatedBook = bookRepository.findById(bookId).orElseThrow();
//         assertThat(updatedBook.getLikeCount()).isEqualTo(1);

//         // When (좋아요 상태 조회)
//         LikeStatusResponse status = likeService.getLikeStatus(bookId, email);
//         assertThat(status.isLiked()).isTrue();
//         assertThat(status.getLikeCount()).isEqualTo(1);

//         // When (좋아요 취소)
//         likeService.unlikeBook(bookId, email);

//         // Then (검증)
//         boolean isLikedAfterUnlike = likeRepository.existsByBook_IdAndUsers_Id(bookId, testUser.getId());
//         assertThat(isLikedAfterUnlike).isFalse();

//         Books unlikedBook = bookRepository.findById(bookId).orElseThrow();
//         assertThat(unlikedBook.getLikeCount()).isEqualTo(0);
//     }

//     @Test
//     void 중복_좋아요_요청시_예외발생() {
//         // Given
//         Long bookId = testBook.getId();
//         String email = testUser.getEmail();
//         likeService.likeBook(bookId, email);

//         // When & Then
//         assertThrows(RuntimeException.class, () -> {
//             likeService.likeBook(bookId, email);
//         });
//     }

//     @Test
//     void 존재하지_않는_책_좋아요_요청시_예외발생() {
//         // Given
//         Long invalidBookId = 9999L;
//         String email = testUser.getEmail();

//         // When & Then
//         assertThrows(RuntimeException.class, () -> {
//             likeService.likeBook(invalidBookId, email);
//         });
//     }

//     @Test
//     void 유저가_좋아요한_도서_목록_조회() {
//         // Given
//         Long bookId = testBook.getId();
//         String email = testUser.getEmail();
//         likeService.likeBook(bookId, email);

//         // When
//         List<Books> likedBooks = likeService.getLikedBooks(email);

//         // Then
//         assertThat(likedBooks).hasSize(1);
//         assertThat(likedBooks.get(0).getTitle()).isEqualTo("테스트 책 제목");
//     }
// }
