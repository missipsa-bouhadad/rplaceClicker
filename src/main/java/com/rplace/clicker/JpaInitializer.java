package com.rplace.clicker;

import jakarta.persistence.EntityManager;
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

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("rplaceClickPU", properties);
            sce.getServletContext().setAttribute("EMF", entityManagerFactory);
            System.out.println("JPA initialisé avec succès");
        } catch (Exception e) {
            System.err.println("ERREUR CRITIQUE lors de l'initialisation de JPA :");
            e.printStackTrace();
            throw e; 
        }

        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();

            Long count = em.createQuery("SELECT COUNT(b) FROM Bonus b", Long.class).getSingleResult();

            if (count == 0) {
                System.out.println("Table des bonus vide : Insertion automatique...");

                String sql = "INSERT INTO bonuses (name, description, type, cost, creditsGeneratedPerSecond) VALUES" +
                "('Autoclicker', 'Génère des crédits automatiquement', 'PASSIVE', 20, 0.1)," +
                "('Mineur', 'Un gentil mineur qui mine des cookies pour toi.', 'PASSIVE', 200, 1)," +
                "('Super Mineur', 'Extrait efficacement les ressources du système.', 'PASSIVE', 500, 3)," +
                "('Ferme', 'Fait pousser des cookies à partir de rien.', 'PASSIVE', 1100, 25)," +
                "('Mine', 'Extrait des cookies bruts directement de la roche.', 'PASSIVE', 12000, 100)," +
                "('Usine', 'Produit des cookies à la chaîne en grande quantité.', 'PASSIVE', 130000, 500)," +
                "('Banque', 'Génère des cookies grâce aux taux d''intérêts.', 'PASSIVE', 1400000, 2500)," +
                "('Temple', 'Prie pour obtenir plus de cookies divins.', 'PASSIVE', 20000000, 15000)," +
                "('Tour de Sorcier', 'Invoque des cookies grâce à de puissants sorts magiques.', 'PASSIVE', 330000000, 100000)," +
                "('Vaisseau Spatial', 'Rapporte de précieux cookies depuis la planète Pixelium.', 'PASSIVE', 5100000000, 2000000)," +
                "('Laboratoire d''Alchimie', 'Transforme la matière pure en cookies.', 'PASSIVE', 75000000000, 45000000)";

                em.createNativeQuery(sql).executeUpdate();
                System.out.println("Bonus insérés avec succès !");
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erreur lors de l'insertion des bonus :");
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}