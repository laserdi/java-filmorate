merge into MPA (MPA_ID, MPA_NAME, MPA_DESC)
    values (1, 'G', 'Фильм демонстрируется без ограничений.'),
           (2, 'PG', 'Детям рекомендуется смотреть фильм с родителями.'),
           (3, 'PG-13', 'Просмотр не желателен детям до 13 лет.'),
           (4, 'R',
            'Лица, не достигшие 17-летнего возраста, допускаются на фильм в сопровождении одного из родителей.'),
           (5, 'NC-17', 'Лица 17-летнего возраста и младше на фильм не допускаются.')
;

merge into GENRES (GENRE_ID, GENRE_NAME)
    values (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик')
;

-- merge into USERS (USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY) values (1, 'email@em.we', 'login', 'username', '2020-01-20' )

merge into STATUS_FRIENDSHIP (FRIENDSHIP_ID, FRIENDSHIP_NAME)
    values (1, 'unconfirmed'),
           (2, 'confirmed')
;


/*Заполним пользователей.*/
/*
merge into USERS (USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY)
    values (1, 'email@user1', 'login1', 'name1', '2001-01-01'),
           (2, 'email@user2', 'login2', 'name2', '2002-01-01'),
           (3, 'email@user3', 'login3', 'name3', '2003-01-01'),
           (4, 'email@user4', 'login4', 'name4', '2004-01-01'),
           (5, 'email@user5', 'login5', 'name5', '2005-01-01')
;
*/

/*Добавим дружбу среди людей.
  1-3 -  (1,3) и (3,1);
  2-3 - запрос только со стороны 2 (2,3);
  3-5 - взаимная дружба (3,5) и (5,3);
  4-2 - запрос только со стороны 4 (4,2);
  */
/*
merge into IDS_FRIENDS (user_id, friend_id, FRIENDSHIP_ID)

    VALUES (1, 3, 1),
           (2, 3, 2)
;
*/

//Список ID, которым отправлена заявка. Ответ: 1, 4, 5
/*
select FRIEND_ID
from IDS_FRIENDS
where USER_ID = 3
;
*/

//Список ID, подписанных на "меня".  Ответ: 1, 2, 5

/*
select USER_ID
from IDS_FRIENDS
where FRIEND_ID = 3;
*/

//Список друзей: 1,5
/*
select send_query_friend as FRIENDS
from (select FRIEND_ID as send_query_friend
      from IDS_FRIENDS
      where USER_ID = 3) as sended_query
inner join (select USER_ID as follower_id
            from IDS_FRIENDS
            where FRIEND_ID = 3) as followers
where send_query_friend = follower_id
;
*/
/*
select *
from GENRES;
*/
