package com.pharmacie.util;

import java.util.UUID;

/**
 * Représente le vendeur connecté dans l'application.
 * Utilise le pattern Singleton pour assurer une unique instance du vendeur connecté.
 */
public class LoggedSeller {
    private static LoggedSeller instance;
    private UUID id;
    private String nom;
    private String prenom;
    private String role;
    private String token;

    /**
     * Constructeur privé pour empêcher l'instanciation directe.
     */
    private LoggedSeller() {
        // Constructeur privé pour le pattern Singleton
    }

    /**
     * Fournit l'instance unique de LoggedSeller.
     * Crée l'instance si elle n'existe pas encore.
     *
     * @return L'instance unique de {@link LoggedSeller}.
     */
    public static LoggedSeller getInstance() {
        if (instance == null) {
            instance = new LoggedSeller();
        }
        return instance;
    }

    /**
     * Configure les informations de l'utilisateur connecté.
     *
     * @param id L'identifiant unique de l'utilisateur.
     * @param nom Le nom de l'utilisateur.
     * @param prenom Le prénom de l'utilisateur.
     * @param role Le rôle de l'utilisateur.
     * @param token Le jeton d'authentification de l'utilisateur.
     */
    public void setUser(UUID id, String nom, String prenom, String role, String token) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.token = token;
        
        // Add debug logging to verify token is being stored
        System.out.println("LoggedSeller initialized - Role: " + role);
        System.out.println("Token stored: " + (token != null ? "Yes" : "No"));
        System.out.println("token : " + token);
    }
    
    /**
     * Récupère le rôle de l'utilisateur connecté.
     *
     * @return Le rôle de l'utilisateur.
     */
    public String getRole() {
        return this.role;
    }
    
    /**
     * Récupère l'identifiant de l'utilisateur connecté.
     *
     * @return L'UUID de l'utilisateur.
     */
    public UUID getId() {
        return this.id;
    }
    
    /**
     * Récupère le nom complet (nom et prénom) de l'utilisateur connecté.
     *
     * @return Le nom complet de l'utilisateur.
     */
    public String getNomComplet() {
        return nom + " " + prenom;
    }
    
    /**
     * Récupère le jeton d'authentification de l'utilisateur connecté.
     *
     * @return Le jeton d'authentification.
     */
    public String getToken() {
        return this.token;
    }
    
    /**
     * Réinitialise les informations de l'utilisateur connecté.
     * Toutes les propriétés de l'utilisateur sont mises à null.
     */
    public void clearUser() {
        this.id = null;
        this.nom = null;
        this.prenom = null;
        this.role = null;
        this.token = null;
    }
}