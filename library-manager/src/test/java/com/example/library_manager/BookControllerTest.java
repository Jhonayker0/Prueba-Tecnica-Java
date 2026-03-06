package com.example.library_manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.library_manager.controller.BookController;
import com.example.library_manager.model.Book;
import com.example.library_manager.model.BookStatus;
import com.example.library_manager.service.LibraryService;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private LibraryService libraryService;

    @InjectMocks
    private BookController bookController;

    @Test
    @DisplayName("POST /api/books retorna 201 Created")
    void createBook_validBook_returnsCreated() {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Check Book");
        book.setAuthor("PlaceHolder");
        book.setIsbn("1234567890123");
        book.setPublicationYear(2018);
        book.setStatus(BookStatus.AVAILABLE);

        when(libraryService.registerBook(any(Book.class))).thenReturn(book);

        // Act
        ResponseEntity<Book> response = bookController.createBook(book);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Check Book", response.getBody().getTitle());
    }

    @Test
    @DisplayName("GET /api/books retorna lista de libros")
    void getAllBooks_returnsBookList() {
        // Arrange
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Check Book");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Second Check");

        when(libraryService.searchBooks(null)).thenReturn(List.of(book1, book2));

        // Act
        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }
}