package com.example.library_manager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library_manager.model.Loan;
import com.example.library_manager.service.LibraryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LibraryService libraryService;

    public LoanController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // POST /api/loans → Crear préstamo
    // PUT /api/loans/{id}/return → Devolver libro
    // GET /api/loans/overdue → Préstamos vencidos
    // GET /api/loans/active → Préstamos activos
    // DTO para crear préstamo:
    public record LoanRequest(
            @NotNull Long bookId,
            @NotBlank String borrowerName,
            @Email String borrowerEmail) {
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@Valid @RequestBody LoanRequest request) {
        Loan loan = libraryService.borrowBook(request.bookId(), request.borrowerName(), request.borrowerEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<Void> returnBook(@PathVariable Long id) {
        libraryService.returnBook(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Loan>> getOverdueLoans() {
        return ResponseEntity.ok(libraryService.getOverdueLoans());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Loan>> getActiveLoans() {
        return ResponseEntity.ok(libraryService.getActiveLoans());
    }

}
