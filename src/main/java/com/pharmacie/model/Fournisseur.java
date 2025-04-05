package com.pharmacie.model;

import javafx.beans.property.*;
import java.util.UUID;

public class Fournisseur {
    private final ObjectProperty<UUID> idFournisseur = new SimpleObjectProperty<>();
    private final StringProperty nomSociete = new SimpleStringProperty();
    private final StringProperty sujetFonction = new SimpleStringProperty();
    private final StringProperty fax = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty telephone = new SimpleStringProperty();
    private final StringProperty adresse = new SimpleStringProperty();

    // Constructeur
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
    public UUID getIdFournisseur() { return idFournisseur.get(); }
    public void setIdFournisseur(UUID value) { idFournisseur.set(value); }
    public ObjectProperty<UUID> idFournisseurProperty() { return idFournisseur; }

    public String getNomSociete() { return nomSociete.get(); }
    public void setNomSociete(String value) { nomSociete.set(value); }
    public StringProperty nomSocieteProperty() { return nomSociete; }

    public String getSujetFonction() { return sujetFonction.get(); }
    public void setSujetFonction(String value) { sujetFonction.set(value); }
    public StringProperty sujetFonctionProperty() { return sujetFonction; }

    public String getFax() { return fax.get(); }
    public void setFax(String value) { fax.set(value); }
    public StringProperty faxProperty() { return fax; }

    public String getEmail() { return email.get(); }
    public void setEmail(String value) { email.set(value); }
    public StringProperty emailProperty() { return email; }

    public String getTelephone() { return telephone.get(); }
    public void setTelephone(String value) { telephone.set(value); }
    public StringProperty telephoneProperty() { return telephone; }

    public String getAdresse() { return adresse.get(); }
    public void setAdresse(String value) { adresse.set(value); }
    public StringProperty adresseProperty() { return adresse; }
}