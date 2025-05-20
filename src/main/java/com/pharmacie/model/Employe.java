package com.pharmacie.model;

/**
 * Représente un employé de la pharmacie, héritant de la classe {@code Personne}.
 * Contient des informations spécifiques à l'employé comme le matricule et le poste.
 */
public class Employe extends Personne {
    private String matricule;
    private String emailPro;
    private String poste;
    private String email;
    private String telephone;
    private String adresse;

    // Getters & Setters
    /**
     * Obtient le matricule de l'employé.
     * @return Le matricule.
     */
    public String getMatricule() { return matricule; }
    /**
     * Définit le matricule de l'employé.
     * @param matricule Le nouveau matricule.
     */
    public void setMatricule(String matricule) { this.matricule = matricule; }

    /**
     * Obtient l'email professionnel de l'employé.
     * @return L'email professionnel.
     */
    public String getEmailPro() { return emailPro; }
    /**
     * Définit l'email professionnel de l'employé.
     * @param emailPro Le nouvel email professionnel.
     */
    public void setEmailPro(String emailPro) { this.emailPro = emailPro; }

    /**
     * Obtient le poste de l'employé.
     * @return Le poste.
     */
    public String getPoste() { return poste; }
    /**
     * Définit le poste de l'employé.
     * @param poste Le nouveau poste.
     */
    public void setPoste(String poste) { this.poste = poste; }

    /**
     * Obtient l'email personnel de l'employé.
     * @return L'email personnel.
     */
    public String getEmail() { return email; }
    /**
     * Définit l'email personnel de l'employé.
     * @param email Le nouvel email personnel.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Obtient le numéro de téléphone de l'employé.
     * @return Le numéro de téléphone.
     */
    public String getTelephone() { return telephone; }
    /**
     * Définit le numéro de téléphone de l'employé.
     * @param telephone Le nouveau numéro de téléphone.
     */
    public void setTelephone(String telephone) { this.telephone = telephone; }

    /**
     * Obtient l'adresse de l'employé.
     * @return L'adresse.
     */
    public String getAdresse() { return adresse; }
    /**
     * Définit l'adresse de l'employé.
     * @param adresse La nouvelle adresse.
     */
    public void setAdresse(String adresse) { this.adresse = adresse; }
}
