package com.pharmacie.model.dto;

/**
 * DTO pour une requête d'envoi d'email concernant une commande.
 * Contient la référence de la commande.
 */
public class EmailCommandeRequest {
    private String commandeReference;

    /**
     * Définit la référence de la commande.
     * @param commandeReference La référence de la commande.
     */
    public void setCommandeReference(String commandeReference) {
        this.commandeReference = commandeReference;
    }
    
    /**
     * Obtient la référence de la commande.
     * @return La référence de la commande.
     */
    public String getCommandeReference() {
        return commandeReference;
    }

    /**
     * Convertit l'objet en une chaîne JSON.
     * @return La représentation JSON de l'objet.
     */
    public String toJson() {
        return "{\"commandeReference\":\"" + commandeReference + "\"}";
    }
}