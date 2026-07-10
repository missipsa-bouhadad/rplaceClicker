package com.rplace.clicker.servlet;

import com.rplace.clicker.service.PlayerService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        PlayerService playerService = new PlayerService(emf);

        String password = req.getParameter("password");

        String pseudo;
        Integer age;
        String country;
        try {
            pseudo = PlayerService.validatePseudo(req.getParameter("pseudo"));
            age = PlayerService.parseAge(req.getParameter("age"));
            country = PlayerService.validateCountry(req.getParameter("country"));
            if (password == null || !password.equals(req.getParameter("confirmPassword"))) {
                throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        boolean isCreated = playerService.register(pseudo, password, age, country);

        if (!isCreated){
            req.setAttribute("error","ce pseudo existe déja !");
            req.getRequestDispatcher("/register.jsp").forward(req,resp);
            return;
        }

        // redirection (et pas forward) 
        resp.sendRedirect(req.getContextPath() + "/login?registered");
    }
}