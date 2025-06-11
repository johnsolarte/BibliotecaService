package com.techsolutions.biblioteca.controller;

import com.techsolutions.biblioteca.entity.Autor;
import com.techsolutions.biblioteca.repository.AutorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Autores", description = "API para gestión de autores")
public class AutorController {

    @Autowired
    private AutorRepository autorRepository;

    @GetMapping
    @Operation(summary = "Listar todos los autores")
    public ResponseEntity<List<Autor>> getAllAuthors() {
        List<Autor> autores = autorRepository.findAll();
        return ResponseEntity.ok(autores);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener autor por ID")
    public ResponseEntity<Autor> getAuthorById(@PathVariable Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado con ID: " + id));
        return ResponseEntity.ok(autor);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo autor")
    public ResponseEntity<Autor> createAuthor(@Valid @RequestBody Autor autor) {
        Autor nuevoAutor = autorRepository.save(autor);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAutor);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar autor")
    public ResponseEntity<Autor> updateAuthor(@PathVariable Long id, @Valid @RequestBody Autor autor) {
        Autor autorExistente = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado con ID: " + id));

        autorExistente.setNombre(autor.getNombre());
        autorExistente.setApellido(autor.getApellido());
        autorExistente.setFechaNacimiento(autor.getFechaNacimiento());
        autorExistente.setNacionalidad(autor.getNacionalidad());

        Autor autorActualizado = autorRepository.save(autorExistente);
        return ResponseEntity.ok(autorActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar autor")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        autorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}