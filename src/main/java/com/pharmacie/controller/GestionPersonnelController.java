package com.pharmacie.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

/**
 * Contrôleur pour la section de gestion du personnel.
 * Fournit des boutons pour naviguer vers les différentes sous-sections de gestion
 * (Administrateurs, Préparateurs, Pharmaciens Adjoints, Apprentis).
 */
public class GestionPersonnelController {
    
    @FXML private Button backButton;
    @FXML private Button GestPerso;
    @FXML private Button GestFournisseurs;
    @FXML private Button GestMedecins;
    @FXML private Button GestCommandes;
    
    private DashboardAdminController parentController;

    /**
     * Initialise le contrôleur après le chargement du FXML.
     * Configure les gestionnaires d'événements pour les boutons de navigation.
     */
    @FXML
    private void initialize() {
        setupButtonHandlers();
    }
    
    /**
     * Configure les actions à exécuter lorsque les boutons de navigation sont cliqués.
     * Chaque bouton charge la vue FXML correspondante dans la zone de contenu du parent.
     */
    private void setupButtonHandlers() {
        GestPerso.setOnAction(e -> handleAdminSection());
        GestFournisseurs.setOnAction(e -> handlePreparateursSection());
        GestMedecins.setOnAction(e -> handlePharmacienSection());
        GestCommandes.setOnAction(e -> handleApprentiSection());
    }
    
    /**
     * Gère la navigation vers la section de gestion des administrateurs.
     * Charge la vue GestionAdmin.fxml.
     */
    private void handleAdminSection() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/GestionAdmin.fxml"));
        Parent view = loader.load();
        GestionAdminController controller = loader.getController();
        controller.setParentController(parentController);
        parentController.getContentArea().getChildren().setAll(view);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    
    /**
     * Gère la navigation vers la section de gestion des préparateurs.
     * Charge la vue GestionPreparateur.fxml.
     */
private void handlePreparateursSection() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/GestionPreparateur.fxml"));
        Parent view = loader.load();
        GestionPreparateurController controller = loader.getController();
        controller.setParentController(parentController);
        parentController.getContentArea().getChildren().setAll(view);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    
    /**
     * Gère la navigation vers la section de gestion des pharmaciens adjoints.
     * Charge la vue GestionPharmacienAdjoint.fxml.
     */
private void handlePharmacienSection() { 
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/GestionPharmacienAdjoint.fxml"));
        Parent view = loader.load();
        GestionPharmacienAdjointController controller = loader.getController();
        controller.setParentController(parentController);
        parentController.getContentArea().getChildren().setAll(view);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    
    /**
     * Gère la navigation vers la section de gestion des apprentis.
     * Charge la vue GestionApprenti.fxml.
     */
private void handleApprentiSection() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/GestionApprenti.fxml"));
        Parent view = loader.load();
        GestionApprentiController controller = loader.getController();
        controller.setParentController(parentController);
        parentController.getContentArea().getChildren().setAll(view);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    /**
     * Définit le contrôleur parent (DashboardAdminController).
     * @param controller Le contrôleur parent.
     */
    public void setParentController(DashboardAdminController controller) {
        this.parentController = controller;
    }

    /**
     * Gère l'action du bouton "Retour".
     * Revient à la vue principale du tableau de bord de l'administrateur.
     */
    @FXML
    private void handleBack() {
        if(parentController != null) {
            parentController.showDashboard();
        }
    }
}