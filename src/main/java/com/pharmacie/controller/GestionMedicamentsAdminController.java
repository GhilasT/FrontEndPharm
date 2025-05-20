package com.pharmacie.controller;

import javafx.fxml.FXML;

/**
 * Contrôleur pour la vue de gestion des médicaments dans l'interface d'administration.
 * Adapte la vue standard des médicaments pour l'interface d'administration.
 */
public class GestionMedicamentsAdminController {

    @FXML private AdminHeaderController headerController;
    
    private DashboardAdminController parentController;
    
    /**
     * Initialise le contrôleur après le chargement du FXML.
     */
    @FXML
    private void initialize() {
        headerController.setHeaderText("Gestion des Médicaments");
    }
    
    /**
     * Définit le contrôleur parent pour permettre la navigation.
     * @param controller Le contrôleur parent (DashboardAdminController).
     */
    public void setParentController(DashboardAdminController controller) {
        this.parentController = controller;
        headerController.setParentController(controller);
    }
}
