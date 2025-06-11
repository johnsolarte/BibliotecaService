package com.techsolutions.biblioteca.controller;

import com.techsolutions.biblioteca.dto.PrestamoDTO;
import com.techsolutions.biblioteca.service.PrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@Tag(name = "Préstamos", description = "API para gestión de préstamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @PostMapping
    @Operation(summary = "Crear nuevo préstamo")
    public ResponseEntity<PrestamoDTO> createLoan(@Valid @RequestBody PrestamoDTO prestamoDTO) {
        PrestamoDTO nuevoPrestamo = prestamoService.createPrestamo(prestamoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPrestamo);
    }

    @PutMapping("/{id}/return")
    @Operation(summary = "Marcar préstamo como devuelto")
    public ResponseEntity<PrestamoDTO> returnBook(@PathVariable Long id) {
        PrestamoDTO prestamo = prestamoService.returnBook(id);
        return ResponseEntity.ok(prestamo);
    }

    @GetMapping("/overdue")
    @Operation(summary = "Obtener préstamos vencidos")
    public ResponseEntity<List<PrestamoDTO>> getOverdueLoans() {
        List<PrestamoDTO> prestamosVencidos = prestamoService.findOverdue();
        return ResponseEntity.ok(prestamosVencidos);
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Obtener préstamos por usuario")
    public ResponseEntity<List<PrestamoDTO>> getLoansByUser(@PathVariable Long userId) {
        List<PrestamoDTO> prestamos = prestamoService.findByUsuario(userId);
        return ResponseEntity.ok(prestamos);
    }
}