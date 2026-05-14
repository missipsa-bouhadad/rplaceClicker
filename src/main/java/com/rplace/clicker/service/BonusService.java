package com.rplace.clicker.service;

import com.rplace.clicker.dao.BonusDAO;
import com.rplace.clicker.dao.PlayerBonusDAO;
import com.rplace.clicker.model.Bonus;
import com.rplace.clicker.model.Player;
import com.rplace.clicker.model.PlayerBonus;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class BonusService {
    private final BonusDAO bonusDAO;
    private final PlayerBonusDAO playerBonusDAO;
    private final PlayerBonusService playerBonusService;

    public BonusService(EntityManagerFactory emf) {
        this.bonusDAO = new BonusDAO(emf);
        this.playerBonusDAO = new PlayerBonusDAO(emf);
        this.playerBonusService = new PlayerBonusService(emf);
    }

    public List<Bonus> loadAllBonuses() {
        return bonusDAO.findAllBonuses();
    }

    public long getDynamicPriceForPlayer(Bonus b, Player player) {
        if (player == null) return b.getCost();

        PlayerBonus pb = playerBonusDAO.findPlayerBonusByBonusId(player.getId(), b.getId());
        if (pb == null) {
            return b.getCost();
        }
        return playerBonusService.calculatePrice(b, pb);
    }
}
