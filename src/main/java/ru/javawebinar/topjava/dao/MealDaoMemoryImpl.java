package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoMemoryImpl implements MealDao {
    private static ConcurrentHashMap<Integer, Meal> mealsDB = new ConcurrentHashMap<>();
    private static AtomicInteger index = new AtomicInteger(0);

    static {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        meals.forEach(meal -> new MealDaoMemoryImpl().add(meal));
    }

    @Override
    public void add(Meal meal) {
        meal.setId(index.incrementAndGet());
        mealsDB.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        mealsDB.remove(id);
    }

    @Override
    public void update(Meal meal) {
        mealsDB.put(meal.getId(), meal);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealsDB.values());
    }

    @Override
    public Meal getById(int id) {
        return mealsDB.get(id);
    }
}
