package com.rplace.clicker.service;

import com.rplace.clicker.dao.PlayerBonusDAO;
import com.rplace.clicker.dao.PlayerDAO;
import com.rplace.clicker.dto.PurchaseResult;
import com.rplace.clicker.model.Bonus;
import com.rplace.clicker.model.Player;
import com.rplace.clicker.model.PlayerBonus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerBonusService {
    private final EntityManagerFactory emf;
    private final PlayerBonusDAO playerBonusDAO;
    private final PlayerDAO playerDAO;

    public PlayerBonusService(EntityManagerFactory emf) {
        this.emf = emf;
        this.playerBonusDAO = new PlayerBonusDAO(emf);
        this.playerDAO = new PlayerDAO(emf);
    }

    // tout dans une transaction (un doubl clic ne peut pas debiter deux fois)
    public PurchaseResult buyBonus(Player player, int bonusId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Player p = playerDAO.lockPlayer(em, player.getId());
            Bonus b = em.find(Bonus.class, bonusId);
            if (p == null || b == null) {
                throw new IllegalArgumentException("Joueur ou bonus introuvable");
            }

            PlayerBonus pb = playerBonusDAO.findPlayerBonusByBonusId(em, p.getId(), b.getId());
            int quantity = pb == null ? 0 : pb.getQuantity();

            // meme calcul que l'affichage de la boutique
            long cost = BonusService.computePrice(b.getCost(), quantity);
            if (p.getCredits() < cost) {
                em.getTransaction().rollback();
                return new PurchaseResult(false, p);
            }

            p.setCredits(p.getCredits() - cost);
            if (pb == null) {
                pb = new PlayerBonus();
                pb.setPlayer(p);
                pb.setBonus(b);
                pb.setQuantity(1);
                em.persist(pb);
            } else {
                pb.setQuantity(quantity + 1);
            }
            em.getTransaction().commit();
            return new PurchaseResult(true, p);
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    // ajoute le gain passif depuis lastUpdate aux credits de session et enregistre en base
    public double saveProgress(Player player, double sessionCredits) {
        LocalDateTime now = LocalDateTime.now();
        double passiveGain = 0;
        if (player.getLastUpdate() != null) {
            long millis = Duration.between(player.getLastUpdate(), now).toMillis();
            float cps = getTotalPassiveIncome(player.getId());
            passiveGain = (millis * cps / 1000.0);
        }

        double newTotalCredits = sessionCredits + passiveGain;
        player.setCredits(newTotalCredits);
        player.setLastUpdate(now);
        playerDAO.updatePlayer(player);
        return newTotalCredits;
    }

    // gain passif de base, meme sans bonus
    public static final float BASE_INCOME = 0.1f;

    public float getTotalPassiveIncome(int playerId){
        List<PlayerBonus> playerBonuses = playerBonusDAO.findPlayerBonuses(playerId);
        float totalIncome = BASE_INCOME;
        for (PlayerBonus pb : playerBonuses){
            if (pb.getBonus().getType().equals("PASSIVE")) {
                totalIncome += pb.getBonus().getCreditsGeneratedPerSecond() * pb.getQuantity();
            }
        }
        return totalIncome;
    }

    // cps de chaque joueur en une seule requete (page stats), un joueur sans bonus n'est pas dans la map
    public Map<Integer, Double> getPassiveIncomeByPlayer() {
        Map<Integer, Double> incomes = new HashMap<>();
        for (Object[] row : playerBonusDAO.sumPassiveIncomeByPlayer()) {
            incomes.put((Integer) row[0], BASE_INCOME + ((Number) row[1]).doubleValue());
        }
        return incomes;
    }

    public List<PlayerBonus> findPlayerBonuses(int playerId){
        return playerBonusDAO.findPlayerBonuses(playerId);
    }
}
