package com.pharmacie.model;

import java.util.UUID;

/**
 * Classe représentant une prescription médicale dans le frontend.
 * Contient les informations liées à la prescription d'un médicament à un patient.
 */
public class Prescription {

    private UUID idPrescription;      // Identifiant unique de la prescription
    private String posologie;         // Posologie indiquée pour le médicament prescrit
    private int quantitePrescrite;    // Quantité prescrite du médicament
    private int duree;                // Durée du traitement en jours
    private String medicament;  // Médicament associé à la prescription
    // L'ordonnance associée (facultatif ici)

    // Constructeur par défaut
    public Prescription() {}

    // Constructeur avec tous les paramètres
    public Prescription(String posologie, int quantitePrescrite, int duree, String medicament) {
        this.posologie = posologie;
        this.quantitePrescrite = quantitePrescrite;
        this.duree = duree;
        this.medicament = medicament;
    }

    // Getters et Setters
    public UUID getIdPrescription() {
        return idPrescription;
    }

    public void setIdPrescription(UUID idPrescription) {
        this.idPrescription = idPrescription;
    }

    public String getPosologie() {
        return posologie;
    }

    public void setPosologie(String posologie) {
        this.posologie = posologie;
    }

    public int getQuantitePrescrite() {
        return quantitePrescrite;
    }

    public void setQuantitePrescrite(int quantitePrescrite) {
        this.quantitePrescrite = quantitePrescrite;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getMedicament() {
        return medicament;
    }

    public void setMedicament(String medicament) {
        this.medicament = medicament;
    }



    @Override
    public String toString() {
        return "Prescription{" +
                "idPrescription=" + idPrescription +
                ", posologie='" + posologie + '\'' +
                ", quantitePrescrite=" + quantitePrescrite +
                ", duree=" + duree +
                ", medicament=" + medicament +
                '}';
    }
}