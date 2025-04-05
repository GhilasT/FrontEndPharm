package com.pharmacie.model.dto;

public class FournisseurCreateRequest {
    private String nomSociete;
    private String sujetFonction;
    private String fax;
    private String email;
    private String telephone;
    private String adresse;

    // Constructeur par défaut
    public FournisseurCreateRequest() {}

    // Constructeur avec champs obligatoires + optionnels
    public FournisseurCreateRequest(String nomSociete, String email, String telephone, String adresse, 
                                    String sujetFonction, String fax) {
        this.nomSociete = nomSociete;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.sujetFonction = sujetFonction;
        this.fax = fax;
    }

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