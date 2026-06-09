package com.aivle.bookapp.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="USERS")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column
    private String avatar;

    @Setter
    @Column
    private Boolean email_visibility;

    @Setter
    @Column
    private Boolean verified;

    //CreatedDate : 생성될 때 시간 자동 입력  업데이트,
    @Setter
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created_at;

    //LastModifiedDate : 수정될 때마다 시간 자동 변경
    @Setter
    @LastModifiedDate
    @Column
    private LocalDateTime updated_at;

}
