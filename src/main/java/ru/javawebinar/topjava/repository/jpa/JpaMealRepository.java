package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User user = em.find(User.class, userId);
        if (meal.isNew()) {
            meal.setUser(user);
            em.persist(meal);
        } else {
            if (em.createNamedQuery(Meal.UPDATE)
                    .setParameter("description", meal.getDescription())
                    .setParameter("calories", meal.getCalories())
                    .setParameter("date_time", meal.getDateTime())
                    .setParameter("id", meal.getId())
                    .setParameter("user", user)
                    .executeUpdate() == 0) {
                return null;
            }
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        User user = em.find(User.class, userId);
        List<Meal> meals = em.createNamedQuery(Meal.GET, Meal.class)
                .setParameter("id", id)
                .setParameter("user", user)
                .getResultList();
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return null;
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return null;
    }
}