package com.pharmacie.model;

public class Employe extends Personne {
    private String matricule;
    private String emailPro;
    private String poste;
    private String email;
    private String telephone;
    private String adresse;

    // Getters & Setters
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getEmailPro() { return emailPro; }
    public void setEmailPro(String emailPro) { this.emailPro = emailPro; }

    public String getPoste() { return poste; }
    public void setPoste(String poste) { this.poste = poste; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
}
