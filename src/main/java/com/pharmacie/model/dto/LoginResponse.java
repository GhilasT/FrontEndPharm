package com.pharmacie.model.dto;

import java.util.UUID;

/**
 * DTO représentant la réponse suite à une tentative de connexion.
 * Contient les informations de l'utilisateur connecté et un token d'authentification.
 */
public class LoginResponse {
    private boolean success;
    private String nom;
    private String prenom;
    private String role;
    private UUID id;
    private String token;

    /**
     * Définit si la connexion a réussi.
     * @param success true si la connexion a réussi, false sinon.
     */
    public void setSuccess(boolean success) { this.success = success; }
    /**
     * Définit le nom de l'utilisateur.
     * @param nom Le nom de l'utilisateur.
     */
    public void setNom(String nom) { this.nom = nom; }
    /**
     * Définit le prénom de l'utilisateur.
     * @param prenom Le prénom de l'utilisateur.
     */
    public void setPrenom(String prenom) { this.prenom = prenom; }
    /**
     * Définit le rôle de l'utilisateur.
     * @param role Le rôle de l'utilisateur.
     */
    public void setRole(String role) { this.role = role; }
    /**
     * Définit l'identifiant de l'utilisateur.
     * @param id L'identifiant de l'utilisateur.
     */
    public void setId(UUID id) { this.id = id; }
    /**
     * Définit le token d'authentification.
     * @param token Le token d'authentification.
     */
    public void setToken(String token) { this.token = token; }

    // Getters
    /**
     * Vérifie si la connexion a réussi.
     * @return true si la connexion a réussi, false sinon.
     */
    public boolean isSuccess() { return success; }
    /**
     * Obtient le nom de l'utilisateur.
     * @return Le nom de l'utilisateur.
     */
    public String getNom() { return nom; }
    /**
     * Obtient le prénom de l'utilisateur.
     * @return Le prénom de l'utilisateur.
     */
    public String getPrenom() { return prenom; }
    /**
     * Obtient le rôle de l'utilisateur.
     * @return Le rôle de l'utilisateur.
     */
    public String getRole() { return role; }
    /**
     * Obtient l'identifiant de l'utilisateur.
     * @return L'identifiant de l'utilisateur.
     */
    public UUID getId() { return id; }
    /**
     * Obtient le token d'authentification.
     * @return Le token d'authentification.
     */
    public String getToken() { return token; }
}