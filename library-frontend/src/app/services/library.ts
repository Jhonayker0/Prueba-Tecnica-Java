import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Book {
  id?: number;
  title: string;
  author: string;
  isbn: string;
  publicationYear: number;
  status?: string;
}

export interface Loan {
  id?: number;
  book: Book;
  borrowerName: string;
  borrowerEmail: string;
  loanDate?: string;
  dueDate?: string;
  returnDate?: string;
}

export interface loanRequest {
  bookId: number;
  borrowerName: string;
  borrowerEmail: string;
}

@Injectable({
  providedIn: 'root',
})

export class LibraryService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.apiUrl}/books`);
  }

  searchBooks(q: string): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.apiUrl}/books/search?q=${q}`);
  }

  createBook(book: Book): Observable<Book> {
    return this.http.post<Book>(`${this.apiUrl}/books`, book);
  }

  deleteBook(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/books/${id}`);
  }
  
  loanBook(loanRequest: loanRequest): Observable<Loan> {
    return this.http.post<Loan>(`${this.apiUrl}/loans`, loanRequest);
  }

  returnBook(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/loans/${id}/return`, {});
  }

  getActiveLoans(): Observable<Loan[]> {
    return this.http.get<Loan[]>(`${this.apiUrl}/loans/active`);
  }

  getOverdueLoans(): Observable<Loan[]> {
    return this.http.get<Loan[]>(`${this.apiUrl}/loans/overdue`);
  }
}
