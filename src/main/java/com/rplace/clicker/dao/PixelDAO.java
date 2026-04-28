package com.rplace.clicker.dao;

import com.rplace.clicker.model.Pixel;
import com.rplace.clicker.model.PixelId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PixelDAO {
    private EntityManagerFactory emf;

    public PixelDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // idée proposée par l'IA : utiliser merge au lieu de créer deux fonctions (create et update)
    public void createOrUpdatePixel(Pixel pixel) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pixel);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Pixel findPixelById(PixelId pixelId) {
        EntityManager em = emf.createEntityManager();
        try {
            Pixel pixel = em.find(Pixel.class, pixelId);
            return pixel;
        } finally {
            em.close();
        }
    }

    public List<Pixel> findAllPixels() {
        EntityManager em = emf.createEntityManager();
        try {
            String query = "Select p From Pixel p";
            List<Pixel> pixels = em.createQuery(query, Pixel.class).getResultList();
            return pixels;
        } finally {
            em.close();
        }
    }
}
