package ru.javawebinar.topjava.service.meal;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.DATAJPA)
public class MealDataJpaServiceTest extends MealServiceTest {}
