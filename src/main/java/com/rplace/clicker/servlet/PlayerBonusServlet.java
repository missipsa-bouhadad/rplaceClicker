package com.rplace.clicker.servlet;

import com.rplace.clicker.dto.PurchaseResult;
import com.rplace.clicker.model.Player;
import com.rplace.clicker.service.PlayerBonusService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/bonuses/buy/*")
public class PlayerBonusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("player") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Vous devez être connecté pour acheter un bonus.");
            return;
        }

        Player player = (Player) session.getAttribute("player");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("ID du bonus manquant.");
            return;
        }

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("EMF");
        PlayerBonusService playerBonusService = new PlayerBonusService(emf);

        try {
            int bonusId = Integer.parseInt(pathInfo.substring(1));

            PurchaseResult result = playerBonusService.buyBonus(player, bonusId);

            if (result.success()) {
                player = result.player();
                session.setAttribute("player", player);
                session.setAttribute("nb_Current_Click", player.getCredits());

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().print(player.getCredits());
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Fonds insuffisants.");
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Format d'ID de bonus invalide.");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Erreur interne du serveur.");
            e.printStackTrace();
        }
    }
}