package com.pharmacie.util;

import java.util.UUID;

public class LoggedSeller {
    private static LoggedSeller instance;
    private UUID id;
    private String nom;
    private String prenom;
    private String role;

    public static LoggedSeller getInstance() {
        if (instance == null) {
            instance = new LoggedSeller();
        }
        return instance;
    }

    public void setUser(UUID id, String nom, String prenom, String role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
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
    public void clearUser() {
        this.id = null;
        this.nom = null;
        this.prenom = null;
        this.role = null;
    }
}