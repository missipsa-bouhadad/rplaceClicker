package com.rplace.clicker.servlet;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import com.rplace.clicker.model.Player;
import com.rplace.clicker.service.PlayerBonusService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//correction effectuer avec l'ia pour eviter le saut lors du click pour le conteur de credits

@WebServlet("/click")
public class ClickerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Player player = (Player) session.getAttribute("player");
        Double currentCredit = (Double) session.getAttribute("nb_Current_Click");

        if (currentCredit == null) {
            currentCredit = 0.0;
        }

        if (player != null) {
            EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("EMF");
            PlayerBonusService bonusService = new PlayerBonusService(emf);
            
            LocalDateTime now = LocalDateTime.now();
            if (player.getLastUpdate() != null) {
                long millis = Duration.between(player.getLastUpdate(), now).toMillis();
                float cps = bonusService.getTotalPassiveIncome(player.getId());
                currentCredit += (millis * cps / 1000.0);
            }
            player.setLastUpdate(now);
        }

        currentCredit += 1.0;

        session.setAttribute("nb_Current_Click", currentCredit);

        response.setContentType("text/plain");
        response.getWriter().write(currentCredit.toString());
    }
}
