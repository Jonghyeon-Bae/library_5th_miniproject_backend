package com.aivle.bookapp.service;

import java.util.List;

import com.aivle.bookapp.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.exception.BookNotFoundException;
import com.aivle.bookapp.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    // 책(ID)를 통한 책 조회
    @Transactional(readOnly = true)
    public Book findById(Long id){
        return bookRepository.findById(id).orElseThrow(()-> new BookNotFoundException(id));
        
    }

    // 모든 책 조회
    @Transactional(readOnly = true)
    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    // public String deleteBook(Long id){
    //     if (bookRepository.existsById(id)){
    //         bookRepository.deleteById(id);
    //         return "Delete Success : "+id;
    //     }throw new RuntimeException("Book not Found : "+id);
    // }

    // 책 개수 조회
    @Transactional(readOnly = true)
    public String getCount(){
        return String.valueOf(bookRepository.count());
    }
    // 책 제목 조회
    @Transactional(readOnly = true)
    public List<Book> searchByTitle(String title){
        return bookRepository.findByTitle(title);
    }
    // 책 저자 조회
    @Transactional(readOnly = true)
    public List<Book> searchByAuthor(String author){
        return bookRepository.findByAuthor(author);
    }
    // 키워드를 통한 책 제목조회
    @Transactional(readOnly = true)
    public List<Book> searchByTitleContaining(String keyword){
        return bookRepository.findByTitleContaining(keyword);
    }
    // 책 제목과 저자 동시 조회
    @Transactional(readOnly = true)
    public List<Book> searchByTitleAndAuthor(String title, String author){
        return bookRepository.findByTitleAndAuthor(title, author);
    }
    // 특정 작가의 도서 제목 목록 조회
    @Transactional(readOnly = true)
    public List<String> authorGetTitle(String author){
        List<Book> books = bookRepository.findByAuthor(author);
        return books.stream().map(b->b.getTitle()).toList();
    }
    // 정렬 기준(오름차순)으로 도서 목록을 페이징 처리하여 반환
    @Transactional(readOnly = true)
    public Page<Book> getPage(int page, int size,String sortBy){
        Sort sort = Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page,size,sort);
        return bookRepository.findAll(pageable);
    }
    // 책 생성
    @Transactional
    public Book createBook(Book book){
        return bookRepository.save(book);
    }

    // 책 수정
    @Transactional
    public Book updateBook(Long id,Book book){
        Book existBook = findById(id);
        if(book.getTitle()!=null){
            existBook.setTitle(book.getTitle());
        }
        if(book.getAuthor()!=null){
            existBook.setAuthor(book.getAuthor());
        }
        return bookRepository.save(existBook);
    }
    // 책 삭제
    @Transactional
    public Book deleteBook(Long id){
        Book book = findById(id);
        bookRepository.delete(book);
        return book;
    }
    // 책 대출
    @Transactional
    public Book borrowBook(Long bookId, Users borrower){
        Book book = findById(bookId);

        if(!book.getIs_available()){
            throw new RuntimeException("이미 대출 중인 책입니다.");
        }
        book.borrowBook(borrower);
        return book;
    }

    // 책 반납
    @Transactional
    public Book returnBook(Long bookId){

        Book book = findById(bookId);

        if(book.getIs_available()){
            throw new RuntimeException("이미 반납된 책입니다.");
        }

        book.returnBook();

        return book;
    }

    // 대출 가능한 책만 조회
    @Transactional(readOnly = true)
    public List<Book> findAvailableBooks(){
        return bookRepository.findByIsAvailableTrue();
    }

    // 특정 사용자가 등록한 책 조회
    @Transactional(readOnly = true)
    public List<Book> findBooksByUser(Long userId){
        return bookRepository.findByUserId(userId);
    }

    // 특정 사용자가 빌린 책 조회
    @Transactional(readOnly = true)
    public List<Book> findBorrowedBooks(Long borrowerId){
        return bookRepository.findByBorrowerId(borrowerId);
    }

    // 도서 카테고리 검색
    @Transactional(readOnly = true)
    public List<Book> searchByCategory(String category){
        return bookRepository.findByCategory(category);
    }

    // 강추 도서 조회
    @Transactional(readOnly = true)
    public List<Book> findBestBooks(){
        return bookRepository.findByBestbookTrue();
    }

    // 좋아요 증가
    @Transactional
    public Book increaseLike(Long bookId){

        Book book = findById(bookId);

        book.increaseLikeCount();

        return book;
    }

    // 좋아요 감소
    @Transactional
    public Book decreaseLike(Long bookId){

        Book book = findById(bookId);

        book.decreaseLikeCount();

        return book;
    }
}



