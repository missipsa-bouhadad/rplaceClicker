package com.rplace.clicker.service;

import com.rplace.clicker.dao.PlayerDAO;
import com.rplace.clicker.model.Player;
import jakarta.persistence.EntityManagerFactory;

public class PlayerService {
    private final PlayerDAO playerDAO;

    public PlayerService(EntityManagerFactory emf) {
        this.playerDAO = new PlayerDAO(emf);
    }

    public Player login(String pseudo, String password){
        Player player = playerDAO.findPlayerByPseudo(pseudo);
        if (player == null || !player.getPassword().equals(password)) {
            return null;
        }
        return player;
    }

    public boolean register(String pseudo, String password, int age, String country) {
        if (playerDAO.findPlayerByPseudo(pseudo) != null) {
            return false;
        }
        Player newPlayer = new Player();
        newPlayer.setPseudo(pseudo);
        newPlayer.setPassword(password);
        newPlayer.setAge(age);
        newPlayer.setCountry(country);
        playerDAO.createPlayer(newPlayer);
        return true;
    }

    public void updatePlayer(Player player){
        playerDAO.updatePlayer(player);
    }
}