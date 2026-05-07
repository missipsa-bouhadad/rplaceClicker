package com.rplace.clicker.servlet;

import java.io.IOException;
import com.rplace.clicker.dao.PlayerDAO;
import com.rplace.clicker.model.Player;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/save")
public class SaveServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Player player = (Player) session.getAttribute("player");
        Integer currentClicks = (Integer) session.getAttribute("nb_Current_Click");

        //Verif connection
        if (player != null && currentClicks != null) {
            EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("EMF");
            PlayerDAO dao = new PlayerDAO(emf);

            player.setCredits(currentClicks);

            dao.updatePlayer(player);

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            //Non connecter
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}