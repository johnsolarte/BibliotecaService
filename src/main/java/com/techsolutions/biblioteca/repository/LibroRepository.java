package com.techsolutions.biblioteca.repository;

import com.techsolutions.biblioteca.entity.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    boolean existsByIsbn(String isbn);

    @Query("SELECT l FROM Libro l WHERE " +
            "(:titulo IS NULL OR LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) AND " +
            "(:autor IS NULL OR LOWER(CONCAT(l.autor.nombre, ' ', l.autor.apellido)) LIKE LOWER(CONCAT('%', :autor, '%'))) AND " +
            "(:genero IS NULL OR LOWER(l.genero) LIKE LOWER(CONCAT('%', :genero, '%')))")
    Page<Libro> findByFilters(@Param("titulo") String titulo,
                              @Param("autor") String autor,
                              @Param("genero") String genero,
                              Pageable pageable);
}