package com.rplace.clicker.service;

import com.rplace.clicker.dao.BonusDAO;
import com.rplace.clicker.dao.PlayerBonusDAO;
import com.rplace.clicker.dao.PlayerDAO;
import com.rplace.clicker.dto.PurchaseResult;
import com.rplace.clicker.model.Bonus;
import com.rplace.clicker.model.Player;
import com.rplace.clicker.model.PlayerBonus;
import jakarta.persistence.EntityManagerFactory;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerBonusService {
    private final PlayerBonusDAO playerBonusDAO;
    private final BonusDAO bonusDAO;
    private final PlayerDAO playerDAO;

    public PlayerBonusService(EntityManagerFactory emf) {
        this.playerBonusDAO = new PlayerBonusDAO(emf);
        this.bonusDAO = new BonusDAO(emf);
        this.playerDAO = new PlayerDAO(emf);
    }

    public PurchaseResult buyBonus(Player player, int bonusId) {
        Bonus b = bonusDAO.findBonusById(bonusId);

        PlayerBonus pb = playerBonusDAO.findPlayerBonusByBonusId(player.getId(), b.getId());
        int quantity = pb == null ? 0 : pb.getQuantity();

        // meme calcul que l'affichage de la boutique
        long cost = BonusService.computePrice(b.getCost(), quantity);
        if (player.getCredits() < cost) {
            return new PurchaseResult(false, player);
        }

        player.setCredits(player.getCredits() - cost);
        playerDAO.updatePlayer(player);

        if (pb == null) {
            pb = new PlayerBonus();
            pb.setPlayer(player);
            pb.setBonus(b);
            pb.setQuantity(1);
            playerBonusDAO.createPlayerBonus(pb);
        } else {
            pb.setQuantity(quantity + 1);
            playerBonusDAO.updatePlayerBonus(pb);
        }
        return new PurchaseResult(true, player);
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

    // hors ligne : moitie du cps, plafonne a 12h pour ne pas desequilibrer la grille
    private static final double OFFLINE_RATE = 0.5;
    private static final long OFFLINE_MAX_SECONDS = 12 * 3600;

    // credits gagnes entre la derniere sauvegarde et maintenant, a appeler avant de remettre lastUpdate a jour
    public double computeOfflineGain(Player player) {
        if (player.getLastUpdate() == null) {
            return 0;
        }
        long seconds = Math.min(Duration.between(player.getLastUpdate(), LocalDateTime.now()).toSeconds(), OFFLINE_MAX_SECONDS);
        if (seconds <= 0) {
            return 0;
        }
        return getTotalPassiveIncome(player.getId()) * seconds * OFFLINE_RATE;
    }

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
