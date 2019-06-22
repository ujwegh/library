DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS authors;
DROP TABLE IF EXISTS genres;

CREATE TABLE authors (
  id int primary key,
  name VARCHAR(255)
);

CREATE TABLE genres (
  id int primary key,
  name VARCHAR(255)
);

CREATE TABLE books (
  id int primary key,
  name VARCHAR(255),
  description VARCHAR(255)
);