package com.example.library_manager.exception;

public class BookNotAvailableException extends RuntimeException { 
    public BookNotAvailableException(String isbn) { 
      super("Book with ISBN " + isbn + " is not available"); 
    } 
}
