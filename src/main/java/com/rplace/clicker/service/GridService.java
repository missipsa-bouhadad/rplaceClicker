package com.rplace.clicker.service;

import com.rplace.clicker.dao.HistoryDAO;
import com.rplace.clicker.dao.PixelDAO;
import com.rplace.clicker.dao.PlayerDAO;
import com.rplace.clicker.dto.PurchaseResult;
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

    public PurchaseResult putPixel(Player player, int x, int y, String color) {
        Player p = playerDAO.findPlayerByPseudo(player.getPseudo());
        if (p == null) return new PurchaseResult(false, player);
        
        Pixel pixel = pixelDAO.findPixelById(new PixelId(x, y));

        if (pixel == null) {
            pixel = new Pixel();
            pixel.setId(new PixelId(x, y));
        }

        int price = pixel.getPrice();
        if (p.getCredits() < price) {
            return new PurchaseResult(false, p);
        }

        Player oldPlayer = pixel.getCurrentPlayer();
        if (oldPlayer != null) {
            History crushedPixel = historyDAO.getCrushedPixel(oldPlayer.getPseudo(), x, y);
            if (crushedPixel != null) {
                long age = Duration.between(crushedPixel.getPlacedAt(), LocalDateTime.now()).toSeconds();
                historyDAO.updateAge(crushedPixel.getId(), age);
            }
        }

        p.setCredits(p.getCredits() - price);
        playerDAO.updatePlayer(p);


        pixel.setColor(color);
        pixel.setModificationCount(pixel.getModificationCount() + 1);
        pixel.setUpdatedAt(LocalDateTime.now());
        pixel.setCurrentPlayer(p);
        pixelDAO.createOrUpdatePixel(pixel);

        History row = new History();
        row.setX(x);
        row.setY(y);
        row.setColor(color);
        row.setPlayer(p);
        historyDAO.insert(row);

        return new PurchaseResult(true, p);

    }

    public Pixel getPixel(int x, int y) {
        return pixelDAO.findPixelById(new PixelId(x, y));
    }

}
