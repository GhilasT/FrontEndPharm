package com.pharmacie.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Medecin {
    private StringProperty civilite;
    private StringProperty nomExercice;
    private StringProperty prenomExercice;
    private StringProperty rppsMedecin;
    private StringProperty profession;
    private StringProperty modeExercice;
    private StringProperty qualifications;
    private StringProperty structureExercice;
    private StringProperty fonctionActivite;
    private StringProperty genreActivite;
    private StringProperty categorieProfessionnelle;

    // Constructeur sans arguments
    public Medecin() {
        this.civilite = new SimpleStringProperty();
        this.nomExercice = new SimpleStringProperty();
        this.prenomExercice = new SimpleStringProperty();
        this.rppsMedecin = new SimpleStringProperty();
        this.profession = new SimpleStringProperty();
        this.modeExercice = new SimpleStringProperty();
        this.qualifications = new SimpleStringProperty();
        this.structureExercice = new SimpleStringProperty();
        this.fonctionActivite = new SimpleStringProperty();
        this.genreActivite = new SimpleStringProperty();
        this.categorieProfessionnelle = new SimpleStringProperty();
    }

    // Constructeur avec paramètres
    public Medecin(String civilite, String nomExercice, String prenomExercice, String rppsMedecin,
                   String profession, String modeExercice, String qualifications,
                   String structureExercice, String fonctionActivite, String genreActivite) {
        this();
        this.civilite.set(civilite);
        this.nomExercice.set(nomExercice);
        this.prenomExercice.set(prenomExercice);
        this.rppsMedecin.set(rppsMedecin);
        this.profession.set(profession);
        this.modeExercice.set(modeExercice);
        this.qualifications.set(qualifications);
        this.structureExercice.set(structureExercice);
        this.fonctionActivite.set(fonctionActivite);
        this.genreActivite.set(genreActivite);
    }

    // Méthode setter
    public void setCategorieProfessionnelle(String categorieProfessionnelle) {
        this.categorieProfessionnelle.set(categorieProfessionnelle);
    }

    public String getCategorieProfessionnelle() {
        return categorieProfessionnelle.get();
    }

    // Getters pour les propriétés
    public StringProperty civiliteProperty() {
        return civilite;
    }

    public StringProperty nomExerciceProperty() {
        return nomExercice;
    }

    public StringProperty prenomExerciceProperty() {
        return prenomExercice;
    }

    public StringProperty rppsMedecinProperty() {
        return rppsMedecin;
    }

    public StringProperty professionProperty() {
        return profession;
    }

    public StringProperty modeExerciceProperty() {
        return modeExercice;
    }

    public StringProperty qualificationsProperty() {
        return qualifications;
    }

    public StringProperty structureExerciceProperty() {
        return structureExercice;
    }

    public StringProperty fonctionActiviteProperty() {
        return fonctionActivite;
    }

    public StringProperty genreActiviteProperty() {
        return genreActivite;
    }

    // Setters
    public void setCivilite(String civilite) {
        this.civilite.set(civilite);
    }

    public void setNomExercice(String nomExercice) {
        this.nomExercice.set(nomExercice);
    }

    public void setPrenomExercice(String prenomExercice) {
        this.prenomExercice.set(prenomExercice);
    }

    public void setRppsMedecin(String rppsMedecin) {
        this.rppsMedecin.set(rppsMedecin);
    }

    public void setProfession(String profession) {
        this.profession.set(profession);
    }

    public void setModeExercice(String modeExercice) {
        this.modeExercice.set(modeExercice);
    }

    public void setQualifications(String qualifications) {
        this.qualifications.set(qualifications);
    }

    public void setStructureExercice(String structureExercice) {
        this.structureExercice.set(structureExercice);
    }

    public void setFonctionActivite(String fonctionActivite) {
        this.fonctionActivite.set(fonctionActivite);
    }

    public void setGenreActivite(String genreActivite) {
        this.genreActivite.set(genreActivite);
    }

    /** Retourne la valeur du RPPS */
    public String getRppsMedecin() {
        return this.rppsMedecin.get();
    }
}