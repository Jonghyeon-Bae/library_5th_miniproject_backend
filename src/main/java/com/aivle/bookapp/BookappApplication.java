package com.aivle.bookapp;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookappApplication {

@Bean
CommandLineRunner init(BookRepository bookRepository) {
return args -> {

	Book book1 = new Book();
	Book book2 = new Book();
	Book book3 = new Book();
	Book book4 = new Book();
	Book book5 = new Book();
	
	book1.setTitle("오승헌의 직박구리 1");
	book1.setAuthor("오승헌");
	bookRepository.save(book1);
	book2.setTitle("오승헌의 직박구리 2");
	book2.setAuthor("오승헌");
	bookRepository.save(book2);
	book3.setTitle("오승헌의 직박구리 3");
	book3.setAuthor("오승헌");
	bookRepository.save(book3);
	book4.setTitle("오승헌의 직박구리 4");
	book4.setAuthor("오승헌");
	bookRepository.save(book4);
	book5.setTitle("오승헌의 직박구리 5");
	book5.setAuthor("오승헌");
	bookRepository.save(book5);

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