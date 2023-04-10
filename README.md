# Это репозиторий проекта Filmorate

Данное приложение **_умеет_**:
1. Хранить и считывать фильмы и пользователей.
2. Выполнять следующие методы:
* Получать список всех пользователей и фильмов;
* Сортировать фильмы по количеству лайков;
* Удалять все фильмы и пользователей;
* Получать фильмы и пользователей по идентификатору;
* Создавать фильмы и пользователей;
* Обновлять фильмы и пользователей;
* Удалять фильмы и пользователей по идентификатору;
* Получать список общих друзей.

Приложение написано на Java. Пример кода:

```java
@SpringBootApplication
public class FilmorateApplication {
    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    }
}
```
#### В рамках дополнительного задания создана схема базы данных:
Ссылка на схему базы данных (https://practicum.yandex.ru/java-developer/)

#### Примеры запросов:
- Вывести идентификатор, название и жанр фильма:
> SELECT f.film_id, f.name, g.name
FROM film AS f
INNER JOIN film-category AS fc ON f.film_id = fc.film_id 
INNER JOIN genre AS g ON fc.genre_id = g.genre_id;

- Вывести идентификатор, название и количество лайков фильма, а также отсортировать по количеству лайков от большего к меньшему:
> SELECT f.film_id, f.name, COUNT(l.user_id) AS likes
FROM film AS f
LEFT OUTER JOIN likes AS l ON f.film_id = l.film_id
GROUP BY f.film_id
ORDER BY likes DESC;

- Вывести общих друзей пользователей c id 1 и 2:
> SELECT friends_id AS common_friends 
FROM friends
WHERE user_id = 1 OR user_id = 2
GROUP BY friends_id
HAVING COUNT(friends_id) >= 2


Данная задача решена в рамках обучения на [Яндекс-Практикуме](https://practicum.yandex.ru/java-developer/)
