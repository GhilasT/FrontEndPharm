package com.pharmacie.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Medicament {
    private long id;
    private String codeCip13;
    private double prix;
    private int quantite;
    private String codeCIS;
    private String libelle;
    private String denomination; // Nouveau champ pour la dénomination
    private String dosage;
    private String reference;
    private String surOrdonnance;
    private int stock;
    private BigDecimal prixHT; // Nouveau champ pour le prix hors taxe
    private BigDecimal prixTTC; // Nouveau champ pour le prix TTC
    private BigDecimal taxe; // Nouveau champ pour la taxe
    private String agrementCollectivites; // Nouveau champ pour l'agrément aux collectivités
    private String tauxRemboursement; // Nouveau champ pour le taux de remboursement
    
    // Constructeur par défaut
    public Medicament() {
        this.stock = 0; // Initialisation du stock à 0 comme demandé
    }
    
    // Constructeur avec paramètres
    public Medicament(String codeCIS, String libelle, String denomination, String dosage, String reference, 
                     String surOrdonnance, int stock, BigDecimal prixHT, BigDecimal prixTTC, 
                     BigDecimal taxe, String agrementCollectivites, String tauxRemboursement) {
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
        public long getId() {
            return id;
        }
        
        public String getCodeCip13() {
            return codeCip13;
        }
        
        public double getPrix() {
            return prix;
        }
        
        public int getQuantite() {
            return quantite;
        }
        
        // Setters
        public void setId(long id) {
            this.id = id;
        }
        
        public void setCodeCip13(String codeCip13) {
            this.codeCip13 = codeCip13;
        }
        
        public void setPrix(double prix) {
            this.prix = prix;
        }
        
        public void setQuantite(int quantite) {
            this.quantite = quantite;
        }
    

    // Getters et setters
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
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    public BigDecimal getPrixHT() { return prixHT; }
    public void setPrixHT(BigDecimal prixHT) { this.prixHT = prixHT; }
    
    public BigDecimal getPrixTTC() { return prixTTC; }
    public void setPrixTTC(BigDecimal prixTTC) { this.prixTTC = prixTTC; }
    
    public BigDecimal getTaxe() { return taxe; }
    public void setTaxe(BigDecimal taxe) { this.taxe = taxe; }
    
    public String getAgrementCollectivites() { return agrementCollectivites; }
    public void setAgrementCollectivites(String agrementCollectivites) { this.agrementCollectivites = agrementCollectivites; }
    
    public String getTauxRemboursement() { return tauxRemboursement; }
    public void setTauxRemboursement(String tauxRemboursement) { this.tauxRemboursement = tauxRemboursement; }
}
