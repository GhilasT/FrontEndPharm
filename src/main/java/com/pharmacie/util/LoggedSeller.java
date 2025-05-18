package com.pharmacie.util;

import java.util.UUID;

public class LoggedSeller {
    private static LoggedSeller instance;
    private UUID id;
    private String nom;
    private String prenom;
    private String role;
    private String token;

    public static LoggedSeller getInstance() {
        if (instance == null) {
            instance = new LoggedSeller();
        }
        return instance;
    }

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
    
    public String getRole() {
        return this.role;
    }
    
    public UUID getId() {
        return this.id;
    }
    
    public String getNomComplet() {
        return nom + " " + prenom;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public void clearUser() {
        this.id = null;
        this.nom = null;
        this.prenom = null;
        this.role = null;
        this.token = null;
    }
}