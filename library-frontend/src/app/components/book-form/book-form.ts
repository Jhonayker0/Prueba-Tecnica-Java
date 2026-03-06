import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { LibraryService, Book } from '../../services/library';

@Component({
  selector: 'app-book-form',
  imports: [FormsModule],
  templateUrl: './book-form.html',
  styleUrl: './book-form.css'
})
export class BookFormComponent {
  book: Book = {
    title: '',
    author: '',
    isbn: '',
    publicationYear: new Date().getFullYear()
  };

  errorMessage: string = '';

  constructor(
    private libraryService: LibraryService,
    private router: Router
  ) {}

  onSubmit(): void {
    this.libraryService.createBook(this.book).subscribe({
      next: () => this.router.navigate(['/books']),
      error: (err) => {
        if (err.error && typeof err.error === 'object') {
          // Errores de validación del backend (campo: mensaje)
          this.errorMessage = Object.values(err.error).join(', ');
        } else {
          this.errorMessage = err.error || 'Error al crear el libro';
        }
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/books']);
  }
}
