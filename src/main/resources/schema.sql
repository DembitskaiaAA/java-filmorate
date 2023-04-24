DROP TABLE IF EXISTS CLIENT, FILM, FILM_CATEGORY, FILM_RATING, FRIEND, GENRE, LIKES, RATING;

create table IF NOT EXISTS CLIENT
(
    CLIENT_ID   BIGINT auto_increment,
    EMAIL       CHARACTER VARYING(64) not null,
    LOGIN       CHARACTER VARYING(64) not null,
    CLIENT_NAME CHARACTER VARYING(64),
    BIRTHDAY    DATE                  not null,
    constraint CLIENT_PK
        primary key (CLIENT_ID)
);

create table IF NOT EXISTS FILM
(
    FILM_ID      BIGINT auto_increment,
    FILM_NAME    CHARACTER VARYING      not null,
    DESCRIPTION  CHARACTER VARYING(200) not null,
    RELEASE_DATE DATE                   not null,
    DURATION     BIGINT                 not null,
    RATE         INTEGER,
    constraint FILM_PK
        primary key (FILM_ID)
);

create table IF NOT EXISTS FRIEND
(
    FRIEND_CLIENT_ID BIGINT  not null,
    FRIEND_FRIEND_ID BIGINT  not null,
    FRIEND_STATUS    BOOLEAN not null,
    constraint FRIEND_CLIENT_CLIENT_ID_FK
        foreign key (FRIEND_CLIENT_ID) references CLIENT
            on delete cascade,
    constraint FRIEND_CLIENT_CLIENT_ID_FK_2
        foreign key (FRIEND_CLIENT_ID) references CLIENT
            on delete cascade
);

create table IF NOT EXISTS GENRE
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING not null,
    constraint GENRE_PK
        primary key (GENRE_ID)
);

create table IF NOT EXISTS FILM_CATEGORY
(
    FILM_CATEGORY_FILM_ID  BIGINT  not null,
    FILM_CATEGORY_GENRE_ID INTEGER not null,
    constraint FILM_CATEGORY_FILM_FILM_ID_FK
        foreign key (FILM_CATEGORY_FILM_ID) references FILM
            on delete cascade,
    constraint FILM_CATEGORY_GENRE_GENRE_ID_FK
        foreign key (FILM_CATEGORY_GENRE_ID) references GENRE
            on delete cascade
);

create table IF NOT EXISTS LIKES
(
    LIKES_FILM_ID   BIGINT,
    LIKES_CLIENT_ID BIGINT,
    constraint LIKES_CLIENT_CLIENT_ID_FK
        foreign key (LIKES_CLIENT_ID) references CLIENT
            on delete cascade,
    constraint LIKES_FILM_FILM_ID_FK
        foreign key (LIKES_FILM_ID) references FILM
            on delete cascade
);

create table IF NOT EXISTS RATING
(
    RATING_MPA_ID   INTEGER auto_increment,
    RATING_MPA_NAME CHARACTER VARYING not null,
    constraint RATING_PK
        primary key (RATING_MPA_ID)
);

create table IF NOT EXISTS FILM_RATING
(
    FILM_RATING_FILM_ID BIGINT  not null,
    FILM_RATING_MPA_ID  INTEGER not null,
    constraint FILM_RATING_PK
        primary key (FILM_RATING_FILM_ID),
    constraint FILM_RATING_FILM_FILM_ID_FK
        foreign key (FILM_RATING_FILM_ID) references FILM
            on delete cascade,
    constraint FILM_RATING_RATING_RATING_MPA_ID_FK
        foreign key (FILM_RATING_MPA_ID) references RATING
            on delete cascade
);

create unique index IF NOT EXISTS RATING_RATING_MPA_ID_UINDEX
    on RATING (RATING_MPA_ID);

