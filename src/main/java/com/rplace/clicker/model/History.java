package com.rplace.clicker.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "history")
public class History implements Serializable {
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

    @Column(nullable = false)
    private long age = 0;

    public History() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setAge(long age) { this.age = age; }

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

    public long getAge() { return age; }
}
