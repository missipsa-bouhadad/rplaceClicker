package com.rplace.clicker.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "player_bonuses")

public class PlayerBonus implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "playerId", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "bonusId", nullable = false)
    private Bonus bonus;

    @Column(nullable = false)
    private int quantity = 1; // Si le joueur achètes le meme bonus deux fois ou plus, on augmente quantity.

    public PlayerBonus() {}

    public Integer getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotalCreditsGeneratedPerSecond() {
        return bonus.getCreditsGeneratedPerSecond() * quantity;
    }
}
