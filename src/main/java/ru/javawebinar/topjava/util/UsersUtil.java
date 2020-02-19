package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UsersUtil {

    public static final List<User> USER_LIST = Arrays.asList(
            new User("Vanya", "vanya@mail.ru", "vanyapass", Role.ROLE_USER),
            new User("Petya", "petya@yandex.ru", "petyapass", Role.ROLE_USER)
    );

}
