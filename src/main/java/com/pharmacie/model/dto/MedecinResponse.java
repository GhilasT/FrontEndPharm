package com.pharmacie.model.dto;

import java.util.UUID;

public class MedecinResponse {

    private UUID idMedecin;                 // Identifiant du médecin
    private String civilite;                // Civilité (M. / Mme, etc.)
    private String nomExercice;             // Nom d'exercice
    private String prenomExercice;          // Prénom d'exercice
    private String rppsMedecin;             // Numéro RPPS unique
    private String categorieProfessionnelle;// Civil, militaire, étudiant, etc.
    private String profession;              // Profession (Médecin, Chirurgien, etc.)
    private String modeExercice;            // Mode d'exercice (libéral, salarié, bénévole)
    private String qualifications;          // Qualifications, diplômes, autorisations, savoir-faire
    private String structureExercice;       // Coordonnées des structures d'exercice (cabinet, hôpital, etc.)
    private String fonctionActivite;        // Fonction dans l'exercice médical (ex. Médecin traitant)
    private String genreActivite;           // Genre d’activité (consultation, chirurgie, soins palliatifs, etc.)

    /** Constructeur vide */
    public MedecinResponse() {
    }

    /** Constructeur avec tous les champs */
    public MedecinResponse(
            UUID idMedecin,
            String civilite,
            String nomExercice,
            String prenomExercice,
            String rppsMedecin,
            String categorieProfessionnelle,
            String profession,
            String modeExercice,
            String qualifications,
            String structureExercice,
            String fonctionActivite,
            String genreActivite
    ) {
        this.idMedecin                = idMedecin;
        this.civilite                 = civilite;
        this.nomExercice              = nomExercice;
        this.prenomExercice           = prenomExercice;
        this.rppsMedecin              = rppsMedecin;
        this.categorieProfessionnelle = categorieProfessionnelle;
        this.profession               = profession;
        this.modeExercice             = modeExercice;
        this.qualifications           = qualifications;
        this.structureExercice        = structureExercice;
        this.fonctionActivite         = fonctionActivite;
        this.genreActivite            = genreActivite;
    }

    // --- Getters & Setters ---

    public UUID getIdMedecin() {
        return idMedecin;
    }

    public void setIdMedecin(UUID idMedecin) {
        this.idMedecin = idMedecin;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getNomExercice() {
        return nomExercice;
    }

    public void setNomExercice(String nomExercice) {
        this.nomExercice = nomExercice;
    }

    public String getPrenomExercice() {
        return prenomExercice;
    }

    public void setPrenomExercice(String prenomExercice) {
        this.prenomExercice = prenomExercice;
    }

    public String getRppsMedecin() {
        return rppsMedecin;
    }

    public void setRppsMedecin(String rppsMedecin) {
        this.rppsMedecin = rppsMedecin;
    }

    public String getCategorieProfessionnelle() {
        return categorieProfessionnelle;
    }

    public void setCategorieProfessionnelle(String categorieProfessionnelle) {
        this.categorieProfessionnelle = categorieProfessionnelle;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getModeExercice() {
        return modeExercice;
    }

    public void setModeExercice(String modeExercice) {
        this.modeExercice = modeExercice;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getStructureExercice() {
        return structureExercice;
    }

    public void setStructureExercice(String structureExercice) {
        this.structureExercice = structureExercice;
    }

    public String getFonctionActivite() {
        return fonctionActivite;
    }

    public void setFonctionActivite(String fonctionActivite) {
        this.fonctionActivite = fonctionActivite;
    }

    public String getGenreActivite() {
        return genreActivite;
    }

    public void setGenreActivite(String genreActivite) {
        this.genreActivite = genreActivite;
    }

    @Override
    public String toString() {
        return "MedecinResponse{" +
                "idMedecin=" + idMedecin +
                ", civilite='" + civilite + '\'' +
                ", nomExercice='" + nomExercice + '\'' +
                ", prenomExercice='" + prenomExercice + '\'' +
                ", rppsMedecin='" + rppsMedecin + '\'' +
                ", categorieProfessionnelle='" + categorieProfessionnelle + '\'' +
                ", profession='" + profession + '\'' +
                ", modeExercice='" + modeExercice + '\'' +
                ", qualifications='" + qualifications + '\'' +
                ", structureExercice='" + structureExercice + '\'' +
                ", fonctionActivite='" + fonctionActivite + '\'' +
                ", genreActivite='" + genreActivite + '\'' +
                '}';
    }
}