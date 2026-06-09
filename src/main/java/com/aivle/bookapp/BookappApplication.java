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

};
}

public static void main(String[] args) {
SpringApplication.run(BookappApplication.class, args);
}
}