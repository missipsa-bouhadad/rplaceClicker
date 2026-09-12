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

@WebServlet("/updatePassword")
public class UpdatePasswordServlet extends HttpServlet {
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

        String oldPwd = request.getParameter("oldPassword");
        String newPwd = request.getParameter("newPassword");

        if (!PlayerService.checkPassword(player, oldPwd)) {
            request.setAttribute("error", "Impossible de mettre à jour votre mot de passe");
            request.getRequestDispatcher("profile.jsp").forward(request, response);
            return;
        }

        player.setPassword(PlayerService.hashPassword(newPwd));

        playerService.updatePlayer(player);
        session.setAttribute("player", player);

        request.setAttribute("success", "Votre mot de passe a été mis à jour avec succès.");

        request.getRequestDispatcher("profile.jsp").forward(request, response);

    }
}
