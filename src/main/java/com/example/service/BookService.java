package com.example.service;

import com.example.model.Book;

import java.util.List;

public interface BookService {

    List<Book> getAllBooks();

    Book getBookById(Integer id);

    Book addBook(Book book);

    Book updateBook(Integer id, Book updatedBook);

    void deleteBookById(Integer id);
}
