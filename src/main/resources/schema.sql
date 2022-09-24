drop all objects;
/*Создание таблицы пользователей*/
CREATE TABLE IF NOT EXISTS users
(
    user_id   int AUTO_INCREMENT PRIMARY KEY,
    email     varchar(50),
    login     varchar(50),
    user_name varchar(100),
    birthday  date
);

/*Таблица со статусами дружбы.
  1 - Unconfirmed   (не подтверждена)
  2 - Confirmed     (подтверждена)
*/
CREATE TABLE IF NOT EXISTS status_friendship
(
    friendship_id   int AUTO_INCREMENT PRIMARY KEY,
    friendship_name varchar(50)
);

/*Создаём таблицы друзей.
  Чтобы понять статус пары, необходимо сделать запрос с "зеркальными" ID-шниками.
  Если ответ (id1, id2), то id1 запросил дружбу первым.
  Если ответ (id2, id1), то id2 запросил дружбу первым.
  where (user_id = id1 AND friend_id = id2) OR (user_id = id2 AND friend_id = id1);
*/
CREATE TABLE IF NOT EXISTS ids_friends
(
    user_id       int REFERENCES users (user_id) ON DELETE CASCADE,
    friend_id     int REFERENCES users (user_id) ON DELETE CASCADE,
    friendship_id int default 1,
    FOREIGN KEY (friendship_id) REFERENCES status_friendship (friendship_id),
    PRIMARY KEY (user_id, friend_id)
);


/*Создание таблицы с фильмами.*/
CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id   int AUTO_INCREMENT PRIMARY KEY,
    mpa_name varchar(10),
    mpa_desc varchar(100)
);

/*Таблица с описанием возрастного рейтинга фильма.*/
CREATE TABLE IF NOT EXISTS films
(
    film_id      int AUTO_INCREMENT PRIMARY KEY,
    film_name    varchar(100),
    film_desc    varchar(200),
    release_date date,
    duration     int,
    mpa_id       int REFERENCES mpa (mpa_id)
);

/*Таблица с описанием жанров фильмов.*/
CREATE TABLE IF NOT EXISTS genres
(
    genre_id   int AUTO_INCREMENT PRIMARY KEY,
    genre_name varchar(20)
);

/*Таблица с информацией о том, какому жанру принадлежит фильм.*/
CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  int REFERENCES films (film_id) ON DELETE CASCADE,
    genre_id int REFERENCES genres (genre_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);


/*
select *
from films;

select *
from users;

select *
from genres;

select *
from film_genre;

select *
from ids_friends;

select *
from mpa;


select *
from;*/


/*
alter table GENRES drop column genre_desc;
*/

/*
alter table mpa
    add mpa_desc varchar(100);

alter table mpa
    alter column mpa_name varchar(10);*/
