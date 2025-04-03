package com.pharmacie.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GestionPersonnelController {
    
    @FXML private Button backButton;
    @FXML private Button GestPerso;
    @FXML private Button GestFournisseurs;
    @FXML private Button GestMedecins;
    @FXML private Button GestCommandes;
    
    private DashboardAdminController parentController;

    @FXML
    private void initialize() {
        setupButtonHandlers();
    }
    
    private void setupButtonHandlers() {
        GestPerso.setOnAction(e -> handleAdminSection());
        GestFournisseurs.setOnAction(e -> handlePreparateursSection());
        GestMedecins.setOnAction(e -> handlePharmacienSection());
        GestCommandes.setOnAction(e -> handleApprentiSection());
    }
    
    // Méthodes pour gérer les différentes sections de personnel
    private void handleAdminSection() {
        System.out.println("Gestion des administrateurs sélectionnée");
        // Implémentez ici la logique pour la gestion des administrateurs
    }
    
    private void handlePreparateursSection() {
        System.out.println("Gestion des préparateurs sélectionnée");
        // Implémentez ici la logique pour la gestion des préparateurs
    }
    
    private void handlePharmacienSection() {
        System.out.println("Gestion des pharmaciens sélectionnée");
        // Implémentez ici la logique pour la gestion des pharmaciens
    }
    
    private void handleApprentiSection() {
        System.out.println("Gestion des apprentis sélectionnée");
        // Implémentez ici la logique pour la gestion des apprentis
    }

    public void setParentController(DashboardAdminController controller) {
        this.parentController = controller;
    }

    @FXML
    private void handleBack() {
        if(parentController != null) {
            parentController.showDashboard();
        }
    }
}