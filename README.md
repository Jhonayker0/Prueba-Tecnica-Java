# Library Manager — Prueba Técnica Java

Sistema de gestión de biblioteca con backend en Spring Boot y frontend en Angular.

---

## Tecnologías

- Backend:        Java 25, Spring Boot 4
- Base de datos:  PostgreSQL 18
- Frontend:       Angular
- Build:          Maven

---

## Requisitos previos

- Java 25+
- Maven (incluido via `mvnw`)
- Node.js y Angular CLI (`npm install -g @angular/cli`)
- PostgreSQL corriendo en `localhost:5432`

---

## Configuración de la base de datos

1. Crear la base de datos en PostgreSQL:
```sql
CREATE DATABASE library_db;
```

2. Las credenciales por defecto configuradas en `application.properties`:
   - **Usuario**: `postgres`
   - **Contraseña**: `postgres`
   - **Puerto**: `5432`

---

## Ejecutar el backend

```bash
cd library-manager
.\mvnw spring-boot:run
```

El servidor inicia en: `http://localhost:8080`

---

## Ejecutar el frontend

```bash
cd library-frontend
npm install
ng serve
```

La aplicación abre en: `http://localhost:4200`

---

## Endpoints principales

### Libros
GET:     `/api/books` | Listar todos los libros
POST:    `/api/books` | Registrar nuevo libro
GET:     `/api/books/search?q={keyword}` | Buscar por título o autor
DELETE:  `/api/books/{id}` | Eliminar libro (solo si disponible)

### Préstamos
POST:   `/api/loans` | Registrar préstamo
PUT:    `/api/loans/{id}/return` | Devolver libro
GET:    `/api/loans/active` | Listar préstamos activos
GET:    `/api/loans/overdue` | Listar préstamos vencidos

---

## Ejecutar los tests

```bash
cd library-manager
.\mvnw test
```

5 tests unitarios:
- `LibraryServiceTest` (3): ISBN duplicado, préstamo exitoso, devolución de libro
- `BookControllerTest` (2): crear libro retorna 201, listar libros retorna lista

---

## Estructura del proyecto

```
Prueba Tecnica Java/
├── library-manager/        # Backend Spring Boot
│   └── src/
│       ├── main/java/      # Código fuente
│       └── test/java/      # Tests unitarios
└── library-frontend/       # Frontend Angular
    └── src/app/
        ├── components/     # BookList, BookForm, LoanManagement
        └── services/       # LibraryService (HTTP)
```
