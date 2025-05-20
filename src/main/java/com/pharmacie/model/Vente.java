package com.pharmacie.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Modèle représentant une vente dans le frontend.
 */
public class Vente {
    private UUID idVente;
    private Date dateVente;
    private String modePaiement;
    private double montantTotal;
    private double montantRembourse;
    private UUID pharmacienAdjointId;
    private UUID clientId;
    private List<Medicament> medicaments;
    private String notification;

    /**
     * Constructeur par défaut.
     */
    public Vente() {
    }

    /**
     * Constructeur avec tous les paramètres.
     * @param idVente L'identifiant unique de la vente.
     * @param dateVente La date à laquelle la vente a été effectuée.
     * @param modePaiement Le mode de paiement utilisé pour la vente.
     * @param montantTotal Le montant total de la vente.
     * @param montantRembourse Le montant remboursé (par exemple, par une assurance).
     * @param pharmacienAdjointId L'identifiant du pharmacien adjoint ayant effectué la vente.
     * @param clientId L'identifiant du client.
     * @param medicaments La liste des médicaments vendus.
     * @param notification Une notification associée à la vente (par exemple, des instructions spéciales).
     */
    public Vente(UUID idVente, Date dateVente, String modePaiement, double montantTotal, 
                double montantRembourse, UUID pharmacienAdjointId, UUID clientId, 
                List<Medicament> medicaments, String notification) {
        this.idVente = idVente;
        this.dateVente = dateVente;
        this.modePaiement = modePaiement;
        this.montantTotal = montantTotal;
        this.montantRembourse = montantRembourse;
        this.pharmacienAdjointId = pharmacienAdjointId;
        this.clientId = clientId;
        this.medicaments = medicaments;
        this.notification = notification;
    }

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
     * Obtient la liste des médicaments vendus.
     * @return La liste des médicaments.
     */
    public List<Medicament> getMedicaments() {
        return medicaments;
    }

    /**
     * Définit la liste des médicaments vendus.
     * @param medicaments La nouvelle liste des médicaments.
     */
    public void setMedicaments(List<Medicament> medicaments) {
        this.medicaments = medicaments;
    }

    /**
     * Obtient la notification associée à la vente.
     * @return La notification.
     */
    public String getNotification() {
        return notification;
    }

    /**
     * Définit la notification associée à la vente.
     * @param notification La nouvelle notification.
     */
    public void setNotification(String notification) {
        this.notification = notification;
    }

    /**
     * Retourne une représentation textuelle de l'objet Vente.
     * @return Une chaîne de caractères représentant la vente.
     */
    @Override
    public String toString() {
        return "Vente{" +
                "idVente=" + idVente +
                ", dateVente=" + dateVente +
                ", modePaiement='" + modePaiement + '\'' +
                ", montantTotal=" + montantTotal +
                ", montantRembourse=" + montantRembourse +
                ", pharmacienAdjointId=" + pharmacienAdjointId +
                ", clientId=" + clientId +
                ", medicaments=" + medicaments +
                ", notification='" + notification + '\'' +
                '}';
    }
}
