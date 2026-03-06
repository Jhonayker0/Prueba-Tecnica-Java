package com.example.library_manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity 
@Table(name = "loans") 
public class Loan { 
// Campos requeridos: 
// - id (autoincremental) 
// - book (ManyToOne con Book) 
// - borrowerName (no nulo) 
// - borrowerEmail (email válido) 
// - loanDate (fecha préstamo, no nula) 
// - dueDate (fecha devolución esperada = loanDate + 14 días) 
// - returnDate (fecha devolución real, nullable) 
// Método helper: isOverdue() 
// Retorna true si dueDate < hoy y returnDate es null 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @NotNull
    private String borrowerName;

    @NotNull
    @Email
    private String borrowerEmail;

    @NotNull
    private LocalDate loanDate = LocalDate.now();

    private LocalDate dueDate = loanDate.plusDays(14);

    private LocalDate returnDate;

    public boolean isOverdue() {
        return returnDate == null && dueDate.isBefore(LocalDate.now());
    }
    
}
