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
    private String medicament;        // Médicament associé à la prescription
    // L'ordonnance associée (facultatif ici)

    /**
     * Constructeur par défaut.
     */
    public Prescription() {}

    /**
     * Constructeur avec tous les paramètres.
     * @param posologie La posologie indiquée pour le médicament prescrit.
     * @param quantitePrescrite La quantité prescrite du médicament.
     * @param duree La durée du traitement en jours.
     * @param medicament Le nom ou l'identifiant du médicament associé à la prescription.
     */
    public Prescription(String posologie, int quantitePrescrite, int duree, String medicament) {
        this.posologie = posologie;
        this.quantitePrescrite = quantitePrescrite;
        this.duree = duree;
        this.medicament = medicament;
    }

    /**
     * Obtient l'identifiant unique de la prescription.
     * @return L'identifiant de la prescription.
     */
    public UUID getIdPrescription() {
        return idPrescription;
    }

    /**
     * Définit l'identifiant unique de la prescription.
     * @param idPrescription Le nouvel identifiant de la prescription.
     */
    public void setIdPrescription(UUID idPrescription) {
        this.idPrescription = idPrescription;
    }

    /**
     * Obtient la posologie du médicament prescrit.
     * @return La posologie.
     */
    public String getPosologie() {
        return posologie;
    }

    /**
     * Définit la posologie du médicament prescrit.
     * @param posologie La nouvelle posologie.
     */
    public void setPosologie(String posologie) {
        this.posologie = posologie;
    }

    /**
     * Obtient la quantité prescrite du médicament.
     * @return La quantité prescrite.
     */
    public int getQuantitePrescrite() {
        return quantitePrescrite;
    }

    /**
     * Définit la quantité prescrite du médicament.
     * @param quantitePrescrite La nouvelle quantité prescrite.
     */
    public void setQuantitePrescrite(int quantitePrescrite) {
        this.quantitePrescrite = quantitePrescrite;
    }

    /**
     * Obtient la durée du traitement en jours.
     * @return La durée du traitement.
     */
    public int getDuree() {
        return duree;
    }

    /**
     * Définit la durée du traitement en jours.
     * @param duree La nouvelle durée du traitement.
     */
    public void setDuree(int duree) {
        this.duree = duree;
    }

    /**
     * Obtient le nom ou l'identifiant du médicament associé à la prescription.
     * @return Le médicament.
     */
    public String getMedicament() {
        return medicament;
    }

    /**
     * Définit le nom ou l'identifiant du médicament associé à la prescription.
     * @param medicament Le nouveau médicament.
     */
    public void setMedicament(String medicament) {
        this.medicament = medicament;
    }

    /**
     * Retourne une représentation textuelle de l'objet Prescription.
     * @return Une chaîne de caractères représentant la prescription.
     */
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