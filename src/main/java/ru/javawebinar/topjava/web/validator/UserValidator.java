package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;

@Component
public class UserValidator implements Validator {

    @Autowired
    UserService service;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        UserTo userTo = (UserTo) target;
        User user = service.getByEmail(userTo.getEmail().toLowerCase());

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.user.email");
        if (user != null) {
            errors.rejectValue("email", "Duplicate.user.email");
        }
    }
}
