package com.rplace.clicker;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.HashMap;
import java.util.Map;

@WebListener
public class JpaInitializer implements ServletContextListener {

    private EntityManagerFactory entityManagerFactory;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        Map<String, String> properties = new HashMap<>();
        if (dbUrl != null) properties.put("jakarta.persistence.jdbc.url", dbUrl);
        if (dbUser != null) properties.put("jakarta.persistence.jdbc.user", dbUser);
        if (dbPassword != null) properties.put("jakarta.persistence.jdbc.password", dbPassword);

        entityManagerFactory = Persistence.createEntityManagerFactory("rplaceClickPU", properties);
        sce.getServletContext().setAttribute("EMF", entityManagerFactory);
        System.out.println("JPA initialisé");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}