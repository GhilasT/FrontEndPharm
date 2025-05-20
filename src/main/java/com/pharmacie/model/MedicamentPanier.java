package com.pharmacie.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Extension de la classe MedicamentPanier pour ajouter le nom du médicament
 * afin de l'afficher dans la liste du panier. (côté front)
 * Représente un médicament ajouté à un panier d'achat, avec sa quantité et son prix.
 */
public class MedicamentPanier {
    @JsonProperty("codeCip13")
    private String codeCip13;
    private int quantite;
    private double prixUnitaire;
    private String nomMedicament;

    /**
     * Constructeur par défaut.
     */
    public MedicamentPanier() {}

    /**
     * Constructeur pour créer un médicament dans le panier.
     * @param codeCip13 Le code CIP13 du médicament.
     * @param quantite La quantité du médicament dans le panier.
     * @param prixUnitaire Le prix unitaire du médicament.
     */
    public MedicamentPanier(String codeCip13, int quantite, double prixUnitaire) {
        this.codeCip13 = codeCip13;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    /**
     * Obtient le code CIP13 du médicament.
     * @return Le code CIP13.
     */
    public String getCodeCip13() {
        return this.codeCip13;
    }

    /**
     * Définit le code CIP13 du médicament.
     * @param CodeCip13 Le nouveau code CIP13.
     */
    public void setCodeCip13(String CodeCip13) {
        this.codeCip13 = CodeCip13;
    }

    /**
     * Obtient la quantité du médicament dans le panier.
     * @return La quantité.
     */
    public int getQuantite() {
        return quantite;
    }

    /**
     * Définit la quantité du médicament dans le panier.
     * @param quantite La nouvelle quantité.
     */
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    /**
     * Obtient le prix unitaire du médicament.
     * @return Le prix unitaire.
     */
    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    /**
     * Définit le prix unitaire du médicament.
     * @param prixUnitaire Le nouveau prix unitaire.
     */
    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    /**
     * Obtient le nom du médicament.
     * @return Le nom du médicament.
     */
    public String getNomMedicament() {
        return nomMedicament;
    }

    /**
     * Définit le nom du médicament.
     * @param nomMedicament Le nouveau nom du médicament.
     */
    public void setNomMedicament(String nomMedicament) {
        this.nomMedicament = nomMedicament;
    }

    /**
     * Calcule et obtient le prix total pour ce médicament dans le panier (quantité * prix unitaire).
     * @return Le prix total.
     */
    public double getPrixTotal() {
        return quantite * prixUnitaire;
    }

    /**
     * Retourne une représentation textuelle de l'objet MedicamentPanier.
     * @return Une chaîne de caractères représentant le médicament dans le panier.
     */
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