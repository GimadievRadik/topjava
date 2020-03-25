package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final String INSERT_ROLES = "INSERT INTO user_roles (user_id, role) VALUES (?, ?)";
    private static final String DELETE_ROLES = "DELETE FROM user_roles WHERE user_id = ?";
    private static final String UPDATE_USER = "UPDATE users SET name=:name, email=:email, password=:password, " +
            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        ArrayList<Role> roles = new ArrayList<>(user.getRoles());
        BatchPreparedStatementSetter batchSetter = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, user.getId());
                ps.setString(2, roles.get(i).name());
            }

            @Override
            public int getBatchSize() {
                return user.getRoles().size();
            }
        };
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(UPDATE_USER, parameterSource) != 0) {
            jdbcTemplate.update(DELETE_ROLES, user.getId());
        } else {
            return null;
        }
        jdbcTemplate.batchUpdate(INSERT_ROLES, batchSetter);
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON u.id = r.user_id WHERE id=?", extractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON u.id = r.user_id WHERE email=?", extractor, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON u.id = r.user_id ORDER BY name, email", extractor);
    }

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private RowMapper<User> withRolesMapper = (rs, rowNum) -> {
        User user = ROW_MAPPER.mapRow(rs, rowNum);
        user.setRoles(Collections.singletonList(Role.valueOf(rs.getString("role"))));
        return user;
    };

    private ResultSetExtractor<List<User>> extractor = (rs) -> {
        Map<User, ArrayList<Role>> results = new LinkedHashMap<>();
        int rowNum = 0;
        while (rs.next()) {
            User user = withRolesMapper.mapRow(rs, rowNum++);
            ArrayList<Role> roles = results.get(user);
            if (roles != null) {
                roles.addAll(new ArrayList<>(user.getRoles()));
            }
            results.computeIfAbsent(user, k -> new ArrayList<>()).addAll(user.getRoles());
        }
        return results.entrySet().stream().map(e -> {
            User user = e.getKey();
            user.setRoles(e.getValue());
            return user;
        }).collect(Collectors.toList());
    };


}
