package com.pharmacie.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Classe représentant une commande de médicaments dans l'interface utilisateur.
 * Contient les informations générales de la commande et la liste des lignes de commande associées.
 */
public class Commande {
    private UUID reference;
    private Date dateCommande;
    private BigDecimal montantTotal;
    private String statut;
    private UUID fournisseurId;
    private UUID pharmacienAdjointId;
    private List<LigneCommande> ligneCommandes;

    /**
     * Constructeur par défaut.
     */
    public Commande() {  
    }

    /**
     * Constructeur avec tous les paramètres pour initialiser une commande.
     * @param reference La référence unique de la commande.
     * @param dateCommande La date de la commande.
     * @param montantTotal Le montant total de la commande.
     * @param statut Le statut actuel de la commande.
     * @param fournisseurId L'identifiant du fournisseur.
     * @param pharmacienAdjointId L'identifiant du pharmacien adjoint ayant passé la commande.
     * @param ligneCommandes La liste des lignes de commande.
     */
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
    /**
     * Obtient la référence de la commande.
     * @return La référence.
     */
    public UUID getReference() {
        return reference;
    }

    /**
     * Définit la référence de la commande.
     * @param reference La nouvelle référence.
     */
    public void setReference(UUID reference) {
        this.reference = reference;
    }

    /**
     * Obtient la date de la commande.
     * @return La date de commande.
     */
    public Date getDateCommande() {
        return dateCommande;
    }

    /**
     * Définit la date de la commande.
     * @param dateCommande La nouvelle date de commande.
     */
    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    /**
     * Obtient le montant total de la commande.
     * @return Le montant total.
     */
    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    /**
     * Définit le montant total de la commande.
     * @param montantTotal Le nouveau montant total.
     */
    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    /**
     * Obtient le statut de la commande.
     * @return Le statut.
     */
    public String getStatut() {
        return statut;
    }

    /**
     * Définit le statut de la commande.
     * @param statut Le nouveau statut.
     */
    public void setStatut(String statut) {
        this.statut = statut;
    }

    /**
     * Obtient l'identifiant du fournisseur associé à la commande.
     * @return L'identifiant du fournisseur.
     */
    public UUID getFournisseurId() {
        return fournisseurId;
    }

    /**
     * Définit l'identifiant du fournisseur associé à la commande.
     * @param fournisseurId Le nouvel identifiant du fournisseur.
     */
    public void setFournisseurId(UUID fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    /**
     * Obtient l'identifiant du pharmacien adjoint ayant passé la commande.
     * @return L'identifiant du pharmacien adjoint.
     */
    public UUID getPharmacienAdjointId() {
        return pharmacienAdjointId;
    }

    /**
     * Définit l'identifiant du pharmacien adjoint ayant passé la commande.
     * @param pharmacienAdjointId Le nouvel identifiant du pharmacien adjoint.
     */
    public void setPharmacienAdjointId(UUID pharmacienAdjointId) {
        this.pharmacienAdjointId = pharmacienAdjointId;
    }

    /**
     * Obtient la liste des lignes de commande associées à cette commande.
     * @return La liste des lignes de commande.
     */
    public List<LigneCommande> getLigneCommandes() {
        return ligneCommandes;
    }

    /**
     * Définit la liste des lignes de commande associées à cette commande.
     * @param ligneCommandes La nouvelle liste des lignes de commande.
     */
    public void setLigneCommandes(List<LigneCommande> ligneCommandes) {
        this.ligneCommandes = ligneCommandes;
    }
}
