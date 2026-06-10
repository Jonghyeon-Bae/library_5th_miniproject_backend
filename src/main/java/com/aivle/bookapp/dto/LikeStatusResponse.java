package com.aivle.bookapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LikeStatusResponse {
    private boolean liked;
    private long likeCount;
}
