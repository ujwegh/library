package ru.nik.library.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.nik.library.domain.Genre;
import ru.nik.library.service.GenreService;

import java.util.List;

@ShellComponent
public class GenreShellController {
    private final GenreService genreService;

    @Autowired
    public GenreShellController(GenreService genreService) {
        this.genreService = genreService;
    }


    @ShellMethod("allGenres")
    public String genres() {
        List<Genre> genres = genreService.getAllGenres();
        StringBuilder builder = new StringBuilder();
        genres.forEach(a -> builder.append("id: ").append(a.getId()).append(" ").append(a.toString()).append("\n"));
        if (builder.toString().isEmpty()) {
            return "Genres not found.";
        }
        return builder.toString();
    }

    @ShellMethod("newgenre")
    public String newgenre(@ShellOption String name) {
        boolean b = genreService.addGenre(name);
        if (b) {
            return "Genre " + name + "adding attempt has been failed.";
        }
        return "Genre " + name + " successfully added.";
    }

    @ShellMethod("updateauthor")
    public String updategenre(@ShellOption Integer id,@ShellOption String name) {
        boolean b = genreService.updateGenre(id, name);
        if (b) {
            return "Genre with " +id+" and " + name + "updating attempt has been failed.";
        }
        return "Genre with " +id+" and " + name + " successfully updated.";
    }

    @ShellMethod("deletegenrebyname")
    public String deletegenrebyname(@ShellOption String name) {
        boolean b = genreService.deleteGenreByName(name);
        if (b) {
            return "Genre " + name + " deleting attempt has been failed.";
        }
        return "Genre " + name + " successfully deleted.";
    }

    @ShellMethod("deletegenrebyid")
    public String deletegenrebyid(@ShellOption Integer id) {
        boolean b = genreService.deleteGenreById(id);
        if (b) {
            return "Genre with " + id + " deleting attempt has been failed.";
        }
        return "Genre with " + id + " successfully deleted.";
    }

    @ShellMethod("getgenrebyname")
    public String getgenrebyname(@ShellOption String name) {
        Genre genre = genreService.getGenreByName(name);
        if (genre == null){
            return "There is no genres with this name.";
        }
        return genre.toString();
    }

    @ShellMethod("getgenrebyid")
    public String getgenrebyid(@ShellOption Integer id) {
        Genre genre = genreService.getGenreById(id);
        if (genre == null){
            return "There is no genres with this name.";
        }
        return genre.toString();
    }
}
