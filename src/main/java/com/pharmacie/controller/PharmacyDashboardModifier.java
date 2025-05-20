package com.pharmacie.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilitaire pour modifier le comportement du {@link PharmacyDashboard}.
 * Permet notamment de redéfinir l'action du bouton "Ventes" et de configurer
 * l'apparence du bouton "Médecins" (anciennement "Fournisseurs").
 */
public class PharmacyDashboardModifier {

    private static final Logger LOGGER = Logger.getLogger(PharmacyDashboardModifier.class.getName());

    /**
     * Modifie le comportement du bouton Ventes dans le PharmacyDashboard et ajuste le bouton Médecins.
     * Utilise la réflexion pour accéder aux composants privés du {@link PharmacyDashboard}.
     * L'action du bouton "Ventes" est modifiée pour charger la vue des ventes.
     * Le bouton "Fournisseurs" est reconfiguré pour afficher "Médecins" avec une icône appropriée.
     * 
     * @param dashboard L'instance du {@link PharmacyDashboard} à modifier.
     */
    public static void modifyDashboard(PharmacyDashboard dashboard) {
        try {
            // Récupérer le bouton des ventes par réflexion
            java.lang.reflect.Field btnSalesField = PharmacyDashboard.class.getDeclaredField("btnSales");
            btnSalesField.setAccessible(true);
            Button btnSales = (Button) btnSalesField.get(dashboard);
            
            // Récupérer également le bouton des fournisseurs pour s'assurer qu'il est correctement configuré
            java.lang.reflect.Field btnSuppliersField = PharmacyDashboard.class.getDeclaredField("btnSuppliers");
            btnSuppliersField.setAccessible(true);
            Button btnSuppliers = (Button) btnSuppliersField.get(dashboard);
            
            // S'assurer que le bouton fournisseurs a le texte "Médecins" et la bonne icône
            HBox medecinsContent = new HBox(10);
            medecinsContent.setAlignment(Pos.CENTER_LEFT);
            
            // Essayer de charger l'icône
            try {
                InputStream iconStream = PharmacyDashboardModifier.class.getResourceAsStream("/com/pharmacie/images/Icones/medecins.png");
                if (iconStream != null) {
                    ImageView icon = new ImageView(new Image(iconStream));
                    icon.setFitHeight(24);
                    icon.setFitWidth(24);
                    icon.setPreserveRatio(true);
                    medecinsContent.getChildren().add(icon);
                } else {
                    medecinsContent.getChildren().add(new Label("•"));
                }
            } catch (Exception e) {
                medecinsContent.getChildren().add(new Label("•"));
            }
            
            Label medecinsLabel = new Label("Médecins");
            medecinsLabel.setTextFill(javafx.scene.paint.Color.WHITE);
            medecinsLabel.setFont(Font.font("Open Sans Semibold", 20));
            medecinsLabel.setWrapText(true);
            
            medecinsContent.getChildren().add(medecinsLabel);
            btnSuppliers.setGraphic(medecinsContent);
            btnSuppliers.setText("");
            btnSuppliers.setStyle("-fx-background-color: transparent; -fx-padding: 0 0 0 20;");
            
            // Modifier l'action du bouton des ventes
            btnSales.setOnAction(event -> {
                try {
                    // Mettre à jour le titre
                    java.lang.reflect.Method updateHeaderTitleMethod = PharmacyDashboard.class.getDeclaredMethod("updateHeaderTitle", String.class);
                    updateHeaderTitleMethod.setAccessible(true);
                    updateHeaderTitleMethod.invoke(dashboard, "Ventes");
                    
                    // Récupérer le contentPane
                    java.lang.reflect.Field contentPaneField = PharmacyDashboard.class.getDeclaredField("contentPane");
                    contentPaneField.setAccessible(true);
                    StackPane contentPane = (StackPane) contentPaneField.get(dashboard);
                    
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
