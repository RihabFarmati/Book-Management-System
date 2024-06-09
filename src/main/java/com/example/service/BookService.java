package com.example.service;

import com.example.model.Book;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookService {

    List<Book> getAllBooks();

    Book getBookById(Integer id);

    ResponseEntity<Book> addBook(Book book) throws Exception;

    Book updateBook(Integer id, Book updatedBook);

    void deleteBookById(Integer id);
}
