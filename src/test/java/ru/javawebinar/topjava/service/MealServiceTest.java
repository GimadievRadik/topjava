package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    private MealRepository repository;

    @Autowired
    public void setRepository(MealRepository repository) {
        this.repository = repository;
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        Meal adminMeal = repository.get(START_SEQ + 10, USER_ID);
        checkNotFoundWithId(adminMeal, START_SEQ + 10);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        checkNotFoundWithId(repository.delete(START_SEQ + 10, USER_ID), START_SEQ + 10);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        Meal adminMeal = repository.get(START_SEQ + 10, ADMIN_ID);
        adminMeal.setDescription("Updated description");
        Meal updatedMeal = repository.save(adminMeal, USER_ID);
        checkNotFoundWithId(updatedMeal, adminMeal.getId());
    }

}