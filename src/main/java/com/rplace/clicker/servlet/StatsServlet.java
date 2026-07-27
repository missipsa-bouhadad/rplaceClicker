package com.rplace.clicker.servlet;

import com.rplace.clicker.dto.PlayerStatsDTO;
import com.rplace.clicker.model.History;
import com.rplace.clicker.model.Pixel;
import com.rplace.clicker.model.Player;
import com.rplace.clicker.service.HistoryService;
import com.rplace.clicker.service.PlayerBonusService;
import com.rplace.clicker.service.PixelService;
import com.rplace.clicker.service.PlayerService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@WebServlet("/stats")
public class StatsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("EMF");

        PlayerService playerService = new PlayerService(emf);

        HistoryService historyService = new HistoryService(emf);
        PixelService pixelService = new PixelService(emf);

        List<Player> players = playerService.getAllPlayers();

        /* j'ai demandé a l'ia comment passer pour chaque le joueur ces stats a la jsp (le model Player n'a pas de stats),
        donc il m'a proposé d'écrire ce qu'on appelle un DTO ou en regroupe toutes les données d'un joueur dans un objet
        */
        List<PlayerStatsDTO> playerStats = new ArrayList<>();
        Map<Integer, Double> incomes = new PlayerBonusService(emf).getPassiveIncomeByPlayer();

        for (Player p : players) {
            long totalPlaced = historyService.getTotalPixelsPlacedByPlayer(p.getPseudo());
            long currentPixels = pixelService.getCurrentPixelsByPlayer(p.getPseudo());
            Pixel oldestPixel = pixelService.getOldestPixelByPlayer(p.getPseudo());
            History record = historyService.getRecord(p.getPseudo());
            double cps = incomes.getOrDefault(p.getId(), (double) PlayerBonusService.BASE_INCOME);
            playerStats.add(new PlayerStatsDTO(p, totalPlaced, currentPixels, oldestPixel, record, cps));
        }

        req.setAttribute("playerStats", playerStats);

        // trier les joueurs selon leurs credits (valeur sauvegardee en base)
        List<PlayerStatsDTO> topCredits = new ArrayList<>(playerStats);
        topCredits.sort(Comparator.comparingDouble((PlayerStatsDTO dto) -> dto.getPlayer().getCredits()).reversed());
        req.setAttribute("topCredits", topCredits);

        // trier les joueurs selon le pourcentage de couverture de la grille
        List<PlayerStatsDTO> topPlayers = new ArrayList<>(playerStats);
        // utilisation de l'ia pour la fonction de comparaison
        topPlayers.sort(Comparator.comparingLong(PlayerStatsDTO::getCurrentPixels).reversed());
        req.setAttribute("topPlayers", topPlayers);

        // trier les joueurs selon l'ancienneté du pixel le plus ancien encore en place (null en dernier)
        List<PlayerStatsDTO> topPlayersWithOldestPixel = new ArrayList<>(playerStats);
        topPlayersWithOldestPixel.sort(Comparator.comparing(dto -> {
            Pixel oldestPixel = dto.getOldestPixel();
            if (oldestPixel == null) {
                return LocalDateTime.MAX;
            }
            LocalDateTime updatedAt = oldestPixel.getUpdatedAt();
            return updatedAt;
        }));
        req.setAttribute("topPlayersWithOldestPixel", topPlayersWithOldestPixel);

        req.getRequestDispatcher("/stats.jsp").forward(req, resp);
    }
}