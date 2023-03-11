package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.RatingDao;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@Qualifier("DbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RatingDao ratingDao;
    private final GenreDao genreDao;


    public FilmDbStorage(JdbcTemplate jdbcTemplate, RatingDao ratingDao, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.ratingDao = ratingDao;
        this.genreDao = genreDao;
    }

    @Override
    public Film addFilmToStorage(Film film) throws AlreadyExistException, NotFoundException {
        return insertInAllTables(film);
    }

    @Override
    public Film updateFilmInStorage(Film film) throws NotFoundException, AlreadyExistException {
        String sqlQuery = "SELECT FILM_ID FROM FILMS WHERE FILM_ID = ?";
        SqlRowSet filmRowSet = jdbcTemplate.queryForRowSet(sqlQuery, film.getId());
        if (filmRowSet.next()) {
            String sqlQuery2 = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
            String sqlQuery3 = "DELETE FROM FILM_USER WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQuery2, film.getId());
            jdbcTemplate.update(sqlQuery3, film.getId());
            return insertInAllTables(film);
        } else {
            throw new NotFoundException("Фильм с id =" + film.getId() + " не найден");
        }
    }

    @Override
    public Film getFilmFromStorage(Long filmId) throws NotFoundException {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        SqlRowSet filmRowSet = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        if (filmRowSet.next()) {
            return makeFilm(filmRowSet);
        } else {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
    }


    @Override
    public List<Film> getAll() throws NotFoundException, SQLException {
        List<Film> allFilms = new ArrayList<>();
        String sqlQuery = "SELECT * FROM FILMS";
        SqlRowSet filmRowSet = jdbcTemplate.queryForRowSet(sqlQuery);
        while (filmRowSet.next()) {
            allFilms.add(makeFilm(filmRowSet));
        }
        return allFilms;
    }

    private Film makeFilm(SqlRowSet rs) throws NotFoundException {

        Long id = rs.getLong("FILM_ID");
        String name = rs.getString("TITLE");
        String description = rs.getString("DESCRIPTION");
        LocalDate releaseDate = Objects.requireNonNull(rs.getDate("RELEASE_DATE")).toLocalDate();
        int duration = rs.getInt("DURATION");
        Long ratingId = rs.getLong("RATING_ID");

        Film film = new Film(id, name, description, releaseDate, duration);
        addRating(film, ratingId);
        addGenres(film);
        addLikes(film);
        return film;
    }

    private void addRating(Film film, Long ratingId) throws NotFoundException {
        film.setMpa(ratingDao.findRatingById(ratingId));
    }

    private void addGenres(Film film) throws NotFoundException {
        List<Genre> allGenres = new ArrayList<>();
        String genreSql = "select * from GENRES WHERE GENRE_ID IN (select GENRE_ID from FILM_GENRE where FILM_ID = ?)";
        SqlRowSet genreRowSet = jdbcTemplate.queryForRowSet(genreSql, film.getId());
        while (genreRowSet.next()) {
            Genre genre = genreDao.findGenreById(genreRowSet.getLong("GENRE_ID"));
            allGenres.add(genre);
        }
        film.setGenres(allGenres);
    }

    public void addLikes(Film film) {

        String sql = "SELECT USER_ID FROM FILM_USER WHERE FILM_ID = ?";
        SqlRowSet likeRowSet = jdbcTemplate.queryForRowSet(sql, film.getId());
        while (likeRowSet.next()) {
            film.setLikes(likeRowSet.getLong("USER_ID"));
        }
    }

    private Film insertInAllTables(Film film) throws NotFoundException, AlreadyExistException {
        try {
            if (film.getId() == null) {
                SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
                simpleJdbcInsert.withTableName("FILMS").usingGeneratedKeyColumns("FILM_ID");

                MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("TITLE", film.getName())
                    .addValue("DESCRIPTION", film.getDescription())
                    .addValue("RELEASE_DATE", film.getReleaseDate())
                    .addValue("DURATION", film.getDuration())
                    .addValue("RATING_ID", film.getMpa().getId());
                Number id = simpleJdbcInsert.executeAndReturnKey(params);

                film.setId(id.longValue());
            } else {
                String sql = "UPDATE FILMS SET title = ?, description = ?, release_date = ?, duration = ?, " +
                    "rating_id = ? WHERE  film_id = ?";
                jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpa().getId(), film.getId());
            }
            addRating(film, film.getMpa().getId());
            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                int size = film.getGenres().size();
                Set<Genre> genresSet = new HashSet<>();
                for (int i = 0; i < size; i++) {
                    Genre genre = genreDao.findGenreById(film.getGenres().get(i).getId());
                    genresSet.add(genre);
                }
                for (Genre genre : genresSet) {
                    String genreSql = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?,?)";
                    jdbcTemplate.update(genreSql, film.getId(), genre.getId());
                }
                film.getGenres().clear();
                film.setGenres(new LinkedList<>(genresSet));
            }
            if (film.getLikes() != null && !film.getLikes().isEmpty()) {
                for (Long id : film.getLikes()) {
                    String likeSql = "INSERT INTO FILM_USER (FILM_ID, USER_ID) values (?,?)";
                    jdbcTemplate.update(likeSql, film.getId(), id);
                }
            }
            return film;
        } catch (DuplicateKeyException exp) {
            throw new AlreadyExistException(film);
        }
    }
}
