package ru.nik.library.shell;

import java.util.Arrays;
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
        if (books != null) {
            StringBuilder builder = new StringBuilder();
            books.forEach(b -> builder.append("id: ").append(b.getId()).append(b.toString()).append("\n"));
            if (builder.toString().isEmpty()) {
                return "Books not found.";
            }
            return builder.toString();
        }
        return "Books not found";

    }

    @ShellMethod("getbook")
    public String getbook(@ShellOption String id) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return "There is no books with this id.";
        }
        return book.toString();
    }

    @ShellMethod("updatebook")
    public String updatebook(@ShellOption String id, @ShellOption String name, @ShellOption String description) {
        boolean b = bookService.updateBook(id, name, description);
        if (!b) {
            return "Book with " + id + " updating attempt has been failed.";
        }
        return "Book with " + id + " and " + name + " successfully updated.";
    }

    @ShellMethod("deletebook")
    public String deletebook(@ShellOption String id) {
        boolean b = bookService.deleteBookById(id);
        if (!b) {
            return "Book with " + id + " deleting attempt has been failed.";
        }
        return "Book with " + id + " successfully deleted.";
    }

    @ShellMethod("addbook")
    public String addbook(@ShellOption String name, @ShellOption String description) {
        boolean b = bookService.addBook(name, description);
        if (!b) {
            return "Book with name " + name + " adding attempt has been failed.";
        }
        return "Book with name " + name + " successfully added.";
    }

    @ShellMethod("addbookauthors")
    public String addbookauthors(@ShellOption String bookId, @ShellOption String... names) {
        Boolean b = bookService.updateBookAuthors(bookId, names);
        if (!b) {
            return "Book with id: " + bookId + " authors update has been failed.";
        }
        return "Book with id: " + bookId + " successfully updated. Updated authors with names: "
                + Arrays.toString(names);
    }

    @ShellMethod("addbookgenres")
    public String addbookgenres(@ShellOption String bookId, @ShellOption String... names) {
        Boolean b = bookService.updateBookGenres(bookId, names);
        if (!b) {
            return "Book with id: " + bookId + " genres update has been failed.";
        }
        return "Book with id: " + bookId + " successfully updated. Updated genres with names: "
                + Arrays.toString(names);
    }

}
