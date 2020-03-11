package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaMealRepository implements MealRepository {

    @Autowired
    private CrudMealRepository mealRepository;
    @Autowired
    private CrudUserRepository userRepository;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        meal.setUser(userRepository.getOne(userId));
        if (meal.isNew()) {
            return mealRepository.save(meal);
        } else {
            Optional<Meal> mealInDb = mealRepository.findById(meal.getId());
            if (mealInDb.isPresent() && mealInDb.get().getUser().getId() == userId) {
                return mealRepository.save(meal);
            }
            return null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Optional<Meal> meal = mealRepository.findById(id);
        if (meal.isPresent() && meal.get().getUser().getId() == userId) {
            return meal.get();
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepository.findAll(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return mealRepository.findAllBetweenHalfOpen(startDateTime, endDateTime, userId);
    }
}
