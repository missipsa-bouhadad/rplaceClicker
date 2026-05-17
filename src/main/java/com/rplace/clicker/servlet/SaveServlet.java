package com.rplace.clicker.servlet;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import com.rplace.clicker.dao.PlayerDAO;
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

@WebServlet("/save")
public class SaveServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Player player = (Player) session.getAttribute("player");
        Double currentClicks = (Double) session.getAttribute("nb_Current_Click");

        //Verif connection
        if (player != null && currentClicks != null) {
            EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("EMF");
            PlayerDAO dao = new PlayerDAO(emf);
            PlayerBonusService bonusService = new PlayerBonusService(emf);

            LocalDateTime now = LocalDateTime.now();
            double passiveGain = 0;
            if(player.getLastUpdate() != null){
                long millis = Duration.between(player.getLastUpdate(), now).toMillis();
                float cps = bonusService.getTotalPassiveIncome(player.getId());
                passiveGain = (millis * cps / 1000.0);
            }

            double newTotalCredits = currentClicks + passiveGain;
            player.setCredits(newTotalCredits);
            player.setLastUpdate(now);

            dao.updatePlayer(player);
            session.setAttribute("player", player);
            session.setAttribute("nb_Current_Click", newTotalCredits);

            response.setContentType("text/plain");
            response.getWriter().write(String.valueOf(newTotalCredits));

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            //Non connecter
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}