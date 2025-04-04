package com.pharmacie.model.dto;

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
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public Double getSalaire() { return salaire; }
    public void setSalaire(Double salaire) { this.salaire = salaire; }

    public String getStatutContrat() { return statutContrat; }
    public void setStatutContrat(String statutContrat) { this.statutContrat = statutContrat; }

    public String getDiplome() { return diplome; }
    public void setDiplome(String diplome) { this.diplome = diplome; }

    public String getEmailPro() { return emailPro; }
    public void setEmailPro(String emailPro) { this.emailPro = emailPro; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
