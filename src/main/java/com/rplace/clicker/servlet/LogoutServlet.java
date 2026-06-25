package com.rplace.clicker.servlet;

import com.rplace.clicker.model.Player;
import com.rplace.clicker.service.PlayerBonusService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            // sauvegarde avant de perdre la session, sinon les gains depuis la derniere autosave disparaissent
            Player player = (Player) session.getAttribute("player");
            Double currentClicks = (Double) session.getAttribute("nb_Current_Click");
            if (player != null && currentClicks != null) {
                EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("EMF");
                new PlayerBonusService(emf).saveProgress(player, currentClicks);
            }
            session.invalidate();
        }
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
