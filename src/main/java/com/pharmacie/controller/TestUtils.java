package com.pharmacie.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilitaire pour les tests des fonctionnalités
 */
public class TestUtils {

    private static final Logger LOGGER = Logger.getLogger(TestUtils.class.getName());

    /**
     * Teste l'ouverture du popup de vente
     */
    public static void testPopupVente() {
        try {
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(TestUtils.class.getResource("/com/pharmacie/view/popup-vente.fxml"));
                    Parent root = loader.load();
                    
                    PopupVenteController controller = loader.getController();
                    
                    Stage stage = new Stage();
                    stage.setTitle("Test Popup Vente");
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    controller.setStage(stage);
                    stage.showAndWait();
                    
                    LOGGER.log(Level.INFO, "Test du popup de vente réussi");
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du test du popup de vente", e);
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du test du popup de vente", 
                            "Impossible d'ouvrir le popup: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du test du popup de vente", e);
        }
    }

    /**
     * Teste l'ouverture du formulaire client
     */
    public static void testFormulaireClient() {
        try {
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(TestUtils.class.getResource("/com/pharmacie/view/formulaire-client.fxml"));
                    Parent root = loader.load();
                    
                    Stage stage = new Stage();
                    stage.setTitle("Test Formulaire Client");
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                    
                    LOGGER.log(Level.INFO, "Test du formulaire client réussi");
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du test du formulaire client", e);
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du test du formulaire client", 
                            "Impossible d'ouvrir le formulaire: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du test du formulaire client", e);
        }
    }

    /**
     * Teste l'ouverture de la page de recherche de médicaments
     */
    public static void testRechercheMedicaments() {
        try {
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(TestUtils.class.getResource("/com/pharmacie/view/recherche-medicaments.fxml"));
                    Parent root = loader.load();
                    
                    RechercheMedicamentsController controller = loader.getController();
                    controller.setClientInfo(java.util.UUID.randomUUID(), "Nom Test", "Prénom Test", "0123456789", "test@example.com", "Adresse Test");
                    
                    Stage stage = new Stage();
                    stage.setTitle("Test Recherche Médicaments");
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                    
                    LOGGER.log(Level.INFO, "Test de la recherche de médicaments réussi");
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du test de la recherche de médicaments", e);
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du test de la recherche de médicaments", 
                            "Impossible d'ouvrir la page: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du test de la recherche de médicaments", e);
        }
    }

    /**
     * Teste l'ouverture de la page des ventes
     */
    public static void testPageVentes() {
        try {
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(TestUtils.class.getResource("/com/pharmacie/view/ventes.fxml"));
                    Parent root = loader.load();
                    
                    Stage stage = new Stage();
                    stage.setTitle("Test Page Ventes");
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                    
                    LOGGER.log(Level.INFO, "Test de la page des ventes réussi");
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du test de la page des ventes", e);
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du test de la page des ventes", 
                            "Impossible d'ouvrir la page: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du test de la page des ventes", e);
        }
    }

    /**
     * Exécute tous les tests
     */
    public static void runAllTests() {
        try {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Tests des fonctionnalités");
                alert.setHeaderText("Voulez-vous exécuter tous les tests?");
                alert.setContentText("Cela va ouvrir successivement toutes les fenêtres pour tester les fonctionnalités.");
                
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    testPageVentes();
                    testPopupVente();
                    testFormulaireClient();
                    testRechercheMedicaments();
                    
                    showAlert(Alert.AlertType.INFORMATION, "Tests terminés", "Tests terminés", 
                            "Tous les tests ont été exécutés avec succès.");
                }
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'exécution des tests", e);
        }
    }

    private static void showAlert(Alert.AlertType type, String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}
