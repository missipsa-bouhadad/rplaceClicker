package com.rplace.clicker.service;

import com.rplace.clicker.dao.HistoryDAO;
import com.rplace.clicker.dao.PixelDAO;
import com.rplace.clicker.dao.PlayerDAO;
import com.rplace.clicker.model.History;
import com.rplace.clicker.model.Pixel;
import com.rplace.clicker.model.PixelId;
import com.rplace.clicker.model.Player;
import jakarta.persistence.EntityManagerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class GridService {
    private final PlayerDAO playerDAO;
    private final PixelDAO pixelDAO;
    private final HistoryDAO historyDAO;

    public GridService(EntityManagerFactory emf) {
        this.playerDAO = new PlayerDAO(emf);
        this.pixelDAO = new PixelDAO(emf);
        this.historyDAO = new HistoryDAO(emf);
    }

    public List<Pixel> loadAllPixels() {
        return pixelDAO.findAllPixels();
    }

    public boolean putPixel(Player player, int x, int y, String color) {
        Pixel pixel = pixelDAO.findPixelById(new PixelId(x, y));

        if (pixel == null) {
            pixel = new Pixel();
            pixel.setId(new PixelId(x, y));
        }

        int price = pixel.getPrice();
        if (player.getCredits() < price) {
            return false;
        }

        Player oldPlayer = pixel.getCurrentPlayer();
        if (oldPlayer != null) {
            History crushedPixel = historyDAO.getCrushedPixel(oldPlayer.getPseudo(), x, y);
            if (crushedPixel != null) {
                long age = Duration.between(crushedPixel.getPlacedAt(), LocalDateTime.now()).toSeconds();
                historyDAO.updateAge(crushedPixel.getId(), age);
            }
        }

        player.setCredits(player.getCredits() - price);
        playerDAO.updatePlayer(player);


        pixel.setColor(color);
        pixel.setModificationCount(pixel.getModificationCount() + 1);
        pixel.setUpdatedAt(LocalDateTime.now());
        pixel.setCurrentPlayer(player);
        pixelDAO.createOrUpdatePixel(pixel);

        History row = new History();
        row.setX(x);
        row.setY(y);
        row.setColor(color);
        row.setPlayer(player);
        historyDAO.insert(row);

        return true;

    }
}
