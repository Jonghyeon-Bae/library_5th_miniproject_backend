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

	Book book1 = new Book();


	book1.setTitle("오승헌의 직박구리 1");
	book1.setAuthor("오승헌");
	bookRepository.save(book1);


// Book book = new Book();
// book.setTitle("Spring boot 입문");
// book.setAuthor("임한울");

// Book saved = bookRepository.save(book);

// System.out.println("책 id: " + saved.getId());

// Book book2 = bookRepository.findById(1L).orElseThrow(() -> new RuntimeException("책이 없음"));

// System.out.println("-------------------");
// System.out.println("book2의 id: " + book2.getId());
// System.out.println("book2의 title: " + book2.getTitle());
// System.out.println("book2의 author: " + book2.getAuthor());
};
}

public static void main(String[] args) {
SpringApplication.run(BookappApplication.class, args);
}
}