package com.rplace.clicker.dao;

import com.rplace.clicker.model.Bonus;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class BonusDAO {
    private EntityManagerFactory emf;

    public BonusDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<Bonus> findAllBonuses() {
        var em = emf.createEntityManager();
        try {
            String query = "Select b From Bonus b";
            return em.createQuery(query, Bonus.class).getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public Bonus findBonusById(int id) {
        var em = emf.createEntityManager();
        try {
            String query = "Select b From Bonus b Where b.id = :id";
            return em.createQuery(query, Bonus.class).setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
}
