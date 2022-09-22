merge into MPA (MPA_ID, MPA_NAME, MPA_DESC)
    values (1, 'G', 'Фильм демонстрируется без ограничений.'),
           (2, 'PG', 'Детям рекомендуется смотреть фильм с родителями.'),
           (3, 'PG-13', 'Просмотр не желателен детям до 13 лет.'),
           (4, 'R', 'Лица, не достигшие 17-летнего возраста, допускаются на фильм в сопровождении одного из родителей.'),
           (5, 'NC-17', 'Лица 17-летнего возраста и младше на фильм не допускаются.');

merge into GENRES (GENRE_ID, GENRE_NAME)
values (1, 'Комедия'),
       (2, 'Драма'),
       (3,'Мультфильм'),
       (4,'Триллер'),
       (5,'Документальный'),
       (6,'Боевик');

-- merge into USERS (USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY) values (1, 'email@em.we', 'login', 'username', '2020-01-20' )

/*
select *
from GENRES;
*/
