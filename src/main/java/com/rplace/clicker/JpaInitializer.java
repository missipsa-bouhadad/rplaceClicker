package com.rplace.clicker;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class JpaInitializer implements ServletContextListener {

    private EntityManagerFactory entityManager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        entityManager = Persistence.createEntityManagerFactory("rplaceClickPU");
        sce.getServletContext().setAttribute("EMF", entityManager);
        System.out.println("JPA initialisé, la table players a été crée");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }
}