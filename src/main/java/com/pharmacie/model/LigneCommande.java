package com.pharmacie.model;

import java.math.BigDecimal;

/**
 * Classe LigneCommande représentant une ligne de commande dans l'interface utilisateur.
 * Chaque ligne correspond à un médicament spécifique avec une quantité et un prix.
 */
public class LigneCommande {

    private Long id;
    private int quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal montantLigne;
    private Medicament medicament; 

    /**
     * Constructeur par défaut.
     */
    public LigneCommande() {

    }

    /**
     * Constructeur avec paramètres pour initialiser une ligne de commande.
     * @param id L'identifiant de la ligne de commande.
     * @param quantite La quantité de médicament commandée.
     * @param prixUnitaire Le prix unitaire du médicament.
     */
    public LigneCommande(Long id, int quantite, BigDecimal prixUnitaire) {
        this.id = id;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.montantLigne = BigDecimal.ZERO; 
    }

    /**
     * Constructeur complet avec le médicament associé.
     * @param id L'identifiant de la ligne de commande.
     * @param quantite La quantité de médicament commandée.
     * @param prixUnitaire Le prix unitaire du médicament.
     * @param medicament Le médicament concerné par cette ligne de commande.
     */
    public LigneCommande(Long id, int quantite, BigDecimal prixUnitaire, Medicament medicament) {
        this.id = id;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.medicament = medicament;
        this.montantLigne = BigDecimal.ZERO;
    }

    /**
     * Calcule le montant total de la ligne de commande avant sa sauvegarde.
     * Le montant est calculé en multipliant le prix unitaire par la quantité.
     * Si le prix unitaire est nul ou la quantité est non positive, le montant est zéro.
     */
    public void calculerMontantLigneAvantSauvegarde() {
        if (prixUnitaire != null && quantite > 0) {
            this.montantLigne = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
        } else {
            this.montantLigne = BigDecimal.ZERO; 
        }
    }

    /**
     * Obtient l'identifiant de la ligne de commande.
     * @return L'identifiant.
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant de la ligne de commande.
     * @param id Le nouvel identifiant.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtient la quantité de médicament pour cette ligne de commande.
     * @return La quantité.
     */
    public int getQuantite() {
        return quantite;
    }

    /**
     * Définit la quantité de médicament pour cette ligne de commande.
     * @param quantite La nouvelle quantité.
     */
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    /**
     * Obtient le prix unitaire du médicament pour cette ligne de commande.
     * @return Le prix unitaire.
     */
    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    /**
     * Définit le prix unitaire du médicament pour cette ligne de commande.
     * @param prixUnitaire Le nouveau prix unitaire.
     */
    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    /**
     * Obtient le montant total de cette ligne de commande.
     * @return Le montant de la ligne.
     */
    public BigDecimal getMontantLigne() {
        return montantLigne;
    }

    /**
     * Définit le montant total de cette ligne de commande.
     * @param montantLigne Le nouveau montant de la ligne.
     */
    public void setMontantLigne(BigDecimal montantLigne) {
        this.montantLigne = montantLigne;
    }

    /**
     * Obtient le médicament associé à cette ligne de commande.
     * @return Le médicament.
     */
    public Medicament getMedicament() {
        return medicament;
    }

    /**
     * Définit le médicament associé à cette ligne de commande.
     * @param medicament Le nouveau médicament.
     */
    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }
}
