package com.pharmacie.model.dto;

public class MedecinCreateRequest {

    private String civilite;
    private String nomExercice;
    private String prenomExercice;
    private String rppsMedecin;
    private String categorieProfessionnelle;
    private String profession;
    private String modeExercice;
    private String qualifications;
    private String structureExercice;
    private String fonctionActivite;
    private String genreActivite;

    /** Constructeur vide */
    public MedecinCreateRequest() {
    }

    /** Constructeur avec tous les champs */
    public MedecinCreateRequest(
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

    // --- Getters ---
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

    // --- Setters ---
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

    @Override
    public String toString() {
        return "MedecinCreateRequest{" +
                "civilite='" + civilite + '\'' +
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