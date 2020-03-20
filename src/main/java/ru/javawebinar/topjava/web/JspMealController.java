package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {

    @Autowired
    MealService service;

    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @PostMapping("/create") //вызывается из mealForm.jsp
    public String create(HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        int userId = SecurityUtil.authUserId();
        if (StringUtils.isEmpty(request.getParameter("id"))) {
            log.info("create {} for user {}", meal, userId);
            service.create(meal, userId);
        } else {
            meal.setId(Integer.parseInt(request.getParameter("id")));
            log.info("update {} for user {}", meal, userId);
            service.update(meal, userId);
        }
        return "redirect:/meals";
    }

    @GetMapping
    public String getAll(Model model) {
        log.info("getAll for user {}", SecurityUtil.authUserId());
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping("/add")
    public String add(HttpServletRequest request) {
        String id = request.getParameter("id");
        int userId = SecurityUtil.authUserId();
        final Meal meal;
        if (id == null) {
            log.info("create new meal for user {}", userId);
            meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        } else {
            log.info("update meal for user {}", userId);
            meal = service.get(Integer.parseInt(id), userId);
        }
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        log.info("delete meal {} for user {}", id, SecurityUtil.authUserId());
        service.delete(id, SecurityUtil.authUserId());
        return "redirect:/meals";
    }

//    @GetMapping("/filter")
//    public String doFilter(HttpServletRequest request) {
//        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
//        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
//        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
//        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
//        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, SecurityUtil.authUserId());
//
//        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, SecurityUtil.authUserId());
//        request.setAttribute("meals", MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
//        return "meals";
//    }

    @GetMapping("/filter")
    public String doFilter(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, SecurityUtil.authUserId());
        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, SecurityUtil.authUserId());

        model.addAttribute("meals", MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        return "forward:/meals";
    }
}
