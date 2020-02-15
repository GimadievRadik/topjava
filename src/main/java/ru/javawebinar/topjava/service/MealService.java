package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.repository.MealRepository;

@Service
public class MealService {
    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    //    public void setRepository(MealRepository repository) {
//        this.repository = repository;
//    }
    //my edit
    public MealRepository getRepository() {
        return repository;
    }

}