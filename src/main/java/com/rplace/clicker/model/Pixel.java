package com.rplace.clicker.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pixels")
public class Pixel {

    @EmbeddedId
    private PixelId id;

    @Column(nullable = false, length = 7)
    private String color = "#FFFFFF";

    @Column(nullable = false)
    private int modificationCount = 0;

    @Column()
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "playerId")
    private Player currentPlayer;

    public Pixel() {}

    public PixelId getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getModificationCount() {
        return modificationCount;
    }

    public void setModificationCount(int modificationCount) {
        this.modificationCount = modificationCount;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
