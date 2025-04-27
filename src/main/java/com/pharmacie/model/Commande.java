package com.pharmacie.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Classe représentant une commande de médicaments dans le front end.
 */
public class Commande {
    private UUID reference;
    private Date dateCommande;
    private BigDecimal montantTotal;
    private String statut;
    private UUID fournisseurId;
    private UUID pharmacienAdjointId;
    private List<LigneCommande> ligneCommandes;

    public Commande() {  
    }


    public Commande(UUID reference, Date dateCommande, BigDecimal montantTotal, String statut, 
                   UUID fournisseurId, UUID pharmacienAdjointId, List<LigneCommande> ligneCommandes) {
        this.reference = reference;
        this.dateCommande = dateCommande;
        this.montantTotal = montantTotal;
        this.statut = statut;
        this.fournisseurId = fournisseurId;
        this.pharmacienAdjointId = pharmacienAdjointId;
        this.ligneCommandes = ligneCommandes;
    }

    // Getters et Setters
    public UUID getReference() {
        return reference;
    }

    public void setReference(UUID reference) {
        this.reference = reference;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public UUID getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(UUID fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public UUID getPharmacienAdjointId() {
        return pharmacienAdjointId;
    }

    public void setPharmacienAdjointId(UUID pharmacienAdjointId) {
        this.pharmacienAdjointId = pharmacienAdjointId;
    }

    public List<LigneCommande> getLigneCommandes() {
        return ligneCommandes;
    }

    public void setLigneCommandes(List<LigneCommande> ligneCommandes) {
        this.ligneCommandes = ligneCommandes;
    }
}
