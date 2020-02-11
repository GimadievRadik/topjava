package ru.javawebinar.topjava.db;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealsDB {

    private static ConcurrentHashMap<Integer, Meal> mealsMap = new ConcurrentHashMap<>();
    public static AtomicInteger index = new AtomicInteger(0);

    static {
        Meal meal1 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        Meal meal2 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        Meal meal3 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
        Meal meal4 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
        Meal meal5 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
        Meal meal6 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
        Meal meal7 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
        mealsMap.put(meal1.getId(), meal1);
        mealsMap.put(meal2.getId(), meal2);
        mealsMap.put(meal3.getId(), meal3);
        mealsMap.put(meal4.getId(), meal4);
        mealsMap.put(meal5.getId(), meal5);
        mealsMap.put(meal6.getId(), meal6);
        mealsMap.put(meal7.getId(), meal7);
    }

    public static List<Meal> getAll() {
        return new ArrayList<>(mealsMap.values());
    }

    public static ConcurrentHashMap<Integer, Meal> getMap() {
        return mealsMap;
    }

}
