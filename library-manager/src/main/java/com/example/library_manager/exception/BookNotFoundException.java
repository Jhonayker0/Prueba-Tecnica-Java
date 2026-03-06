package com.example.library_manager.exception;

public class BookNotFoundException extends RuntimeException { 
    public BookNotFoundException(Long id) { 
        super("Book not found with id: " + id); 
    } 
}
