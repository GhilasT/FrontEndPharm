package com.pharmacie.model.dto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.IOException;


@Builder  // Génère un constructeur avec des paramètres pour initialiser les champs
@NoArgsConstructor  // Génère un constructeur sans argument
@AllArgsConstructor  // Génère un constructeur avec tous les arguments
public class MedecinCreateRequest {

    private String civilite;  // Civilité (M. / Mme, etc.)
    private String nomExercice;  // Nom d'exercice
    private String prenomExercice;  // Prénom d'exercice
    private String rppsMedecin;  // Numéro RPPS unique
    private String categorieProfessionnelle;  // Civil, militaire, étudiant, etc.
    private String profession;  // Profession (Médecin, Chirurgien, etc.)
    private String modeExercice;  // Mode d'exercice (libéral, salarié, bénévole)
    private String qualifications;  // Qualifications, diplômes, autorisations, savoir-faire
    private String structureExercice;  // Coordonnées des structures d'exercice (cabinet, hôpital, etc.)
    private String fonctionActivite;  // Fonction dans l'exercice médical (par exemple, Médecin traitant)
    private String genreActivite;  // Genre d’activité (consultation, chirurgie, soins palliatifs, etc.)

    // Getters
    public String getCivilite() {
        return civilite;
    }

    public String getNomExercice() {
        return nomExercice;
    }

    public String getPrenomExercice() {
        return prenomExercice;
    }

    public String getRppsMedecin() {
        return rppsMedecin;
    }

    public String getCategorieProfessionnelle() {
        return categorieProfessionnelle;
    }

    public String getProfession() {
        return profession;
    }

    public String getModeExercice() {
        return modeExercice;
    }

    public String getQualifications() {
        return qualifications;
    }

    public String getStructureExercice() {
        return structureExercice;
    }

    public String getFonctionActivite() {
        return fonctionActivite;
    }

    public String getGenreActivite() {
        return genreActivite;
    }

    // Setters
    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public void setNomExercice(String nomExercice) {
        this.nomExercice = nomExercice;
    }

    public void setPrenomExercice(String prenomExercice) {
        this.prenomExercice = prenomExercice;
    }

    public void setRppsMedecin(String rppsMedecin) {
        this.rppsMedecin = rppsMedecin;
    }

    public void setCategorieProfessionnelle(String categorieProfessionnelle) {
        this.categorieProfessionnelle = categorieProfessionnelle;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setModeExercice(String modeExercice) {
        this.modeExercice = modeExercice;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public void setStructureExercice(String structureExercice) {
        this.structureExercice = structureExercice;
    }

    public void setFonctionActivite(String fonctionActivite) {
        this.fonctionActivite = fonctionActivite;
    }

    public void setGenreActivite(String genreActivite) {
        this.genreActivite = genreActivite;
    }

}