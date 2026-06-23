package com.rplace.clicker.service;

import com.rplace.clicker.dao.PlayerDAO;
import com.rplace.clicker.model.Player;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

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

    // age facultatif, sinon un entier entre 1 et 120
    public static Integer parseAge(String age) {
        if (age == null || age.isBlank()) {
            return null;
        }
        try {
            int value = Integer.parseInt(age.trim());
            if (value < 1 || value > 120) {
                throw new IllegalArgumentException("Âge invalide");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Âge invalide");
        }
    }

    // colonne pseudo limitee a 20 en base
    public static String validatePseudo(String pseudo) {
        String value = pseudo == null ? "" : pseudo.trim();
        if (value.isEmpty() || value.length() > 20) {
            throw new IllegalArgumentException("Le pseudo doit faire entre 1 et 20 caractères");
        }
        return value;
    }

    // pays facultatif, colonne limitee a 20 en base
    public static String validateCountry(String country) {
        if (country == null || country.isBlank()) {
            return null;
        }
        String value = country.trim();
        if (value.length() > 20) {
            throw new IllegalArgumentException("Le pays ne doit pas dépasser 20 caractères");
        }
        return value;
    }

    public boolean register(String pseudo, String password, Integer age, String country) {
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

    public List<Player> getAllPlayers() {
        return playerDAO.findAllPlayers();
    }
}