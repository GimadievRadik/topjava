package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoMemoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealDao dao;
    private static final long serialVersionUID = 1L;
    private static String INSERT_OR_EDIT = "/meal.jsp";
    private static String LIST_MEAL = "/meals.jsp";

    DateTimeFormatter servletFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    DateTimeFormatter jspFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm");

    public MealServlet() {
        super();
        log.debug("Constructor, get dao object");
        dao = new MealDaoMemoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals, doGet method");
        String action = request.getParameter("action") == null ? "listMeal" : request.getParameter("action");
        log.debug("Action = {}", action);

        String forward;
        if (action.equalsIgnoreCase("delete")) {
            int id = Integer.parseInt(request.getParameter("id"));
            log.debug("Delete meal id = {}", id);
            dao.delete(id);
            forward = LIST_MEAL;
            request.setAttribute("mealsTo", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
            request.setAttribute("jspFormatter", jspFormatter);
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            int id = Integer.parseInt(request.getParameter("id"));
            log.debug("Receive meal id = {}", id);
            Meal meal = dao.getById(id);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("listMeal")) {
            forward = LIST_MEAL;
            log.debug("Put all MealTo objects to request attribute");
            request.setAttribute("mealsTo", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
            request.setAttribute("jspFormatter", jspFormatter);
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
        log.debug("End of doGet method");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals, doPost method");
        request.setCharacterEncoding("UTF-8");
        LocalDateTime parsedDate = LocalDateTime.parse(request.getParameter("dateTime"), servletFormatter);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String idString = request.getParameter("id");

        Meal meal = new Meal(parsedDate, description, calories);
        if (idString == null || idString.isEmpty()) {
            log.debug("Add meal, id = {}", meal.getId());
            dao.add(meal);
        } else {
            meal.setId(Integer.parseInt(idString));
            log.debug("Update meal, id = {}", meal.getId());
            dao.update(meal);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(LIST_MEAL);
        request.setAttribute("mealsTo", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
        request.setAttribute("jspFormatter", jspFormatter);
        log.debug("End of doPost method");
        dispatcher.forward(request, response);
    }
}
