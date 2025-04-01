package com.pharmacie.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contrôleur pour modifier le PharmacyDashboard afin d'intégrer la vue des ventes
 */
public class PharmacyDashboardModifier {

    private static final Logger LOGGER = Logger.getLogger(PharmacyDashboardModifier.class.getName());

    /**
     * Modifie le comportement du bouton Ventes dans le PharmacyDashboard
     * 
     * @param dashboard Instance du PharmacyDashboard
     */
    public static void modifyDashboard(PharmacyDashboard dashboard) {
        try {
            // Récupérer le bouton des ventes par réflexion
            java.lang.reflect.Field btnSalesField = PharmacyDashboard.class.getDeclaredField("btnSales");
            btnSalesField.setAccessible(true);
            Button btnSales = (Button) btnSalesField.get(dashboard);
            
            // Modifier l'action du bouton
            btnSales.setOnAction(event -> {
                try {
                    // Mettre à jour le titre
                    java.lang.reflect.Method updateHeaderTitleMethod = PharmacyDashboard.class.getDeclaredMethod("updateHeaderTitle", String.class);
                    updateHeaderTitleMethod.setAccessible(true);
                    updateHeaderTitleMethod.invoke(dashboard, "Ventes");
                    
                    // Récupérer le contentPane
                    java.lang.reflect.Field contentPaneField = PharmacyDashboard.class.getDeclaredField("contentPane");
                    contentPaneField.setAccessible(true);
                    javafx.scene.layout.StackPane contentPane = (javafx.scene.layout.StackPane) contentPaneField.get(dashboard);
                    
                    // Charger la vue des ventes
                    FXMLLoader loader = new FXMLLoader(PharmacyDashboardModifier.class.getResource("/com/pharmacie/view/ventes.fxml"));
                    Parent ventesView = loader.load();
                    contentPane.getChildren().setAll(ventesView);
                    
                    // Fermer le menu si ouvert
                    java.lang.reflect.Field menuVisibleField = PharmacyDashboard.class.getDeclaredField("menuVisible");
                    menuVisibleField.setAccessible(true);
                    javafx.beans.property.BooleanProperty menuVisible = (javafx.beans.property.BooleanProperty) menuVisibleField.get(dashboard);
                    
                    if (menuVisible.get()) {
                        java.lang.reflect.Method toggleMenuMethod = PharmacyDashboard.class.getDeclaredMethod("toggleMenu");
                        toggleMenuMethod.setAccessible(true);
                        toggleMenuMethod.invoke(dashboard);
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du chargement de la vue des ventes", e);
                }
            });
            
            LOGGER.log(Level.INFO, "Modification du dashboard réussie");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la modification du dashboard", e);
        }
    }
}
