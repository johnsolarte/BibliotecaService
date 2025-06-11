package com.techsolutions.biblioteca.service;

import com.techsolutions.biblioteca.dto.LibroDTO;
import com.techsolutions.biblioteca.entity.Autor;
import com.techsolutions.biblioteca.entity.Libro;
import com.techsolutions.biblioteca.repository.AutorRepository;
import com.techsolutions.biblioteca.repository.LibroRepository;
import com.techsolutions.biblioteca.repository.PrestamoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private LibroService libroService;

    private Autor autor;
    private Libro libro;
    private LibroDTO libroDTO;

    @BeforeEach
    void setUp() {
        autor = new Autor("Gabriel", "García Márquez", LocalDate.of(1927, 3, 6), "Colombiano");
        autor.setId(1L);

        libro = new Libro("Cien años de soledad", "978-0-06-088328-7", 1967, "Realismo Mágico", autor);
        libro.setId(1L);

        libroDTO = new LibroDTO();
        libroDTO.setTitulo("Cien años de soledad");
        libroDTO.setIsbn("978-0-06-088328-7");
        libroDTO.setAnoPublicacion(1967);
        libroDTO.setGenero("Realismo Mágico");
        libroDTO.setAutorId(1L);
    }

    @Test
    void testSaveLibro_Success() {
        // Given
        when(libroRepository.existsByIsbn(libroDTO.getIsbn())).thenReturn(false);
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(libroRepository.save(any(Libro.class))).thenReturn(libro);

        // When
        LibroDTO result = libroService.save(libroDTO);

        // Then
        assertNotNull(result);
        assertEquals("Cien años de soledad", result.getTitulo());
        assertEquals("978-0-06-088328-7", result.getIsbn());
        verify(libroRepository).save(any(Libro.class));
    }

    @Test
    void testSaveLibro_IsbnDuplicado() {
        // Given
        when(libroRepository.existsByIsbn(libroDTO.getIsbn())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            libroService.save(libroDTO);
        });

        assertEquals("Ya existe un libro con el ISBN: " + libroDTO.getIsbn(), exception.getMessage());
        verify(libroRepository, never()).save(any(Libro.class));
    }

    @Test
    void testFindById_Success() {
        // Given
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));

        // When
        LibroDTO result = libroService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Cien años de soledad", result.getTitulo());
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(libroRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            libroService.findById(1L);
        });

        assertEquals("Libro no encontrado con ID: 1", exception.getMessage());
    }
}