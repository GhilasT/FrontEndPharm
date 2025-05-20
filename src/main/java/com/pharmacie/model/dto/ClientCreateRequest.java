package com.pharmacie.model.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO (Data Transfer Object) pour la création d'un client.
 * Cette classe est utilisée pour transporter les données nécessaires à la création d'un nouveau client.
 * Elle implémente {@link Serializable} pour permettre sa sérialisation.
 */
public class ClientCreateRequest implements Serializable {

    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String adresse;

    /**
     * Constructeur par défaut.
     */
    public ClientCreateRequest() {
    }

    /**
     * Constructeur avec tous les champs.
     *
     * @param nom Le nom du client.
     * @param prenom Le prénom du client.
     * @param telephone Le numéro de téléphone du client.
     * @param email L'adresse email du client.
     * @param adresse L'adresse postale du client.
     */
    public ClientCreateRequest(String nom, String prenom, String telephone, String email, String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
    }

    /**
     * Obtient le nom du client.
     * @return Le nom du client.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom du client.
     * @param nom Le nouveau nom du client.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Obtient le prénom du client.
     * @return Le prénom du client.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Définit le prénom du client.
     * @param prenom Le nouveau prénom du client.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Obtient le numéro de téléphone du client.
     * @return Le numéro de téléphone du client.
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Définit le numéro de téléphone du client.
     * @param telephone Le nouveau numéro de téléphone du client.
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Obtient l'adresse email du client.
     * @return L'adresse email du client.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse email du client.
     * @param email La nouvelle adresse email du client.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtient l'adresse postale du client.
     * @return L'adresse postale du client.
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Définit l'adresse postale du client.
     * @param adresse La nouvelle adresse postale du client.
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * Compare cet objet ClientCreateRequest à un autre objet pour vérifier l'égalité.
     * Deux objets ClientCreateRequest sont considérés comme égaux si tous leurs champs sont égaux.
     *
     * @param o L'objet à comparer avec cet objet ClientCreateRequest.
     * @return true si les objets sont égaux, false sinon.
     */
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

    /**
     * Calcule le code de hachage pour cet objet ClientCreateRequest.
     * Le code de hachage est basé sur les valeurs de tous les champs.
     *
     * @return Le code de hachage de l'objet.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nom, prenom, telephone, email, adresse);
    }

    /**
     * Retourne une représentation textuelle de l'objet ClientCreateRequest.
     *
     * @return Une chaîne de caractères représentant l'objet, incluant les valeurs de ses champs.
     */
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