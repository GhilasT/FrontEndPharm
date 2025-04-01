package com.pharmacie.model;

import java.util.List;

/**
 * Modèle représentant une requête de création de vente.
 */
public class VenteCreateRequest {
    private String pharmacienAdjointId;
    private String clientId;
    private List<MedicamentPanier> medicaments;
    private String dateVente;
    private String modePaiement;
    private double montantTotal;
    private double montantRembourse;
    
    // Constructeur par défaut
    public VenteCreateRequest() {
    }
    
    // Constructeur avec tous les paramètres
    public VenteCreateRequest(String pharmacienAdjointId, String clientId, List<MedicamentPanier> medicaments,
                             String dateVente, String modePaiement, double montantTotal, double montantRembourse) {
        this.pharmacienAdjointId = pharmacienAdjointId;
        this.clientId = clientId;
        this.medicaments = medicaments;
        this.dateVente = dateVente;
        this.modePaiement = modePaiement;
        this.montantTotal = montantTotal;
        this.montantRembourse = montantRembourse;
    }
    
    // Getters et Setters
    public String getPharmacienAdjointId() {
        return pharmacienAdjointId;
    }
    
    public void setPharmacienAdjointId(String pharmacienAdjointId) {
        this.pharmacienAdjointId = pharmacienAdjointId;
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public List<MedicamentPanier> getMedicaments() {
        return medicaments;
    }
    
    public void setMedicaments(List<MedicamentPanier> medicaments) {
        this.medicaments = medicaments;
    }
    
    public String getDateVente() {
        return dateVente;
    }
    
    public void setDateVente(String dateVente) {
        this.dateVente = dateVente;
    }
    
    public String getModePaiement() {
        return modePaiement;
    }
    
    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }
    
    public double getMontantTotal() {
        return montantTotal;
    }
    
    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }
    
    public double getMontantRembourse() {
        return montantRembourse;
    }
    
    public void setMontantRembourse(double montantRembourse) {
        this.montantRembourse = montantRembourse;
    }
}
