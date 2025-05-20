package com.pharmacie.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Contrôleur pour le composant d'en-tête d'administration.
 * Permet d'afficher un titre et un bouton de retour de manière uniforme
 * dans toutes les vues de l'interface d'administration.
 */
public class AdminHeaderController {

    @FXML private Button backButton;
    @FXML private Label headerLabel;
    
    private DashboardAdminController parentController;

    /**
     * Initialise le contrôleur après le chargement du FXML.
     */
    @FXML
    private void initialize() {
        // Initialisation du composant
    }
    
    /**
     * Définit le texte de l'en-tête.
     * @param text Le texte à afficher comme titre.
     */
    public void setHeaderText(String text) {
        headerLabel.setText(text);
    }
    
    /**
     * Définit le contrôleur parent pour permettre la navigation retour.
     * @param controller Le contrôleur parent (DashboardAdminController).
     */
    public void setParentController(DashboardAdminController controller) {
        this.parentController = controller;
    }
    
    /**
     * Gère l'action du bouton "Retour".
     * Revient à la vue principale du tableau de bord de l'administrateur.
     * @param event L'événement d'action.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        if (parentController != null) {
            parentController.showDashboard();
        }
    }
}
