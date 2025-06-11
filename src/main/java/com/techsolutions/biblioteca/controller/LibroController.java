package com.techsolutions.biblioteca.controller;

import com.techsolutions.biblioteca.dto.LibroDTO;
import com.techsolutions.biblioteca.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Libros", description = "API para gestión de libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping
    @Operation(summary = "Listar todos los libros con paginación")
    public ResponseEntity<Page<LibroDTO>> getAllBooks(Pageable pageable) {
        Page<LibroDTO> libros = libroService.findAll(pageable);
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener libro por ID")
    public ResponseEntity<LibroDTO> getBookById(@PathVariable Long id) {
        LibroDTO libro = libroService.findById(id);
        return ResponseEntity.ok(libro);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo libro")
    public ResponseEntity<LibroDTO> createBook(@Valid @RequestBody LibroDTO libroDTO) {
        LibroDTO nuevoLibro = libroService.save(libroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoLibro);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar libro")
    public ResponseEntity<LibroDTO> updateBook(@PathVariable Long id, @Valid @RequestBody LibroDTO libroDTO) {
        LibroDTO libroActualizado = libroService.update(id, libroDTO);
        return ResponseEntity.ok(libroActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar libro")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        libroService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar libros por filtros")
    public ResponseEntity<Page<LibroDTO>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            Pageable pageable) {
        Page<LibroDTO> libros = libroService.search(title, author, genre, pageable);
        return ResponseEntity.ok(libros);
    }
}