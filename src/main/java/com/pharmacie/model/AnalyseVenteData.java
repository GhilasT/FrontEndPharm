package com.pharmacie.model;

import java.time.LocalDate;

public class AnalyseVenteData {
    private LocalDate date;
    private int ventes;
    private int commandes;
    private double chiffreAffaire;

    // Constructeur
    public AnalyseVenteData(LocalDate date, int ventes, int commandes, double chiffreAffaire) {
        this.date = date;
        this.ventes = ventes;
        this.commandes = commandes;
        this.chiffreAffaire = chiffreAffaire;
    }

    // Getters et Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getVentes() {
        return ventes;
    }

    public void setVentes(int ventes) {
        this.ventes = ventes;
    }

    public int getCommandes() {
        return commandes;
    }

    public void setCommandes(int commandes) {
        this.commandes = commandes;
    }

    public double getChiffreAffaire() {
        return chiffreAffaire;
    }

    public void setChiffreAffaire(double chiffreAffaire) {
        this.chiffreAffaire = chiffreAffaire;
    }
}
