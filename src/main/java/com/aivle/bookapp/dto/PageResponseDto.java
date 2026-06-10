package com.aivle.bookapp.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;
import java.util.List;

//응답형태 맞춰줄 DTO 클래스
@Getter
public class PageResponseDto<T> {
    private List<T> content;
    private int page;
    private long totalElements;

    // Spring Data JPA의 Page 객체를 받아서 명세서 규격에 맞게 변환
    public PageResponseDto(Page<T> page) {
        this.content = page.getContent();
        this.page = page.getNumber(); // 현재 페이지 번호 (0부터 시작)
        this.totalElements = page.getTotalElements(); // 전체 데이터 개수
    }
}
