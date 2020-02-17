package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    @Autowired
    private MealRepository repository;

    public Meal create(Meal meal, int userId) {
        ValidationUtil.checkNew(meal);
        return repository.save(meal, userId);
    }

    public Meal update(Meal meal, int mealId, int userId) {
        return checkNotFoundWithId(repository.save(meal, userId), mealId);
    }

    public void delete(int mealId, int userId) {
        checkNotFoundWithId(repository.delete(mealId, userId), mealId);
    }

    public Meal get(int mealId, int userId) {
        return checkNotFoundWithId(repository.get(mealId, userId), mealId);
    }

    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        return MealsUtil.getTos(repository.getAll(userId), caloriesPerDay);
    }

    public List<MealTo> getAllFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int userId, int caloriesPerDay) {
         return MealsUtil.getTos(repository.getDateFiltered(startDate, endDate, userId), caloriesPerDay).stream()
                 .filter(mealTo -> DateTimeUtil.isBetween(mealTo.getTime(), startTime, endTime))
                 .collect(Collectors.toList());
    }
}