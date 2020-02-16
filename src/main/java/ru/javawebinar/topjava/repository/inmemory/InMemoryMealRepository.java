package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.UsersUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    private InMemoryUserRepository userRepository = new InMemoryUserRepository();

    {
        userRepository.getAll().forEach(user -> MealsUtil.MEALS.forEach(meal -> {
            Meal newMeal = new Meal(counter.incrementAndGet(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
            newMeal.setUserId(user.getId());
            repository.put(counter.get(), newMeal);
        }));
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(SecurityUtil.authUserId());
            repository.put(meal.getId(), meal);
            return meal;
        }
        if (meal.getUserId() == SecurityUtil.authUserId()) {
            // handle case: update, but not present in storage
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        if (repository.containsKey(id) || repository.get(id).getUserId() == SecurityUtil.authUserId()) {
            repository.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id) {
        if (repository.containsKey(id) || repository.get(id).getUserId() == SecurityUtil.authUserId()) {
            return repository.get(id);
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values().stream()
//                .filter(meal -> meal.getUserId() == SecurityUtil.authUserId())
                .sorted(dateTimeComparator)
                .collect(Collectors.toList());
    }

    public Collection<Meal> getAll(LocalTime startDate, LocalTime endDate) {
        return getAll().stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getTime(), startDate, endDate))
                .collect(Collectors.toList());
    }

    Comparator<Meal> dateTimeComparator = (m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime());
}

