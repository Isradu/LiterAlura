package com.literalura.literalura.repository;

import com.literalura.literalura.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTitulo(String titulo);
    List<Libro> findByIdioma(String idioma);
    List<Libro> findTop10ByOrderByNumeroDescargasDesc();
}
