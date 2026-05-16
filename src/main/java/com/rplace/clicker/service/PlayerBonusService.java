package com.rplace.clicker.service;

import com.rplace.clicker.dao.BonusDAO;
import com.rplace.clicker.dao.PlayerBonusDAO;
import com.rplace.clicker.dao.PlayerDAO;
import com.rplace.clicker.dto.PurchaseResult;
import com.rplace.clicker.model.Bonus;
import com.rplace.clicker.model.Player;
import com.rplace.clicker.model.PlayerBonus;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

public class PlayerBonusService {
    private final PlayerBonusDAO playerBonusDAO;
    private final BonusDAO bonusDAO;
    private final PlayerDAO playerDAO;

    public PlayerBonusService(EntityManagerFactory emf) {
        this.playerBonusDAO = new PlayerBonusDAO(emf);
        this.bonusDAO = new BonusDAO(emf);
        this.playerDAO = new PlayerDAO(emf);
    }

    public int calculatePrice(Bonus bonus, PlayerBonus bonusPlayer) {
        double q = 1.15;
        long u0 = bonus.getCost();
        int n = bonusPlayer.getQuantity();

        double nextPrice = u0 * Math.pow(q, n);

        return (int) Math.ceil(nextPrice);
    }

    public PurchaseResult buyBonus(Player player, int bonusId) {
        Bonus b = bonusDAO.findBonusById(bonusId);

        PlayerBonus pb = playerBonusDAO.findPlayerBonusByBonusId(player.getId(), b.getId());

        if (pb == null) {
            long cost = b.getCost();
            if (player.getCredits() >= cost) {
                player.setCredits((int) (player.getCredits() - cost));
                playerDAO.updatePlayer(player);

                pb = new PlayerBonus();
                pb.setPlayer(player);
                pb.setBonus(b);
                pb.setQuantity(1);

                playerBonusDAO.createPlayerBonus(pb);
                return new PurchaseResult(true, player);
            }
        }

        else {
            int cost = calculatePrice(b, pb);
            if (player.getCredits() >= cost) {
                player.setCredits(player.getCredits() - cost);
                playerDAO.updatePlayer(player);

                pb.setQuantity(pb.getQuantity() + 1);
                playerBonusDAO.updatePlayerBonus(pb);
                return new PurchaseResult(true, player);
            }
        }
        return new PurchaseResult(false, player);
    }

    public float getTotalPassiveIncome(int playerId){
        List<PlayerBonus> playerBonuses = playerBonusDAO.findPlayerBonuses(playerId);
        float totalIncome = 0.1f;
        for (PlayerBonus pb : playerBonuses){
            if (pb.getBonus().getType().equals("PASSIVE")) {
                totalIncome += pb.getBonus().getCreditsGeneratedPerSecond() * pb.getQuantity();
            }
        }
        return totalIncome;
    }

    public List<PlayerBonus> findPlayerBonuses(int playerId){
        return playerBonusDAO.findPlayerBonuses(playerId);
    }
}
