package com.pharmacie.model.dto;

import java.util.UUID;

/**
 * DTO représentant la réponse pour les informations d'un médecin.
 * Utilisé pour afficher les détails d'un médecin.
 */
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

    /**
     * Constructeur par défaut.
     */
    public MedecinResponse() {
    }

    /**
     * Constructeur avec tous les champs.
     *
     * @param idMedecin Identifiant unique du médecin.
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

    /**
     * Obtient l'identifiant du médecin.
     * @return L'identifiant du médecin.
     */
    public UUID getIdMedecin() {
        return idMedecin;
    }

    /**
     * Définit l'identifiant du médecin.
     * @param idMedecin Le nouvel identifiant du médecin.
     */
    public void setIdMedecin(UUID idMedecin) {
        this.idMedecin = idMedecin;
    }

    /**
     * Obtient la civilité du médecin.
     * @return La civilité du médecin.
     */
    public String getCivilite() {
        return civilite;
    }

    /**
     * Définit la civilité du médecin.
     * @param civilite La nouvelle civilité du médecin.
     */
    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    /**
     * Obtient le nom d'exercice du médecin.
     * @return Le nom d'exercice du médecin.
     */
    public String getNomExercice() {
        return nomExercice;
    }

    /**
     * Définit le nom d'exercice du médecin.
     * @param nomExercice Le nouveau nom d'exercice du médecin.
     */
    public void setNomExercice(String nomExercice) {
        this.nomExercice = nomExercice;
    }

    /**
     * Obtient le prénom d'exercice du médecin.
     * @return Le prénom d'exercice du médecin.
     */
    public String getPrenomExercice() {
        return prenomExercice;
    }

    /**
     * Définit le prénom d'exercice du médecin.
     * @param prenomExercice Le nouveau prénom d'exercice du médecin.
     */
    public void setPrenomExercice(String prenomExercice) {
        this.prenomExercice = prenomExercice;
    }

    /**
     * Obtient le numéro RPPS du médecin.
     * @return Le numéro RPPS du médecin.
     */
    public String getRppsMedecin() {
        return rppsMedecin;
    }

    /**
     * Définit le numéro RPPS du médecin.
     * @param rppsMedecin Le nouveau numéro RPPS du médecin.
     */
    public void setRppsMedecin(String rppsMedecin) {
        this.rppsMedecin = rppsMedecin;
    }

    /**
     * Obtient la catégorie professionnelle du médecin.
     * @return La catégorie professionnelle du médecin.
     */
    public String getCategorieProfessionnelle() {
        return categorieProfessionnelle;
    }

    /**
     * Définit la catégorie professionnelle du médecin.
     * @param categorieProfessionnelle La nouvelle catégorie professionnelle du médecin.
     */
    public void setCategorieProfessionnelle(String categorieProfessionnelle) {
        this.categorieProfessionnelle = categorieProfessionnelle;
    }

    /**
     * Obtient la profession du médecin.
     * @return La profession du médecin.
     */
    public String getProfession() {
        return profession;
    }

    /**
     * Définit la profession du médecin.
     * @param profession La nouvelle profession du médecin.
     */
    public void setProfession(String profession) {
        this.profession = profession;
    }

    /**
     * Obtient le mode d'exercice du médecin.
     * @return Le mode d'exercice du médecin.
     */
    public String getModeExercice() {
        return modeExercice;
    }

    /**
     * Définit le mode d'exercice du médecin.
     * @param modeExercice Le nouveau mode d'exercice du médecin.
     */
    public void setModeExercice(String modeExercice) {
        this.modeExercice = modeExercice;
    }

    /**
     * Obtient les qualifications du médecin.
     * @return Les qualifications du médecin.
     */
    public String getQualifications() {
        return qualifications;
    }

    /**
     * Définit les qualifications du médecin.
     * @param qualifications Les nouvelles qualifications du médecin.
     */
    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    /**
     * Obtient la structure d'exercice du médecin.
     * @return La structure d'exercice du médecin.
     */
    public String getStructureExercice() {
        return structureExercice;
    }

    /**
     * Définit la structure d'exercice du médecin.
     * @param structureExercice La nouvelle structure d'exercice du médecin.
     */
    public void setStructureExercice(String structureExercice) {
        this.structureExercice = structureExercice;
    }

    /**
     * Obtient la fonction/activité du médecin.
     * @return La fonction/activité du médecin.
     */
    public String getFonctionActivite() {
        return fonctionActivite;
    }

    /**
     * Définit la fonction/activité du médecin.
     * @param fonctionActivite La nouvelle fonction/activité du médecin.
     */
    public void setFonctionActivite(String fonctionActivite) {
        this.fonctionActivite = fonctionActivite;
    }

    /**
     * Obtient le genre d'activité du médecin.
     * @return Le genre d'activité du médecin.
     */
    public String getGenreActivite() {
        return genreActivite;
    }

    /**
     * Définit le genre d'activité du médecin.
     * @param genreActivite Le nouveau genre d'activité du médecin.
     */
    public void setGenreActivite(String genreActivite) {
        this.genreActivite = genreActivite;
    }

    /**
     * Retourne une représentation textuelle de l'objet MedecinResponse.
     * @return Une chaîne de caractères représentant l'objet.
     */
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