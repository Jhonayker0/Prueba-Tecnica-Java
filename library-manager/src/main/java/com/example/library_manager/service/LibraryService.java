package com.example.library_manager.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.library_manager.exception.BookNotAvailableException;
import com.example.library_manager.exception.BookNotFoundException;
import com.example.library_manager.model.Book;
import com.example.library_manager.model.BookStatus;
import com.example.library_manager.model.Loan;
import com.example.library_manager.repository.BookRepository;
import com.example.library_manager.repository.LoanRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LibraryService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    public LibraryService(BookRepository bookRepository, LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
    }

    // 1. Registrar nuevo libro
    public Book registerBook(Book book) {
        // Validar: ISBN único
        // Establecer createdAt = LocalDateTime.now()
        // Establecer status = AVAILABLE
        // Guardar y retornar
        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("ISBN must be unique");
        }
        book.setCreatedAt(LocalDateTime.now());
        book.setStatus(BookStatus.AVAILABLE);
        return bookRepository.save(book);
    }

    // 2. Realizar préstamo
    public Loan borrowBook(Long bookId, String borrowerName, String borrowerEmail) {
        // Validaciones:
        // - Libro existe
        // - Libro está AVAILABLE
        // - Email válido
        // Lógica:
        // 1. Cambiar estado libro a BORROWED
        // 2. Crear registro Loan
        // 3. loanDate = hoy
        // 4. dueDate = hoy + 14 días
        // 5. Guardar y retornar préstamo
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new BookNotAvailableException(book.getIsbn());
        }

        book.setStatus(BookStatus.BORROWED);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setBorrowerName(borrowerName);
        loan.setBorrowerEmail(borrowerEmail);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(loan.getLoanDate().plusDays(14));
        return loanRepository.save(loan);
    }

    // 3. Devolver libro
    public Book returnBook(Long loanId) {
        // 1. Buscar préstamo
        // 2. Verificar que no esté ya devuelto
        // 3. Establecer returnDate = hoy
        // 4. Cambiar estado libro a AVAILABLE
        // 5. Retornar libro actualizado
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found with id " + loanId));
        if (loan.getReturnDate() != null) {
            throw new IllegalStateException("Book already returned");
        }
        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);

        Book book = loan.getBook();
        book.setStatus(BookStatus.AVAILABLE);
        return bookRepository.save(book);
    }

    // 4. Buscar libros
    public List<Book> searchBooks(String keyword) {
        // Usar repositorio para búsqueda
        // Si keyword es null o vacío, retornar todos
        if (keyword == null || keyword.isBlank()) {
            return bookRepository.findAll();
        }
        return bookRepository.searchByTitleOrAuthor(keyword);
    }

    // 5. Obtener estadísticas
    public Map<String, Object> getLibraryStats() {
        // Retornar Map con:
        // - totalBooks
        // - availableBooks
        // - borrowedBooks
        // - activeLoans
        // - overdueLoans
        Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalBooks", bookRepository.count());
        stats.put("availableBooks", bookRepository.findByStatus(BookStatus.AVAILABLE).size());
        stats.put("borrowedBooks", bookRepository.findByStatus(BookStatus.BORROWED).size());
        stats.put("activeLoans", loanRepository.findByReturnDateIsNull().size());
        stats.put("overdueLoans", loanRepository.findOverdueLoans().size());
        return stats;
    }
}