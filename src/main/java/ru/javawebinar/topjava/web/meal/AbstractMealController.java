package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.service.MealService;

public abstract class AbstractMealController {

    protected final MealService service;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public AbstractMealController(MealService service) {
        this.service = service;
    }
}
