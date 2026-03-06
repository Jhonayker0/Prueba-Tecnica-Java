import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { LibraryService, Book } from '../../services/library';

@Component({
  selector: 'app-book-list',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './book-list.html',
  styleUrl: './book-list.css'
})
export class BookListComponent implements OnInit {
  books: Book[] = [];
  keyword: string = '';
  message: string = '';
  messageType: string = '';

  constructor(private libraryService: LibraryService) {}

  ngOnInit(): void {
    this.loadBooks();
  }

  loadBooks(): void {
    this.libraryService.getBooks().subscribe({
      next: (data: Book[]) => this.books = data,
      error: () => this.showMessage('Error al cargar libros', 'danger')
    });
  }

  search(): void {
    if (this.keyword.trim()) {
      this.libraryService.searchBooks(this.keyword).subscribe({
        next: (data: Book[]) => this.books = data,
        error: () => this.showMessage('Error en la búsqueda', 'danger')
      });
    } else {
      this.loadBooks();
    }
  }

  deleteBook(id: number): void {
    if (confirm('¿Estás seguro de eliminar este libro?')) {
      this.libraryService.deleteBook(id).subscribe({
        next: () => {
          this.showMessage('Libro eliminado correctamente', 'success');
          this.loadBooks();
        },
        error: () => this.showMessage('No se puede eliminar un libro prestado', 'danger')
      });
    }
  }

  showMessage(msg: string, type: string): void {
    this.message = msg;
    this.messageType = type;
    setTimeout(() => this.message = '', 3000);
  }
}
