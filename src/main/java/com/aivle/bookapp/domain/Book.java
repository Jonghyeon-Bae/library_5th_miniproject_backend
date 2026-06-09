package com.aivle.bookapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
// @Table(name="Book2")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Setter 
    @Column(nullable = false, length = 200)
    @NotBlank(message = "Title is required")
    private String title;
    
    @Setter 
    @Column(nullable = false)
    @NotBlank(message = "Author is required")
    private String author;



    // public Book(Long id, String title, String author) {
    //     this.id = id;
    //     this.title = title;
    //     this.author = author;
    // }

    // public void setId (Long id){
    //     this.id = id;
    // }

    // public void setTitle (String title){
    //     this.title = title;
    // }

    // public void setAuthor (String author){
    //     this.author = author;
    // }

    // public Long getId(){
    //     return this.id;
    // }

    // public String getTitle(){
    //     return this.title;
    // }

    // public String getAuthor(){
    //     return this.author;
    // }
}
