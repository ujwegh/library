package ru.nik.library.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import ru.nik.library.repository.datajpa.BookRepository;

@Component
public class BooksExistsCheck extends AbstractHealthIndicator {

	@Autowired
	private BookRepository repository;

	@Override
	protected void doHealthCheck(Health.Builder bldr) throws Exception {
		// implement some check
		boolean running = repository.findAll().size() > 0;
		if (running) {
			bldr.up();
		} else {
			bldr.down();
		}
	}
}
