package com.rplace.clicker.dao;

import com.rplace.clicker.model.Player;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PlayerDAO {
    private EntityManagerFactory emf;

    public PlayerDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void createPlayer(Player player) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(player);
        em.getTransaction().commit();
        em.close();
    }

    public Player findPlayerByPseudo(String pseudo) {
        EntityManager em = emf.createEntityManager();
        try {
            String query = "Select p From Player p Where p.pseudo = :pseudo";
            Player player = em.createQuery(query, Player.class).setParameter("pseudo", pseudo).getSingleResult();
            return player;
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public void updatePlayer(Player player) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(player);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Player> findAllPlayers() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Player> players = em.createQuery("SELECT p FROM Player p ORDER BY p.pseudo", Player.class).getResultList();
            return players;
        } finally {
            em.close();
        }
    }

}
