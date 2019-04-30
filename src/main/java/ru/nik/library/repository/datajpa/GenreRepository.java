package ru.nik.library.repository.datajpa;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.repository.CrudRepository;
import ru.nik.library.domain.Genre;

import java.util.List;

public interface GenreRepository extends CrudRepository<Genre, Integer> {
    List<Genre> findAll();

    List<Genre> findAllByNameIn(String... names);

    Genre findByName(String name);

    Genre findById(int id);

    void deleteById(int id);

    void deleteByName(String name);
}
