package com.rplace.clicker.dao;

import com.rplace.clicker.model.Pixel;
import com.rplace.clicker.model.PixelId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PixelDAO {
    private EntityManagerFactory emf;

    public PixelDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Pixel findPixelById(PixelId pixelId) {
        EntityManager em = emf.createEntityManager();
        try {
            Pixel pixel = em.find(Pixel.class, pixelId);
            return pixel;
        } finally {
            em.close();
        }
    }

    public List<Pixel> findAllPixels() {
        EntityManager em = emf.createEntityManager();
        try {
            String query = "Select p From Pixel p";
            List<Pixel> pixels = em.createQuery(query, Pixel.class).getResultList();
            return pixels;
        } finally {
            em.close();
        }
    }

    public long countCurrentPixelsByPlayer(String pseudo) {
        EntityManager em = emf.createEntityManager();
        try {
            long total = em.createQuery("Select COUNT(p) From Pixel p Where p.currentPlayer.pseudo = :pseudo", Long.class)
            .setParameter("pseudo", pseudo).getSingleResult();
            return total;
        } finally {
            em.close();
        }
    }

    public Pixel findOldestPixelByPlayer(String pseudo) {
        EntityManager em = emf.createEntityManager();
        try {
            // utilisation de l'ia pour la requete
            List<Pixel> pixels = em.createQuery("SELECT p FROM Pixel p WHERE p.currentPlayer.pseudo = :pseudo ORDER BY p.updatedAt ASC", Pixel.class)
                    .setParameter("pseudo", pseudo)
                    .setMaxResults(1)
                    .getResultList();

            return pixels.isEmpty() ? null : pixels.get(0);
        } finally {
            em.close();
        }
    }
}
