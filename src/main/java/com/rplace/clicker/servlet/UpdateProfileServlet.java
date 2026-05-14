package com.rplace.clicker.servlet;

import com.rplace.clicker.model.Player;
import com.rplace.clicker.service.PlayerService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/updateProfile")
public class UpdateProfileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Player player = (Player) session.getAttribute("player");

        if (player == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("EMF");
        PlayerService playerService = new PlayerService(emf);

        String age = request.getParameter("age");
        String country = request.getParameter("country");

        player.setAge(Integer.valueOf(age));
        player.setCountry(country);

        playerService.updatePlayer(player);
        session.setAttribute("player", player);

        request.setAttribute("success", "Vos informations personnelles ont été mises à jour avec succès.");

        request.getRequestDispatcher("profile.jsp").forward(request, response);

    }
}
