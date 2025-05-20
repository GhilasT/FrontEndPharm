package com.pharmacie.controller;

import com.pharmacie.model.Client;
import com.pharmacie.util.Global;
import com.pharmacie.util.HttpClientUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contrôleur pour le formulaire de création ou de recherche de client.
 * Gère la saisie des informations client et l'interaction avec l'API pour
 * trouver ou créer un client.
 */
public class FormulaireClientController {

    private static final Logger LOGGER = Logger.getLogger(FormulaireClientController.class.getName());

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField adresseField;

    @FXML
    private Button btnAnnuler;

    @FXML
    private Button btnSuivant;

    @FXML
    private Label errorLabel;

    // clientId sera récupéré dynamiquement depuis l'API
    private UUID clientId;

    /**
     * Initialise le contrôleur lors de son chargement.
     * Efface le label d'erreur.
     */
    @FXML
    void initialize() {
        errorLabel.setText("");
    }

    /**
     * Gère l'action du bouton "Annuler".
     * Ferme la fenêtre du formulaire.
     * @param event L'événement d'action.
     */
    @FXML
    void handleAnnuler(ActionEvent event) {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    /**
     * Gère l'action du bouton "Suivant".
     * Valide le formulaire, puis tente de trouver ou créer le client via l'API.
     * Met à jour le client global et ferme la fenêtre en cas de succès.
     * Affiche des alertes en cas d'erreur.
     * @param event L'événement d'action.
     */
    @FXML
    void handleSuivant(ActionEvent event) {
        if (validateForm()) {
            try {
                // Récupérer les infos saisies
                String nom = nomField.getText().trim();
                String prenom = prenomField.getText().trim();
                String telephone = telephoneField.getText().trim();
                String email = emailField.getText().trim();
                String adresse = adresseField.getText().trim();

                // Si l'email est vide, on le considère comme non fourni
                if(email.isEmpty()){
                    email = null;
                }

                // Créer un objet Client avec email pouvant être null
                Client clientPayload = new Client(null, nom, prenom, telephone, email, adresse);

                try {
                    // Appel à l'API : findOrCreateClient
                    Client clientResponse = HttpClientUtil.findOrCreateClient(clientPayload);
                    clientId = clientResponse.getIdPersonne();
                    // Mettre à jour le client global
                    Global.GlobalClient = clientResponse;
                    Global.GlobalClient.setId(clientId);
                    
                    // Fermer la fenêtre du formulaire
                    Stage currentStage = (Stage) btnSuivant.getScene().getWindow();
                    currentStage.close();
                } catch (java.net.ConnectException e) {
                    LOGGER.log(Level.SEVERE, "Erreur de connexion au serveur", e);
                    showAlert(Alert.AlertType.ERROR, "Erreur de connexion",
                        "Impossible de se connecter au serveur",
                        "Vérifiez que le serveur est en fonctionnement et que votre réseau est connecté.");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de l'appel API client", e);
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "Erreur lors de la création du client",
                        "Impossible de créer/récupérer le client: " + e.getMessage());
            }
        }
    }

    /**
     * Valide les champs du formulaire.
     * Vérifie que le nom, le prénom et le téléphone ne sont pas vides.
     * Affiche un message d'erreur si la validation échoue.
     * @return true si le formulaire est valide, false sinon.
     */
    private boolean validateForm() {
        if (nomField.getText().trim().isEmpty()) {
            errorLabel.setText("Le nom est obligatoire");
            return false;
        }
        if (prenomField.getText().trim().isEmpty()) {
            errorLabel.setText("Le prénom est obligatoire");
            return false;
        }
        if (telephoneField.getText().trim().isEmpty()) {
            errorLabel.setText("Le téléphone est obligatoire");
            return false;
        }
        errorLabel.setText("");
        return true;
    }

    /**
     * Affiche une boîte de dialogue d'alerte.
     * @param type Le type d'alerte (par exemple, ERROR, INFORMATION).
     * @param title Le titre de l'alerte.
     * @param header Le texte d'en-tête de l'alerte.
     * @param content Le contenu principal de l'alerte.
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Récupère l'ID du client qui a été créé ou trouvé.
     * @return L'UUID du client.
     */
    public UUID getClientId() {
        System.out.println(" le id client test dans formulaire client "+ clientId);
        return clientId;
    }
}