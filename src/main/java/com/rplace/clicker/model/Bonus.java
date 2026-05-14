package com.rplace.clicker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bonuses")
public class Bonus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private long cost;

    @Column
    private float creditsGeneratedPerSecond;

    @Column(length = 7)
    private String unlockedColor;

    @Column
    private Integer pixelCount;

    @Transient
    private long currentPrice;

    public Bonus() {}

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public float getCreditsGeneratedPerSecond() {
        return creditsGeneratedPerSecond;
    }

    public void setCreditsGeneratedPerSecond(float creditsGeneratedPerSecond) {
        this.creditsGeneratedPerSecond = creditsGeneratedPerSecond;
    }

    public String getUnlockedColor() {
        return unlockedColor;
    }

    public void setUnlockedColor(String unlockedColor) {
        this.unlockedColor = unlockedColor;
    }

    public int getPixelCount() {
        return pixelCount;
    }

    public void setPixelCount(int pixelCount) {
        this.pixelCount = pixelCount;
    }

    public long getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(long currentPrice) {
        this.currentPrice = currentPrice;
    }
}
