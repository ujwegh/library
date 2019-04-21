DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS book_authors;
DROP TABLE IF EXISTS genres;
DROP SEQUENCE IF EXISTS seq;

CREATE SEQUENCE seq START 1000;

CREATE TABLE book_authors (
  id   INTEGER PRIMARY KEY default nextval('seq'),
  name VARCHAR(255) unique NOT NULL
);

CREATE TABLE genres (
  id   INTEGER PRIMARY KEY default nextval('seq'),
  name VARCHAR(255) unique NOT NULL
);

CREATE TABLE books (
  id          INTEGER PRIMARY KEY default nextval('seq'),
  name        VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  author      VARCHAR(255) references book_authors (name) on DELETE cascade on UPDATE cascade ,
  genre       VARCHAR(255) references genres (name) on DELETE cascade on UPDATE cascade
);

