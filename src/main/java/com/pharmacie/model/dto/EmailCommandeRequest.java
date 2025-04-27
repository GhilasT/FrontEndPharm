package com.pharmacie.model.dto;

public class EmailCommandeRequest {
    private String commandeReference;

    public void setCommandeReference(String commandeReference) {
        this.commandeReference = commandeReference;
    }
    public String toJson() {
        return "{\"commandeReference\":\"" + commandeReference + "\"}";
    }
}