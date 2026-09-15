package com.rplace.clicker.service;

import com.rplace.clicker.dao.HistoryDAO;
import com.rplace.clicker.dao.PixelDAO;
import com.rplace.clicker.dao.PlayerDAO;
import com.rplace.clicker.dto.PurchaseResult;
import com.rplace.clicker.model.History;
import com.rplace.clicker.model.Pixel;
import com.rplace.clicker.model.PixelId;
import com.rplace.clicker.model.Player;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class GridService {
    private final EntityManagerFactory emf;
    private final PlayerDAO playerDAO;
    private final PixelDAO pixelDAO;
    private final HistoryDAO historyDAO;

    public GridService(EntityManagerFactory emf) {
        this.emf = emf;
        this.playerDAO = new PlayerDAO(emf);
        this.pixelDAO = new PixelDAO(emf);
        this.historyDAO = new HistoryDAO(emf);
    }

    public List<Pixel> loadAllPixels() {
        return pixelDAO.findAllPixels();
    }

    // tout dans une transaction (debit, pixel et historique passent ensemble ou pas du tout)
    public PurchaseResult putPixel(Player player, int x, int y, String color) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Player p = playerDAO.lockPlayer(em, player.getId());
            if (p == null) {
                em.getTransaction().rollback();
                return new PurchaseResult(false, player);
            }

            // verrou sur la case aussi (deux joueurs sur la meme case passent l'un apres l'autre)
            Pixel pixel = em.find(Pixel.class, new PixelId(x, y), LockModeType.PESSIMISTIC_WRITE);
            boolean isNew = pixel == null;
            if (isNew) {
                pixel = new Pixel();
                pixel.setId(new PixelId(x, y));
            }

            int price = pixel.getPrice();
            if (p.getCredits() < price) {
                em.getTransaction().rollback();
                return new PurchaseResult(false, p);
            }

            Player oldPlayer = pixel.getCurrentPlayer();
            if (oldPlayer != null) {
                History crushedPixel = historyDAO.getCrushedPixel(em, oldPlayer.getPseudo(), x, y);
                if (crushedPixel != null) {
                    crushedPixel.setAge(Duration.between(crushedPixel.getPlacedAt(), LocalDateTime.now()).toSeconds());
                }
            }

            p.setCredits(p.getCredits() - price);

            pixel.setColor(color);
            pixel.setModificationCount(pixel.getModificationCount() + 1);
            pixel.setUpdatedAt(LocalDateTime.now());
            pixel.setCurrentPlayer(p);
            if (isNew) {
                em.persist(pixel);
            }

            History row = new History();
            row.setX(x);
            row.setY(y);
            row.setColor(color);
            row.setPlayer(p);
            em.persist(row);

            em.getTransaction().commit();
            return new PurchaseResult(true, p);
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    public Pixel getPixel(int x, int y) {
        return pixelDAO.findPixelById(new PixelId(x, y));
    }

}
