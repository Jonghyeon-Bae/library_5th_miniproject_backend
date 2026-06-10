package com.aivle.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TokenResponse {
    private String token;
    private Long id;
    private String email;
    private String name;
    private String avatar;
    private LocalDateTime createdAt;
}
