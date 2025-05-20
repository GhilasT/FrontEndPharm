package com.pharmacie.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un médicament avec ses informations détaillées et ses stocks.
 */
public class Medicament {
    private long id;
    private String codeCip13;
    private String codeCIS;
    private String libelle;
    private String denomination;
    private String dosage;
    private String reference;
    private String surOrdonnance;
    private int quantite; // Quantité vendue dans la vente
    private int stock; // Stock global (si nécessaire)
    private BigDecimal prixHT;
    private BigDecimal prixTTC;
    private BigDecimal taxe;
    private String agrementCollectivites;
    private String tauxRemboursement;
    private String stockId;
    private String numeroLot; // Du premier stock (pour rétrocompatibilité)
    private String datePeremption; // Du premier stock (pour rétrocompatibilité)
    private List<Stock> stocks = new ArrayList<>(); // Liste complète des stocks

    /**
     * Classe interne représentant un stock spécifique d'un médicament.
     */
    public static class Stock {
        private String numeroLot;
        private String datePeremption;
        private String emplacement;
        private int seuilAlerte;
        private int quantite; // Quantité disponible dans ce stock

        // Getters & Setters
        /**
         * Obtient le numéro de lot du stock.
         * @return Le numéro de lot.
         */
        public String getNumeroLot() { return numeroLot; }
        /**
         * Définit le numéro de lot du stock.
         * @param numeroLot Le nouveau numéro de lot.
         */
        public void setNumeroLot(String numeroLot) { this.numeroLot = numeroLot; }

        /**
         * Obtient la date de péremption du stock.
         * @return La date de péremption.
         */
        public String getDatePeremption() { return datePeremption; }
        /**
         * Définit la date de péremption du stock.
         * @param datePeremption La nouvelle date de péremption.
         */
        public void setDatePeremption(String datePeremption) { this.datePeremption = datePeremption; }

        /**
         * Obtient l'emplacement du stock.
         * @return L'emplacement.
         */
        public String getEmplacement() { return emplacement; }
        /**
         * Définit l'emplacement du stock.
         * @param emplacement Le nouvel emplacement.
         */
        public void setEmplacement(String emplacement) { this.emplacement = emplacement; }

        /**
         * Obtient le seuil d'alerte pour ce stock.
         * @return Le seuil d'alerte.
         */
        public int getSeuilAlerte() { return seuilAlerte; }
        /**
         * Définit le seuil d'alerte pour ce stock.
         * @param seuilAlerte Le nouveau seuil d'alerte.
         */
        public void setSeuilAlerte(int seuilAlerte) { this.seuilAlerte = seuilAlerte; }

        /**
         * Obtient la quantité disponible dans ce stock.
         * @return La quantité.
         */
        public int getQuantite() { return quantite; }
        /**
         * Définit la quantité disponible dans ce stock.
         * @param quantite La nouvelle quantité.
         */
        public void setQuantite(int quantite) { this.quantite = quantite; }
    }

    // Getters & Setters principaux
    /**
     * Obtient l'identifiant du médicament.
     * @return L'identifiant.
     */
    public long getId() { return id; }
    /**
     * Définit l'identifiant du médicament.
     * @param id Le nouvel identifiant.
     */
    public void setId(long id) { this.id = id; }

    /**
     * Obtient le code CIP13 du médicament.
     * @return Le code CIP13.
     */
    public String getCodeCip13() { return codeCip13; }
    /**
     * Définit le code CIP13 du médicament.
     * @param codeCip13 Le nouveau code CIP13.
     */
    public void setCodeCip13(String codeCip13) { this.codeCip13 = codeCip13; }

    /**
     * Obtient le code CIS du médicament.
     * @return Le code CIS.
     */
    public String getCodeCIS() { return codeCIS; }
    /**
     * Définit le code CIS du médicament.
     * @param codeCIS Le nouveau code CIS.
     */
    public void setCodeCIS(String codeCIS) { this.codeCIS = codeCIS; }

    /**
     * Obtient le libellé du médicament.
     * @return Le libellé.
     */
    public String getLibelle() { return libelle; }
    /**
     * Définit le libellé du médicament.
     * @param libelle Le nouveau libellé.
     */
    public void setLibelle(String libelle) { this.libelle = libelle; }

    /**
     * Obtient la dénomination du médicament.
     * @return La dénomination.
     */
    public String getDenomination() { return denomination; }
    /**
     * Définit la dénomination du médicament.
     * @param denomination La nouvelle dénomination.
     */
    public void setDenomination(String denomination) { this.denomination = denomination; }

    /**
     * Obtient le dosage du médicament.
     * @return Le dosage.
     */
    public String getDosage() { return dosage; }
    /**
     * Définit le dosage du médicament.
     * @param dosage Le nouveau dosage.
     */
    public void setDosage(String dosage) { this.dosage = dosage; }

    /**
     * Obtient la référence du médicament.
     * @return La référence.
     */
    public String getReference() { return reference; }
    /**
     * Définit la référence du médicament.
     * @param reference La nouvelle référence.
     */
    public void setReference(String reference) { this.reference = reference; }

    /**
     * Indique si le médicament est sur ordonnance.
     * @return "Oui" ou "Non", ou une autre indication.
     */
    public String getSurOrdonnance() { return surOrdonnance; }
    /**
     * Définit si le médicament est sur ordonnance.
     * @param surOrdonnance La nouvelle valeur.
     */
    public void setSurOrdonnance(String surOrdonnance) { this.surOrdonnance = surOrdonnance; }

    /**
     * Obtient la quantité vendue du médicament (dans le contexte d'une vente).
     * @return La quantité vendue.
     */
    public int getQuantite() { return quantite; }
    /**
     * Définit la quantité vendue du médicament.
     * @param quantite La nouvelle quantité vendue.
     */
    public void setQuantite(int quantite) { this.quantite = quantite; }

    /**
     * Obtient le stock global du médicament.
     * @return Le stock global.
     */
    public int getStock() { return stock; }
    /**
     * Définit le stock global du médicament.
     * @param stock Le nouveau stock global.
     */
    public void setStock(int stock) { this.stock = stock; }

    /**
     * Obtient le prix hors taxe (HT) du médicament.
     * @return Le prix HT.
     */
    public BigDecimal getPrixHT() { return prixHT; }
    /**
     * Définit le prix hors taxe (HT) du médicament.
     * @param prixHT Le nouveau prix HT.
     */
    public void setPrixHT(BigDecimal prixHT) { this.prixHT = prixHT; }

    /**
     * Obtient le prix toutes taxes comprises (TTC) du médicament.
     * @return Le prix TTC.
     */
    public BigDecimal getPrixTTC() { return prixTTC; }
    /**
     * Définit le prix toutes taxes comprises (TTC) du médicament.
     * @param prixTTC Le nouveau prix TTC.
     */
    public void setPrixTTC(BigDecimal prixTTC) { this.prixTTC = prixTTC; }

    /**
     * Obtient la taxe appliquée au médicament.
     * @return La taxe.
     */
    public BigDecimal getTaxe() { return taxe; }
    /**
     * Définit la taxe appliquée au médicament.
     * @param taxe La nouvelle taxe.
     */
    public void setTaxe(BigDecimal taxe) { this.taxe = taxe; }

    /**
     * Obtient l'information sur l'agrément aux collectivités.
     * @return L'agrément aux collectivités.
     */
    public String getAgrementCollectivites() { return agrementCollectivites; }
    /**
     * Définit l'information sur l'agrément aux collectivités.
     * @param agrementCollectivites La nouvelle valeur d'agrément.
     */
    public void setAgrementCollectivites(String agrementCollectivites) { 
        this.agrementCollectivites = agrementCollectivites; 
    }

    /**
     * Obtient le taux de remboursement du médicament.
     * @return Le taux de remboursement.
     */
    public String getTauxRemboursement() { return tauxRemboursement; }
    /**
     * Définit le taux de remboursement du médicament.
     * @param tauxRemboursement Le nouveau taux de remboursement.
     */
    public void setTauxRemboursement(String tauxRemboursement) { 
        this.tauxRemboursement = tauxRemboursement; 
    }

    /**
     * Obtient l'identifiant du stock (potentiellement pour un stock principal ou par défaut).
     * @return L'identifiant du stock.
     */
    public String getStockId() { return stockId; }
    /**
     * Définit l'identifiant du stock.
     * @param stockId Le nouvel identifiant du stock.
     */
    public void setStockId(String stockId) { this.stockId = stockId; }

    /**
     * Obtient le numéro de lot du premier stock (pour rétrocompatibilité).
     * @return Le numéro de lot.
     */
    public String getNumeroLot() { return numeroLot; }
    /**
     * Définit le numéro de lot du premier stock.
     * @param numeroLot Le nouveau numéro de lot.
     */
    public void setNumeroLot(String numeroLot) { this.numeroLot = numeroLot; }

    /**
     * Obtient la date de péremption du premier stock (pour rétrocompatibilité).
     * @return La date de péremption.
     */
    public String getDatePeremption() { return datePeremption; }
    /**
     * Définit la date de péremption du premier stock.
     * @param datePeremption La nouvelle date de péremption.
     */
    public void setDatePeremption(String datePeremption) { this.datePeremption = datePeremption; }

    /**
     * Obtient la liste complète des stocks pour ce médicament.
     * @return La liste des stocks.
     */
    public List<Stock> getStocks() { return stocks; }
    /**
     * Définit la liste complète des stocks pour ce médicament.
     * @param stocks La nouvelle liste des stocks.
     */
    public void setStocks(List<Stock> stocks) { this.stocks = stocks; }

    // Constructeurs
    /**
     * Constructeur par défaut pour Medicament. Initialise le stock à 0.
     */
    public Medicament() {
        this.stock = 0;
    }

    /**
     * Constructeur avec paramètres pour Medicament.
     * @param codeCIS Le code CIS.
     * @param libelle Le libellé.
     * @param denomination La dénomination.
     * @param dosage Le dosage.
     * @param reference La référence.
     * @param surOrdonnance Indication si sur ordonnance.
     * @param stock Le stock global.
     * @param prixHT Le prix HT.
     * @param prixTTC Le prix TTC.
     * @param taxe La taxe.
     * @param agrementCollectivites L'agrément aux collectivités.
     * @param tauxRemboursement Le taux de remboursement.
     */
    public Medicament(String codeCIS, String libelle, String denomination, String dosage, 
                     String reference, String surOrdonnance, int stock, BigDecimal prixHT, 
                     BigDecimal prixTTC, BigDecimal taxe, String agrementCollectivites, 
                     String tauxRemboursement) {
        this.codeCIS = codeCIS;
        this.libelle = libelle;
        this.denomination = denomination;
        this.dosage = dosage;
        this.reference = reference;
        this.surOrdonnance = surOrdonnance;
        this.stock = stock;
        this.prixHT = prixHT;
        this.prixTTC = prixTTC;
        this.taxe = taxe;
        this.agrementCollectivites = agrementCollectivites;
        this.tauxRemboursement = tauxRemboursement;
    }
}