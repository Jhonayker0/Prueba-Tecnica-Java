package com.example.library_manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.library_manager.exception.DuplicateIsbnException;
import com.example.library_manager.model.Book;
import com.example.library_manager.model.BookStatus;
import com.example.library_manager.model.Loan;
import com.example.library_manager.repository.BookRepository;
import com.example.library_manager.repository.LoanRepository;
import com.example.library_manager.service.LibraryService;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LibraryService libraryService;

    @Test
    @DisplayName("Registrar libro con ISBN duplicado debe fallar")
    void registerBook_duplicateIsbn_throwsException() {
        // Arrange
        when(bookRepository.findByIsbn("1234567890123"))
                .thenReturn(Optional.of(new Book()));
        Book newBook = new Book();
        newBook.setIsbn("1234567890123");
        // Act & Assert
        assertThrows(DuplicateIsbnException.class, () -> {
            libraryService.registerBook(newBook);
        });
    }

    @Test
    @DisplayName("Prestar libro disponible debe funcionar")
    void borrowBook_availableBook_success() {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        book.setStatus(com.example.library_manager.model.BookStatus.AVAILABLE);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(loanRepository.save(any(Loan.class))).thenAnswer(i -> i.getArgument(0));
        // Act
        Loan result = libraryService.borrowBook(1L, "PlaceHolder", "example@example.com");
        // Assert
        assertEquals(BookStatus.BORROWED, book.getStatus());
        assertEquals("PlaceHolder", result.getBorrowerName());
        assertEquals("example@example.com", result.getBorrowerEmail());
    }

    @Test
    @DisplayName("Devolver libro prestado debe actualizar estado")
    void returnBook_borrowedBook_updatesStatus() {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setBook(book);
        book.setStatus(com.example.library_manager.model.BookStatus.BORROWED);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArgument(0));
        // Act
        libraryService.returnBook(1L);
        // Assert
        assertEquals(BookStatus.AVAILABLE, book.getStatus());
    }
}
