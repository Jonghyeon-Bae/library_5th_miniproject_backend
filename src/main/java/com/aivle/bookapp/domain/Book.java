package com.aivle.bookapp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.w3c.dom.Text;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOOKS")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) //날짜 자동생성
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

    //내용
    @Setter
    @Column(nullable = false)
    private Text contents;

    @Setter
    @Column(nullable = false)
    private String author;

    @Setter
    @Column(nullable = false)
    private String publisher;

    @Setter
    @Column
    private String thumbnail;

    @Setter
    @Column
    private Boolean is_available;

    @Setter
    @Column
    private Boolean bestbook;

    @Setter
    @Column
    private Integer like_count;

    @Setter
    @Column
    private Text ai_review;

    @Setter
    @Column
    private String isbn13;

    @Setter
    @Column
    private String category;

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
