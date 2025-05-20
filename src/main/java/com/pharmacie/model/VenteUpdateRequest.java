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

    /**
     * Constructeur par défaut.
     */
    public VenteUpdateRequest() {
    }

    /**
     * Constructeur avec tous les paramètres.
     * @param idVente L'identifiant de la vente à mettre à jour.
     * @param medicaments La liste des médicaments concernés par la mise à jour.
     * @param dateVente La nouvelle date de la vente.
     * @param modePaiement Le nouveau mode de paiement.
     * @param ordonnanceAjoutee Indique si une ordonnance a été ajoutée.
     */
    public VenteUpdateRequest(UUID idVente, List<MedicamentPanier> medicaments,
                             Date dateVente, String modePaiement, boolean ordonnanceAjoutee) {
        this.idVente = idVente;
        this.medicaments = medicaments;
        this.dateVente = dateVente;
        this.modePaiement = modePaiement;
        this.ordonnanceAjoutee = ordonnanceAjoutee;
    }
    
    // Getters et Setters
    /**
     * Obtient l'identifiant de la vente.
     * @return L'identifiant de la vente.
     */
    public UUID getIdVente() {
        return idVente;
    }
    
    /**
     * Définit l'identifiant de la vente.
     * @param idVente Le nouvel identifiant de la vente.
     */
    public void setIdVente(UUID idVente) {
        this.idVente = idVente;
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
