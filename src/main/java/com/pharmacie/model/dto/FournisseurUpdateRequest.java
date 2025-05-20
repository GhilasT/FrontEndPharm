package com.pharmacie.model.dto;

/**
 * DTO pour la mise à jour des informations d'un fournisseur.
 * Contient les champs modifiables d'un fournisseur.
 */
public class FournisseurUpdateRequest {
    private String nomSociete;
    private String sujetFonction;
    private String fax;
    private String email;
    private String telephone;
    private String adresse;

    // Getters & Setters
    /**
     * Obtient le nom de la société du fournisseur.
     * @return Le nom de la société.
     */
    public String getNomSociete() { return nomSociete; }
    /**
     * Définit le nom de la société du fournisseur.
     * @param nomSociete Le nouveau nom de la société.
     */
    public void setNomSociete(String nomSociete) { this.nomSociete = nomSociete; }

    /**
     * Obtient le sujet/fonction du contact chez le fournisseur.
     * @return Le sujet/fonction.
     */
    public String getSujetFonction() { return sujetFonction; }
    /**
     * Définit le sujet/fonction du contact chez le fournisseur.
     * @param sujetFonction Le nouveau sujet/fonction.
     */
    public void setSujetFonction(String sujetFonction) { this.sujetFonction = sujetFonction; }

    /**
     * Obtient le numéro de fax du fournisseur.
     * @return Le numéro de fax.
     */
    public String getFax() { return fax; }
    /**
     * Définit le numéro de fax du fournisseur.
     * @param fax Le nouveau numéro de fax.
     */
    public void setFax(String fax) { this.fax = fax; }

    /**
     * Obtient l'adresse email du fournisseur.
     * @return L'adresse email.
     */
    public String getEmail() { return email; }
    /**
     * Définit l'adresse email du fournisseur.
     * @param email La nouvelle adresse email.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Obtient le numéro de téléphone du fournisseur.
     * @return Le numéro de téléphone.
     */
    public String getTelephone() { return telephone; }
    /**
     * Définit le numéro de téléphone du fournisseur.
     * @param telephone Le nouveau numéro de téléphone.
     */
    public void setTelephone(String telephone) { this.telephone = telephone; }

    /**
     * Obtient l'adresse postale du fournisseur.
     * @return L'adresse postale.
     */
    public String getAdresse() { return adresse; }
    /**
     * Définit l'adresse postale du fournisseur.
     * @param adresse La nouvelle adresse postale.
     */
    public void setAdresse(String adresse) { this.adresse = adresse; }
}