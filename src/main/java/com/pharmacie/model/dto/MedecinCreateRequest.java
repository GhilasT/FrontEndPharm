package com.pharmacie.model.dto;

import java.time.LocalDate;
import java.util.List;

public class MedecinCreateRequest {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String password;
    private String rpps;
    private String adeli;
    private String civilite;
    private String profession;
    private String specialitePrincipale;
    private String specialiteSecondaire;
    private String modeExercice;
    private String codePostal;
    private String ville;
    private String siteWeb;
    private String secteur;
    private String conventionnement;
    private String honoraires;
    private Boolean carteVitale;
    private Boolean teleconsultation;
    private List<String> languesParlees;
    private String siret;
    private LocalDate dateMiseAJour;

    // Constructeurs
    public MedecinCreateRequest() {}

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

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRpps() { return rpps; }
    public void setRpps(String rpps) { this.rpps = rpps; }

    public String getAdeli() { return adeli; }
    public void setAdeli(String adeli) { this.adeli = adeli; }

    public String getCivilite() { return civilite; }
    public void setCivilite(String civilite) { this.civilite = civilite; }

    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }

    public String getSpecialitePrincipale() { return specialitePrincipale; }
    public void setSpecialitePrincipale(String specialitePrincipale) { this.specialitePrincipale = specialitePrincipale; }

    public String getSpecialiteSecondaire() { return specialiteSecondaire; }
    public void setSpecialiteSecondaire(String specialiteSecondaire) { this.specialiteSecondaire = specialiteSecondaire; }

    public String getModeExercice() { return modeExercice; }
    public void setModeExercice(String modeExercice) { this.modeExercice = modeExercice; }

    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getSiteWeb() { return siteWeb; }
    public void setSiteWeb(String siteWeb) { this.siteWeb = siteWeb; }

    public String getSecteur() { return secteur; }
    public void setSecteur(String secteur) { this.secteur = secteur; }

    public String getConventionnement() { return conventionnement; }
    public void setConventionnement(String conventionnement) { this.conventionnement = conventionnement; }

    public String getHonoraires() { return honoraires; }
    public void setHonoraires(String honoraires) { this.honoraires = honoraires; }

    public Boolean getCarteVitale() { return carteVitale; }
    public void setCarteVitale(Boolean carteVitale) { this.carteVitale = carteVitale; }

    public Boolean getTeleconsultation() { return teleconsultation; }
    public void setTeleconsultation(Boolean teleconsultation) { this.teleconsultation = teleconsultation; }

    public List<String> getLanguesParlees() { return languesParlees; }
    public void setLanguesParlees(List<String> languesParlees) { this.languesParlees = languesParlees; }

    public String getSiret() { return siret; }
    public void setSiret(String siret) { this.siret = siret; }

    public LocalDate getDateMiseAJour() { return dateMiseAJour; }
    public void setDateMiseAJour(LocalDate dateMiseAJour) { this.dateMiseAJour = dateMiseAJour; }
}