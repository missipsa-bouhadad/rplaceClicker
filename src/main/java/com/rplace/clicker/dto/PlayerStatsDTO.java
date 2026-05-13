package com.rplace.clicker.dto;

import com.rplace.clicker.model.History;
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
    private final History record;

    public PlayerStatsDTO(Player player, long totalPlaced, long currentPixels, Pixel oldestPixel, History record) {
        this.player = player;
        this.totalPlaced = totalPlaced;
        this.currentPixels = currentPixels;
        this.oldestPixel = oldestPixel;
        this.record = record;
    }

    public Player getPlayer() { return player; }
    public long getTotalPlaced() { return totalPlaced; }
    public long getCurrentPixels() { return currentPixels; }
    public Pixel getOldestPixel() { return oldestPixel; }
    // fonction corrigée par l'ia par ce qu'avant, je retournais un double comme 9.854515 %
    public String getCoveragePercentForPlayer() {
        return String.format("%.2f", (currentPixels / 2500.0) * 100);
    }
    public History getRecord() { return record; }

    public String getRecordFormatted() {
        if (record == null || record.getAge() == 0) return "–";

        long sec = record.getAge();
        long days = sec / 86400;
        long hours = (sec % 86400) / 3600;
        long minutes = (sec % 3600) / 60;

        if (days > 0) return days + "j " + hours + "h " + minutes + "m";
        if (hours > 0) return hours + "h " + minutes + "m";
        return minutes + " minutes";
    }
}