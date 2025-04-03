package com.pharmacie.model.dto;

import java.util.UUID;

public class LoginResponse {
    private boolean success;
    private String nom;
    private String prenom;
    private String role;
    private UUID id;

    // Setters nécessaires pour Gson
    public void setSuccess(boolean success) { this.success = success; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setRole(String role) { this.role = role; }
    public void setId(UUID id) { this.id = id; }

    // Getters
    public boolean isSuccess() { return success; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getRole() { return role; }
    public UUID getId() { return id; }
}