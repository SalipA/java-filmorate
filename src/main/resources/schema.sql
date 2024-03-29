CREATE TABLE IF NOT EXISTS RATINGS
(
    rating_id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
    name varchar(5) UNIQUE NOT NULL,
    description varchar(100)
    );
CREATE TABLE IF NOT EXISTS GENRES
(
    genre_id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name     varchar(20) UNIQUE NOT NULL
    );
CREATE TABLE IF NOT EXISTS FILMS
(
    film_id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title     varchar UNIQUE NOT NULL,
    description varchar(200),
    release_date date NOT NULL,
    duration int NOT NULL,
    rating_id bigint REFERENCES RATINGS (rating_id),
    CONSTRAINT releaseDateConstraint CHECK (release_date > '1895-12-28'),
    CONSTRAINT durationConstraint CHECK (duration > 0),
    CONSTRAINT idConstraint CHECK (film_id > 0)
    );
CREATE TABLE IF NOT EXISTS FILM_GENRE
(
    film_id  bigint NOT NULL REFERENCES FILMS (film_id),
    genre_id bigint NOT NULL REFERENCES GENRES (genre_id),
    CONSTRAINT films_genre_pk PRIMARY KEY (film_id,genre_id)
    );
CREATE TABLE IF NOT EXISTS USERS
(
    user_id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email     varchar (20) UNIQUE NOT NULL,
    login varchar(20) NOT NULL,
    name varchar(20) NOT NULL ,
    birthday date NOT NULL,
    CONSTRAINT idUserConstraint CHECK (user_id > 0),
    CONSTRAINT emailConstraint CHECK (email LIKE '%@%')
    );
CREATE TABLE IF NOT EXISTS USER_USER
(
    user_id  bigint NOT NULL REFERENCES USERS (user_id),
    friend_id bigint NOT NULL REFERENCES USERS (user_id),
    CONSTRAINT user_user_pk PRIMARY KEY (user_id,friend_id)
    );
CREATE TABLE IF NOT EXISTS FILM_USER
(
    film_id  bigint NOT NULL REFERENCES FILMS (film_id),
    user_id bigint NOT NULL REFERENCES USERS (user_id),
    CONSTRAINT film_user_pk PRIMARY KEY (film_id,user_id)
    );