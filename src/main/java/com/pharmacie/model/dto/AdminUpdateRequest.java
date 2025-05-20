package com.pharmacie.model.dto;

/**
 * DTO pour la mise à jour des informations d'un administrateur.
 * Contient les champs modifiables d'un administrateur.
 */
public class AdminUpdateRequest {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private Double salaire;
    private String statutContrat;
    private String diplome;
    private String emailPro;
    private String role;

    // Getters et Setters
    /**
     * Obtient le nom de l'administrateur.
     * @return Le nom de l'administrateur.
     */
    public String getNom() { return nom; }
    /**
     * Définit le nom de l'administrateur.
     * @param nom Le nouveau nom de l'administrateur.
     */
    public void setNom(String nom) { this.nom = nom; }

    /**
     * Obtient le prénom de l'administrateur.
     * @return Le prénom de l'administrateur.
     */
    public String getPrenom() { return prenom; }
    /**
     * Définit le prénom de l'administrateur.
     * @param prenom Le nouveau prénom de l'administrateur.
     */
    public void setPrenom(String prenom) { this.prenom = prenom; }

    /**
     * Obtient l'email personnel de l'administrateur.
     * @return L'email personnel de l'administrateur.
     */
    public String getEmail() { return email; }
    /**
     * Définit l'email personnel de l'administrateur.
     * @param email Le nouvel email personnel de l'administrateur.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Obtient le numéro de téléphone de l'administrateur.
     * @return Le numéro de téléphone de l'administrateur.
     */
    public String getTelephone() { return telephone; }
    /**
     * Définit le numéro de téléphone de l'administrateur.
     * @param telephone Le nouveau numéro de téléphone de l'administrateur.
     */
    public void setTelephone(String telephone) { this.telephone = telephone; }

    /**
     * Obtient l'adresse de l'administrateur.
     * @return L'adresse de l'administrateur.
     */
    public String getAdresse() { return adresse; }
    /**
     * Définit l'adresse de l'administrateur.
     * @param adresse La nouvelle adresse de l'administrateur.
     */
    public void setAdresse(String adresse) { this.adresse = adresse; }

    /**
     * Obtient le salaire de l'administrateur.
     * @return Le salaire de l'administrateur.
     */
    public Double getSalaire() { return salaire; }
    /**
     * Définit le salaire de l'administrateur.
     * @param salaire Le nouveau salaire de l'administrateur.
     */
    public void setSalaire(Double salaire) { this.salaire = salaire; }

    /**
     * Obtient le statut du contrat de l'administrateur.
     * @return Le statut du contrat.
     */
    public String getStatutContrat() { return statutContrat; }
    /**
     * Définit le statut du contrat de l'administrateur.
     * @param statutContrat Le nouveau statut du contrat.
     */
    public void setStatutContrat(String statutContrat) { this.statutContrat = statutContrat; }

    /**
     * Obtient le diplôme de l'administrateur.
     * @return Le diplôme de l'administrateur.
     */
    public String getDiplome() { return diplome; }
    /**
     * Définit le diplôme de l'administrateur.
     * @param diplome Le nouveau diplôme de l'administrateur.
     */
    public void setDiplome(String diplome) { this.diplome = diplome; }

    /**
     * Obtient l'email professionnel de l'administrateur.
     * @return L'email professionnel de l'administrateur.
     */
    public String getEmailPro() { return emailPro; }
    /**
     * Définit l'email professionnel de l'administrateur.
     * @param emailPro Le nouvel email professionnel de l'administrateur.
     */
    public void setEmailPro(String emailPro) { this.emailPro = emailPro; }

    /**
     * Obtient le rôle de l'administrateur.
     * @return Le rôle de l'administrateur.
     */
    public String getRole() { return role; }
    /**
     * Définit le rôle de l'administrateur.
     * @param role Le nouveau rôle de l'administrateur.
     */
    public void setRole(String role) { this.role = role; }
}
