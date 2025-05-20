package com.pharmacie.model;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Représente un apprenti au sein de la pharmacie.
 * Contient des informations personnelles, contractuelles et de formation.
 * Utilise des propriétés JavaFX pour la liaison de données.
 */
public class Apprenti {
    private final ObjectProperty<UUID> idPersonne = new SimpleObjectProperty<>();
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
    private final StringProperty ecole = new SimpleStringProperty(); // Champ unique
    private final StringProperty emailPro = new SimpleStringProperty();

    /**
     * Constructeur pour initialiser un apprenti.
     * @param idPersonne L'identifiant unique de la personne (apprenti).
     * @param nom Le nom de l'apprenti.
     * @param prenom Le prénom de l'apprenti.
     * @param email L'adresse email personnelle de l'apprenti.
     * @param telephone Le numéro de téléphone de l'apprenti.
     * @param adresse L'adresse postale de l'apprenti.
     * @param matricule Le matricule de l'apprenti.
     * @param dateEmbauche La date d'embauche de l'apprenti.
     * @param salaire Le salaire de l'apprenti.
     * @param poste Le poste occupé par l'apprenti.
     * @param statutContrat Le statut du contrat de l'apprenti.
     * @param diplome Le diplôme préparé ou obtenu par l'apprenti.
     * @param ecole L'école de l'apprenti.
     * @param emailPro L'adresse email professionnelle de l'apprenti.
     */
    public Apprenti(UUID idPersonne, String nom, String prenom, String email, String telephone,
                    String adresse, String matricule, LocalDate dateEmbauche, double salaire,
                    String poste, String statutContrat, String diplome, String ecole, String emailPro) {
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
        setEcole(ecole);
        setEmailPro(emailPro);
    }

    // Getters et Setters pour chaque propriété
    /**
     * Obtient l'identifiant UUID de la personne (apprenti).
     * @return L'identifiant de la personne.
     */
    public UUID getIdPersonne() { return idPersonne.get(); }
    /**
     * Définit l'identifiant UUID de la personne (apprenti).
     * @param value Le nouvel identifiant.
     */
    public void setIdPersonne(UUID value) { idPersonne.set(value); }
    /**
     * Retourne la propriété JavaFX pour l'identifiant de la personne.
     * @return La propriété idPersonne.
     */
    public ObjectProperty<UUID> idPersonneProperty() { return idPersonne; }

    /**
     * Obtient le nom de l'apprenti.
     * @return Le nom.
     */
    public String getNom() { return nom.get(); }
    /**
     * Définit le nom de l'apprenti.
     * @param value Le nouveau nom.
     */
    public void setNom(String value) { nom.set(value); }
    /**
     * Retourne la propriété JavaFX pour le nom.
     * @return La propriété nom.
     */
    public StringProperty nomProperty() { return nom; }

    /**
     * Obtient le prénom de l'apprenti.
     * @return Le prénom.
     */
    public String getPrenom() { return prenom.get(); }
    /**
     * Définit le prénom de l'apprenti.
     * @param value Le nouveau prénom.
     */
    public void setPrenom(String value) { prenom.set(value); }
    /**
     * Retourne la propriété JavaFX pour le prénom.
     * @return La propriété prenom.
     */
    public StringProperty prenomProperty() { return prenom; }

    /**
     * Obtient l'adresse email personnelle de l'apprenti.
     * @return L'email personnel.
     */
    public String getEmail() { return email.get(); }
    /**
     * Définit l'adresse email personnelle de l'apprenti.
     * @param value Le nouvel email personnel.
     */
    public void setEmail(String value) { email.set(value); }
    /**
     * Retourne la propriété JavaFX pour l'email personnel.
     * @return La propriété email.
     */
    public StringProperty emailProperty() { return email; }

    /**
     * Obtient le numéro de téléphone de l'apprenti.
     * @return Le numéro de téléphone.
     */
    public String getTelephone() { return telephone.get(); }
    /**
     * Définit le numéro de téléphone de l'apprenti.
     * @param value Le nouveau numéro de téléphone.
     */
    public void setTelephone(String value) { telephone.set(value); }
    /**
     * Retourne la propriété JavaFX pour le téléphone.
     * @return La propriété telephone.
     */
    public StringProperty telephoneProperty() { return telephone; }

    /**
     * Obtient l'adresse postale de l'apprenti.
     * @return L'adresse.
     */
    public String getAdresse() { return adresse.get(); }
    /**
     * Définit l'adresse postale de l'apprenti.
     * @param value La nouvelle adresse.
     */
    public void setAdresse(String value) { adresse.set(value); }
    /**
     * Retourne la propriété JavaFX pour l'adresse.
     * @return La propriété adresse.
     */
    public StringProperty adresseProperty() { return adresse; }

    /**
     * Obtient le matricule de l'apprenti.
     * @return Le matricule.
     */
    public String getMatricule() { return matricule.get(); }
    /**
     * Définit le matricule de l'apprenti.
     * @param value Le nouveau matricule.
     */
    public void setMatricule(String value) { matricule.set(value); }
    /**
     * Retourne la propriété JavaFX pour le matricule.
     * @return La propriété matricule.
     */
    public StringProperty matriculeProperty() { return matricule; }

    /**
     * Obtient la date d'embauche de l'apprenti.
     * @return La date d'embauche.
     */
    public LocalDate getDateEmbauche() { return dateEmbauche.get(); }
    /**
     * Définit la date d'embauche de l'apprenti.
     * @param value La nouvelle date d'embauche.
     */
    public void setDateEmbauche(LocalDate value) { dateEmbauche.set(value); }
    /**
     * Retourne la propriété JavaFX pour la date d'embauche.
     * @return La propriété dateEmbauche.
     */
    public ObjectProperty<LocalDate> dateEmbaucheProperty() { return dateEmbauche; }

    /**
     * Obtient le salaire de l'apprenti.
     * @return Le salaire.
     */
    public double getSalaire() { return salaire.get(); }
    /**
     * Définit le salaire de l'apprenti.
     * @param value Le nouveau salaire.
     */
    public void setSalaire(double value) { salaire.set(value); }
    /**
     * Retourne la propriété JavaFX pour le salaire.
     * @return La propriété salaire.
     */
    public DoubleProperty salaireProperty() { return salaire; }

    /**
     * Obtient le poste de l'apprenti.
     * @return Le poste.
     */
    public String getPoste() { return poste.get(); }
    /**
     * Définit le poste de l'apprenti.
     * @param value Le nouveau poste.
     */
    public void setPoste(String value) { poste.set(value); }
    /**
     * Retourne la propriété JavaFX pour le poste.
     * @return La propriété poste.
     */
    public StringProperty posteProperty() { return poste; }

    /**
     * Obtient le statut du contrat de l'apprenti.
     * @return Le statut du contrat.
     */
    public String getStatutContrat() { return statutContrat.get(); }
    /**
     * Définit le statut du contrat de l'apprenti.
     * @param value Le nouveau statut du contrat.
     */
    public void setStatutContrat(String value) { statutContrat.set(value); }
    /**
     * Retourne la propriété JavaFX pour le statut du contrat.
     * @return La propriété statutContrat.
     */
    public StringProperty statutContratProperty() { return statutContrat; }

    /**
     * Obtient le diplôme de l'apprenti.
     * @return Le diplôme.
     */
    public String getDiplome() { return diplome.get(); }
    /**
     * Définit le diplôme de l'apprenti.
     * @param value Le nouveau diplôme.
     */
    public void setDiplome(String value) { diplome.set(value); }
    /**
     * Retourne la propriété JavaFX pour le diplôme.
     * @return La propriété diplome.
     */
    public StringProperty diplomeProperty() { return diplome; }

    /**
     * Obtient l'école de l'apprenti.
     * @return L'école.
     */
    public String getEcole() { return ecole.get(); }
    /**
     * Définit l'école de l'apprenti.
     * @param value La nouvelle école.
     */
    public void setEcole(String value) { ecole.set(value); }
    /**
     * Retourne la propriété JavaFX pour l'école.
     * @return La propriété ecole.
     */
    public StringProperty ecoleProperty() { return ecole; }

    /**
     * Obtient l'adresse email professionnelle de l'apprenti.
     * @return L'email professionnel.
     */
    public String getEmailPro() { return emailPro.get(); }
    /**
     * Définit l'adresse email professionnelle de l'apprenti.
     * @param value Le nouvel email professionnel.
     */
    public void setEmailPro(String value) { emailPro.set(value); }
    /**
     * Retourne la propriété JavaFX pour l'email professionnel.
     * @return La propriété emailPro.
     */
    public StringProperty emailProProperty() { return emailPro; }
}
