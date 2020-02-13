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
    private ConcurrentHashMap<Integer, Meal> mealsdb = new ConcurrentHashMap<>();
    private AtomicInteger index = new AtomicInteger(0);

    public MealDaoMemoryImpl() {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        meals.forEach(this::add);
    }

    @Override
    public void add(Meal meal) {
        meal.setId(index.incrementAndGet());
        mealsdb.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        mealsdb.remove(id);
    }

    @Override
    public void update(Meal meal) {
        if (mealsdb.containsKey(meal.getId())) {
            mealsdb.put(meal.getId(), meal);
        }
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealsdb.values());
    }

    @Override
    public Meal getById(int id) {
        return mealsdb.get(id);
    }
}
