package com.pharmacie.model.dto;

/**
 * DTO pour la création d'un médecin.
 * Contient les informations nécessaires pour enregistrer un nouveau médecin.
 */
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

    /**
     * Constructeur par défaut.
     */
    public MedecinCreateRequest() {
    }

    /**
     * Constructeur avec tous les champs.
     *
     * @param civilite Civilité du médecin.
     * @param nomExercice Nom d'exercice du médecin.
     * @param prenomExercice Prénom d'exercice du médecin.
     * @param rppsMedecin Numéro RPPS du médecin.
     * @param categorieProfessionnelle Catégorie professionnelle du médecin.
     * @param profession Profession du médecin.
     * @param modeExercice Mode d'exercice du médecin.
     * @param qualifications Qualifications du médecin.
     * @param structureExercice Structure d'exercice du médecin.
     * @param fonctionActivite Fonction/activité du médecin.
     * @param genreActivite Genre d'activité du médecin.
     */
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
    /**
     * Obtient la civilité du médecin.
     * @return La civilité.
     */
    public String getCivilite() {
        return civilite;
    }
    /**
     * Obtient le nom d'exercice du médecin.
     * @return Le nom d'exercice.
     */
    public String getNomExercice() {
        return nomExercice;
    }
    /**
     * Obtient le prénom d'exercice du médecin.
     * @return Le prénom d'exercice.
     */
    public String getPrenomExercice() {
        return prenomExercice;
    }
    /**
     * Obtient le numéro RPPS du médecin.
     * @return Le numéro RPPS.
     */
    public String getRppsMedecin() {
        return rppsMedecin;
    }
    /**
     * Obtient la catégorie professionnelle du médecin.
     * @return La catégorie professionnelle.
     */
    public String getCategorieProfessionnelle() {
        return categorieProfessionnelle;
    }
    /**
     * Obtient la profession du médecin.
     * @return La profession.
     */
    public String getProfession() {
        return profession;
    }
    /**
     * Obtient le mode d'exercice du médecin.
     * @return Le mode d'exercice.
     */
    public String getModeExercice() {
        return modeExercice;
    }
    /**
     * Obtient les qualifications du médecin.
     * @return Les qualifications.
     */
    public String getQualifications() {
        return qualifications;
    }
    /**
     * Obtient la structure d'exercice du médecin.
     * @return La structure d'exercice.
     */
    public String getStructureExercice() {
        return structureExercice;
    }
    /**
     * Obtient la fonction/activité du médecin.
     * @return La fonction/activité.
     */
    public String getFonctionActivite() {
        return fonctionActivite;
    }
    /**
     * Obtient le genre d'activité du médecin.
     * @return Le genre d'activité.
     */
    public String getGenreActivite() {
        return genreActivite;
    }

    // --- Setters ---
    /**
     * Définit la civilité du médecin.
     * @param civilite La nouvelle civilité.
     */
    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }
    /**
     * Définit le nom d'exercice du médecin.
     * @param nomExercice Le nouveau nom d'exercice.
     */
    public void setNomExercice(String nomExercice) {
        this.nomExercice = nomExercice;
    }
    /**
     * Définit le prénom d'exercice du médecin.
     * @param prenomExercice Le nouveau prénom d'exercice.
     */
    public void setPrenomExercice(String prenomExercice) {
        this.prenomExercice = prenomExercice;
    }
    /**
     * Définit le numéro RPPS du médecin.
     * @param rppsMedecin Le nouveau numéro RPPS.
     */
    public void setRppsMedecin(String rppsMedecin) {
        this.rppsMedecin = rppsMedecin;
    }
    /**
     * Définit la catégorie professionnelle du médecin.
     * @param categorieProfessionnelle La nouvelle catégorie professionnelle.
     */
    public void setCategorieProfessionnelle(String categorieProfessionnelle) {
        this.categorieProfessionnelle = categorieProfessionnelle;
    }
    /**
     * Définit la profession du médecin.
     * @param profession La nouvelle profession.
     */
    public void setProfession(String profession) {
        this.profession = profession;
    }
    /**
     * Définit le mode d'exercice du médecin.
     * @param modeExercice Le nouveau mode d'exercice.
     */
    public void setModeExercice(String modeExercice) {
        this.modeExercice = modeExercice;
    }
    /**
     * Définit les qualifications du médecin.
     * @param qualifications Les nouvelles qualifications.
     */
    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }
    /**
     * Définit la structure d'exercice du médecin.
     * @param structureExercice La nouvelle structure d'exercice.
     */
    public void setStructureExercice(String structureExercice) {
        this.structureExercice = structureExercice;
    }
    /**
     * Définit la fonction/activité du médecin.
     * @param fonctionActivite La nouvelle fonction/activité.
     */
    public void setFonctionActivite(String fonctionActivite) {
        this.fonctionActivite = fonctionActivite;
    }
    /**
     * Définit le genre d'activité du médecin.
     * @param genreActivite Le nouveau genre d'activité.
     */
    public void setGenreActivite(String genreActivite) {
        this.genreActivite = genreActivite;
    }

    /**
     * Retourne une représentation textuelle de l'objet MedecinCreateRequest.
     * @return Une chaîne de caractères représentant l'objet.
     */
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