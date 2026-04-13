package com.rplace.clicker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

// On a utilisé l'IA ici pour savoir comment JPA peut gérer une clé composite (x,y)

@Embeddable
public class PixelId implements Serializable {

    private int x;
    private int y;

    PixelId(){}

    public PixelId(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null || obj.getClass() != this.getClass()){
            return false;
        }
        PixelId pixelId = (PixelId) obj;
        return x == pixelId.x && y == pixelId.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x,y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
