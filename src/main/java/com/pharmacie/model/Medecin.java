package com.pharmacie.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Représente un médecin avec ses informations professionnelles.
 * Utilise des propriétés JavaFX pour la liaison de données.
 */
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

    /**
     * Constructeur par défaut. Initialise toutes les propriétés StringProperty.
     */
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

    /**
     * Constructeur avec paramètres pour initialiser un médecin.
     * @param civilite La civilité du médecin.
     * @param nomExercice Le nom d'exercice du médecin.
     * @param prenomExercice Le prénom d'exercice du médecin.
     * @param rppsMedecin Le numéro RPPS du médecin.
     * @param profession La profession du médecin.
     * @param modeExercice Le mode d'exercice.
     * @param qualifications Les qualifications.
     * @param structureExercice La structure d'exercice.
     * @param fonctionActivite La fonction/activité.
     * @param genreActivite Le genre d'activité.
     */
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

    /**
     * Définit la catégorie professionnelle du médecin.
     * @param categorieProfessionnelle La nouvelle catégorie professionnelle.
     */
    public void setCategorieProfessionnelle(String categorieProfessionnelle) {
        this.categorieProfessionnelle.set(categorieProfessionnelle);
    }

    /**
     * Obtient la catégorie professionnelle du médecin.
     * @return La catégorie professionnelle.
     */
    public String getCategorieProfessionnelle() {
        return categorieProfessionnelle.get();
    }

    // Getters pour les propriétés
    /**
     * Retourne la propriété JavaFX pour la civilité.
     * @return La propriété civilité.
     */
    public StringProperty civiliteProperty() {
        return civilite;
    }

    /**
     * Retourne la propriété JavaFX pour le nom d'exercice.
     * @return La propriété nom d'exercice.
     */
    public StringProperty nomExerciceProperty() {
        return nomExercice;
    }

    /**
     * Retourne la propriété JavaFX pour le prénom d'exercice.
     * @return La propriété prénom d'exercice.
     */
    public StringProperty prenomExerciceProperty() {
        return prenomExercice;
    }

    /**
     * Retourne la propriété JavaFX pour le numéro RPPS du médecin.
     * @return La propriété RPPS.
     */
    public StringProperty rppsMedecinProperty() {
        return rppsMedecin;
    }

    /**
     * Retourne la propriété JavaFX pour la profession.
     * @return La propriété profession.
     */
    public StringProperty professionProperty() {
        return profession;
    }

    /**
     * Retourne la propriété JavaFX pour le mode d'exercice.
     * @return La propriété mode d'exercice.
     */
    public StringProperty modeExerciceProperty() {
        return modeExercice;
    }

    /**
     * Retourne la propriété JavaFX pour les qualifications.
     * @return La propriété qualifications.
     */
    public StringProperty qualificationsProperty() {
        return qualifications;
    }

    /**
     * Retourne la propriété JavaFX pour la structure d'exercice.
     * @return La propriété structure d'exercice.
     */
    public StringProperty structureExerciceProperty() {
        return structureExercice;
    }

    /**
     * Retourne la propriété JavaFX pour la fonction/activité.
     * @return La propriété fonction/activité.
     */
    public StringProperty fonctionActiviteProperty() {
        return fonctionActivite;
    }

    /**
     * Retourne la propriété JavaFX pour le genre d'activité.
     * @return La propriété genre d'activité.
     */
    public StringProperty genreActiviteProperty() {
        return genreActivite;
    }

    // Setters
    /**
     * Définit la civilité du médecin.
     * @param civilite La nouvelle civilité.
     */
    public void setCivilite(String civilite) {
        this.civilite.set(civilite);
    }

    /**
     * Définit le nom d'exercice du médecin.
     * @param nomExercice Le nouveau nom d'exercice.
     */
    public void setNomExercice(String nomExercice) {
        this.nomExercice.set(nomExercice);
    }

    /**
     * Définit le prénom d'exercice du médecin.
     * @param prenomExercice Le nouveau prénom d'exercice.
     */
    public void setPrenomExercice(String prenomExercice) {
        this.prenomExercice.set(prenomExercice);
    }

    /**
     * Définit le numéro RPPS du médecin.
     * @param rppsMedecin Le nouveau numéro RPPS.
     */
    public void setRppsMedecin(String rppsMedecin) {
        this.rppsMedecin.set(rppsMedecin);
    }

    /**
     * Définit la profession du médecin.
     * @param profession La nouvelle profession.
     */
    public void setProfession(String profession) {
        this.profession.set(profession);
    }

    /**
     * Définit le mode d'exercice du médecin.
     * @param modeExercice Le nouveau mode d'exercice.
     */
    public void setModeExercice(String modeExercice) {
        this.modeExercice.set(modeExercice);
    }

    /**
     * Définit les qualifications du médecin.
     * @param qualifications Les nouvelles qualifications.
     */
    public void setQualifications(String qualifications) {
        this.qualifications.set(qualifications);
    }

    /**
     * Définit la structure d'exercice du médecin.
     * @param structureExercice La nouvelle structure d'exercice.
     */
    public void setStructureExercice(String structureExercice) {
        this.structureExercice.set(structureExercice);
    }

    /**
     * Définit la fonction/activité du médecin.
     * @param fonctionActivite La nouvelle fonction/activité.
     */
    public void setFonctionActivite(String fonctionActivite) {
        this.fonctionActivite.set(fonctionActivite);
    }

    /**
     * Définit le genre d'activité du médecin.
     * @param genreActivite Le nouveau genre d'activité.
     */
    public void setGenreActivite(String genreActivite) {
        this.genreActivite.set(genreActivite);
    }

    /** 
     * Retourne la valeur du RPPS du médecin.
     * @return Le numéro RPPS.
     */
    public String getRppsMedecin() {
        return this.rppsMedecin.get();
    }
}