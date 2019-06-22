package ru.nik.library.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.nik.library.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class AuthorDaoImpl implements AuthorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public AuthorDaoImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public int insert(Author author) {
        if (author.isNew()){
            return namedParameterJdbcOperations.getJdbcOperations()
                    .update("insert into book_authors (name) values (?)", author.getName());
        } else {
            return namedParameterJdbcOperations.getJdbcOperations()
                    .update("update book_authors set name=? where id=?", author.getName(), author.getId());
        }
    }

    @Override
    public Author getById(int id) throws DataAccessException {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select * from book_authors where id = :id", params, new AuthorMapper()
        );
    }

    @Override
    public Author getByName(String name) throws DataAccessException {
        Map<String, Object> params = Collections.singletonMap("name", name);
        return namedParameterJdbcOperations.queryForObject(
                "select * from book_authors where name = :name", params, new AuthorMapper()
        );
    }

    @Override
    public List<Author> getAll() {
        return namedParameterJdbcOperations.query("select * from book_authors", new AuthorMapper());

    }

    @Override
    public int deleteById(int id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.update(
                "delete from book_authors where id = :id", params);
    }

    @Override
    public int deleteByName(String name) {
        Map<String, Object> params = Collections.singletonMap("name", name);
        return namedParameterJdbcOperations.update(
                "delete from book_authors where name = :name", params);
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            return new Author(id, name);
        }
    }
}
