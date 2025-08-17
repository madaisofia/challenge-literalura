package com.literalura.repository;

import com.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE a.nacimiento <= ?1 AND a.fallecimiento >= ?1")
    List<Autor> findByNacimientoBeforeAndFallecimientoAfter(int año);
}
