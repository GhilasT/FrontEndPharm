package com.pharmacie.controller;

import com.pharmacie.model.AnalyseVenteData;
import com.pharmacie.service.AnalyseVenteApi;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class AnalyseVentesController {
    @FXML
    private LineChart<String, Number> ventesChart;
    @FXML
    private LineChart<String, Number> caChart;
    @FXML
    private Spinner<Integer> nbMoisSpinner;

    @FXML
    public void initialize() {
        nbMoisSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 24, 6));
        handleSemaine(); // Par défaut
    }

    private void updateCharts(Map<LocalDate, AnalyseVenteData> data) {
        // Réinitialiser les graphiques
        ventesChart.getData().clear();
        caChart.getData().clear();
        
        // Débogage - vérifier les données
        System.out.println("Mise à jour des graphiques avec " + data.size() + " entrées");
        
        if (data.isEmpty()) {
            System.out.println("Attention: Aucune donnée à afficher!");
            return;
        }
    
        // Créer les séries
        XYChart.Series<String, Number> ventesSeries = new XYChart.Series<>();
        ventesSeries.setName("Ventes");
        
        XYChart.Series<String, Number> commandesSeries = new XYChart.Series<>();
        commandesSeries.setName("Commandes");
        
        XYChart.Series<String, Number> caSeries = new XYChart.Series<>();
        caSeries.setName("Chiffre d'Affaires");
    
        // Remplir les séries
        data.forEach((date, values) -> {
            String label = date.format(DateTimeFormatter.ofPattern("dd/MM"));
            ventesSeries.getData().add(new XYChart.Data<>(label, values.getVentes()));
            commandesSeries.getData().add(new XYChart.Data<>(label, values.getCommandes()));
            caSeries.getData().add(new XYChart.Data<>(label, values.getChiffreAffaire()));
        });
    
        // Ajouter les séries aux graphiques
        ventesChart.getData().addAll(ventesSeries, commandesSeries);
        caChart.getData().add(caSeries);
    
        // Forcer un rafraîchissement visuel
        ventesChart.setAnimated(false);
        ventesChart.setAnimated(true);
        caChart.setAnimated(false);
        caChart.setAnimated(true);
    }

    @FXML
    private void handleSemaine() {
        try {
            updateCharts(AnalyseVenteApi.getVentesSemaine());
        } catch (Exception e) {
            showError("Erreur lors du chargement des données hebdomadaires");
        }
    }

    @FXML
    private void handleMois() {
        try {
            updateCharts(AnalyseVenteApi.getVentesMoisJour());
        } catch (Exception e) {
            showError("Erreur lors du chargement des données mensuelles");
        }
    }

    @FXML
    private void handleSixMois() {
        try {
            updateCharts(AnalyseVenteApi.getHistoriqueVentes(6));
        } catch (Exception e) {
            showError("Erreur lors du chargement des données historiques");
        }
    }

    @FXML
    private void handleAnnee() {
        try {
            updateCharts(AnalyseVenteApi.getHistoriqueVentes(12));
        } catch (Exception e) {
            showError("Erreur lors du chargement des données annuelles");
        }
    }

    @FXML
    private void handlePersonnalise() {
        int nbMois = nbMoisSpinner.getValue();
        try {
            updateCharts(AnalyseVenteApi.getHistoriqueVentes(nbMois));
        } catch (Exception e) {
            showError("Erreur lors du chargement des données personnalisées");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}