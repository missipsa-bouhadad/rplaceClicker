package com.rplace.clicker.servlet;

import com.rplace.clicker.dao.PlayerDAO;
import com.rplace.clicker.model.Player;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.swing.plaf.metal.MetalBorders;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("EMF");
        PlayerDAO playerDAO = new PlayerDAO(emf);

        String pseudo = req.getParameter("pseudo");
        String password = req.getParameter("password");
        int age = Integer.parseInt(req.getParameter("age"));
        String country = req.getParameter("country");

        if(playerDAO.findPlayerByPseudo(pseudo) != null){
            req.setAttribute("error","ce pseudo existe déja !");
            req.getRequestDispatcher("/register.jsp").forward(req,resp);
            return;
        }

        Player newPlayer = new Player();
        newPlayer.setPseudo(pseudo);
        newPlayer.setPassword(password);
        newPlayer.setAge(age);
        newPlayer.setCountry(country);

        playerDAO.createPlayer(newPlayer);

        req.setAttribute("success","Compte créé avec succès!");
        req.getRequestDispatcher("/register.jsp").forward(req,resp);

    }
}
