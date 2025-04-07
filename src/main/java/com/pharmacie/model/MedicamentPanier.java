package com.pharmacie.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Extension de la classe MedicamentPanier pour ajouter le nom du médicament
 * afin de l'afficher dans la liste du panier. (côté front)
 */
public class MedicamentPanier {
    @JsonProperty("codeCip13")
    private String codeCIS;
    private int quantite;
    private double prixUnitaire;
    private String nomMedicament;

    public MedicamentPanier() {}

    public MedicamentPanier(String codeCIS, int quantite, double prixUnitaire) {
        this.codeCIS = codeCIS;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public String getCodeCIS() {
        return codeCIS;
    }

    public void setCodeCIS(String codeCIS) {
        this.codeCIS = codeCIS;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public String getNomMedicament() {
        return nomMedicament;
    }

    public void setNomMedicament(String nomMedicament) {
        this.nomMedicament = nomMedicament;
    }

    public double getPrixTotal() {
        return quantite * prixUnitaire;
    }

    @Override
    public String toString() {
        return "MedicamentPanier{" +
                "codeCIS='" + codeCIS + '\'' +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                ", nomMedicament='" + nomMedicament + '\'' +
                '}';
    }

}