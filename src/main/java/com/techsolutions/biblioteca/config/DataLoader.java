package com.techsolutions.biblioteca.config;

import com.techsolutions.biblioteca.entity.Autor;
import com.techsolutions.biblioteca.entity.Libro;
import com.techsolutions.biblioteca.entity.Usuario;
import com.techsolutions.biblioteca.repository.AutorRepository;
import com.techsolutions.biblioteca.repository.LibroRepository;
import com.techsolutions.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        if (autorRepository.count() == 0) {
            // Crear autores
            Autor autor1 = new Autor("Gabriel", "García Márquez", LocalDate.of(1927, 3, 6), "Colombiano");
            Autor autor2 = new Autor("Isabel", "Allende", LocalDate.of(1942, 8, 2), "Chilena");
            Autor autor3 = new Autor("Mario", "Vargas Llosa", LocalDate.of(1936, 3, 28), "Peruano");

            autor1 = autorRepository.save(autor1);
            autor2 = autorRepository.save(autor2);
            autor3 = autorRepository.save(autor3);

            // Crear libros
            libroRepository.save(new Libro("Cien años de soledad", "978-0-06-088328-7", 1967, "Realismo Mágico", autor1));
            libroRepository.save(new Libro("El amor en los tiempos del cólera", "978-0-14-024857-3", 1985, "Romance", autor1));
            libroRepository.save(new Libro("La casa de los espíritus", "978-0-553-38356-4", 1982, "Realismo Mágico", autor2));
            libroRepository.save(new Libro("De amor y de sombra", "978-0-06-091507-8", 1984, "Drama", autor2));
            libroRepository.save(new Libro("La ciudad y los perros", "978-84-663-0002-4", 1963, "Drama", autor3));
            libroRepository.save(new Libro("Conversación en La Catedral", "978-84-663-0003-1", 1969, "Drama", autor3));

            // Crear usuarios
            usuarioRepository.save(new Usuario("Juan Pérez", "juan.perez@email.com"));
            usuarioRepository.save(new Usuario("María González", "maria.gonzalez@email.com"));
            usuarioRepository.save(new Usuario("Carlos Rodríguez", "carlos.rodriguez@email.com"));
            usuarioRepository.save(new Usuario("Ana Martínez", "ana.martinez@email.com"));

            System.out.println("Datos de prueba cargados correctamente!");
        }
    }
}