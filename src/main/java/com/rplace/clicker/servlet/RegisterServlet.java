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

        String pseudo = req.getParameter("pseudo");
        String password = req.getParameter("password");
        int age = Integer.parseInt(req.getParameter("age"));
        String country = req.getParameter("country");

        boolean isCreated = playerService.register(pseudo, password, age, country);

        if (!isCreated){
            req.setAttribute("error","ce pseudo existe déja !");
            req.getRequestDispatcher("/register.jsp").forward(req,resp);
            return;
        }

        req.setAttribute("success","Compte créé avec succès!");
        req.getRequestDispatcher("/register.jsp").forward(req,resp);
    }
}