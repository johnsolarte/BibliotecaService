package com.techsolutions.biblioteca.repository;

import com.techsolutions.biblioteca.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    @Query("SELECT COUNT(p) FROM Prestamo p WHERE p.usuario.id = :usuarioId AND p.devuelto = false")
    long countPrestamosActivosByUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT p FROM Prestamo p WHERE p.devuelto = false AND p.fechaPrestamo < :fechaLimite")
    List<Prestamo> findPrestamosVencidos(@Param("fechaLimite") LocalDateTime fechaLimite);

    List<Prestamo> findByUsuarioIdOrderByFechaPrestamoDesc(Long usuarioId);

    boolean existsByLibroIdAndDevueltoFalse(Long libroId);
}