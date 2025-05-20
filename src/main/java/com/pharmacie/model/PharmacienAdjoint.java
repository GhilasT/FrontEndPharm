package com.pharmacie.model;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Modèle représentant un pharmacien adjoint.
 * Utilise des propriétés JavaFX pour la liaison de données dans l'interface utilisateur.
 */
public class PharmacienAdjoint {
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
    private final StringProperty emailPro = new SimpleStringProperty();

    /**
     * Constructeur pour créer un nouvel objet PharmacienAdjoint.
     * @param idPersonne L'identifiant unique de la personne.
     * @param nom Le nom du pharmacien adjoint.
     * @param prenom Le prénom du pharmacien adjoint.
     * @param email L'email personnel du pharmacien adjoint.
     * @param telephone Le numéro de téléphone du pharmacien adjoint.
     * @param adresse L'adresse du pharmacien adjoint.
     * @param matricule Le matricule du pharmacien adjoint.
     * @param dateEmbauche La date d'embauche du pharmacien adjoint.
     * @param salaire Le salaire du pharmacien adjoint.
     * @param poste Le poste occupé par le pharmacien adjoint.
     * @param statutContrat Le statut du contrat du pharmacien adjoint.
     * @param diplome Le diplôme du pharmacien adjoint.
     * @param emailPro L'email professionnel du pharmacien adjoint.
     */
    public PharmacienAdjoint(UUID idPersonne, String nom, String prenom, String email, String telephone,
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

    /**
     * Obtient l'identifiant de la personne.
     * @return L'identifiant de la personne.
     */
    public UUID getIdPersonne() { return idPersonne.get(); }
    /**
     * Définit l'identifiant de la personne.
     * @param value Le nouvel identifiant de la personne.
     */
    public void setIdPersonne(UUID value) { idPersonne.set(value); }
    /**
     * Obtient la propriété de l'identifiant de la personne.
     * @return La propriété de l'identifiant.
     */
    public ObjectProperty<UUID> idPersonneProperty() { return idPersonne; }

    /**
     * Obtient le nom du pharmacien adjoint.
     * @return Le nom.
     */
    public String getNom() { return nom.get(); }
    /**
     * Définit le nom du pharmacien adjoint.
     * @param value Le nouveau nom.
     */
    public void setNom(String value) { nom.set(value); }
    /**
     * Obtient la propriété du nom.
     * @return La propriété du nom.
     */
    public StringProperty nomProperty() { return nom; }

    /**
     * Obtient le prénom du pharmacien adjoint.
     * @return Le prénom.
     */
    public String getPrenom() { return prenom.get(); }
    /**
     * Définit le prénom du pharmacien adjoint.
     * @param value Le nouveau prénom.
     */
    public void setPrenom(String value) { prenom.set(value); }
    /**
     * Obtient la propriété du prénom.
     * @return La propriété du prénom.
     */
    public StringProperty prenomProperty() { return prenom; }

    /**
     * Obtient l'email personnel du pharmacien adjoint.
     * @return L'email personnel.
     */
    public String getEmail() { return email.get(); }
    /**
     * Définit l'email personnel du pharmacien adjoint.
     * @param value Le nouvel email personnel.
     */
    public void setEmail(String value) { email.set(value); }
    /**
     * Obtient la propriété de l'email personnel.
     * @return La propriété de l'email personnel.
     */
    public StringProperty emailProperty() { return email; }

    /**
     * Obtient le numéro de téléphone du pharmacien adjoint.
     * @return Le numéro de téléphone.
     */
    public String getTelephone() { return telephone.get(); }
    /**
     * Définit le numéro de téléphone du pharmacien adjoint.
     * @param value Le nouveau numéro de téléphone.
     */
    public void setTelephone(String value) { telephone.set(value); }
    /**
     * Obtient la propriété du numéro de téléphone.
     * @return La propriété du numéro de téléphone.
     */
    public StringProperty telephoneProperty() { return telephone; }

    /**
     * Obtient l'adresse du pharmacien adjoint.
     * @return L'adresse.
     */
    public String getAdresse() { return adresse.get(); }
    /**
     * Définit l'adresse du pharmacien adjoint.
     * @param value La nouvelle adresse.
     */
    public void setAdresse(String value) { adresse.set(value); }
    /**
     * Obtient la propriété de l'adresse.
     * @return La propriété de l'adresse.
     */
    public StringProperty adresseProperty() { return adresse; }

    /**
     * Obtient le matricule du pharmacien adjoint.
     * @return Le matricule.
     */
    public String getMatricule() { return matricule.get(); }
    /**
     * Définit le matricule du pharmacien adjoint.
     * @param value Le nouveau matricule.
     */
    public void setMatricule(String value) { matricule.set(value); }
    /**
     * Obtient la propriété du matricule.
     * @return La propriété du matricule.
     */
    public StringProperty matriculeProperty() { return matricule; }

    /**
     * Obtient la date d'embauche du pharmacien adjoint.
     * @return La date d'embauche.
     */
    public LocalDate getDateEmbauche() { return dateEmbauche.get(); }
    /**
     * Définit la date d'embauche du pharmacien adjoint.
     * @param value La nouvelle date d'embauche.
     */
    public void setDateEmbauche(LocalDate value) { dateEmbauche.set(value); }
    /**
     * Obtient la propriété de la date d'embauche.
     * @return La propriété de la date d'embauche.
     */
    public ObjectProperty<LocalDate> dateEmbaucheProperty() { return dateEmbauche; }

    /**
     * Obtient le salaire du pharmacien adjoint.
     * @return Le salaire.
     */
    public double getSalaire() { return salaire.get(); }
    /**
     * Définit le salaire du pharmacien adjoint.
     * @param value Le nouveau salaire.
     */
    public void setSalaire(double value) { salaire.set(value); }
    /**
     * Obtient la propriété du salaire.
     * @return La propriété du salaire.
     */
    public DoubleProperty salaireProperty() { return salaire; }

    /**
     * Obtient le poste du pharmacien adjoint.
     * @return Le poste.
     */
    public String getPoste() { return poste.get(); }
    /**
     * Définit le poste du pharmacien adjoint.
     * @param value Le nouveau poste.
     */
    public void setPoste(String value) { poste.set(value); }
    /**
     * Obtient la propriété du poste.
     * @return La propriété du poste.
     */
    public StringProperty posteProperty() { return poste; }

    /**
     * Obtient le statut du contrat du pharmacien adjoint.
     * @return Le statut du contrat.
     */
    public String getStatutContrat() { return statutContrat.get(); }
    /**
     * Définit le statut du contrat du pharmacien adjoint.
     * @param value Le nouveau statut du contrat.
     */
    public void setStatutContrat(String value) { statutContrat.set(value); }
    /**
     * Obtient la propriété du statut du contrat.
     * @return La propriété du statut du contrat.
     */
    public StringProperty statutContratProperty() { return statutContrat; }

    /**
     * Obtient le diplôme du pharmacien adjoint.
     * @return Le diplôme.
     */
    public String getDiplome() { return diplome.get(); }
    /**
     * Définit le diplôme du pharmacien adjoint.
     * @param value Le nouveau diplôme.
     */
    public void setDiplome(String value) { diplome.set(value); }
    /**
     * Obtient la propriété du diplôme.
     * @return La propriété du diplôme.
     */
    public StringProperty diplomeProperty() { return diplome; }

    /**
     * Obtient l'email professionnel du pharmacien adjoint.
     * @return L'email professionnel.
     */
    public String getEmailPro() { return emailPro.get(); }
    /**
     * Définit l'email professionnel du pharmacien adjoint.
     * @param value Le nouvel email professionnel.
     */
    public void setEmailPro(String value) { emailPro.set(value); }
    /**
     * Obtient la propriété de l'email professionnel.
     * @return La propriété de l'email professionnel.
     */
    public StringProperty emailProProperty() { return emailPro; }
}
