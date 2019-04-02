package ru.nik.library.shell;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.nik.library.domain.Book;
import ru.nik.library.service.BookService;

@ShellComponent
public class BookShellController {

    private final BookService bookService;

    @Autowired
    public BookShellController(BookService bookService) {
        this.bookService = bookService;
    }

    @ShellMethod("books")
    public String books() {
        List<Book> books = bookService.getAllBooks();
        StringBuilder builder = new StringBuilder();
        books.forEach(b -> builder.append("id: ").append(b.getId()).append(b.toString()).append("\n"));
        if (builder.toString().isEmpty()) {
            return "Books not found.";
        }
        return builder.toString();
    }

    @ShellMethod("getbook")
    public String getbook(@ShellOption Integer id) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return "There is no books with this id.";
        }
        return book.toString();
    }

    @ShellMethod("updatebook")
    public String updatebook(@ShellOption Integer id, @ShellOption String name, @ShellOption String description,
                             @ShellOption String authorName, @ShellOption String genreName) {
        int i = bookService.updateBook(id, name, description, authorName, genreName);
        if (i == 0) {
            return "Book with " + id + " updating attempt has been failed.";
        }
        return "Book with " + id + " and " + name + " successfully updated.";
    }

    @ShellMethod("deletebook")
    public String deletebook(@ShellOption Integer id) {
        int i = bookService.deleteBookById(id);
        if (i == 0) {
            return "Book with " + id + " deleting attempt has been failed.";
        }
        return "Book with " + id + " successfully deleted.";
    }

    @ShellMethod("addbook")
    public String addbook(@ShellOption String name, @ShellOption String description,
                          @ShellOption String authorName, @ShellOption String genreName) {
        int i = bookService.addBook(name, description, authorName, genreName);
        if (i == 0) {
            return "Book " + name + " adding attempt has been failed.";
        }
        return "Book with " + name + " successfully added.";
    }
}
