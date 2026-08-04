package com.rplace.clicker.servlet;

import com.rplace.clicker.model.Player;
import com.rplace.clicker.service.PlayerBonusService;
import com.rplace.clicker.service.PlayerService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("EMF");
        PlayerService playerService = new PlayerService(emf);

        String pseudo = req.getParameter("pseudo");
        String password = req.getParameter("password");

        Player player = playerService.login(pseudo, password);

        if (player == null) {
            req.setAttribute("error","Pseudo ou mot de passe incorrect");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();

        //recuperation du score farmer en amont (hors connextion)
        Double farmClicks = (Double) session.getAttribute("nb_Current_Click");
        if (farmClicks == null) farmClicks = 0.0;
        // gains des bonus pendant l'absence, calcules depuis l'ancien lastUpdate
        double offlineGain = new PlayerBonusService(emf).computeOfflineGain(player);
        //mise a jours  BD
        double totalCredits = player.getCredits() + farmClicks + offlineGain;
        player.setCredits(totalCredits);
        player.setLastUpdate(java.time.LocalDateTime.now());
        playerService.updatePlayer(player);

        //Session credit msj
        session.setAttribute("player", player);
        session.setAttribute("nb_Current_Click", totalCredits);
        // lu une seule fois par index.jsp pour afficher la notification
        if (offlineGain >= 1) {
            session.setAttribute("offlineGain", offlineGain);
        }

        resp.sendRedirect(req.getContextPath() + "/");

    }
}