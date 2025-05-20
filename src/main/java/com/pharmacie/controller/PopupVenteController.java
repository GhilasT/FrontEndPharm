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

/**
 * Contrôleur pour le popup de sélection du type de vente (avec ou sans ordonnance).
 * Permet à l'utilisateur de choisir le flux de vente approprié.
 */
public class PopupVenteController {

    private static final Logger LOGGER = Logger.getLogger(PopupVenteController.class.getName());

    @FXML
    private Button btnAvecOrdonnance;

    @FXML
    private Button btnSansOrdonnance;

    private Stage stage;

    /**
     * Définit la {@link Stage} (fenêtre) de ce popup.
     * Utilisé pour pouvoir fermer le popup.
     * @param stage La fenêtre du popup.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Gère l'action du bouton "Avec Ordonnance".
     * Affiche une information indiquant que la fonctionnalité n'est pas encore implémentée
     * et ferme le popup.
     * @param event L'événement d'action déclenché.
     */
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

    /**
     * Gère l'action du bouton "Sans Ordonnance".
     * Ferme le popup actuel et ouvre le formulaire client pour une vente sans ordonnance.
     * @param event L'événement d'action déclenché.
     */
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
    
    /**
     * Affiche une boîte de dialogue d'alerte.
     * @param type Le type d'alerte (ex: INFORMATION, ERROR).
     * @param title Le titre de la fenêtre d'alerte.
     * @param header Le texte d'en-tête de l'alerte.
     * @param content Le message principal de l'alerte.
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
