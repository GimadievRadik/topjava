package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.db.MealsDB;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MealDaoMemoryImpl implements MealDao {
    private ConcurrentHashMap<Integer, Meal> mealsDB;

    public MealDaoMemoryImpl() {
        mealsDB = MealsDB.getMap();
    }

    @Override
    public void add(Meal meal) {
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
        return MealsDB.getAll();
    }

    @Override
    public Meal getById(int id) {
        return mealsDB.get(id);
    }
}
