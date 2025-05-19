package com.pharmacie.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Modèle représentant une requête de mise à jour de vente.
 */
public class VenteUpdateRequest {
    private UUID idVente;
    private List<MedicamentPanier> medicaments;
    private Date dateVente;
    private String modePaiement;
    private boolean ordonnanceAjoutee;

    // Constructeur par défaut
    public VenteUpdateRequest() {
    }

    // Constructeur avec tous les paramètres
    public VenteUpdateRequest(UUID idVente, List<MedicamentPanier> medicaments,
                             Date dateVente, String modePaiement, boolean ordonnanceAjoutee) {
        this.idVente = idVente;
        this.medicaments = medicaments;
        this.dateVente = dateVente;
        this.modePaiement = modePaiement;
        this.ordonnanceAjoutee = ordonnanceAjoutee;
    }
    
    // Getters et Setters
    public UUID getIdVente() {
        return idVente;
    }
    
    public void setIdVente(UUID idVente) {
        this.idVente = idVente;
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
    
    public boolean isOrdonnanceAjoutee() {
        return ordonnanceAjoutee;
    }
    
    public void setOrdonnanceAjoutee(boolean ordonnanceAjoutee) {
        this.ordonnanceAjoutee = ordonnanceAjoutee;
    }
}
