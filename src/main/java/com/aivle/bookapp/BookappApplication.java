package com.aivle.bookapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.repository.BookRepository;

@EnableJpaAuditing
@SpringBootApplication
public class BookappApplication {

@Bean
CommandLineRunner init(BookRepository bookRepository) {
return args -> {
    /* 기존 H2용 더미 데이터 생성 주석 처리 (MySQL 외래키 제약조건 방지)
	Book book1 = new Book();
	book1.setTitle("오승헌의 직박구리 1");
	book1.setAuthor("오승헌");
	book1.setContents("직박구리 내용 1");
	book1.setPublisher("오승헌 출판사");
	book1.setIs_available(true);
	book1.setBestbook(false);
	bookRepository.save(book1);
    */
};
}

public static void main(String[] args) {
SpringApplication.run(BookappApplication.class, args);
}
}