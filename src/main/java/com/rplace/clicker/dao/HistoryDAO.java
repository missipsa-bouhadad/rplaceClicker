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
}
