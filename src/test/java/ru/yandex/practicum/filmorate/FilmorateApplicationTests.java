package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.RatingDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmorateApplicationTests {

	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private final RatingDao ratingDao;

	@Test
	@Order(1)
	void contextLoads() {
	}

	@Test
	@Order(2)
	public void shouldAddUserToStorageStandardCase() {
		User STANDARD_CASE_USER = new User();
		STANDARD_CASE_USER.setEmail("test@test.ru");
		STANDARD_CASE_USER.setLogin("testLogin");
		STANDARD_CASE_USER.setName("testName");
		STANDARD_CASE_USER.setBirthday(LocalDate.of(2000, 2, 23));

		User user = userStorage.addUserToStorage(STANDARD_CASE_USER);
		Assertions.assertEquals(1, user.getId());
		Assertions.assertEquals("test@test.ru", user.getEmail());
		Assertions.assertEquals("testLogin", user.getLogin());
		Assertions.assertEquals("testName", user.getName());
		Assertions.assertEquals(STANDARD_CASE_USER.getBirthday(), user.getBirthday());
	}

	@Test
	@Order(3)
	public void shouldGetUserFromStorageStandardCase() {
		User user = userStorage.getUserFromStorage(1L);
		Assertions.assertEquals(1L, user.getId());
		Assertions.assertEquals("test@test.ru", user.getEmail());
		Assertions.assertEquals("testLogin", user.getLogin());
		Assertions.assertEquals("testName", user.getName());
		Assertions.assertEquals(LocalDate.of(2000, 2, 23), user.getBirthday());
	}

	@Test
	@Order(4)
	public void shouldUpdateUsersInStorageStandardCase() {
		User update = new User();
		update.setId(1L);
		update.setEmail("update@test.ru");
		update.setLogin("updateLogin");
		update.setName("updateName");
		update.setBirthday(LocalDate.of(2022, 2, 23));

		User fromStorage = userStorage.updateUserInStorage(update);

		Assertions.assertEquals(update, fromStorage);
	}

	@Test
	@Order(5)
	public void shouldGetAllUsersFromStorageStandardCase() {
		List<User> allUsers = userStorage.getAll();
		Assertions.assertEquals(1, allUsers.size());
		Assertions.assertEquals(userStorage.getUserFromStorage(1L), allUsers.get(0));

	}

	@Test
	@Order(6)
	public void shouldAddFilmToStorageStandardCase() {
		Film STANDARD_CASE_FILM = new Film();
		STANDARD_CASE_FILM.setName("testFilm");
		STANDARD_CASE_FILM.setDescription("testDescription");
		STANDARD_CASE_FILM.setReleaseDate(LocalDate.of(2023, 2, 23));
		STANDARD_CASE_FILM.setDuration(60);
		STANDARD_CASE_FILM.setMpa(ratingDao.findRatingById(1L));

		Film film = filmStorage.addFilmToStorage(STANDARD_CASE_FILM);
		Assertions.assertEquals(1, film.getId());
		Assertions.assertEquals("testFilm", film.getName());
		Assertions.assertEquals("testDescription", film.getDescription());
		Assertions.assertEquals(LocalDate.of(2023, 2, 23), film.getReleaseDate());
		Assertions.assertEquals(60, film.getDuration());
		Assertions.assertEquals(STANDARD_CASE_FILM.getMpa(), film.getMpa());
	}

	@Test
	@Order(7)
	public void shouldGetFilmFromStorageStandardCase() {
		Film film = filmStorage.getFilmFromStorage(1L);
		Assertions.assertEquals(1, film.getId());
		Assertions.assertEquals("testFilm", film.getName());
		Assertions.assertEquals("testDescription", film.getDescription());
		Assertions.assertEquals(LocalDate.of(2023, 2, 23), film.getReleaseDate());
		Assertions.assertEquals(60, film.getDuration());
		Assertions.assertEquals(ratingDao.findRatingById(1L), film.getMpa());
	}

	@Test
	@Order(8)
	public void shouldUpdateFilmFromStorageStandardCase() {
		Film update = new Film();
		update.setId(1L);
		update.setName("updateFilm");
		update.setDescription("updateDescription");
		update.setDuration(120);
		update.setReleaseDate(LocalDate.of(2023, 1, 23));
		update.setMpa(ratingDao.findRatingById(2L));

		Film fromStorage = filmStorage.updateFilmInStorage(update);
		Assertions.assertEquals(update, fromStorage);
	}

	@Test
	@Order(9)
	public void shouldGetAllFilmsFromStorageStandardCase() {
		List<Film> allUsers = filmStorage.getAll();
		Assertions.assertEquals(1, allUsers.size());
		Assertions.assertEquals(filmStorage.getFilmFromStorage(1L), allUsers.get(0));
	}
}