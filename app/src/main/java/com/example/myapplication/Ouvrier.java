package com.example.myapplication;

public class Ouvrier {
    private String nom;
    private String metier;
    private String ville;

    public Ouvrier(String nom, String metier, String ville) {
        this.nom = nom;
        this.metier = metier;
        this.ville = ville;
    }

    public String getNom() { return nom; }
    public String getMetier() { return metier; }
    public String getVille() { return ville; }
}
