package com.pharmacie.model.dto;

import java.time.LocalDate;

public class MedecinUpdateRequest {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String password;
    private String codeCIS;
    private String denomination;
    private String formePharmaceutique;
    private String voiesAdministration;
    private String statutAdministratifAMM;
    private String typeProcedureAMM;
    private String etatCommercialisation;
    private LocalDate dateAMM;
    private String titulaire;
    private String statutBdm;
    private Double prix;
    private String surveillanceRenforcee;
    private String numAutorisationEuropeenne;
    private String codeCIP;
    private String indication;
    private String posologie;
    private String generique;

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

    public String getCodeCIS() { return codeCIS; }
    public void setCodeCIS(String codeCIS) { this.codeCIS = codeCIS; }

    public String getDenomination() { return denomination; }
    public void setDenomination(String denomination) { this.denomination = denomination; }

    public String getFormePharmaceutique() { return formePharmaceutique; }
    public void setFormePharmaceutique(String formePharmaceutique) { this.formePharmaceutique = formePharmaceutique; }

    public String getVoiesAdministration() { return voiesAdministration; }
    public void setVoiesAdministration(String voiesAdministration) { this.voiesAdministration = voiesAdministration; }

    public String getStatutAdministratifAMM() { return statutAdministratifAMM; }
    public void setStatutAdministratifAMM(String statutAdministratifAMM) { this.statutAdministratifAMM = statutAdministratifAMM; }

    public String getTypeProcedureAMM() { return typeProcedureAMM; }
    public void setTypeProcedureAMM(String typeProcedureAMM) { this.typeProcedureAMM = typeProcedureAMM; }

    public String getEtatCommercialisation() { return etatCommercialisation; }
    public void setEtatCommercialisation(String etatCommercialisation) { this.etatCommercialisation = etatCommercialisation; }

    public LocalDate getDateAMM() { return dateAMM; }
    public void setDateAMM(LocalDate dateAMM) { this.dateAMM = dateAMM; }

    public String getTitulaire() { return titulaire; }
    public void setTitulaire(String titulaire) { this.titulaire = titulaire; }

    public String getStatutBdm() { return statutBdm; }
    public void setStatutBdm(String statutBdm) { this.statutBdm = statutBdm; }

    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }

    public String getSurveillanceRenforcee() { return surveillanceRenforcee; }
    public void setSurveillanceRenforcee(String surveillanceRenforcee) { this.surveillanceRenforcee = surveillanceRenforcee; }

    public String getNumAutorisationEuropeenne() { return numAutorisationEuropeenne; }
    public void setNumAutorisationEuropeenne(String numAutorisationEuropeenne) { this.numAutorisationEuropeenne = numAutorisationEuropeenne; }

    public String getCodeCIP() { return codeCIP; }
    public void setCodeCIP(String codeCIP) { this.codeCIP = codeCIP; }

    public String getIndication() { return indication; }
    public void setIndication(String indication) { this.indication = indication; }

    public String getPosologie() { return posologie; }
    public void setPosologie(String posologie) { this.posologie = posologie; }

    public String getGenerique() { return generique; }
    public void setGenerique(String generique) { this.generique = generique; }
}