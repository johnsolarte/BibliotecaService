package com.techsolutions.biblioteca.service;

import com.techsolutions.biblioteca.dto.PrestamoDTO;
import com.techsolutions.biblioteca.entity.Libro;
import com.techsolutions.biblioteca.entity.Prestamo;
import com.techsolutions.biblioteca.entity.Usuario;
import com.techsolutions.biblioteca.repository.LibroRepository;
import com.techsolutions.biblioteca.repository.PrestamoRepository;
import com.techsolutions.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LibroRepository libroRepository;

    public PrestamoDTO createPrestamo(PrestamoDTO prestamoDTO) {
        Usuario usuario = usuarioRepository.findById(prestamoDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Libro libro = libroRepository.findById(prestamoDTO.getLibroId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        // Validar reglas de negocio
        if (!libro.getDisponible()) {
            throw new RuntimeException("El libro no está disponible para préstamo");
        }

        long prestamosActivos = prestamoRepository.countPrestamosActivosByUsuario(usuario.getId());
        if (prestamosActivos >= 3) {
            throw new RuntimeException("El usuario ya tiene 3 préstamos activos");
        }

        // Crear préstamo
        Prestamo prestamo = new Prestamo(usuario, libro);
        prestamo = prestamoRepository.save(prestamo);

        // Marcar libro como no disponible
        libro.setDisponible(false);
        libroRepository.save(libro);

        return convertToDTO(prestamo);
    }

    public PrestamoDTO returnBook(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if (prestamo.getDevuelto()) {
            throw new RuntimeException("El libro ya fue devuelto");
        }

        prestamo.setDevuelto(true);
        prestamo.setFechaDevolucion(LocalDateTime.now());
        prestamo = prestamoRepository.save(prestamo);

        // Marcar libro como disponible
        Libro libro = prestamo.getLibro();
        libro.setDisponible(true);
        libroRepository.save(libro);

        return convertToDTO(prestamo);
    }

    public List<PrestamoDTO> findOverdue() {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(30);
        return prestamoRepository.findPrestamosVencidos(fechaLimite)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PrestamoDTO> findByUsuario(Long usuarioId) {
        return prestamoRepository.findByUsuarioIdOrderByFechaPrestamoDesc(usuarioId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PrestamoDTO convertToDTO(Prestamo prestamo) {
        PrestamoDTO dto = new PrestamoDTO();
        dto.setId(prestamo.getId());
        dto.setUsuarioId(prestamo.getUsuario().getId());
        dto.setLibroId(prestamo.getLibro().getId());
        dto.setFechaPrestamo(prestamo.getFechaPrestamo());
        dto.setFechaDevolucion(prestamo.getFechaDevolucion());
        dto.setDevuelto(prestamo.getDevuelto());
        dto.setUsuarioNombre(prestamo.getUsuario().getNombre());
        dto.setLibroTitulo(prestamo.getLibro().getTitulo());
        return dto;
    }
}
