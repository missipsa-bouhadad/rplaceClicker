package com.rplace.clicker.service;

import com.rplace.clicker.dao.PixelDAO;
import com.rplace.clicker.model.Pixel;
import jakarta.persistence.EntityManagerFactory;

public class PixelService {
    private final PixelDAO pixelDAO;

    public PixelService(EntityManagerFactory emf) {
        this.pixelDAO = new PixelDAO(emf);
    }

    public long getCurrentPixelsByPlayer(String pseudo) {
        return pixelDAO.countCurrentPixelsByPlayer(pseudo);
    }

    public Pixel getOldestPixelByPlayer(String pseudo) {
        return pixelDAO.findOldestPixelByPlayer(pseudo);
    }
}