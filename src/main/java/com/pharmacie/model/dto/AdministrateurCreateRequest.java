package com.pharmacie.model.dto;

import java.time.LocalDate;
import java.util.Date;

public class AdministrateurCreateRequest {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String dateEmbauche;
    private Double salaire;
    private String poste = "ADMINISTRATEUR"; // Valeur par défaut
    private String statutContrat;
    private String diplome;
    private String emailPro;
    private String role;
    private String password;

    // Constructeur par défaut
    public AdministrateurCreateRequest() {}

    // Constructeur avec tous les champs
    public AdministrateurCreateRequest(String nom, String prenom, String email, String telephone, 
                                      String adresse, String dateEmbauche, Double salaire, 
                                      String statutContrat, String diplome, String emailPro, 
                                      String role, String password) {
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
        this.role = role;
        this.password = password;
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(String dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public Double getSalaire() {
        return salaire;
    }

    public void setSalaire(Double salaire) {
        this.salaire = salaire;
    }

    public String getPoste() {
        return poste;
    }

    // Pas besoin de setter pour poste car il est toujours ADMINISTRATEUR

    public String getStatutContrat() {
        return statutContrat;
    }

    public void setStatutContrat(String statutContrat) {
        this.statutContrat = statutContrat;
    }

    public String getDiplome() {
        return diplome;
    }

    public void setDiplome(String diplome) {
        this.diplome = diplome;
    }

    public String getEmailPro() {
        return emailPro;
    }

    public void setEmailPro(String emailPro) {
        this.emailPro = emailPro;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}