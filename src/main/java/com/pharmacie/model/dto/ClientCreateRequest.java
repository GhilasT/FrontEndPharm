package com.pharmacie.model.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO pour la création d'un client.
 */
public class ClientCreateRequest implements Serializable {

    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String adresse;

    public ClientCreateRequest() {
    }

    public ClientCreateRequest(String nom, String prenom, String telephone, String email, String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
    }

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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientCreateRequest)) return false;
        ClientCreateRequest that = (ClientCreateRequest) o;
        return Objects.equals(nom, that.nom) &&
                Objects.equals(prenom, that.prenom) &&
                Objects.equals(telephone, that.telephone) &&
                Objects.equals(email, that.email) &&
                Objects.equals(adresse, that.adresse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, prenom, telephone, email, adresse);
    }

    @Override
    public String toString() {
        return "ClientCreateRequest{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}