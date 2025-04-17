package com.pharmacie.model;

import java.math.BigDecimal;

public class Medicament {
    private long id;
    private String codeCip13;
    private String codeCIS;
    private String libelle;
    private String denomination;
    private String dosage;
    private String reference;
    private String surOrdonnance;
    private int quantite;
    private int stock;
    private BigDecimal prixHT;
    private BigDecimal prixTTC;
    private BigDecimal taxe;
    private String agrementCollectivites;
    private String tauxRemboursement;
    private String stockId;
    private String numeroLot;
    private String datePeremption;

    public Medicament() {
        this.stock = 0;
    }

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

    // Getters
    public long getId() { return id; }
    public String getCodeCip13() { return codeCip13; }
    public String getCodeCIS() { return codeCIS; }
    public String getLibelle() { return libelle; }
    public String getDenomination() { return denomination; }
    public String getDosage() { return dosage; }
    public String getReference() { return reference; }
    public String getSurOrdonnance() { return surOrdonnance; }
    public int getQuantite() { return quantite; }
    public int getStock() { return stock; }
    public BigDecimal getPrixHT() { return prixHT; }
    public BigDecimal getPrixTTC() { return prixTTC; }
    public BigDecimal getTaxe() { return taxe; }
    public String getAgrementCollectivites() { return agrementCollectivites; }
    public String getTauxRemboursement() { return tauxRemboursement; }
    public String getStockId() { return stockId; }
    public String getNumeroLot() { return numeroLot; }
    public String getDatePeremption() { return datePeremption; }

    // Setters
    public void setId(long id) { this.id = id; }
    public void setCodeCip13(String codeCip13) { this.codeCip13 = codeCip13; }
    public void setCodeCIS(String codeCIS) { this.codeCIS = codeCIS; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public void setDenomination(String denomination) { this.denomination = denomination; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public void setReference(String reference) { this.reference = reference; }
    public void setSurOrdonnance(String surOrdonnance) { this.surOrdonnance = surOrdonnance; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public void setStock(int stock) { this.stock = stock; }
    public void setPrixHT(BigDecimal prixHT) { this.prixHT = prixHT; }
    public void setPrixTTC(BigDecimal prixTTC) { this.prixTTC = prixTTC; }
    public void setTaxe(BigDecimal taxe) { this.taxe = taxe; }
    public void setAgrementCollectivites(String agrementCollectivites) { 
        this.agrementCollectivites = agrementCollectivites; 
    }
    public void setTauxRemboursement(String tauxRemboursement) { 
        this.tauxRemboursement = tauxRemboursement; 
    }
    public void setStockId(String stockId) { this.stockId = stockId; }
    public void setNumeroLot(String numeroLot) { this.numeroLot = numeroLot; }
    public void setDatePeremption(String datePeremption) { this.datePeremption = datePeremption; }
}