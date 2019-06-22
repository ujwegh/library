DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS book_authors;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS comments;

CREATE TABLE book_authors (
  id   IDENTITY PRIMARY KEY,
  name VARCHAR(255) unique NOT NULL
);

CREATE TABLE genres (
  id   IDENTITY PRIMARY KEY,
  name VARCHAR(255) unique NOT NULL
);

CREATE TABLE books (
  id          IDENTITY PRIMARY KEY,
  name        VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  authors     VARCHAR(255) references book_authors (name) on DELETE cascade on UPDATE cascade,
  genres      VARCHAR(255) references genres (name) on DELETE cascade on UPDATE cascade
);

CREATE TABLE comments (
  id      IDENTITY PRIMARY KEY,
  comment VARCHAR(1000),
  book_id INTEGER NOT NULL REFERENCES books (id) ON DELETE CASCADE on UPDATE cascade
);

CREATE TABLE map_books_authors (
  book_id   INTEGER REFERENCES books,
  author_id INTEGER references book_authors,
  primary key (book_id, author_id)
)
