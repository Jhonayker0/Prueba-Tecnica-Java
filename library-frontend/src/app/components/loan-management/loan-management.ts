import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Book, LibraryService, Loan } from '../../services/library';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-loan-management',
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './loan-management.html',
  styleUrl: './loan-management.css',
})

export class LoanManagementComponent implements OnInit {
  loans: Loan[] = [];
  books: Book[] = [];
  loanForm = {
    bookId: 0,
    borrowerName: '',
    borrowerEmail: ''
  }
  message: string = '';
  messageType: string = '';

  constructor(private libraryService: LibraryService) {}

  ngOnInit(): void {
    this.loadLoans();
    this.loadBooks();
  }

  loadLoans(): void {
    this.libraryService.getActiveLoans().subscribe({
      next: (data) => this.loans = data,
      error: () => this.showMessage('Error al cargar préstamos', 'danger')
    });
  }

  loadBooks(): void {
    this.libraryService.getBooks().subscribe({
      next: (data) => this.books = data.filter(b => b.status === 'AVAILABLE'),
      error: () => this.showMessage('Error al cargar libros', 'danger')
    });
  }

  borrowBook(): void {
    this.libraryService.loanBook(this.loanForm).subscribe({
      next: () => {
        this.showMessage('Préstamo registrado correctamente', 'success');
        this.loanForm = { bookId: 0, borrowerName: '', borrowerEmail: '' };
        this.loadLoans();
        this.loadBooks();
      },
      error: (err) => this.showMessage(err.error || 'Error al registrar préstamo', 'danger')
    });
  }

  returnBook(id: number): void {
    this.libraryService.returnBook(id).subscribe({
      next: () => {
        this.showMessage('Libro devuelto correctamente', 'success');
        this.loadLoans();
        this.loadBooks();
      },
      error: () => this.showMessage('Error al devolver el libro', 'danger')
    });
  }

  showMessage(msg: string, type: string): void {
    this.message = msg;
    this.messageType = type;
    setTimeout(() => this.message = '', 3000);
  }
}
