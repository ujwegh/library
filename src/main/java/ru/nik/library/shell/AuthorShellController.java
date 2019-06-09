package ru.nik.library.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.nik.library.domain.Author;
import ru.nik.library.service.AuthorService;

import java.util.List;

@ShellComponent
public class AuthorShellController {
    private final AuthorService authorService;

    @Autowired
    public AuthorShellController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ShellMethod("allAuthors")
    public String authors() {
        List<Author> authors = authorService.getAllAuthors();
        StringBuilder builder = new StringBuilder();
        authors.forEach(a -> builder.append("id: ").append(a.getId()).append(" ").append(a.toString()).append("\n"));
        if (builder.toString().isEmpty()) {
            return "Authors not found.";
        }
        return builder.toString();
    }

    @ShellMethod("newauthor")
    public String newauthor(@ShellOption String name) {
        boolean b = authorService.addAuthor(name);
        if (!b) {
            return "Author " + name + " adding attempt has been failed.";
        }
        return "Author " + name + " successfully added.";
    }

    @ShellMethod("updateauthor")
    public String updateauthor(@ShellOption String id, @ShellOption String name) {
        boolean b = authorService.updateAuthor(id, name);
        if (!b) {
            return "Author with id: " + id + " and name: " + name + " updating attempt has been failed.";
        }
        return "Author with id: " + id + " and name: " + name + " successfully updated.";
    }

    @ShellMethod("deleteauthorbyname")
    public String deleteauthorbyname(@ShellOption String name) {
        boolean b = authorService.deleteAuthorByName(name);
        if (!b) {
            return "Author " + name + " deleting attempt has been failed.";
        }
        return "Author " + name + " successfully deleted.";
    }

    @ShellMethod("deleteauthorbyid")
    public String deleteauthorbyid(@ShellOption String id) {
        boolean b = authorService.deleteAuthorById(id);
        if (!b) {
            return "Author with id: " + id + " deleting attempt has been failed.";
        }
        return "Author with id: " + id + " successfully deleted.";
    }

    @ShellMethod("getauthorbyname")
    public String getauthorbyname(@ShellOption String name) {
        Author author = authorService.getAuthorByName(name);
        if (author == null) {
            return "There is no authors with this name.";
        }
        return author.toString();
    }

    @ShellMethod("getAuthorById")
    public String getauthorbyid(@ShellOption String id) {
        Author author = authorService.getAuthorById(id);
        if (author == null) {
            return "There is no authors with this name.";
        }
        return author.toString();
    }
}
