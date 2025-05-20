package com.pharmacie.model;

import javafx.beans.property.*;
import java.util.UUID;

/**
 * Représente un fournisseur avec ses informations de contact.
 * Utilise des propriétés JavaFX pour la liaison de données.
 */
public class Fournisseur {
    private final ObjectProperty<UUID> idFournisseur = new SimpleObjectProperty<>();
    private final StringProperty nomSociete = new SimpleStringProperty();
    private final StringProperty sujetFonction = new SimpleStringProperty();
    private final StringProperty fax = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty telephone = new SimpleStringProperty();
    private final StringProperty adresse = new SimpleStringProperty();

    /**
     * Constructeur pour initialiser un fournisseur.
     * @param idFournisseur L'identifiant unique du fournisseur.
     * @param nomSociete Le nom de la société du fournisseur.
     * @param sujetFonction Le sujet ou la fonction du contact chez le fournisseur.
     * @param fax Le numéro de fax du fournisseur.
     * @param email L'adresse email du fournisseur.
     * @param telephone Le numéro de téléphone du fournisseur.
     * @param adresse L'adresse postale du fournisseur.
     */
    public Fournisseur(UUID idFournisseur, String nomSociete, String sujetFonction, String fax,
                       String email, String telephone, String adresse) {
        setIdFournisseur(idFournisseur);
        setNomSociete(nomSociete);
        setSujetFonction(sujetFonction);
        setFax(fax);
        setEmail(email);
        setTelephone(telephone);
        setAdresse(adresse);
    }

    // Getters, Setters et méthodes Property
    /**
     * Obtient l'identifiant UUID du fournisseur.
     * @return L'identifiant du fournisseur.
     */
    public UUID getIdFournisseur() { return idFournisseur.get(); }
    /**
     * Définit l'identifiant UUID du fournisseur.
     * @param value Le nouvel identifiant du fournisseur.
     */
    public void setIdFournisseur(UUID value) { idFournisseur.set(value); }
    /**
     * Retourne la propriété JavaFX pour l'identifiant du fournisseur.
     * @return La propriété idFournisseur.
     */
    public ObjectProperty<UUID> idFournisseurProperty() { return idFournisseur; }

    /**
     * Obtient le nom de la société du fournisseur.
     * @return Le nom de la société.
     */
    public String getNomSociete() { return nomSociete.get(); }
    /**
     * Définit le nom de la société du fournisseur.
     * @param value Le nouveau nom de la société.
     */
    public void setNomSociete(String value) { nomSociete.set(value); }
    /**
     * Retourne la propriété JavaFX pour le nom de la société.
     * @return La propriété nomSociete.
     */
    public StringProperty nomSocieteProperty() { return nomSociete; }

    /**
     * Obtient le sujet ou la fonction du contact chez le fournisseur.
     * @return Le sujet/fonction.
     */
    public String getSujetFonction() { return sujetFonction.get(); }
    /**
     * Définit le sujet ou la fonction du contact chez le fournisseur.
     * @param value Le nouveau sujet/fonction.
     */
    public void setSujetFonction(String value) { sujetFonction.set(value); }
    /**
     * Retourne la propriété JavaFX pour le sujet/fonction.
     * @return La propriété sujetFonction.
     */
    public StringProperty sujetFonctionProperty() { return sujetFonction; }

    /**
     * Obtient le numéro de fax du fournisseur.
     * @return Le numéro de fax.
     */
    public String getFax() { return fax.get(); }
    /**
     * Définit le numéro de fax du fournisseur.
     * @param value Le nouveau numéro de fax.
     */
    public void setFax(String value) { fax.set(value); }
    /**
     * Retourne la propriété JavaFX pour le fax.
     * @return La propriété fax.
     */
    public StringProperty faxProperty() { return fax; }

    /**
     * Obtient l'adresse email du fournisseur.
     * @return L'adresse email.
     */
    public String getEmail() { return email.get(); }
    /**
     * Définit l'adresse email du fournisseur.
     * @param value La nouvelle adresse email.
     */
    public void setEmail(String value) { email.set(value); }
    /**
     * Retourne la propriété JavaFX pour l'email.
     * @return La propriété email.
     */
    public StringProperty emailProperty() { return email; }

    /**
     * Obtient le numéro de téléphone du fournisseur.
     * @return Le numéro de téléphone.
     */
    public String getTelephone() { return telephone.get(); }
    /**
     * Définit le numéro de téléphone du fournisseur.
     * @param value Le nouveau numéro de téléphone.
     */
    public void setTelephone(String value) { telephone.set(value); }
    /**
     * Retourne la propriété JavaFX pour le téléphone.
     * @return La propriété telephone.
     */
    public StringProperty telephoneProperty() { return telephone; }

    /**
     * Obtient l'adresse postale du fournisseur.
     * @return L'adresse postale.
     */
    public String getAdresse() { return adresse.get(); }
    /**
     * Définit l'adresse postale du fournisseur.
     * @param value La nouvelle adresse postale.
     */
    public void setAdresse(String value) { adresse.set(value); }
    /**
     * Retourne la propriété JavaFX pour l'adresse.
     * @return La propriété adresse.
     */
    public StringProperty adresseProperty() { return adresse; }
}