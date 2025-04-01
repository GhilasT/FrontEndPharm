package com.pharmacie.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PopupVenteController {

    private static final Logger LOGGER = Logger.getLogger(PopupVenteController.class.getName());

    @FXML
    private Button btnAvecOrdonnance;

    @FXML
    private Button btnSansOrdonnance;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void handleAvecOrdonnance(ActionEvent event) {
        // Fonctionnalité non implémentée pour le moment
        showAlert(Alert.AlertType.INFORMATION, "Information", 
                "Fonctionnalité non disponible", 
                "La vente avec ordonnance n'est pas encore implémentée.");
        
        // Fermer le popup
        if (stage != null) {
            stage.close();
        }
    }

    @FXML
    void handleSansOrdonnance(ActionEvent event) {
        try {
            // Fermer le popup actuel
            if (stage != null) {
                stage.close();
            }
            
            // Ouvrir le formulaire client
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/formulaire-client.fxml"));
            Parent root = loader.load();
            
            FormulaireClientController controller = loader.getController();
            
            Stage formStage = new Stage();
            formStage.setTitle("Formulaire Client");
            formStage.setScene(new Scene(root));
            formStage.initModality(Modality.APPLICATION_MODAL);
            formStage.showAndWait();
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ouverture du formulaire client", e);
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ouverture du formulaire client", 
                    "Impossible d'ouvrir le formulaire: " + e.getMessage());
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
