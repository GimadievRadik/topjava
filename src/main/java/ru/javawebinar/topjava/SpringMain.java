package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (ConfigurableApplicationContext appCtxt = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            MealRestController controller = appCtxt.getBean(MealRestController.class);
            controller.getAll().forEach(System.out::println);
            System.out.println();
            controller.getAllFiltered(LocalDate.of(2020, Month.JANUARY, 27), LocalDate.of(2020, Month.JANUARY, 29), null, null)
                    .forEach(System.out::println);
            System.out.println();
            controller.getAllFiltered(null, null, null, LocalTime.of(10, 0))
                    .forEach(System.out::println);
            System.out.println();
            System.out.println(controller.get(100));
        }
    }
}
