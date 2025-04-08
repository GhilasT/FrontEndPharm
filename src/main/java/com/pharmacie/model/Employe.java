package com.pharmacie.model;

public class Employe extends Personne {
    private String matricule;
    private String emailPro;

  

    // Getters & Setters
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getEmailPro() { return emailPro; }
    public void setEmailPro(String emailPro) { this.emailPro = emailPro; }
}