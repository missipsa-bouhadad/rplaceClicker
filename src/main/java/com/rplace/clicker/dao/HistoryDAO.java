package com.rplace.clicker.dao;

import com.rplace.clicker.model.History;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class HistoryDAO {
    private EntityManagerFactory emf;

    public HistoryDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void insert(History history) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(history);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
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
    public History getCrushedPixel(String pseudo, int x, int y) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("Select h From History h Where h.player.pseudo =  :pseudo and h.x = :x and h.y = :y ORDER BY h.placedAt DESC", History.class)
            .setParameter("pseudo", pseudo).setParameter("x", x).setParameter("y", y)
            .setMaxResults(1)
            .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public void updateAge(int historyId, long age) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            History h = em.find(History.class, historyId);
            if (h != null) {
                h.setAge(age);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
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
