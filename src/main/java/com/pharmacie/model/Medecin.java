package com.pharmacie.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Medecin {
    private final ObjectProperty<UUID> idPersonne = new SimpleObjectProperty<>();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty prenom = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty telephone = new SimpleStringProperty();
    private final StringProperty adresse = new SimpleStringProperty();
    private final StringProperty rpps = new SimpleStringProperty();
    private final StringProperty adeli = new SimpleStringProperty();
    private final StringProperty civilite = new SimpleStringProperty();
    private final StringProperty profession = new SimpleStringProperty();
    private final StringProperty specialitePrincipale = new SimpleStringProperty();
    private final StringProperty specialiteSecondaire = new SimpleStringProperty();
    private final StringProperty modeExercice = new SimpleStringProperty();
    private final StringProperty codePostal = new SimpleStringProperty();
    private final StringProperty ville = new SimpleStringProperty();
    private final StringProperty siteWeb = new SimpleStringProperty();
    private final StringProperty secteur = new SimpleStringProperty();
    private final StringProperty conventionnement = new SimpleStringProperty();
    private final StringProperty honoraires = new SimpleStringProperty();
    private final ObservableList<String> languesParlees = FXCollections.observableArrayList();
    private final StringProperty siret = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dateMiseAJour = new SimpleObjectProperty<>();

    // Constructeur
    public Medecin(UUID idPersonne, String nom, String prenom, String email, String telephone,
                   String adresse, String rpps, String adeli, String civilite, String profession,
                   String specialitePrincipale, String specialiteSecondaire, String modeExercice,
                   String codePostal, String ville, String siteWeb, String secteur,
                   String conventionnement, String honoraires, List<String> languesParlees,
                   String siret, LocalDate dateMiseAJour) {
        setIdPersonne(idPersonne);
        setNom(nom);
        setPrenom(prenom);
        setEmail(email);
        setTelephone(telephone);
        setAdresse(adresse);
        setRpps(rpps);
        setAdeli(adeli);
        setCivilite(civilite);
        setProfession(profession);
        setSpecialitePrincipale(specialitePrincipale);
        setSpecialiteSecondaire(specialiteSecondaire);
        setModeExercice(modeExercice);
        setCodePostal(codePostal);
        setVille(ville);
        setSiteWeb(siteWeb);
        setSecteur(secteur);
        setConventionnement(conventionnement);
        setHonoraires(honoraires);
        setLanguesParlees(languesParlees);
        setSiret(siret);
        setDateMiseAJour(dateMiseAJour);
    }

    public Medecin(UUID idPersonne, String nom, String prenom, String email, String telephone,
               String rpps, String adeli, String civilite, String profession,
               String specialitePrincipale, String modeExercice, String codePostal,
               String ville, String secteur, String conventionnement, String honoraires,
               String siret, LocalDate dateMiseAJour) {
    setIdPersonne(idPersonne);
    setNom(nom);
    setPrenom(prenom);
    setEmail(email);
    setTelephone(telephone);
    setRpps(rpps);
    setAdeli(adeli);
    setCivilite(civilite);
    setProfession(profession);
    setSpecialitePrincipale(specialitePrincipale);
    setModeExercice(modeExercice);
    setCodePostal(codePostal);
    setVille(ville);
    setSecteur(secteur);
    setConventionnement(conventionnement);
    setHonoraires(honoraires);
    setSiret(siret);
    setDateMiseAJour(dateMiseAJour);
}

    // Getters, Setters et méthodes Property
    public UUID getIdPersonne() { return idPersonne.get(); }
    public void setIdPersonne(UUID value) { idPersonne.set(value); }
    public ObjectProperty<UUID> idPersonneProperty() { return idPersonne; }

    public String getNom() { return nom.get(); }
    public void setNom(String value) { nom.set(value); }
    public StringProperty nomProperty() { return nom; }

    public String getPrenom() { return prenom.get(); }
    public void setPrenom(String value) { prenom.set(value); }
    public StringProperty prenomProperty() { return prenom; }

    public String getEmail() { return email.get(); }
    public void setEmail(String value) { email.set(value); }
    public StringProperty emailProperty() { return email; }

    public String getTelephone() { return telephone.get(); }
    public void setTelephone(String value) { telephone.set(value); }
    public StringProperty telephoneProperty() { return telephone; }

    public String getAdresse() { return adresse.get(); }
    public void setAdresse(String value) { adresse.set(value); }
    public StringProperty adresseProperty() { return adresse; }

    public String getRpps() { return rpps.get(); }
    public void setRpps(String value) { rpps.set(value); }
    public StringProperty rppsProperty() { return rpps; }

    public String getAdeli() { return adeli.get(); }
    public void setAdeli(String value) { adeli.set(value); }
    public StringProperty adeliProperty() { return adeli; }

    public String getCivilite() { return civilite.get(); }
    public void setCivilite(String value) { civilite.set(value); }
    public StringProperty civiliteProperty() { return civilite; }

    public String getProfession() { return profession.get(); }
    public void setProfession(String value) { profession.set(value); }
    public StringProperty professionProperty() { return profession; }

    public String getSpecialitePrincipale() { return specialitePrincipale.get(); }
    public void setSpecialitePrincipale(String value) { specialitePrincipale.set(value); }
    public StringProperty specialitePrincipaleProperty() { return specialitePrincipale; }

    public String getSpecialiteSecondaire() { return specialiteSecondaire.get(); }
    public void setSpecialiteSecondaire(String value) { specialiteSecondaire.set(value); }
    public StringProperty specialiteSecondaireProperty() { return specialiteSecondaire; }

    public String getModeExercice() { return modeExercice.get(); }
    public void setModeExercice(String value) { modeExercice.set(value); }
    public StringProperty modeExerciceProperty() { return modeExercice; }

    public String getCodePostal() { return codePostal.get(); }
    public void setCodePostal(String value) { codePostal.set(value); }
    public StringProperty codePostalProperty() { return codePostal; }

    public String getVille() { return ville.get(); }
    public void setVille(String value) { ville.set(value); }
    public StringProperty villeProperty() { return ville; }

    public String getSiteWeb() { return siteWeb.get(); }
    public void setSiteWeb(String value) { siteWeb.set(value); }
    public StringProperty siteWebProperty() { return siteWeb; }

    public String getSecteur() { return secteur.get(); }
    public void setSecteur(String value) { secteur.set(value); }
    public StringProperty secteurProperty() { return secteur; }

    public String getConventionnement() { return conventionnement.get(); }
    public void setConventionnement(String value) { conventionnement.set(value); }
    public StringProperty conventionnementProperty() { return conventionnement; }

    public String getHonoraires() { return honoraires.get(); }
    public void setHonoraires(String value) { honoraires.set(value); }
    public StringProperty honorairesProperty() { return honoraires; }

    public ObservableList<String> getLanguesParlees() { return languesParlees; }
    public void setLanguesParlees(List<String> value) { languesParlees.setAll(value); }

    public String getSiret() { return siret.get(); }
    public void setSiret(String value) { siret.set(value); }
    public StringProperty siretProperty() { return siret; }

    public LocalDate getDateMiseAJour() { return dateMiseAJour.get(); }
    public void setDateMiseAJour(LocalDate value) { dateMiseAJour.set(value); }
    public ObjectProperty<LocalDate> dateMiseAJourProperty() { return dateMiseAJour; }
}
