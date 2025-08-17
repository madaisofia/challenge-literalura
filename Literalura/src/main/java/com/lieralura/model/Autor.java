package com.literalura.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String nacimiento;
    private String fallecimiento;

    public Autor() {}

    public Autor(String nombre, String nacimiento, String fallecimiento) {
        this.nombre = nombre;
        this.nacimiento = nacimiento;
        this.fallecimiento = fallecimiento;
    }

    // Getters y setters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNacimiento() { return nacimiento; }
    public void setNacimiento(String nacimiento) { this.nacimiento = nacimiento; }
    public String getFallecimiento() { return fallecimiento; }
    public void setFallecimiento(String fallecimiento) { this.fallecimiento = fallecimiento; }

    @Override
    public String toString() {
        return nombre + " (" + nacimiento + " - " + fallecimiento + ")";
    }
}
