package com.pharmacie.model.dto;

/**
 * DTO (Data Transfer Object) pour la création d'un nouveau préparateur.
 * Contient les informations nécessaires pour enregistrer un nouveau préparateur.
 */
public class PreparateurCreateRequest {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String dateEmbauche; // Format : "yyyy-MM-dd"
    private Double salaire;
    private String poste = "PREPARATEUR"; // Valeur fixée
    private String statutContrat;
    private String diplome;
    private String emailPro;
    private String password;

    /**
     * Constructeur par défaut.
     */
    public PreparateurCreateRequest() {}

    /**
     * Constructeur avec tous les champs pour initialiser une demande de création de préparateur.
     * @param nom Le nom du préparateur.
     * @param prenom Le prénom du préparateur.
     * @param email L'adresse email personnelle.
     * @param telephone Le numéro de téléphone.
     * @param adresse L'adresse postale.
     * @param dateEmbauche La date d'embauche (format "yyyy-MM-dd").
     * @param salaire Le salaire.
     * @param statutContrat Le statut du contrat.
     * @param diplome Le diplôme.
     * @param emailPro L'adresse email professionnelle.
     * @param password Le mot de passe pour le nouveau compte.
     */
    public PreparateurCreateRequest(String nom, String prenom, String email, String telephone,
                                    String adresse, String dateEmbauche, Double salaire,
                                    String statutContrat, String diplome, String emailPro, 
                                    String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dateEmbauche = dateEmbauche;
        this.salaire = salaire;
        this.statutContrat = statutContrat;
        this.diplome = diplome;
        this.emailPro = emailPro;
        this.password = password;
    }

    // Getters et Setters
    /**
     * Obtient le nom du préparateur.
     * @return Le nom.
     */
    public String getNom() { return nom; }
    /**
     * Définit le nom du préparateur.
     * @param nom Le nouveau nom.
     */
    public void setNom(String nom) { this.nom = nom; }

    /**
     * Obtient le prénom du préparateur.
     * @return Le prénom.
     */
    public String getPrenom() { return prenom; }
    /**
     * Définit le prénom du préparateur.
     * @param prenom Le nouveau prénom.
     */
    public void setPrenom(String prenom) { this.prenom = prenom; }

    /**
     * Obtient l'adresse email personnelle du préparateur.
     * @return L'email personnel.
     */
    public String getEmail() { return email; }
    /**
     * Définit l'adresse email personnelle du préparateur.
     * @param email Le nouvel email personnel.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Obtient le numéro de téléphone du préparateur.
     * @return Le numéro de téléphone.
     */
    public String getTelephone() { return telephone; }
    /**
     * Définit le numéro de téléphone du préparateur.
     * @param telephone Le nouveau numéro de téléphone.
     */
    public void setTelephone(String telephone) { this.telephone = telephone; }

    /**
     * Obtient l'adresse postale du préparateur.
     * @return L'adresse.
     */
    public String getAdresse() { return adresse; }
    /**
     * Définit l'adresse postale du préparateur.
     * @param adresse La nouvelle adresse.
     */
    public void setAdresse(String adresse) { this.adresse = adresse; }

    /**
     * Obtient la date d'embauche du préparateur (format "yyyy-MM-dd").
     * @return La date d'embauche.
     */
    public String getDateEmbauche() { return dateEmbauche; }
    /**
     * Définit la date d'embauche du préparateur.
     * @param dateEmbauche La nouvelle date d'embauche (format "yyyy-MM-dd").
     */
    public void setDateEmbauche(String dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    /**
     * Obtient le salaire du préparateur.
     * @return Le salaire.
     */
    public Double getSalaire() { return salaire; }
    /**
     * Définit le salaire du préparateur.
     * @param salaire Le nouveau salaire.
     */
    public void setSalaire(Double salaire) { this.salaire = salaire; }

    /**
     * Obtient le poste du préparateur (valeur fixée à "PREPARATEUR").
     * @return Le poste.
     */
    public String getPoste() { return poste; } // Pas de setter

    /**
     * Obtient le statut du contrat du préparateur.
     * @return Le statut du contrat.
     */
    public String getStatutContrat() { return statutContrat; }
    /**
     * Définit le statut du contrat du préparateur.
     * @param statutContrat Le nouveau statut du contrat.
     */
    public void setStatutContrat(String statutContrat) { this.statutContrat = statutContrat; }

    /**
     * Obtient le diplôme du préparateur.
     * @return Le diplôme.
     */
    public String getDiplome() { return diplome; }
    /**
     * Définit le diplôme du préparateur.
     * @param diplome Le nouveau diplôme.
     */
    public void setDiplome(String diplome) { this.diplome = diplome; }

    /**
     * Obtient l'adresse email professionnelle du préparateur.
     * @return L'email professionnel.
     */
    public String getEmailPro() { return emailPro; }
    /**
     * Définit l'adresse email professionnelle du préparateur.
     * @param emailPro Le nouvel email professionnel.
     */
    public void setEmailPro(String emailPro) { this.emailPro = emailPro; }

    /**
     * Obtient le mot de passe pour le nouveau compte du préparateur.
     * @return Le mot de passe.
     */
    public String getPassword() { return password; }
    /**
     * Définit le mot de passe pour le nouveau compte du préparateur.
     * @param password Le nouveau mot de passe.
     */
    public void setPassword(String password) { this.password = password; }
}