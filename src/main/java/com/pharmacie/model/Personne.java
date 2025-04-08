package com.pharmacie.model;

import java.util.UUID;

public class Personne {
    private UUID idPersonne;
    private String nom;
    private String prenom;

    // Getters & Setters
    public UUID getIdPersonne() { return idPersonne; }
    public void setIdPersonne(UUID id) { this.idPersonne = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
}