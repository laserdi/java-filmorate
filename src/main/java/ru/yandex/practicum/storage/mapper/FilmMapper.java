package ru.yandex.practicum.storage.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class FilmMapper implements RowMapper<Film> {
    private final MpaMapper mpaMapper;
    
    @Autowired
    public FilmMapper(MpaMapper mpaMapper) {
        this.mpaMapper = mpaMapper;
    }
    
    
    /**
     * Метод получения фильма из таблицы `films`
     * @param rs набор строк.
     * @param rowNum номер строки.
     * @return фильм.
     * @throws SQLException ошибка в работе с БД.
     */
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film().toBuilder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("film_desc"))
                .releaseDate(rs.getObject("release_date", LocalDate.class))
                .duration(rs.getInt("duration"))
                .mpa(mpaMapper.mapRow(rs, rowNum))
                .build();
        
        return film;
    }
}
