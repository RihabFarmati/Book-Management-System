package com.example.controllers;

import com.example.models.Book;
import com.example.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/store")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<Page<Book>> getBooks(
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        Page<Book> booksPage = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(booksPage);
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to retrieve all books: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Integer id) {
        return bookService.getBookById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@RequestBody Book book) throws Exception {
        return bookService.addBook(book);
    }

    @PutMapping("/update/{id}")
    public Book updateBook(@PathVariable Integer id, @RequestBody Book updatedBook) {
        return bookService.updateBook(id, updatedBook);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBook(@PathVariable Integer id) {
        bookService.deleteBookById(id);
    }

}
