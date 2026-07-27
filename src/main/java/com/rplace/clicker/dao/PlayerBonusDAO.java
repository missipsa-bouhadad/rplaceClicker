package com.rplace.clicker.dao;

import com.rplace.clicker.model.PlayerBonus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

public class PlayerBonusDAO {
    private EntityManagerFactory emf;

    public PlayerBonusDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<PlayerBonus> findPlayerBonuses(Integer playerId){
        EntityManager em = emf.createEntityManager();
        try{
            String query = "Select pb From PlayerBonus pb Where pb.player.id = :playerId";
            return em.createQuery(query, PlayerBonus.class)
                    .setParameter("playerId", playerId)
                    .getResultList();
        }finally{
            em.close();
        }
    }

    // [playerId, somme cps * quantite] des bonus passifs, groupe par joueur
    public List<Object[]> sumPassiveIncomeByPlayer() {
        EntityManager em = emf.createEntityManager();
        try {
            String query = "Select pb.player.id, Sum(pb.bonus.creditsGeneratedPerSecond * pb.quantity) From PlayerBonus pb "
                    + "Where pb.bonus.type = 'PASSIVE' Group By pb.player.id";
            return em.createQuery(query, Object[].class).getResultList();
        } finally {
            em.close();
        }
    }

    public PlayerBonus findPlayerBonusByBonusId(Integer playerId, Integer bonusId) {
        EntityManager em = emf.createEntityManager();
        try {
            String query = "Select pb From PlayerBonus pb Where pb.player.id = :playerId And pb.bonus.id = :bonusId";
            return em.createQuery(query, PlayerBonus.class)
                    .setParameter("playerId", playerId)
                    .setParameter("bonusId", bonusId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public void createPlayerBonus(PlayerBonus playerBonus) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(playerBonus);
        em.getTransaction().commit();
        em.close();
    }

    public void updatePlayerBonus(PlayerBonus playerBonus) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(playerBonus);
        em.getTransaction().commit();
        em.close();
    }
}
