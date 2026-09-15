package com.rplace.clicker.dao;

import com.rplace.clicker.model.History;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class HistoryDAO {
    private final EntityManagerFactory emf;

    public HistoryDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public long countTotalPixelsPlacedByPlayer(String pseudo) {
        EntityManager em = emf.createEntityManager();
        try {
            // COUNT retourne un long en JPQL
            long total = em.createQuery("Select COUNT(h) From History h Where h.player.pseudo = :pseudo", Long.class)
            .setParameter("pseudo", pseudo).getSingleResult();
            return total;
        } finally {
            em.close();
        }
    }

    // placedAt du pixel écrasé
    public History getCrushedPixel(EntityManager em, String pseudo, int x, int y) {
        List<History> result = em.createQuery("Select h From History h Where h.player.pseudo = :pseudo and h.x = :x and h.y = :y ORDER BY h.placedAt DESC", History.class)
        .setParameter("pseudo", pseudo).setParameter("x", x).setParameter("y", y)
        .setMaxResults(1)
        .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    // la ligne history avec le plus grand age pour un joueur
    public History getRecordFromHistory(String pseudo) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("Select h From History h Where h.player.pseudo = :pseudo ORDER BY h.age DESC", History.class)
            .setParameter("pseudo", pseudo)
            .setMaxResults(1).getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
}
