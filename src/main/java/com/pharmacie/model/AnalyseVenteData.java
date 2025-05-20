package com.pharmacie.model;

import java.time.LocalDate;

/**
 * Modèle de données pour l'analyse des ventes.
 * Contient des informations agrégées sur les ventes, commandes et chiffre d'affaires pour une date donnée.
 */
public class AnalyseVenteData {
    private LocalDate date;
    private int ventes;
    private int commandes;
    private double chiffreAffaire;

    /**
     * Constructeur pour initialiser les données d'analyse des ventes.
     * @param date La date à laquelle les données se rapportent.
     * @param ventes Le nombre de ventes pour cette date.
     * @param commandes Le nombre de commandes pour cette date.
     * @param chiffreAffaire Le chiffre d'affaires réalisé à cette date.
     */
    public AnalyseVenteData(LocalDate date, int ventes, int commandes, double chiffreAffaire) {
        this.date = date;
        this.ventes = ventes;
        this.commandes = commandes;
        this.chiffreAffaire = chiffreAffaire;
    }

    // Getters et Setters
    /**
     * Obtient la date des données d'analyse.
     * @return La date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Définit la date des données d'analyse.
     * @param date La nouvelle date.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Obtient le nombre de ventes.
     * @return Le nombre de ventes.
     */
    public int getVentes() {
        return ventes;
    }

    /**
     * Définit le nombre de ventes.
     * @param ventes Le nouveau nombre de ventes.
     */
    public void setVentes(int ventes) {
        this.ventes = ventes;
    }

    /**
     * Obtient le nombre de commandes.
     * @return Le nombre de commandes.
     */
    public int getCommandes() {
        return commandes;
    }

    /**
     * Définit le nombre de commandes.
     * @param commandes Le nouveau nombre de commandes.
     */
    public void setCommandes(int commandes) {
        this.commandes = commandes;
    }

    /**
     * Obtient le chiffre d'affaires.
     * @return Le chiffre d'affaires.
     */
    public double getChiffreAffaire() {
        return chiffreAffaire;
    }

    /**
     * Définit le chiffre d'affaires.
     * @param chiffreAffaire Le nouveau chiffre d'affaires.
     */
    public void setChiffreAffaire(double chiffreAffaire) {
        this.chiffreAffaire = chiffreAffaire;
    }

    /**
     * Retourne une représentation textuelle de l'objet AnalyseVenteData.
     * @return Une chaîne de caractères décrivant l'objet.
     */
    @Override
    public String toString() {
        return "AnalyseVenteData [date=" + date + ", ventes=" + ventes + ", commandes=" + commandes + 
               ", chiffreAffaire=" + String.format("%.2f", chiffreAffaire) + "€]";
    }
}
