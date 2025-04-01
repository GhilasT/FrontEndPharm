package com.pharmacie.controller;

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

    private UUID clientId;

    @FXML
    void initialize() {
        // Générer un UUID temporaire pour le client
        clientId = UUID.randomUUID();
        errorLabel.setText("");
    }

    @FXML
    void handleAnnuler(ActionEvent event) {
        // Fermer la fenêtre
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleSuivant(ActionEvent event) throws IOException {
        if (validateForm()) {
            // Créer un objet client avec les informations saisies
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String telephone = telephoneField.getText().trim();
            String email = emailField.getText().trim();
            String adresse = adresseField.getText().trim();

            // Charger la page de recherche de médicaments dans la même fenêtre
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/recherche-medicaments.fxml"));
                Parent root = loader.load();
                
                // Obtenir le contrôleur et définir les informations du client
                RechercheMedicamentsController controller = loader.getController();
                controller.setClientInfo(clientId, nom, prenom, telephone, email, adresse);
                
                // Remplacer le contenu de la scène actuelle
                Scene scene = btnSuivant.getScene();
                scene.setRoot(root);
                
                // Mettre à jour le titre de la fenêtre
                Stage stage = (Stage) scene.getWindow();
                stage.setTitle("Recherche de médicaments");
                
                LOGGER.log(Level.INFO, "Page de recherche de médicaments chargée avec succès");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erreur lors du chargement de la page de recherche de médicaments", e);
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement", 
                        "Impossible de charger la page de recherche: " + e.getMessage());
            }
        }
    }

    private boolean validateForm() {
        // Vérifier que les champs obligatoires sont remplis
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

        // Validation réussie
        errorLabel.setText("");
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
