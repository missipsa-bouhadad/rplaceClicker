package com.rplace.clicker.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

    @Column(nullable = false)
    private String color;

    @ManyToOne
    @JoinColumn(name = "playerId", nullable = false)
    private Player player;

    @Column(nullable = false)
    private LocalDateTime placedAt = LocalDateTime.now();

    public History(){}

    public Integer getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getColor() {
        return color;
    }

    public Player getPlayer() {
        return player;
    }

    public LocalDateTime getPlacedAt() {
        return placedAt;
    }
}
