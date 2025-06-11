package com.techsolutions.biblioteca.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "El ISBN es obligatorio")
    @Column(unique = true, nullable = false)
    private String isbn;

    @NotNull(message = "El año de publicación es obligatorio")
    @Column(name = "ano_publicacion")
    private Integer anoPublicacion;

    private String genero;

    @Column(nullable = false)
    private Boolean disponible = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    @OneToMany(mappedBy = "libro", fetch = FetchType.LAZY)
    private List<Prestamo> prestamos;

    public Libro() {}

    public Libro(String titulo, String isbn, Integer anoPublicacion, String genero, Autor autor) {
        this.titulo = titulo;
        this.isbn = isbn;
        this.anoPublicacion = anoPublicacion;
        this.genero = genero;
        this.autor = autor;
        this.disponible = true;
    }

}