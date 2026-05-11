package com.rplace.clicker.service;

import com.rplace.clicker.dao.HistoryDAO;
import jakarta.persistence.EntityManagerFactory;

public class HistoryService {
    private final HistoryDAO historyDAO;

    public HistoryService(EntityManagerFactory emf) {
         this.historyDAO = new HistoryDAO(emf);
    }

    public long getTotalPixelsPlacedByPlayer(String pseudo) {
        return historyDAO.countTotalPixelsPlacedByPlayer(pseudo);
    }
}