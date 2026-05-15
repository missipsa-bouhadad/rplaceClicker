package com.rplace.clicker.servlet;

import com.google.gson.Gson;
import com.rplace.clicker.model.Bonus;
import com.rplace.clicker.model.Player;
import com.rplace.clicker.model.PlayerBonus;
import com.rplace.clicker.service.BonusService;
import com.rplace.clicker.service.PlayerBonusService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/bonuses")
public class BonusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("EMF");
        BonusService bonusService = new BonusService(emf);
        PlayerBonusService playerBonusService = new PlayerBonusService(emf);
        float totalCps = 0;

        List<Bonus> bonuses = bonusService.loadAllBonuses();

        HttpSession session = req.getSession(false);
        Player player = null;
        if (session != null && session.getAttribute("player") != null) {
            player = (Player) session.getAttribute("player");
            totalCps = playerBonusService.getTotalPassiveIncome(player.getId());
            List<PlayerBonus> allPlayerBonuses = playerBonusService.findPlayerBonuses(player.getId());

            bonusService.updateBonusesPriceAndQuantity(bonuses, allPlayerBonuses);
        } else {
            bonusService.updateBonusesPriceAndQuantity(bonuses, null);
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();
        result.put("bonuses", bonuses);
        result.put("totalCps", totalCps);

        Gson gson = new Gson();
        resp.getWriter().write(gson.toJson(result));
    }
}
