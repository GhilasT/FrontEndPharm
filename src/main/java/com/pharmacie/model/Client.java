package com.pharmacie.model;

import java.util.UUID;

/**
 * Modèle représentant un client dans l'interface utilisateur.
 * Contient les informations personnelles et de contact du client.
 */
public class Client {
    private UUID idPersonne;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String adresse;
    private String numeroSecu;
    private String mutuelle;

    /**
     * Constructeur par défaut pour Client.
     */
    public Client() {
    }

    /**
     * Constructeur avec paramètres pour initialiser un client.
     * @param id L'identifiant unique de la personne (client).
     * @param nom Le nom du client.
     * @param prenom Le prénom du client.
     * @param telephone Le numéro de téléphone du client.
     * @param email L'adresse email du client.
     * @param adresse L'adresse postale du client.
     */
    public Client(UUID id, String nom, String prenom, String telephone, String email, String adresse) {
        this.idPersonne = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
    }

    // Getters et Setters
    /**
     * Obtient l'identifiant de la personne (client).
     * @return L'identifiant UUID.
     */
    public UUID getIdPersonne() {
        return idPersonne;
    }

    /**
     * Définit l'identifiant de la personne (client).
     * @param id Le nouvel identifiant UUID.
     */
    public void setId(UUID id) {
        this.idPersonne = id;
    }

    /**
     * Obtient le nom du client.
     * @return Le nom.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom du client.
     * @param nom Le nouveau nom.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Obtient le prénom du client.
     * @return Le prénom.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Définit le prénom du client.
     * @param prenom Le nouveau prénom.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Obtient le numéro de téléphone du client.
     * @return Le numéro de téléphone.
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Définit le numéro de téléphone du client.
     * @param telephone Le nouveau numéro de téléphone.
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Obtient l'adresse email du client.
     * @return L'adresse email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse email du client.
     * @param email La nouvelle adresse email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtient l'adresse postale du client.
     * @return L'adresse postale.
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Définit l'adresse postale du client.
     * @param adresse La nouvelle adresse postale.
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * Obtient le numéro de sécurité sociale du client.
     * @return Le numéro de sécurité sociale.
     */
    public String getNumeroSecu() {
        return numeroSecu;
    }

    /**
     * Définit le numéro de sécurité sociale du client.
     * @param numeroSecu Le nouveau numéro de sécurité sociale.
     */
    public void setNumeroSecu(String numeroSecu) {
        this.numeroSecu = numeroSecu;
    }

    /**
     * Obtient la mutuelle du client.
     * @return La mutuelle.
     */
    public String getMutuelle() {
        return mutuelle;
    }

    /**
     * Définit la mutuelle du client.
     * @param mutuelle La nouvelle mutuelle.
     */
    public void setMutuelle(String mutuelle) {
        this.mutuelle = mutuelle;
    }

    /**
     * Retourne une représentation textuelle du client (prénom et nom).
     * @return Le prénom suivi du nom.
     */
    @Override
    public String toString() {
        return prenom + " " + nom;
    }
}
