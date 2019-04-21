package ru.nik.library.shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.nik.library.service.GenreService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:postgreschema.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data-test.sql")
})
class GenreShellControllerTest {

    @Autowired
    private GenreService service;
    private GenreShellController controller;

    @BeforeEach
    void init(){
        controller = new GenreShellController(service);
    }

    @Test
    void genres() {
        String result = controller.genres();
        assertEquals("id: 1002 Genre(name=фантастика)\n" +"id: 1003 Genre(name=детектив)\n", result);
    }

    @Test
    void newgenre() {
        String result = controller.newgenre("роман");
        assertEquals("Genre роман successfully added.", result);
    }

    @Test
    void updategenre() {
        String result = controller.updategenre(1002,"роман");
        assertEquals("Genre with 1002 and роман successfully updated.", result);
    }

    @Test
    void deletegenrebyname() {
        String result = controller.deletegenrebyname("фантастика");
        assertEquals("Genre фантастика successfully deleted.", result);
    }

    @Test
    void deletegenrebyid() {
        String result = controller.deletegenrebyid(1002);
        assertEquals("Genre with 1002 successfully deleted.", result);
    }

    @Test
    void getgenrebyname() {
        String result = controller.getgenrebyname("фантастика");
        assertEquals("Genre(name=фантастика)", result);
    }

    @Test
    void getgenrebyid() {
        String result = controller.getgenrebyid(1002);
        assertEquals("Genre(name=фантастика)", result);
    }
}