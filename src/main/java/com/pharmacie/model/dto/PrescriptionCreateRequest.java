package com.pharmacie.model.dto;

/**
 * DTO (Data Transfer Object) pour la création d'une prescription.
 * Contient les informations nécessaires pour enregistrer une nouvelle prescription.
 */
public class PrescriptionCreateRequest {

    private String medicament;
    private int quantitePrescrite;
    private int duree;
    private String posologie;

    /**
     * Constructeur par défaut.
     */
    public PrescriptionCreateRequest() {
    }

    /**
     * Constructeur avec tous les champs pour initialiser une demande de création de prescription.
     * @param medicament Le nom ou l'identifiant du médicament prescrit.
     * @param quantitePrescrite La quantité de médicament prescrite.
     * @param duree La durée du traitement en jours.
     * @param posologie Les instructions de posologie.
     */
    public PrescriptionCreateRequest(
            String medicament,
            int quantitePrescrite,
            int duree,
            String posologie
    ) {
        this.medicament = medicament;
        this.quantitePrescrite = quantitePrescrite;
        this.duree = duree;
        this.posologie = posologie;
    }

    // --- Getters ---
    /**
     * Obtient le nom ou l'identifiant du médicament.
     * @return Le médicament.
     */
    public String getMedicament() {
        return medicament;
    }

    /**
     * Obtient la quantité prescrite du médicament.
     * @return La quantité prescrite.
     */
    public int getQuantitePrescrite() {
        return quantitePrescrite;
    }

    /**
     * Obtient la durée du traitement.
     * @return La durée en jours.
     */
    public int getDuree() {
        return duree;
    }

    /**
     * Obtient la posologie de la prescription.
     * @return La posologie.
     */
    public String getPosologie() {
        return posologie;
    }

    // --- Setters ---
    /**
     * Définit le nom ou l'identifiant du médicament.
     * @param medicament Le nouveau médicament.
     */
    public void setMedicament(String medicament) {
        this.medicament = medicament;
    }

    /**
     * Définit la quantité prescrite du médicament.
     * @param quantitePrescrite La nouvelle quantité prescrite.
     */
    public void setQuantitePrescrite(int quantitePrescrite) {
        this.quantitePrescrite = quantitePrescrite;
    }

    /**
     * Définit la durée du traitement.
     * @param duree La nouvelle durée en jours.
     */
    public void setDuree(int duree) {
        this.duree = duree;
    }

    /**
     * Définit la posologie de la prescription.
     * @param posologie La nouvelle posologie.
     */
    public void setPosologie(String posologie) {
        this.posologie = posologie;
    }
}

