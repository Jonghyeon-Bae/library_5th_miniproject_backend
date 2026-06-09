package com.aivle.bookapp.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.w3c.dom.Text;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book")
@Getter
@NoArgsConstructor
@AllArgsConstructor
// @EntityListeners(AuditingEntityListener.class) //날짜 자동생성 -> 적용 되어있는걸로 확인됨.
public class Book {


    //기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //외래키 설정 1:M 관계, 책 등록자는 Not null
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    //외래키 설정, 1:M 관계, 대여자는 Null 허용
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id")
    private Users borrower;

    //제목
    @Setter
    @Column(nullable = false)
    private String title;

    // 저자
    @Setter
    @Column(nullable = false)
    private String author;
    // 출판사
    @Setter
    @Column(nullable = false)
    private String publisher;

    //내용
    @Setter
    @Column(nullable = false)
    private Text contents;
    // 표지
    @Setter
    @Column
    private String thumbnail;
    //대여 가능 여부
    @Setter
    @Column
    private Boolean is_available;
    // 베스트셀러 여부
    @Setter
    @Column
    private Boolean bestbook;

    // 쪼아요
    @Setter
    @Column
    private Integer like_count;
    // AI Review
    @Setter
    @Column
    private Text ai_review;
    // ISBN
    @Setter
    @Column
    private String isbn13;
    // 카테고리
    @Setter
    @Column
    private String category;
    // 판매량
    @Setter
    @Column
    private Integer sales;

    // 생성될 때 시간 자동 입력  업데이트
    @Setter
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created_at;

    // 수정될 때마다 시간 자동 변경
    @Setter
    @LastModifiedDate
    @Column
    private LocalDateTime updated_at;

}
