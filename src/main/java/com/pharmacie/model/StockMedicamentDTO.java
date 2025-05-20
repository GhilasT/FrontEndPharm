package com.pharmacie.model;

/**
 * Data Transfer Object (DTO) représentant les informations d'un médicament en stock.
 * Utilisé pour transférer les données de stock des médicaments entre les couches de l'application.
 */
public class StockMedicamentDTO {
    private String codeCIS;
    private int id;
    private String codeCip13;
    private String libelle;
    private String denomination;
    private String dosage;
    private String reference;
    private String surOrdonnance;
    private int stock;
    private double prixHT;
    private double prixTTC;
    private double taxe;
    private String agrementCollectivites;
    private String tauxRemboursement;

    /**
     * Constructeur par défaut.
     */
    public StockMedicamentDTO() {
    }

    /**
     * Constructeur avec tous les attributs.
     * @param codeCIS Le code CIS du médicament.
     * @param id L'identifiant interne du médicament.
     * @param codeCip13 Le code CIP13 du médicament.
     * @param libelle Le libellé du médicament.
     * @param denomination La dénomination du médicament.
     * @param dosage Le dosage du médicament.
     * @param reference La référence du médicament.
     * @param surOrdonnance Indique si le médicament est sur ordonnance ("Oui" ou "Non").
     * @param stock La quantité en stock.
     * @param prixHT Le prix hors taxes.
     * @param prixTTC Le prix toutes taxes comprises.
     * @param taxe Le montant de la taxe.
     * @param agrementCollectivites L'agrément aux collectivités.
     * @param tauxRemboursement Le taux de remboursement.
     */
    public StockMedicamentDTO(String codeCIS, int id, String codeCip13, String libelle, String denomination, 
                              String dosage, String reference, String surOrdonnance, int stock, 
                              double prixHT, double prixTTC, double taxe, 
                              String agrementCollectivites, String tauxRemboursement) {
        this.codeCIS = codeCIS;
        this.id = id;
        this.codeCip13 = codeCip13;
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

    // Getters
    /**
     * Obtient le code CIS du médicament.
     * @return Le code CIS.
     */
    public String getCodeCIS() {
        return codeCIS;
    }

    /**
     * Obtient l'identifiant interne du médicament.
     * @return L'identifiant.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtient le code CIP13 du médicament.
     * @return Le code CIP13.
     */
    public String getCodeCip13() {
        return codeCip13;
    }

    /**
     * Obtient le libellé du médicament.
     * @return Le libellé.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Obtient la dénomination du médicament.
     * @return La dénomination.
     */
    public String getDenomination() {
        return denomination;
    }

    /**
     * Obtient le dosage du médicament.
     * @return Le dosage.
     */
    public String getDosage() {
        return dosage;
    }

    /**
     * Obtient la référence du médicament.
     * @return La référence.
     */
    public String getReference() {
        return reference;
    }

    /**
     * Obtient l'indication si le médicament est sur ordonnance.
     * @return "Oui" si sur ordonnance, "Non" sinon.
     */
    public String getSurOrdonnance() {
        return surOrdonnance;
    }

    /**
     * Obtient la quantité en stock du médicament.
     * @return La quantité en stock.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Obtient le prix hors taxes (HT) du médicament.
     * @return Le prix HT.
     */
    public double getPrixHT() {
        return prixHT;
    }

    /**
     * Obtient le prix toutes taxes comprises (TTC) du médicament.
     * @return Le prix TTC.
     */
    public double getPrixTTC() {
        return prixTTC;
    }

    /**
     * Obtient le montant de la taxe appliquée au médicament.
     * @return Le montant de la taxe.
     */
    public double getTaxe() {
        return taxe;
    }

    /**
     * Obtient l'information sur l'agrément aux collectivités du médicament.
     * @return L'agrément aux collectivités.
     */
    public String getAgrementCollectivites() {
        return agrementCollectivites;
    }

    /**
     * Obtient le taux de remboursement du médicament.
     * @return Le taux de remboursement.
     */
    public String getTauxRemboursement() {
        return tauxRemboursement;
    }

    // Setters
    /**
     * Définit le code CIS du médicament.
     * @param codeCIS Le nouveau code CIS.
     */
    public void setCodeCIS(String codeCIS) {
        this.codeCIS = codeCIS;
    }

    /**
     * Définit l'identifiant interne du médicament.
     * @param id Le nouvel identifiant.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Définit le code CIP13 du médicament.
     * @param codeCip13 Le nouveau code CIP13.
     */
    public void setCodeCip13(String codeCip13) {
        this.codeCip13 = codeCip13;
    }

    /**
     * Définit le libellé du médicament.
     * @param libelle Le nouveau libellé.
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Définit la dénomination du médicament.
     * @param denomination La nouvelle dénomination.
     */
    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    /**
     * Définit le dosage du médicament.
     * @param dosage Le nouveau dosage.
     */
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    /**
     * Définit la référence du médicament.
     * @param reference La nouvelle référence.
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Définit si le médicament est sur ordonnance.
     * @param surOrdonnance "Oui" ou "Non".
     */
    public void setSurOrdonnance(String surOrdonnance) {
        this.surOrdonnance = surOrdonnance;
    }

    /**
     * Définit la quantité en stock du médicament.
     * @param stock La nouvelle quantité en stock.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Définit le prix hors taxes (HT) du médicament.
     * @param prixHT Le nouveau prix HT.
     */
    public void setPrixHT(double prixHT) {
        this.prixHT = prixHT;
    }

    /**
     * Définit le prix toutes taxes comprises (TTC) du médicament.
     * @param prixTTC Le nouveau prix TTC.
     */
    public void setPrixTTC(double prixTTC) {
        this.prixTTC = prixTTC;
    }

    /**
     * Définit le montant de la taxe appliquée au médicament.
     * @param taxe Le nouveau montant de la taxe.
     */
    public void setTaxe(double taxe) {
        this.taxe = taxe;
    }

    /**
     * Définit l'information sur l'agrément aux collectivités du médicament.
     * @param agrementCollectivites Le nouvel agrément aux collectivités.
     */
    public void setAgrementCollectivites(String agrementCollectivites) {
        this.agrementCollectivites = agrementCollectivites;
    }

    /**
     * Définit le taux de remboursement du médicament.
     * @param tauxRemboursement Le nouveau taux de remboursement.
     */
    public void setTauxRemboursement(String tauxRemboursement) {
        this.tauxRemboursement = tauxRemboursement;
    }
}
