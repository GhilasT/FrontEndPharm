package com.pharmacie.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Extension de la classe MedicamentPanier pour ajouter le nom du médicament
 * afin de l'afficher dans la liste du panier. (côté front)
 */
public class MedicamentPanier {
    @JsonProperty("codeCip13")
    private String codeCip13;
    private int quantite;
    private double prixUnitaire;
    private String nomMedicament;

    public MedicamentPanier() {}

    public MedicamentPanier(String codeCip13, int quantite, double prixUnitaire) {
        this.codeCip13 = codeCip13;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public String getCodeCip13() {
        return this.codeCip13;
    }

    public void setCodeCip13(String CodeCip13) {
        this.codeCip13 = CodeCip13;
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
                "codeCIP13='" + codeCip13 + '\'' +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                ", nomMedicament='" + nomMedicament + '\'' +
                '}';
    }

}