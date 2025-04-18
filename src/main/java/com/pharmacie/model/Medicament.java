package com.pharmacie.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    // Classe interne pour représenter un stock
    public static class Stock {
        private String numeroLot;
        private String datePeremption;
        private String emplacement;
        private int seuilAlerte;
        private int quantite; // Quantité disponible dans ce stock

        // Getters & Setters
        public String getNumeroLot() { return numeroLot; }
        public void setNumeroLot(String numeroLot) { this.numeroLot = numeroLot; }

        public String getDatePeremption() { return datePeremption; }
        public void setDatePeremption(String datePeremption) { this.datePeremption = datePeremption; }

        public String getEmplacement() { return emplacement; }
        public void setEmplacement(String emplacement) { this.emplacement = emplacement; }

        public int getSeuilAlerte() { return seuilAlerte; }
        public void setSeuilAlerte(int seuilAlerte) { this.seuilAlerte = seuilAlerte; }

        public int getQuantite() { return quantite; }
        public void setQuantite(int quantite) { this.quantite = quantite; }
    }

    // Getters & Setters principaux
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getCodeCip13() { return codeCip13; }
    public void setCodeCip13(String codeCip13) { this.codeCip13 = codeCip13; }

    public String getCodeCIS() { return codeCIS; }
    public void setCodeCIS(String codeCIS) { this.codeCIS = codeCIS; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getDenomination() { return denomination; }
    public void setDenomination(String denomination) { this.denomination = denomination; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getSurOrdonnance() { return surOrdonnance; }
    public void setSurOrdonnance(String surOrdonnance) { this.surOrdonnance = surOrdonnance; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public BigDecimal getPrixHT() { return prixHT; }
    public void setPrixHT(BigDecimal prixHT) { this.prixHT = prixHT; }

    public BigDecimal getPrixTTC() { return prixTTC; }
    public void setPrixTTC(BigDecimal prixTTC) { this.prixTTC = prixTTC; }

    public BigDecimal getTaxe() { return taxe; }
    public void setTaxe(BigDecimal taxe) { this.taxe = taxe; }

    public String getAgrementCollectivites() { return agrementCollectivites; }
    public void setAgrementCollectivites(String agrementCollectivites) { 
        this.agrementCollectivites = agrementCollectivites; 
    }

    public String getTauxRemboursement() { return tauxRemboursement; }
    public void setTauxRemboursement(String tauxRemboursement) { 
        this.tauxRemboursement = tauxRemboursement; 
    }

    public String getStockId() { return stockId; }
    public void setStockId(String stockId) { this.stockId = stockId; }

    public String getNumeroLot() { return numeroLot; }
    public void setNumeroLot(String numeroLot) { this.numeroLot = numeroLot; }

    public String getDatePeremption() { return datePeremption; }
    public void setDatePeremption(String datePeremption) { this.datePeremption = datePeremption; }

    public List<Stock> getStocks() { return stocks; }
    public void setStocks(List<Stock> stocks) { this.stocks = stocks; }

    // Constructeurs
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
}