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
}
