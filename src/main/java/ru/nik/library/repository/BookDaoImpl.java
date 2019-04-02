package ru.nik.library.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class BookDaoImpl implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public BookDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public int insert(Book book) {
        if (book.isNew()) {
            return namedParameterJdbcOperations.getJdbcOperations()
                    .update("insert into books (name, description, author, genre) values (?, ?, ?, ?) ",
                            book.getName(), book.getDescription(), book.getAuthor().getName(),
                            book.getGenre().getName());
        } else {
            return namedParameterJdbcOperations.getJdbcOperations()
                    .update("update books set name=?, description=?, author=?, genre=? where id=?",
                            book.getName(), book.getDescription(), book.getAuthor().getName(), book.getGenre().getName(),
                            book.getId());
        }
    }

    @Override
    public Book getById(int id) throws DataAccessException {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select * from books where id = :id", params, new BookMapper()
        );
    }

    @Override
    public List<Book> getAll() {
        return namedParameterJdbcOperations.query("select * from books", new BookMapper());
    }

    @Override
    public int deleteById(int id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.update(
                "delete from books where id = :id", params);
    }

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            Author author = new Author(null, resultSet.getString("author"));
            Genre genre = new Genre(null, resultSet.getString("genre"));
            return new Book(id, name, description, author, genre);
        }
    }
}
