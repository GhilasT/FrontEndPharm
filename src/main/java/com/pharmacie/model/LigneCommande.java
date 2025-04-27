package com.pharmacie.model;

import java.math.BigDecimal;

/**
 * Classe Ligne de Commande représentant une ligne de commande dans le front end.
 */
public class LigneCommande {

    private Long id;
    private int quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal montantLigne;
    private Medicament medicament; 

    public LigneCommande() {

    }

    // Constructeur avec paramètres
    public LigneCommande(Long id, int quantite, BigDecimal prixUnitaire) {
        this.id = id;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.montantLigne = BigDecimal.ZERO; 
    }

    // Constructeur complet avec médicament
    public LigneCommande(Long id, int quantite, BigDecimal prixUnitaire, Medicament medicament) {
        this.id = id;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.medicament = medicament;
        this.montantLigne = BigDecimal.ZERO;
    }

    // Méthode pour calculer le montant de la ligne de commande
    public void calculerMontantLigneAvantSauvegarde() {
        if (prixUnitaire != null && quantite > 0) {
            this.montantLigne = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
        } else {
            this.montantLigne = BigDecimal.ZERO; 
        }
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public BigDecimal getMontantLigne() {
        return montantLigne;
    }

    public void setMontantLigne(BigDecimal montantLigne) {
        this.montantLigne = montantLigne;
    }

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }
}
