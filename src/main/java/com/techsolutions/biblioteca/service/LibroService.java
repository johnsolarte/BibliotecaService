package com.techsolutions.biblioteca.service;

import com.techsolutions.biblioteca.dto.LibroDTO;
import com.techsolutions.biblioteca.entity.Autor;
import com.techsolutions.biblioteca.entity.Libro;
import com.techsolutions.biblioteca.repository.AutorRepository;
import com.techsolutions.biblioteca.repository.LibroRepository;
import com.techsolutions.biblioteca.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    public Page<LibroDTO> findAll(Pageable pageable) {
        return libroRepository.findAll(pageable).map(this::convertToDTO);
    }

    public LibroDTO findById(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + id));
        return convertToDTO(libro);
    }

    public LibroDTO save(LibroDTO libroDTO) {
        if (libroRepository.existsByIsbn(libroDTO.getIsbn())) {
            throw new RuntimeException("Ya existe un libro con el ISBN: " + libroDTO.getIsbn());
        }

        Autor autor = autorRepository.findById(libroDTO.getAutorId())
                .orElseThrow(() -> new RuntimeException("Autor no encontrado con ID: " + libroDTO.getAutorId()));

        Libro libro = new Libro();
        libro.setTitulo(libroDTO.getTitulo());
        libro.setIsbn(libroDTO.getIsbn());
        libro.setAnoPublicacion(libroDTO.getAnoPublicacion());
        libro.setGenero(libroDTO.getGenero());
        libro.setAutor(autor);
        libro.setDisponible(true);

        libro = libroRepository.save(libro);
        return convertToDTO(libro);
    }

    public LibroDTO update(Long id, LibroDTO libroDTO) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + id));

        if (!libro.getIsbn().equals(libroDTO.getIsbn()) &&
                libroRepository.existsByIsbn(libroDTO.getIsbn())) {
            throw new RuntimeException("Ya existe un libro con el ISBN: " + libroDTO.getIsbn());
        }

        Autor autor = autorRepository.findById(libroDTO.getAutorId())
                .orElseThrow(() -> new RuntimeException("Autor no encontrado con ID: " + libroDTO.getAutorId()));

        libro.setTitulo(libroDTO.getTitulo());
        libro.setIsbn(libroDTO.getIsbn());
        libro.setAnoPublicacion(libroDTO.getAnoPublicacion());
        libro.setGenero(libroDTO.getGenero());
        libro.setAutor(autor);

        libro = libroRepository.save(libro);
        return convertToDTO(libro);
    }

    public void delete(Long id) {
        if (prestamoRepository.existsByLibroIdAndDevueltoFalse(id)) {
            throw new RuntimeException("No se puede eliminar el libro porque tiene préstamos activos");
        }
        libroRepository.deleteById(id);
    }

    public Page<LibroDTO> search(String titulo, String autor, String genero, Pageable pageable) {
        return libroRepository.findByFilters(titulo, autor, genero, pageable)
                .map(this::convertToDTO);
    }

    private LibroDTO convertToDTO(Libro libro) {
        LibroDTO dto = new LibroDTO();
        dto.setId(libro.getId());
        dto.setTitulo(libro.getTitulo());
        dto.setIsbn(libro.getIsbn());
        dto.setAnoPublicacion(libro.getAnoPublicacion());
        dto.setGenero(libro.getGenero());
        dto.setDisponible(libro.getDisponible());
        dto.setAutorId(libro.getAutor().getId());
        dto.setAutorNombre(libro.getAutor().getNombre() + " " + libro.getAutor().getApellido());
        return dto;
    }
}