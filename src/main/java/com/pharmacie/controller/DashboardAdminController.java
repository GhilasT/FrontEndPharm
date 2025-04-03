package com.pharmacie.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class DashboardAdminController {

    @FXML private StackPane contentArea;
    @FXML private Pane dashboardPane;
    @FXML private Button GestPerso;
    @FXML private Button GestFournisseurs;
    @FXML private Button GestMedecins;
    @FXML private Button GestCommandes;
    @FXML private Button GestVentes;
    @FXML private Button GestAutorisations;
    @FXML private Button GestStocks;
    @FXML private Button GestMedicaments;

    @FXML
    private void initialize() {
        setupButtonHandlers();
    }

    private void setupButtonHandlers() {
        GestPerso.setOnAction(e -> loadGestionPersonnel());
        // Ajoutez ici les gestionnaires pour les autres boutons
    }

    private void loadGestionPersonnel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/GestionPersonnel.fxml"));
            Parent gestionPersonnelView = loader.load();
            
            // Transfère la référence au contrôleur parent
            GestionPersonnelController controller = loader.getController();
            controller.setParentController(this);
            
            // Remplace le contenu du StackPane par la vue de gestion du personnel
            contentArea.getChildren().setAll(gestionPersonnelView);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void showDashboard() {
        // Remet le dashboardPane comme seul enfant du contentArea
        contentArea.getChildren().setAll(dashboardPane);
    }
}