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
    
    /**
     * Constructeur par défaut.
     */
    public VenteCreateRequest() {
    }
    
    /**
     * Constructeur avec tous les paramètres.
     * @param pharmacienAdjointId L'identifiant du pharmacien adjoint.
     * @param clientId L'identifiant du client.
     * @param medicaments La liste des médicaments dans le panier.
     * @param dateVente La date de la vente.
     * @param modePaiement Le mode de paiement.
     * @param montantTotal Le montant total de la vente.
     * @param montantRembourse Le montant remboursé.
     */
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
    /**
     * Obtient l'identifiant du pharmacien adjoint.
     * @return L'identifiant du pharmacien adjoint.
     */
    public UUID getPharmacienAdjointId() {
        return pharmacienAdjointId;
    }
    /**
     * Définit l'identifiant du pharmacien adjoint.
     * @param pharmacienAdjointId Le nouvel identifiant du pharmacien adjoint.
     */
    public void setPharmacienAdjointId(UUID pharmacienAdjointId) {
        this.pharmacienAdjointId = pharmacienAdjointId;
    }

    /**
     * Obtient l'identifiant du client.
     * @return L'identifiant du client.
     */
    public UUID getClientId() {
        return clientId;
    }
    /**
     * Définit l'identifiant du client.
     * @param clientId Le nouvel identifiant du client.
     */
    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }
    
    /**
     * Obtient la liste des médicaments du panier.
     * @return La liste des médicaments.
     */
    public List<MedicamentPanier> getMedicaments() {
        return medicaments;
    }
    
    /**
     * Définit la liste des médicaments du panier.
     * @param medicaments La nouvelle liste des médicaments.
     */
    public void setMedicaments(List<MedicamentPanier> medicaments) {
        this.medicaments = medicaments;
    }
    
    /**
     * Obtient la date de la vente.
     * @return La date de la vente.
     */
    public Date getDateVente() {
        return dateVente;
    }
    
    /**
     * Définit la date de la vente.
     * @param dateVente La nouvelle date de la vente.
     */
    public void setDateVente(Date dateVente) {
        this.dateVente = dateVente;
    }
    
    /**
     * Obtient le mode de paiement.
     * @return Le mode de paiement.
     */
    public String getModePaiement() {
        return modePaiement;
    }
    
    /**
     * Définit le mode de paiement.
     * @param modePaiement Le nouveau mode de paiement.
     */
    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }
    
    /**
     * Obtient le montant total de la vente.
     * @return Le montant total.
     */
    public double getMontantTotal() {
        return montantTotal;
    }
    
    /**
     * Définit le montant total de la vente.
     * @param montantTotal Le nouveau montant total.
     */
    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }
    
    /**
     * Obtient le montant remboursé.
     * @return Le montant remboursé.
     */
    public double getMontantRembourse() {
        return montantRembourse;
    }
    
    /**
     * Définit le montant remboursé.
     * @param montantRembourse Le nouveau montant remboursé.
     */
    public void setMontantRembourse(double montantRembourse) {
        this.montantRembourse = montantRembourse;
    }
    /**
     * Vérifie si une ordonnance a été ajoutée.
     * @return true si une ordonnance a été ajoutée, false sinon.
     */
    public boolean isOrdonnanceAjoutee() {
        return ordonnanceAjoutee;
    }

    /**
     * Définit si une ordonnance a été ajoutée.
     * @param ordonnanceAjoutee true si une ordonnance a été ajoutée, false sinon.
     */
    public void setOrdonnanceAjoutee(boolean ordonnanceAjoutee) {
        this.ordonnanceAjoutee = ordonnanceAjoutee;
    }
}
