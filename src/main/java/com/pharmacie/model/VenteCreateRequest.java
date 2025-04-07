package com.pharmacie.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Modèle représentant une requête de création de vente. du front
 */
public class VenteCreateRequest {
    private UUID pharmacienAdjointId;
    private UUID clientId;
    private List<MedicamentPanier> medicaments;
    private Date dateVente;
    private String modePaiement;
    private double montantTotal;
    private double montantRembourse;
    private boolean ordonnanceAjoutee;
    
    // Constructeur par défaut
    public VenteCreateRequest() {
    }
    
    // Constructeur avec tous les paramètres
    public VenteCreateRequest(UUID pharmacienAdjointId, UUID clientId, List<MedicamentPanier> medicaments,
                             Date dateVente, String modePaiement, double montantTotal, double montantRembourse) {
        this.pharmacienAdjointId = pharmacienAdjointId;
        this.clientId = clientId;
        this.medicaments = medicaments;
        this.dateVente = dateVente;
        this.modePaiement = modePaiement;
        this.montantTotal = montantTotal;
        this.montantRembourse = montantRembourse;
    }
    
    // Getters et Setters
    public UUID getPharmacienAdjointId() {
        return pharmacienAdjointId;
    }
    public void setPharmacienAdjointId(UUID pharmacienAdjointId) {
        this.pharmacienAdjointId = pharmacienAdjointId;
    }

    public UUID getClientId() {
        return clientId;
    }
    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }
    
    public List<MedicamentPanier> getMedicaments() {
        return medicaments;
    }
    
    public void setMedicaments(List<MedicamentPanier> medicaments) {
        this.medicaments = medicaments;
    }
    
    public Date getDateVente() {
        return dateVente;
    }
    
    public void setDateVente(Date dateVente) {
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
    public boolean isOrdonnanceAjoutee() {
        return ordonnanceAjoutee;
    }

    public void setOrdonnanceAjoutee(boolean ordonnanceAjoutee) {
        this.ordonnanceAjoutee = ordonnanceAjoutee;
    }
}
