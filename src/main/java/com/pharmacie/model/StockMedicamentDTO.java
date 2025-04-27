package com.pharmacie.model;


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

    // Constructeur par défaut
    public StockMedicamentDTO() {
    }

    // Constructeur avec tous les attributs
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
    public String getCodeCIS() {
        return codeCIS;
    }

    public int getId() {
        return id;
    }

    public String getCodeCip13() {
        return codeCip13;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getDenomination() {
        return denomination;
    }

    public String getDosage() {
        return dosage;
    }

    public String getReference() {
        return reference;
    }

    public String getSurOrdonnance() {
        return surOrdonnance;
    }

    public int getStock() {
        return stock;
    }

    public double getPrixHT() {
        return prixHT;
    }

    public double getPrixTTC() {
        return prixTTC;
    }

    public double getTaxe() {
        return taxe;
    }

    public String getAgrementCollectivites() {
        return agrementCollectivites;
    }

    public String getTauxRemboursement() {
        return tauxRemboursement;
    }

    // Setters
    public void setCodeCIS(String codeCIS) {
        this.codeCIS = codeCIS;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCodeCip13(String codeCip13) {
        this.codeCip13 = codeCip13;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setSurOrdonnance(String surOrdonnance) {
        this.surOrdonnance = surOrdonnance;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setPrixHT(double prixHT) {
        this.prixHT = prixHT;
    }

    public void setPrixTTC(double prixTTC) {
        this.prixTTC = prixTTC;
    }

    public void setTaxe(double taxe) {
        this.taxe = taxe;
    }

    public void setAgrementCollectivites(String agrementCollectivites) {
        this.agrementCollectivites = agrementCollectivites;
    }

    public void setTauxRemboursement(String tauxRemboursement) {
        this.tauxRemboursement = tauxRemboursement;
    }
}
