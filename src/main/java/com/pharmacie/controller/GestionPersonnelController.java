package com.pharmacie.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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