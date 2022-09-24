package ru.yandex.practicum.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.film.dao.FilmStorage;
import ru.yandex.practicum.storage.mapper.FilmMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Qualifier("FilmDBStorage")
public class FilmDBStorage implements FilmStorage {
    
    private JdbcTemplate jdbcTemplate;
    
    private FilmMapper filmMapper;
    
    @Autowired
    public FilmDBStorage(JdbcTemplate jdbcTemplate, FilmMapper filmMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmMapper = filmMapper;
    }
    
    
    /**
     * Создание фильма в БД.
     *
     * @param film объект фильма.
     * @return ответ (созданный фильм) или ошибка.
     */
    @Override
    public Film addInStorage(Film film) {
        String sql = "insert into FILMS " +
                "(FILM_NAME, FILM_DESC, RELEASE_DATE, DURATION, MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        //Фильму, записанному в БД, присваиваем ID из БД, если ID был равен 'null'.
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return film;
    }
    
    /**
     * Обновление информации о существующем фильме.
     *
     * @param film обновляемый фильм.
     * @return обновлённый фильм.
     */
    @Override
    public Film updateInStorage(Film film) {
        String sqlQuery = "update FILMS " +
                "SET FILM_NAME = ?, FILM_DESC = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }
    
    /**
     * Удалить фильм из библиотеки.
     *
     * @param film - фильм.
     * @return удалённый фильм.
     */
    @Override
    public Film removeFromStorage(Film film) {
        String sqlQuery = "delete from FILMS where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        return film;
    }
    
    /**
     * Вывести список всех фильмов.
     *
     * @return список фильмов.
     */
    @Override
    public List<Film> getAllFilms() {
        List<Film> result;
        //Запросить с объединением таблиц 'FILMS' и 'MPA' по 'MPA_ID'.
        String sqlQuery = "select * from FILMS join MPA on FILMS.MPA_ID = MPA.MPA_ID";
        result = jdbcTemplate.query(sqlQuery, filmMapper);
        return result;
    }
    
    
    /**
     * Найти популярные фильмы.
     *
     * @param count число фильмов для результата.
     *              <p>Если null, то вывести весь список.</p>
     */
    @Override
    public List<Film> getPopularFilms(Integer count) {
        return null;
    }
    
    
    /**
     * Метод получения фильма из БД по его ID.
     *
     * @param id ID фильма, наличие которого необходимо проверить в библиотеке.
     * @return Film - фильм присутствует в библиотеке.
     * <p>null - фильма нет в библиотеке.</p>
     */
    @Override
    public Film getFilmById(Integer id) {
        String sqlQuery = "select * from FILMS join MPA ON FILMS.mpa_id = MPA.mpa_id WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, filmMapper, id);
    }
    
    /**
     * Получить фильм из базы данных по его названию.
     * @param name название фильма.
     * @return фильм из БД. Если его нет, то 'null'.
     */
    @Override
    public Film getFilmByName(String name) {
        return null;
    }
    
    
}
