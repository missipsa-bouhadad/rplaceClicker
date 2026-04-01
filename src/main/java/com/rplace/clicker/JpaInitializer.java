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
        try {
            entityManager = Persistence.createEntityManagerFactory("rplaceClickPU");
            sce.getServletContext().setAttribute("EMF", entityManager);
            System.out.println("JPA initialisé avec succès !");
        } catch (Exception e) {
            System.err.println("ALERTE : La base de données n'est pas encore prête : " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }
}