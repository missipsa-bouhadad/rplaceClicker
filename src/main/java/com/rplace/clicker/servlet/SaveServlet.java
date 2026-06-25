package com.rplace.clicker.servlet;

import java.io.IOException;

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
            PlayerBonusService bonusService = new PlayerBonusService(emf);

            double newTotalCredits = bonusService.saveProgress(player, currentClicks);
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
