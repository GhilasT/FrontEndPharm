package com.pharmacie.model;

import java.util.UUID;

/**
 * Modèle représentant un client dans le frontend.
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

    // Constructeur par défaut
    public Client() {
    }

    // Constructeur avec tous les paramètres
    public Client(UUID id, String nom, String prenom, String telephone, String email, String adresse) {
        this.idPersonne = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
    }

    // Getters et Setters
    public UUID getIdPersonne() {
        return idPersonne;
    }

    public void setId(UUID id) {
        this.idPersonne = id;
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

    public String getNumeroSecu() {
        return numeroSecu;
    }

    public void setNumeroSecu(String numeroSecu) {
        this.numeroSecu = numeroSecu;
    }

    public String getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(String mutuelle) {
        this.mutuelle = mutuelle;
    }

    @Override
    public String toString() {
        return prenom + " " + nom;
    }
}
