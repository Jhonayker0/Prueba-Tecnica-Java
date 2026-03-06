package com.example.library_manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity 
@Table(name = "books") 
public class Book { 
// Campos requeridos: 
// - id (autoincremental) 
// - title (no nulo) 
// - author (no nulo) 
// - isbn (único, no nulo, 13 caracteres) 
// - publicationYear (entre 1000 y año actual) 
// - status: AVAILABLE, BORROWED, RESERVED (enum) 
// - createdAt (fecha creación) 
// Validaciones: 
// - @NotNull en campos obligatorios 
// - @Size para ISBN 
// - @Min/@Max para año 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String author;

    @NotNull
    @Size(min = 13, max = 13)
    @Column(unique = true)
    private String isbn;

    @Min(1000)
    @Max(2026)
    private int publicationYear;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}