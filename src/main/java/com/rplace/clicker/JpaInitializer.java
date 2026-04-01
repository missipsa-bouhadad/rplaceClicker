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
    // Création d'une map
    java.util.Map<String, String> properties = new java.util.HashMap<>();
    
    //Recuperation des variable d'environnement de Render
    properties.put("jakarta.persistence.jdbc.url", System.getenv("DB_URL"));
    properties.put("jakarta.persistence.jdbc.user", System.getenv("DB_USER"));
    properties.put("jakarta.persistence.jdbc.password", System.getenv("DB_PASSWORD"));

    try {
        entityManager = Persistence.createEntityManagerFactory("rplaceClickPU", properties);
        sce.getServletContext().setAttribute("EMF", entityManager);
        System.out.println("JPA initialisé avec succès grâce aux variables d'environnement !");
    } catch (Exception e) {
        System.err.println("ALERTE : Échec de connexion à la base de données : " + e.getMessage());
        e.printStackTrace();
    }
}

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }
}