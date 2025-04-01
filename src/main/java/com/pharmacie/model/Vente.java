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

    // Constructeur par défaut
    public Vente() {
    }

    // Constructeur avec tous les paramètres
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

    // Getters et Setters
    public UUID getIdVente() {
        return idVente;
    }

    public void setIdVente(UUID idVente) {
        this.idVente = idVente;
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

    public List<Medicament> getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(List<Medicament> medicaments) {
        this.medicaments = medicaments;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

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
