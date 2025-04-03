package com.pharmacie.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Preparateur {
    private final IntegerProperty idPersonne = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty prenom = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty telephone = new SimpleStringProperty();
    private final StringProperty adresse = new SimpleStringProperty();
    private final StringProperty matricule = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dateEmbauche = new SimpleObjectProperty<>();
    private final DoubleProperty salaire = new SimpleDoubleProperty();
    private final StringProperty poste = new SimpleStringProperty();
    private final StringProperty statutContrat = new SimpleStringProperty();
    private final StringProperty diplome = new SimpleStringProperty();
    private final StringProperty emailPro = new SimpleStringProperty();

    // Constructeur
    public Preparateur(int idPersonne, String nom, String prenom, String email, String telephone,
                       String adresse, String matricule, LocalDate dateEmbauche, double salaire,
                       String poste, String statutContrat, String diplome, String emailPro) {
        setIdPersonne(idPersonne);
        setNom(nom);
        setPrenom(prenom);
        setEmail(email);
        setTelephone(telephone);
        setAdresse(adresse);
        setMatricule(matricule);
        setDateEmbauche(dateEmbauche);
        setSalaire(salaire);
        setPoste(poste);
        setStatutContrat(statutContrat);
        setDiplome(diplome);
        setEmailPro(emailPro);
    }

    // Getters et Setters pour chaque propriété
    public int getIdPersonne() { return idPersonne.get(); }
    public void setIdPersonne(int value) { idPersonne.set(value); }
    public IntegerProperty idPersonneProperty() { return idPersonne; }

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

    public String getMatricule() { return matricule.get(); }
    public void setMatricule(String value) { matricule.set(value); }
    public StringProperty matriculeProperty() { return matricule; }

    public LocalDate getDateEmbauche() { return dateEmbauche.get(); }
    public void setDateEmbauche(LocalDate value) { dateEmbauche.set(value); }
    public ObjectProperty<LocalDate> dateEmbaucheProperty() { return dateEmbauche; }

    public double getSalaire() { return salaire.get(); }
    public void setSalaire(double value) { salaire.set(value); }
    public DoubleProperty salaireProperty() { return salaire; }

    public String getPoste() { return poste.get(); }
    public void setPoste(String value) { poste.set(value); }
    public StringProperty posteProperty() { return poste; }

    public String getStatutContrat() { return statutContrat.get(); }
    public void setStatutContrat(String value) { statutContrat.set(value); }
    public StringProperty statutContratProperty() { return statutContrat; }

    public String getDiplome() { return diplome.get(); }
    public void setDiplome(String value) { diplome.set(value); }
    public StringProperty diplomeProperty() { return diplome; }

    public String getEmailPro() { return emailPro.get(); }
    public void setEmailPro(String value) { emailPro.set(value); }
    public StringProperty emailProProperty() { return emailPro; }
}
