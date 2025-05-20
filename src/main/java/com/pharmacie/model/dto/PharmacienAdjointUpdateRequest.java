package com.pharmacie.model.dto;

/**
 * DTO (Data Transfer Object) pour la mise à jour des informations d'un pharmacien adjoint.
 * Contient les champs modifiables pour un pharmacien adjoint existant.
 */
public class PharmacienAdjointUpdateRequest {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String dateEmbauche; // Format : "yyyy-MM-dd"
    private Double salaire;
    private String statutContrat;
    private String diplome;
    private String emailPro;

    // Getters et Setters
    /**
     * Obtient le nom du pharmacien adjoint.
     * @return Le nom.
     */
    public String getNom() { return nom; }
    /**
     * Définit le nom du pharmacien adjoint.
     * @param nom Le nouveau nom.
     */
    public void setNom(String nom) { this.nom = nom; }

    /**
     * Obtient le prénom du pharmacien adjoint.
     * @return Le prénom.
     */
    public String getPrenom() { return prenom; }
    /**
     * Définit le prénom du pharmacien adjoint.
     * @param prenom Le nouveau prénom.
     */
    public void setPrenom(String prenom) { this.prenom = prenom; }

    /**
     * Obtient l'adresse email personnelle du pharmacien adjoint.
     * @return L'email personnel.
     */
    public String getEmail() { return email; }
    /**
     * Définit l'adresse email personnelle du pharmacien adjoint.
     * @param email Le nouvel email personnel.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Obtient le numéro de téléphone du pharmacien adjoint.
     * @return Le numéro de téléphone.
     */
    public String getTelephone() { return telephone; }
    /**
     * Définit le numéro de téléphone du pharmacien adjoint.
     * @param telephone Le nouveau numéro de téléphone.
     */
    public void setTelephone(String telephone) { this.telephone = telephone; }

    /**
     * Obtient l'adresse postale du pharmacien adjoint.
     * @return L'adresse.
     */
    public String getAdresse() { return adresse; }
    /**
     * Définit l'adresse postale du pharmacien adjoint.
     * @param adresse La nouvelle adresse.
     */
    public void setAdresse(String adresse) { this.adresse = adresse; }

    /**
     * Obtient la date d'embauche du pharmacien adjoint (format "yyyy-MM-dd").
     * @return La date d'embauche.
     */
    public String getDateEmbauche() { return dateEmbauche; }
    /**
     * Définit la date d'embauche du pharmacien adjoint.
     * @param dateEmbauche La nouvelle date d'embauche (format "yyyy-MM-dd").
     */
    public void setDateEmbauche(String dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    /**
     * Obtient le salaire du pharmacien adjoint.
     * @return Le salaire.
     */
    public Double getSalaire() { return salaire; }
    /**
     * Définit le salaire du pharmacien adjoint.
     * @param salaire Le nouveau salaire.
     */
    public void setSalaire(Double salaire) { this.salaire = salaire; }

    /**
     * Obtient le statut du contrat du pharmacien adjoint.
     * @return Le statut du contrat.
     */
    public String getStatutContrat() { return statutContrat; }
    /**
     * Définit le statut du contrat du pharmacien adjoint.
     * @param statutContrat Le nouveau statut du contrat.
     */
    public void setStatutContrat(String statutContrat) { this.statutContrat = statutContrat; }

    /**
     * Obtient le diplôme du pharmacien adjoint.
     * @return Le diplôme.
     */
    public String getDiplome() { return diplome; }
    /**
     * Définit le diplôme du pharmacien adjoint.
     * @param diplome Le nouveau diplôme.
     */
    public void setDiplome(String diplome) { this.diplome = diplome; }

    /**
     * Obtient l'adresse email professionnelle du pharmacien adjoint.
     * @return L'email professionnel.
     */
    public String getEmailPro() { return emailPro; }
    /**
     * Définit l'adresse email professionnelle du pharmacien adjoint.
     * @param emailPro Le nouvel email professionnel.
     */
    public void setEmailPro(String emailPro) { this.emailPro = emailPro; }
}