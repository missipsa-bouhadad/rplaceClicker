package com.rplace.clicker.dto;

import com.rplace.clicker.model.Pixel;
import com.rplace.clicker.model.Player;

/* j'ai demandé a l'ia comment passer pour chaque le joueur ces stats a la jsp (le model Player n'a pas de stats),
donc il m'a proposé d'écrire ce qu'on appelle un DTO ou en regroupe toutes les données d'un joueur dans un objet
*/
public class PlayerStatsDTO {
    private final Player player;
    private final long totalPlaced;
    private final long currentPixels;
    private final Pixel oldestPixel;

    public PlayerStatsDTO(Player player, long totalPlaced, long currentPixels, Pixel oldestPixel) {
        this.player = player;
        this.totalPlaced = totalPlaced;
        this.currentPixels = currentPixels;
        this.oldestPixel = oldestPixel;
    }

    public Player getPlayer() { return player; }
    public long getTotalPlaced() { return totalPlaced; }
    public long getCurrentPixels() { return currentPixels; }
    public Pixel getOldestPixel() { return oldestPixel; }
}