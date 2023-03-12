package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
@Qualifier("DbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;


    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUserToStorage(User user) {
        return insertInAllTables(user);
    }

    @Override
    public User updateUserInStorage(User user) {
        String sqlGetUserById = "SELECT USER_ID FROM USERS WHERE USER_ID = ?";
        SqlRowSet userRowSet = jdbcTemplate.queryForRowSet(sqlGetUserById, user.getId());
        if (userRowSet.next()) {
            String sqlDeleteUserFromUserUserTable = "DELETE FROM USER_USER WHERE USER_ID = ?";
            String sqlDeleteUserFromFilmUserTable = "DELETE FROM FILM_USER WHERE USER_ID = ?";
            jdbcTemplate.update(sqlDeleteUserFromUserUserTable, user.getId());
            jdbcTemplate.update(sqlDeleteUserFromFilmUserTable, user.getId());
            return insertInAllTables(user);
        } else {
            throw new NotFoundException("Пользователь с id =" + user.getId() + " не найден");
        }
    }


    @Override
    public User getUserFromStorage(Long id) {
        String sqlGetUserById = "SELECT * FROM USERS WHERE USER_ID = ?";
        SqlRowSet userRowSet = jdbcTemplate.queryForRowSet(sqlGetUserById, id);
        if (userRowSet.next()) {
            return makeUser(userRowSet);
        } else {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    @Override
    public List<User> getAll() {
        List<User> allUsers = new ArrayList<>();
        String sqlGetAllUsers = "SELECT * FROM USERS";
        SqlRowSet userRowSet = jdbcTemplate.queryForRowSet(sqlGetAllUsers);
        while (userRowSet.next()) {
            allUsers.add(makeUser(userRowSet));
        }
        return allUsers;
    }


    private User insertInAllTables(User user) {
        try {
            if (user.getId() == null) {
                SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
                simpleJdbcInsert.withTableName("USERS").usingGeneratedKeyColumns("USER_ID");
                MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("EMAIL", user.getEmail())
                    .addValue("LOGIN", user.getLogin())
                    .addValue("NAME", user.getName())
                    .addValue("BIRTHDAY", user.getBirthday());
                Number id = simpleJdbcInsert.executeAndReturnKey(params);
                user.setId(id.longValue());

            } else {
                String sqlUpdateUser = "UPDATE USERS SET NAME = ?, EMAIL = ?, LOGIN = ?, BIRTHDAY = ? " +
                    "WHERE  USER_ID = ?";
                jdbcTemplate.update(sqlUpdateUser, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(),
                    user.getId());
            }
            if (user.getFriends() != null && !user.getFriends().isEmpty()) {
                for (User friend : user.getFriends()) {
                    String sqlPutFriends = "INSERT INTO USER_USER (USER_ID, FRIEND_ID) values (?,?)";
                    jdbcTemplate.update(sqlPutFriends, user.getId(), friend.getId());
                }
            }
            return user;
        } catch (DuplicateKeyException exp) {
            throw new AlreadyExistException(user);
        }
    }

    private User makeUser(SqlRowSet rs) {

        Long id = rs.getLong("USER_ID");
        String email = rs.getString("EMAIL");
        String login = rs.getString("LOGIN");
        String name = rs.getString("NAME");
        LocalDate birthday = Objects.requireNonNull(rs.getDate("BIRTHDAY")).toLocalDate();

        User user = new User(id, email, login, name, birthday);
        addFriends(user);
        return user;
    }

    private void addFriends(User user) {
        Set<User> allFriends = new LinkedHashSet<>();
        String sqlGetFriends = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM USER_USER " +
            "WHERE USER_ID = ? GROUP BY FRIEND_ID) ORDER BY USER_ID";
        SqlRowSet userRowSet = jdbcTemplate.queryForRowSet(sqlGetFriends, user.getId());
        while (userRowSet.next()) {
            allFriends.add(makeUser(userRowSet));
        }
        user.setFriendsSet(allFriends);
    }
}
