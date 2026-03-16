package com.rplace.clicker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pseudo;
    private String password;
    private int credits = 0;

    public Long getId() { return id; }
    public String getPseudo() { return pseudo; }
    public void setPseudo(String pseudo) { this.pseudo = pseudo; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
}