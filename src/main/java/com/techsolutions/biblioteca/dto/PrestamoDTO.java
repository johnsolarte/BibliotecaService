package com.techsolutions.biblioteca.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrestamoDTO {
    @Id
    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El ID del libro es obligatorio")
    private Long libroId;

    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaDevolucion;
    private Boolean devuelto;

    private String usuarioNombre;
    private String libroTitulo;
}
