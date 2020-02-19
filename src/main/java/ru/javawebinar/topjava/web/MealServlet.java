package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private ConfigurableApplicationContext springCtxt;
    private MealRestController mealController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        springCtxt = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealController = springCtxt.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        springCtxt.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew()) {
            mealController.create(meal);
        } else mealController.update(meal, meal.getId());
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        String leftDate = request.getParameter("startDate");
        LocalDate startDate = null;
        if (leftDate != null && !leftDate.equals("")) {
            startDate = LocalDate.parse(leftDate, DateTimeFormatter.ISO_LOCAL_DATE);
        }

        String rightDate = request.getParameter("endDate");
        LocalDate endDate = null;
        if (rightDate != null && !rightDate.equals("")) {
            endDate = LocalDate.parse(rightDate, DateTimeFormatter.ISO_LOCAL_DATE);
        }

        String leftTime = request.getParameter("startTime");
        LocalTime startTime = null;
        if (leftTime != null && !leftTime.equals("")) {
            startTime = LocalTime.parse(leftTime, DateTimeFormatter.ofPattern("HH:mm"));
        }

        String rightTime = request.getParameter("endTime");
        LocalTime endTime = null;
        if (rightTime != null && !rightTime.equals("")) {
            endTime = LocalTime.parse(rightTime, DateTimeFormatter.ofPattern("HH:mm"));
        }

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filtered":
                log.info("filtered");
                request.setAttribute("meals", mealController.getAllFiltered(startDate, endDate, startTime, endTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        mealController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
