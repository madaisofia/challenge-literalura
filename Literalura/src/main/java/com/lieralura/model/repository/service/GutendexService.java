package com.literalura.service;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.literalura.model.Autor;
import com.literalura.model.Libro;
import com.literalura.repository.AutorRepository;
import com.literalura.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class GutendexService {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public GutendexService(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public String buscarYGuardarLibro(String titulo) {
        try {
            String url = "https://gutendex.com/books?search=" + titulo.replace(" ", "%20");

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            GutendexResponse gutendexResponse = mapper.readValue(response.body(), GutendexResponse.class);

            if (gutendexResponse.results == null || gutendexResponse.results.isEmpty()) {
                return "Libro no encontrado.";
            }
          
            GutendexBook resultado = gutendexResponse.results.get(0);

            
            if (libroRepository.existsByTitulo(resultado.title)) {
                return "El libro ya se encuentra registrado.";
            }

            
            GutendexAuthor autorJson = resultado.authors.get(0); // solo el primer autor
            Autor autor = autorRepository.findByNombre(autorJson.name)
                    .orElseGet(() -> {
                        Autor nuevo = new Autor();
                        nuevo.setNombre(autorJson.name);
                        nuevo.setNacimiento(autorJson.birth_year);
                        nuevo.setFallecimiento(autorJson.death_year);
                        return autorRepository.save(nuevo);
                    });

            
            Libro libro = new Libro();
            libro.setTitulo(resultado.title);
            libro.setIdioma(resultado.languages.get(0)); // solo primer idioma
            libro.setDescargas(resultado.download_count);
            libro.setAutor(autor);

            libroRepository.save(libro);

            return libro.toString();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error al buscar el libro.";
        }
    }

  
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GutendexResponse {
        public List<GutendexBook> results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GutendexBook {
        public String title;
        public List<GutendexAuthor> authors;
        public List<String> languages;
        @JsonAlias("download_count")
        public int download_count;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GutendexAuthor {
        public String name;
        @JsonAlias("birth_year")
        public Integer birth_year;
        @JsonAlias("death_year")
        public Integer death_year;
    }
}
