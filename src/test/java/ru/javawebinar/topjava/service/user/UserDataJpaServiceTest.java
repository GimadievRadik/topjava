package ru.javawebinar.topjava.service.user;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.DATAJPA)
public class UserDataJpaServiceTest extends UserServiceTest {}
