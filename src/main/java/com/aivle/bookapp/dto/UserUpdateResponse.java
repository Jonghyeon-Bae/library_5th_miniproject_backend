package com.aivle.bookapp.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserUpdateResponse {
    private Long id;
    private String email;
    private String name;
    private String avatar;
}
