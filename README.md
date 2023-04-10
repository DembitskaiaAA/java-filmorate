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
![Cхема базы данных](/database_FilmorateApp.png)

#### Примеры запросов:
- Вывести идентификатор, название и жанр фильма:
```sql
SELECT f.film_id, f.name, g.name
FROM film AS f
INNER JOIN film-category AS fc ON f.film_id = fc.film_id 
INNER JOIN genre AS g ON fc.genre_id = g.genre_id;
```

- Вывести идентификатор, название и количество лайков фильма, а также отсортировать список по количеству лайков от большего к меньшему:
```sql
SELECT f.film_id, f.name, COUNT(l.user_id) AS likes
FROM film AS f
LEFT OUTER JOIN likes AS l ON f.film_id = l.film_id
GROUP BY f.film_id
ORDER BY likes DESC;
```

- Вывести общих друзей пользователей c id 1 и 2:
```sql
SELECT u.user_id,
    u.email,
    u.login,
    u.name,
    u.birthday
FROM (
    SELECT friends_id
    FROM friends
    WHERE (user_id = 1 OR user_id = 2) AND status = 'friend'
    GROUP BY friends_id
    HAVING COUNT(friends_id) = 2) AS cf
INNER JOIN user AS u ON cf.friends_id = u.user_id;
```


Данная задача решена в рамках обучения на [Яндекс-Практикуме](https://practicum.yandex.ru/java-developer/)
