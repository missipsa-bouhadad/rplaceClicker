package com.rplace.clicker.service;

import com.rplace.clicker.dao.BonusDAO;
import com.rplace.clicker.model.Bonus;
import com.rplace.clicker.model.PlayerBonus;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class BonusService {
    private final BonusDAO bonusDAO;

    public BonusService(EntityManagerFactory emf) {
        this.bonusDAO = new BonusDAO(emf);
    }

    public List<Bonus> loadAllBonuses() {
        return bonusDAO.findAllBonuses();
    }

    public void updateBonusesPriceAndQuantity(List<Bonus> bonuses, List<PlayerBonus> playerBonuses) {
        for (Bonus bonus : bonuses) {
            int quantityOwned = 0;

            if (playerBonuses != null) {
                for (PlayerBonus pb : playerBonuses) {
                    if (pb.getBonus().getId() == bonus.getId()) {
                        quantityOwned = pb.getQuantity();
                        break;
                    }
                }
            }

            bonus.setUserQuantityOwned(quantityOwned);

            bonus.setCurrentPrice(computePrice(bonus.getCost(), quantityOwned));
        }
    }

    // prix du prochain exemplaire : +15% par exemplaire deja possede, arrondi au superieur
    public static long computePrice(long baseCost, int quantityOwned) {
        return (long) Math.ceil(baseCost * Math.pow(1.15, quantityOwned));
    }
}
