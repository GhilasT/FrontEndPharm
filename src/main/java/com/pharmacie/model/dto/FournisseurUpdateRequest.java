package com.pharmacie.model.dto;

public class FournisseurUpdateRequest {
    private String nomSociete;
    private String sujetFonction;
    private String fax;
    private String email;
    private String telephone;
    private String adresse;

    // Getters & Setters
    public String getNomSociete() { return nomSociete; }
    public void setNomSociete(String nomSociete) { this.nomSociete = nomSociete; }

    public String getSujetFonction() { return sujetFonction; }
    public void setSujetFonction(String sujetFonction) { this.sujetFonction = sujetFonction; }

    public String getFax() { return fax; }
    public void setFax(String fax) { this.fax = fax; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
}