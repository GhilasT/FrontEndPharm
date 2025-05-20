package com.pharmacie.model;

import java.util.UUID;

/**
 * Modèle de base représentant une personne avec un identifiant, un nom et un prénom.
 * Cette classe peut être étendue par des modèles plus spécifiques comme Client, Pharmacien, etc.
 */
public class Personne {
    private UUID idPersonne;
    private String nom;
    private String prenom;

    // Getters & Setters
    /**
     * Obtient l'identifiant unique de la personne.
     * @return L'identifiant de la personne.
     */
    public UUID getIdPersonne() { return idPersonne; }
    /**
     * Définit l'identifiant unique de la personne.
     * @param id Le nouvel identifiant de la personne.
     */
    public void setIdPersonne(UUID id) { this.idPersonne = id; }

    /**
     * Obtient le nom de la personne.
     * @return Le nom de la personne.
     */
    public String getNom() { return nom; }
    /**
     * Définit le nom de la personne.
     * @param nom Le nouveau nom de la personne.
     */
    public void setNom(String nom) { this.nom = nom; }

    /**
     * Obtient le prénom de la personne.
     * @return Le prénom de la personne.
     */
    public String getPrenom() { return prenom; }
    /**
     * Définit le prénom de la personne.
     * @param prenom Le nouveau prénom de la personne.
     */
    public void setPrenom(String prenom) { this.prenom = prenom; }
}