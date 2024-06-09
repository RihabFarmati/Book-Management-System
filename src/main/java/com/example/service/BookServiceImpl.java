package com.example.service;

import com.example.exceptions.BookException;
import com.example.model.Book;
import com.example.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Integer id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElse(null);
    }

    @Override
    public ResponseEntity<Book> addBook(Book book) throws BookException {
        Optional<Book> existingBook = bookRepository.findBookByTitleAndAuth(book.getTitle(), book.getAuth());
        if (existingBook.isPresent()) {
            throw new BookException("Book already exist", "BOOK_ALREADY_EXIST");
        }

        return new ResponseEntity<>(bookRepository.save(book), HttpStatus.CREATED);
    }

    @Override
    public Book updateBook(Integer id, Book updatedBook) {
        return bookRepository.save(updatedBook);
    }

    @Override
    public void deleteBookById(Integer id) {
        bookRepository.deleteById(id);
    }
}
