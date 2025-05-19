package com.pharmacie.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import com.pharmacie.App;
import com.pharmacie.util.LoggedSeller;

public class DashboardAdminController {

    // Composants de l'interface utilisateur
    @FXML private StackPane contentArea; // Zone principale pour afficher le contenu
    @FXML private Pane dashboardPane; // Vue par défaut du tableau de bord
    @FXML private Button GestPerso;
    @FXML private Button GestFournisseurs;
    @FXML private Button GestMedecins;
    @FXML private Button GestCommandes;
    @FXML private Button GestVentes;
    @FXML private Button GestAutorisations;
    @FXML private Button GestStocks;
    @FXML private Button GestMedicaments;
    @FXML private Button btnLogoutAdmin;
    @FXML private Button btnSwitchToPharmacien;

    // Méthode d'initialisation appelée automatiquement par JavaFX
    @FXML
    private void initialize() {
        setupTooltips();        // Configure les infobulles
        setupButtonHandlers();  // Lie les boutons aux actions
        setupButtonEffects();   // Applique des effets de survol
    }

    // Ajoute des infobulles explicatives aux boutons
    private void setupTooltips() {
        Tooltip.install(btnSwitchToPharmacien, new Tooltip("Basculer vers l'interface Pharmacien"));
        Tooltip.install(btnLogoutAdmin, new Tooltip("Se déconnecter de l'application"));

        Tooltip.install(GestPerso, new Tooltip("Gérer le personnel de la pharmacie"));
        Tooltip.install(GestFournisseurs, new Tooltip("Gérer les fournisseurs de la pharmacie"));
        Tooltip.install(GestMedecins, new Tooltip("Gérer les médecins partenaires"));
        Tooltip.install(GestCommandes, new Tooltip("Gérer les commandes"));
        Tooltip.install(GestVentes, new Tooltip("Consulter et gérer les ventes"));
        Tooltip.install(GestAutorisations, new Tooltip("Gérer les autorisations utilisateurs"));
        Tooltip.install(GestStocks, new Tooltip("Gérer les stocks de médicaments"));
        Tooltip.install(GestMedicaments, new Tooltip("Gérer le catalogue de médicaments"));
    }

    // Attribue les actions aux différents boutons
    private void setupButtonHandlers() {
        btnLogoutAdmin.setOnAction(e -> handleLogout());
        btnSwitchToPharmacien.setOnAction(e -> handleSwitchToPharmacien());
        GestPerso.setOnAction(e -> loadGestionPersonnel());
        GestFournisseurs.setOnAction(e -> loadGestionFournisseurs());
        GestMedecins.setOnAction(e -> loadGestionMedecins());
        GestCommandes.setOnAction(e -> loadGestionCommandes());
        GestVentes.setOnAction(e -> loadGestionVentes());
        GestMedicaments.setOnAction(e -> loadGestionMedicaments());
    }

    // Applique des effets de changement de couleur au survol des boutons
    private void setupButtonEffects() {
        setupButtonHoverEffect(btnSwitchToPharmacien, "#1F82F2", "#2196F3");
        setupButtonHoverEffect(btnLogoutAdmin, "#E74C3C", "#C0392B");

        setupButtonHoverEffect(GestPerso, "#1F82F2", "#2196F3");
        setupButtonHoverEffect(GestFournisseurs, "#1F82F2", "#2196F3");
        setupButtonHoverEffect(GestMedecins, "#1F82F2", "#2196F3");
        setupButtonHoverEffect(GestCommandes, "#1F82F2", "#2196F3");
        setupButtonHoverEffect(GestVentes, "#1F82F2", "#2196F3");
        setupButtonHoverEffect(GestAutorisations, "#1F82F2", "#2196F3");
        setupButtonHoverEffect(GestStocks, "#1F82F2", "#2196F3");
        setupButtonHoverEffect(GestMedicaments, "#1F82F2", "#2196F3");
    }

    // Gère les effets de survol (hover) sur un bouton
    private void setupButtonHoverEffect(Button button, String defaultColor, String hoverColor) {
        button.setStyle(button.getStyle() + "; -fx-background-color: " + defaultColor + ";");

        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle().replace(defaultColor, hoverColor));
        });

        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace(hoverColor, defaultColor));
        });
    }

    // Gère la déconnexion de l'utilisateur
    private void handleLogout() {
        LoggedSeller.getInstance().clearUser();
        Stage stage = (Stage) contentArea.getScene().getWindow();
        stage.setScene(App.getLoginScene());
        ((Login) App.getLoginScene().getRoot()).clearFields();
    }

    // Bascule vers le tableau de bord pharmacien
    private void handleSwitchToPharmacien() {
        try {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), contentArea);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setOnFinished(event -> {
                try {
                    PharmacyDashboard dashboard = new PharmacyDashboard();

                    Scene newScene = new Scene(dashboard);
                    Stage stage = (Stage) contentArea.getScene().getWindow();
                    stage.setScene(newScene);
                    stage.setTitle("Dashboard Pharmacien - " + LoggedSeller.getInstance().getNomComplet());

                    PharmacyDashboardModifier.modifyDashboard(dashboard);
                    dashboard.refreshUserInfo();
                    
                } catch (Exception e) {
                    showErrorAlert("Impossible de basculer vers l'interface Pharmacien", e);
                }
            });

            fadeOut.play();
            
        } catch (Exception e) {
            showErrorAlert("Erreur lors du changement d'interface", e);
        }
    }

    // Affiche une boîte de dialogue d'erreur
    private void showErrorAlert(String message, Exception e) {
        e.printStackTrace();
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message + ": " + e.getMessage());
        alert.showAndWait();
    }

    // Retourne le composant principal d'affichage
    public StackPane getContentArea() {
        return contentArea;
    }

    // Charge la vue de gestion du personnel
    public void loadGestionPersonnel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/GestionPersonnel.fxml"));
            Parent gestionPersonnelView = loader.load();

            GestionPersonnelController controller = loader.getController();
            controller.setParentController(this);

            contentArea.getChildren().setAll(gestionPersonnelView);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Charge la vue de gestion des fournisseurs
    public void loadGestionFournisseurs() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/GestionFournisseur.fxml"));
            Parent gestionFournisseurView = loader.load();

            GestionFournisseurController controller = loader.getController();
            controller.setParentController(this);

            contentArea.getChildren().setAll(gestionFournisseurView);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Charge la vue de gestion des médecins
    public void loadGestionMedecins() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/MedecinsPage.fxml"));
            Parent medecinsView = loader.load();

            contentArea.getChildren().setAll(medecinsView);
        } catch (IOException ex) {
            showErrorAlert("Erreur lors du chargement de la gestion des médecins", ex);
        }
    }

    // Charge la vue de gestion des commandes
    public void loadGestionCommandes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/GestionCommande.fxml"));
            Parent commandesView = loader.load();

            contentArea.getChildren().setAll(commandesView);
        } catch (IOException ex) {
            showErrorAlert("Erreur lors du chargement de la gestion des commandes", ex);
        }
    }

    // Charge la vue de gestion des ventes
    public void loadGestionVentes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/liste-ventes.fxml"));
            Parent ventesView = loader.load();

            contentArea.getChildren().setAll(ventesView);
        } catch (IOException ex) {
            showErrorAlert("Erreur lors du chargement de la gestion des ventes", ex);
        }
    }

    // Charge la vue de gestion des médicaments
    public void loadGestionMedicaments() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/médicaments.fxml"));
            Parent medicamentsView = loader.load();
            
            contentArea.getChildren().setAll(medicamentsView);
        } catch (IOException ex) {
            showErrorAlert("Erreur lors du chargement de la gestion des médicaments", ex);
        }
    }

    // Réaffiche le tableau de bord initial
    public void showDashboard() {
        contentArea.getChildren().setAll(dashboardPane);
    }
}
