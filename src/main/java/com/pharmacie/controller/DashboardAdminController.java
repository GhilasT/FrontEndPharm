package com.pharmacie.controller;

/**
 * Contrôleur principal du tableau de bord administrateur.
 * 
 * Cette classe gère l'interface principale de l'administrateur, y compris:
 * - La navigation entre les différentes sections (Personnel, Fournisseurs, Médecins, etc.)
 * - Le changement de mode (Admin/Pharmacien)
 * - La déconnexion
 * 
 * Le tableau de bord permet d'accéder à toutes les fonctionnalités d'administration de la pharmacie
 * à travers une interface utilisateur organisée en modules fonctionnels.
 */
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

/**
 * Contrôleur pour le tableau de bord de l'administrateur.
 * Gère la navigation principale, les actions de l'utilisateur comme la déconnexion,
 * le changement de rôle et le chargement des différentes vues de gestion.
 */
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
    @FXML private Button btnLogoutAdmin;
    @FXML private Button btnSwitchToPharmacien;

    /**
     * Initialise le contrôleur après le chargement de son élément racine.
     * Configure les infobulles, les gestionnaires d'événements des boutons et les effets visuels.
     */
    @FXML
    private void initialize() {
        setupTooltips();
        setupButtonHandlers();
        setupButtonEffects();
    }

    /**
     * Configure les infobulles pour les boutons principaux du tableau de bord.
     */
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

    /**
     * Configure les gestionnaires d'événements pour les actions des boutons.
     * Associe chaque bouton à sa méthode de gestion correspondante.
     */
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

    /**
     * Configure les effets visuels (par exemple, changement de couleur au survol) pour les boutons.
     */
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

    /**
     * Applique un effet de survol à un bouton, changeant sa couleur de fond.
     * @param button Le bouton auquel appliquer l'effet.
     * @param defaultColor La couleur de fond par défaut.
     * @param hoverColor La couleur de fond au survol.
     */
    private void setupButtonHoverEffect(Button button, String defaultColor, String hoverColor) {
        button.setStyle(button.getStyle() + "; -fx-background-color: " + defaultColor + ";");
        
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle().replace(defaultColor, hoverColor));
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace(hoverColor, defaultColor));
        });
    }

    /**
     * Gère l'action de déconnexion de l'utilisateur.
     * Efface les informations de l'utilisateur connecté et retourne à l'écran de connexion.
     */
    private void handleLogout() {
        LoggedSeller.getInstance().clearUser();
        Stage stage = (Stage) contentArea.getScene().getWindow();
        stage.setScene(App.getLoginScene());
        ((Login) App.getLoginScene().getRoot()).clearFields();
    }

    /**
     * Gère le passage de l'interface administrateur à l'interface pharmacien.
     * Charge la vue du tableau de bord pharmacien.
     */
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

    /**
     * Affiche une boîte de dialogue d'alerte en cas d'erreur.
     * @param message Le message principal de l'alerte.
     * @param e L'exception qui a causé l'erreur (peut être null).
     */
    private void showErrorAlert(String message, Exception e) {
        e.printStackTrace();
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message + ": " + e.getMessage());
        alert.showAndWait();
    }

    /**
     * Retourne le conteneur principal (StackPane) où les différentes vues sont chargées.
     * @return Le StackPane de la zone de contenu.
     */
    public StackPane getContentArea() {
        return contentArea;
    }
    
    /**
     * Charge la vue de gestion du personnel dans la zone de contenu.
     */
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

    /**
     * Charge la vue de gestion des fournisseurs dans la zone de contenu.
     */
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

    /**
     * Charge la vue de gestion des médecins dans la zone de contenu.
     */
    public void loadGestionMedecins() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/GestionMedecinsAdmin.fxml"));
            Parent medecinsView = loader.load();
            
            GestionMedecinsAdminController controller = loader.getController();
            controller.setParentController(this);
            
            contentArea.getChildren().setAll(medecinsView);
        } catch (IOException ex) {
            showErrorAlert("Erreur lors du chargement de la gestion des médecins", ex);
        }
    }
    
    /**
     * Charge la vue de gestion des commandes dans la zone de contenu.
     */
    public void loadGestionCommandes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/GestionCommandesAdmin.fxml"));
            Parent commandesView = loader.load();
            
            GestionCommandesAdminController controller = loader.getController();
            controller.setParentController(this);
            
            contentArea.getChildren().setAll(commandesView);
        } catch (IOException ex) {
            showErrorAlert("Erreur lors du chargement de la gestion des commandes", ex);
        }
    }
    
    /**
     * Charge la vue de gestion des ventes dans la zone de contenu.
     */
    public void loadGestionVentes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/liste-ventes.fxml"));
            Parent ventesView = loader.load();
            
            contentArea.getChildren().setAll(ventesView);
        } catch (IOException ex) {
            showErrorAlert("Erreur lors du chargement de la gestion des ventes", ex);
        }
    }
    
    /**
     * Charge la vue de gestion des médicaments dans la zone de contenu.
     */
    public void loadGestionMedicaments() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/GestionMedicaments.fxml"));
            Parent medicamentsView = loader.load();
            
            GestionMedicamentsAdminController controller = loader.getController();
            controller.setParentController(this);
            
            contentArea.getChildren().setAll(medicamentsView);
        } catch (IOException ex) {
            showErrorAlert("Erreur lors du chargement de la gestion des médicaments", ex);
        }
    }

    /**
     * Affiche le panneau principal du tableau de bord dans la zone de contenu.
     * Utilisé pour revenir à la vue d'accueil du tableau de bord.
     */
    public void showDashboard() {
        contentArea.getChildren().setAll(dashboardPane);
    }
}