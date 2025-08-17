package com.literalura;

import com.literalura.model.Autor;
import com.literalura.model.Libro;
import com.literalura.repository.AutorRepository;
import com.literalura.repository.LibroRepository;
import com.literalura.service.GutendexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n=== MENU LITERALURA ===");
            System.out.println("1. Buscar libro por título");
            System.out.println("2. Listar todos los libros");
            System.out.println("3. Listar todos los autores");
            System.out.println("4. Listar autores vivos en un año");
            System.out.println("5. Listar libros por idioma");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1" -> buscarLibro(scanner);
                case "2" -> listarLibros();
                case "3" -> listarAutores();
                case "4" -> listarAutoresVivos(scanner);
                case "5" -> listarLibrosPorIdioma(scanner);
                case "6" -> salir = true;
                default -> System.out.println("Opción inválida.");
            }
        }
        scanner.close();
    }

    private void buscarLibro(Scanner scanner) {
        System.out.print("Ingrese el título del libro: ");
        String titulo = scanner.nextLine();

        Optional<Libro> existe = libroRepository.findByTitulo(titulo);
        if (existe.isPresent()) {
            System.out.println("El libro ya existe en la base de datos.");
            return;
        }

        GutendexService gutendexService = new GutendexService();
        Libro libro = gutendexService.buscarLibroPorTitulo(titulo);

        if (libro == null) {
            System.out.println("Libro no encontrado en la API.");
            return;
        }

        autorRepository.save(libro.getAutor());
        libroRepository.save(libro);
        System.out.println("Libro agregado: " + libro);
    }

    private void listarLibros() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) System.out.println("No hay libros registrados.");
        else libros.forEach(System.out::println);
    }

    private void listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) System.out.println("No hay autores registrados.");
        else autores.forEach(System.out::println);
    }

    private void listarAutoresVivos(Scanner scanner) {
        System.out.print("Ingrese el año: ");
        int año = Integer.parseInt(scanner.nextLine());
        List<Autor> autoresVivos = autorRepository.findByNacimientoBeforeAndFallecimientoAfter(año);
        if (autoresVivos.isEmpty()) System.out.println("No hay autores vivos en ese año.");
        else autoresVivos.forEach(System.out::println);
    }

    private void listarLibrosPorIdioma(Scanner scanner) {
        System.out.print("Ingrese idioma (ES, EN, FR, PT): ");
        String idioma = scanner.nextLine();
        List<Libro> libros = libroRepository.findByIdioma(idioma);
        if (libros.isEmpty()) System.out.println("No hay libros en ese idioma.");
        else libros.forEach(System.out::println);
    }
  }
