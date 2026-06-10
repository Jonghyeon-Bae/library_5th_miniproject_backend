package com.aivle.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
}
