package com.example.service;

import com.example.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookService {

    List<Book> getAllBooks();

    Page<Book> getAllBooks(Pageable pageable);

    Book getBookById(Integer id);

    ResponseEntity<Book> addBook(Book book) throws Exception;

    Book updateBook(Integer id, Book updatedBook);

    void deleteBookById(Integer id);
}
