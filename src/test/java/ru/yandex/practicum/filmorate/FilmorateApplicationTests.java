package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;

	@Test
	public void testFindUserById() {
		User user1 = new User();
		user1.setEmail("mail@mail.ru");
		user1.setName(null);
		user1.setLogin("Login");
		user1.setBirthday(LocalDate.of(1996, 04, 5));

		userStorage.postUser(user1);

		User userTest = userStorage.getUser(1L);

		assertThat(userTest).hasFieldOrPropertyWithValue("id", 1L);
	}

	@Test
	public void testUpdateUser() {
		User user1 = new User();
		user1.setEmail("mail@mail.ru");
		user1.setName(null);
		user1.setLogin("Login");
		user1.setBirthday(LocalDate.of(1996, 04, 5));

		userStorage.postUser(user1);

		User user2 = new User();
		user2.setId(1L);
		user2.setEmail("test@mail.ru");
		user2.setName("NameTest");
		user2.setLogin("LoginTest");
		user2.setBirthday(LocalDate.of(2000, 1, 13));

		userStorage.updateUser(user2);

		User userTest = userStorage.getUser(1L);

		assertThat(userTest)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("email", "test@mail.ru")
						.hasFieldOrPropertyWithValue("name", "NameTest")
						.hasFieldOrPropertyWithValue("login", "LoginTest")
						.hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 13));
	}
	@Test
	public void testAddFilm() {
		Film film = new Film();
		film.setName("Hitman");
		film.setDescription("Bald man");
		film.setReleaseDate(LocalDate.of(20080, 10, 7));
		film.setDuration(150L);
		film.setMpa(new Mpa(4, "R"));

		filmStorage.postFilm(film);

		Film filmTest = filmStorage.getFilm(1L);

assertThat(filmTest)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("name", "Hitman")
						.hasFieldOrPropertyWithValue("description", "Bald man")
						.hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(20080, 10, 7))
						.hasFieldOrPropertyWithValue("duration", 150L);
	}

	@Test
	public void testUpdateFilm() {

		Film film = new Film();
		film.setName("Hitman");
		film.setDescription("Bald man");
		film.setReleaseDate(LocalDate.of(20080, 10, 7));
		film.setDuration(150L);
		film.setMpa(new Mpa(4, "R"));

		filmStorage.postFilm(film);

		Film filmTest = new Film();
		filmTest.setId(1L);
		filmTest.setName("Doka 2");
		filmTest.setDescription("Soon Doka 3");
		filmTest.setReleaseDate(LocalDate.of(2023, 04, 20));
		filmTest.setDuration(100L);
		filmTest.setMpa(new Mpa(4, "R"));

		filmStorage.updateFilm(filmTest);

		Film filmResult = filmStorage.getFilm(1L);

		assertThat(filmResult)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("name", "Doka 2")
						.hasFieldOrPropertyWithValue("description", "Soon Doka 3")
						.hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2023, 04, 20))
						.hasFieldOrPropertyWithValue("duration", 100L);
	}
}
