package com.pharmacie.model.dto;

/**
 * DTO pour la mise à jour des informations d'un apprenti.
 * Contient les champs modifiables d'un apprenti.
 */
public class ApprentiUpdateRequest {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String dateEmbauche; // Format : "yyyy-MM-dd"
    private Double salaire;
    private String statutContrat;
    private String diplome;
    private String ecole;
    private String emailPro;

    // Getters et Setters
    /**
     * Obtient le nom de l'apprenti.
     * @return Le nom de l'apprenti.
     */
    public String getNom() { return nom; }
    /**
     * Définit le nom de l'apprenti.
     * @param nom Le nouveau nom de l'apprenti.
     */
    public void setNom(String nom) { this.nom = nom; }

    /**
     * Obtient le prénom de l'apprenti.
     * @return Le prénom de l'apprenti.
     */
    public String getPrenom() { return prenom; }
    /**
     * Définit le prénom de l'apprenti.
     * @param prenom Le nouveau prénom de l'apprenti.
     */
    public void setPrenom(String prenom) { this.prenom = prenom; }

    /**
     * Obtient l'email personnel de l'apprenti.
     * @return L'email personnel de l'apprenti.
     */
    public String getEmail() { return email; }
    /**
     * Définit l'email personnel de l'apprenti.
     * @param email Le nouvel email personnel de l'apprenti.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Obtient le numéro de téléphone de l'apprenti.
     * @return Le numéro de téléphone de l'apprenti.
     */
    public String getTelephone() { return telephone; }
    /**
     * Définit le numéro de téléphone de l'apprenti.
     * @param telephone Le nouveau numéro de téléphone de l'apprenti.
     */
    public void setTelephone(String telephone) { this.telephone = telephone; }

    /**
     * Obtient l'adresse de l'apprenti.
     * @return L'adresse de l'apprenti.
     */
    public String getAdresse() { return adresse; }
    /**
     * Définit l'adresse de l'apprenti.
     * @param adresse La nouvelle adresse de l'apprenti.
     */
    public void setAdresse(String adresse) { this.adresse = adresse; }

    /**
     * Obtient la date d'embauche de l'apprenti (format "yyyy-MM-dd").
     * @return La date d'embauche.
     */
    public String getDateEmbauche() { return dateEmbauche; }
    /**
     * Définit la date d'embauche de l'apprenti (format "yyyy-MM-dd").
     * @param dateEmbauche La nouvelle date d'embauche.
     */
    public void setDateEmbauche(String dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    /**
     * Obtient le salaire de l'apprenti.
     * @return Le salaire de l'apprenti.
     */
    public Double getSalaire() { return salaire; }
    /**
     * Définit le salaire de l'apprenti.
     * @param salaire Le nouveau salaire de l'apprenti.
     */
    public void setSalaire(Double salaire) { this.salaire = salaire; }

    /**
     * Obtient le statut du contrat de l'apprenti.
     * @return Le statut du contrat.
     */
    public String getStatutContrat() { return statutContrat; }
    /**
     * Définit le statut du contrat de l'apprenti.
     * @param statutContrat Le nouveau statut du contrat.
     */
    public void setStatutContrat(String statutContrat) { this.statutContrat = statutContrat; }

    /**
     * Obtient le diplôme préparé par l'apprenti.
     * @return Le diplôme.
     */
    public String getDiplome() { return diplome; }
    /**
     * Définit le diplôme préparé par l'apprenti.
     * @param diplome Le nouveau diplôme.
     */
    public void setDiplome(String diplome) { this.diplome = diplome; }

    /**
     * Obtient l'école de l'apprenti.
     * @return L'école de l'apprenti.
     */
    public String getEcole() { return ecole; }
    /**
     * Définit l'école de l'apprenti.
     * @param ecole La nouvelle école de l'apprenti.
     */
    public void setEcole(String ecole) { this.ecole = ecole; }

    /**
     * Obtient l'email professionnel de l'apprenti.
     * @return L'email professionnel de l'apprenti.
     */
    public String getEmailPro() { return emailPro; }
    /**
     * Définit l'email professionnel de l'apprenti.
     * @param emailPro Le nouvel email professionnel de l'apprenti.
     */
    public void setEmailPro(String emailPro) { this.emailPro = emailPro; }
}