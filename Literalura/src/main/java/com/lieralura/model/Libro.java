package com.literalura.model;

import jakarta.persistence.*;

@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String idioma;
    private int descargas;

    @ManyToOne
    private Autor autor;

    public Libro() {}

    public Libro(String titulo, String idioma, int descargas, Autor autor) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.descargas = descargas;
        this.autor = autor;
    }

    // Getters y setters
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public int getDescargas() { return descargas; }
    public void setDescargas(int descargas) { this.descargas = descargas; }
    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }

    @Override
    public String toString() {
        return titulo + " - " + autor.getNombre() + " [" + idioma + "] Descargas: " + descargas;
    }
}
