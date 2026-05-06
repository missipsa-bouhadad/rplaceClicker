package com.rplace.clicker.service;

import com.rplace.clicker.dao.BonusDAO;
import com.rplace.clicker.model.Bonus;
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
}
