package com.rplace.clicker.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/click")
public class ClickerServlet extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
        HttpSession session = request.getSession();

        Integer currentCredit = (Integer) session.getAttribute("nb_Current_Click");

        if (currentCredit == null){
            currentCredit = 0;
        } currentCredit++;

        session.setAttribute("nb_Current_Click", currentCredit);

        

        response.setContentType("text/plain");
        response.getWriter().write(currentCredit.toString());
    }
}
