package com.pharmacie.model.dto;

public class PrescriptionCreateRequest {

    private String medicament;
    private int quantitePrescrite;
    private int duree;
    private String posologie;

    /**
     * Constructeur vide
     */
    public PrescriptionCreateRequest() {
    }

    /**
     * Constructeur avec tous les champs
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
    public String getMedicament() {
        return medicament;
    }

    public int getQuantitePrescrite() {
        return quantitePrescrite;
    }

    public int getDuree() {
        return duree;
    }

    public String getPosologie() {
        return posologie;
    }

    // --- Setters ---
    public void setMedicament(String medicament) {
        this.medicament = medicament;
    }

    public void setQuantitePrescrite(int quantitePrescrite) {
        this.quantitePrescrite = quantitePrescrite;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public void setPosologie(String posologie) {
        this.posologie = posologie;
    }
}

