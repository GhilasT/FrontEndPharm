package com.pharmacie.controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pharmacie.App;
import com.pharmacie.model.Dashboard;
import com.pharmacie.service.ApiRest;
import com.pharmacie.util.LoggedSeller;
import com.pharmacie.util.ResourceLoader;

/**
 * Contrôleur principal du tableau de bord de la pharmacie.
 * Gère la navigation, l'affichage des différentes sections (tableau de bord, ventes, médicaments, etc.),
 * l'authentification de l'utilisateur et le passage en mode administrateur.
 */
public class PharmacyDashboard extends StackPane {
    private static final int MENU_WIDTH = 300;
    private static final int CARD_WIDTH = 246;
    private static final int CARD_HEIGHT = 150;
    private static final int CARD_SPACING = 20;
    private static final double MINIMUM_WINDOW_WIDTH = 800;

    @FXML
    private Button menuButton;
    @FXML
    private VBox sideMenu;
    @FXML
    private StackPane contentPane;
    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnSales;
    @FXML
    private Button btnMedics;
    @FXML
    private Button btnCommandes;
    @FXML
    private Button btnSuppliers; // Changed back to btnSuppliers to match FXML
    @FXML
    private Button btnAnalytics;
    @FXML
    private Label headerTitle;
    @FXML
    private Label userLabel;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnSwitchToAdmin; // New button for switching to admin mode
    @FXML
    private HBox topBar; // Ajout de la référence à la barre supérieure
    
    // Nouvelle propriété pour garantir un espacement adéquat
    private final double HEADER_SPACING = 20.0;

    private BooleanProperty menuVisible = new SimpleBooleanProperty(false);
    private TilePane dashboardTilePane;

    /**
     * Constructeur du PharmacyDashboard.
     * Charge le fichier FXML associé et initialise les composants.
     */
    public PharmacyDashboard() {
        loadFXML();
        initializeComponents();
        initialize();
    }

    /**
     * Charge le fichier FXML pour ce composant.
     * Définit cette instance comme racine et contrôleur du FXML.
     */
    private void loadFXML() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/Dashboard1.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
            
            // Repositionner le menu pour qu'il soit toujours visible au-dessus
            getChildren().remove(sideMenu);
            getChildren().add(sideMenu);
            
            // S'assurer que le sideMenu est bien positionné
            StackPane.setAlignment(sideMenu, Pos.CENTER_LEFT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialise les composants et les fonctionnalités du tableau de bord.
     * Configure les boutons du menu, les actions, le bouton de déconnexion,
     * le bouton de mode administrateur, charge le tableau de bord initial,
     * et met en place la gestion responsive de la fenêtre.
     */
    public void initialize() {
        configureMenuButtons();
        menuButton.setOnAction(event -> toggleMenu());
        setupMenuActions();
        btnLogout.setOnAction(event -> handleLogout());
        configureMenuButton(btnLogout, "logout.png", "Déconnexion");
        
        // Configure the Admin Mode button
        configureAdminButton();
        
        loadDashboard();
        userLabel.setText(LoggedSeller.getInstance().getNomComplet());

        // Ajouter les listeners pour le redimensionnement
        widthProperty().addListener((obs, oldVal, newVal) -> handleWindowResize());
        heightProperty().addListener((obs, oldVal, newVal) -> handleWindowResize());

        // Configuration initiale responsive
        handleWindowResize();
        
        // Configurer le menu comme une couche indépendante
        setupIndependentMenu();
    }

    /**
     * Configure la visibilité et l'action du bouton de passage en mode administrateur.
     * Le bouton est visible et son action dépend du rôle de l'utilisateur connecté.
     * Si l'utilisateur est administrateur, il peut accéder au tableau de bord admin.
     * Sinon, une alerte d'accès restreint est affichée.
     */
    private void configureAdminButton() {
        btnSwitchToAdmin.setVisible(true);
        
        // Ensure button width is properly set
        btnSwitchToAdmin.setPrefWidth(200);
        btnSwitchToAdmin.setMinWidth(200);
        
        // Set the button action based on user role
        boolean isAdmin = "ADMINISTRATEUR".equals(LoggedSeller.getInstance().getRole());
        
        // Add hover effect with blue colors
        setupButtonHoverEffect(btnSwitchToAdmin, "#2E86C1", "#3498DB");
        
        btnSwitchToAdmin.setOnAction(event -> {
            if (isAdmin) {
                // Admin user can access admin dashboard
                handleSwitchToAdmin();
            } else {
                // Non-admin users see an error message
                showAlert(Alert.AlertType.WARNING,
                        "Accès restreint",
                        "Autorisation requise",
                        "Vous devez avoir le rôle Administrateur pour accéder à cette page.");
            }
        });
    }
    
    /**
     * Gère le passage vers le tableau de bord administrateur.
     * Effectue une transition de fondu avant de charger la vue du tableau de bord admin.
     */
    private void handleSwitchToAdmin() {
        try {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), this);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            
            fadeOut.setOnFinished(event -> {
                try {
                    // Load the Admin dashboard
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/DashboardAdmin.fxml"));
                    Parent adminView = loader.load();
                    
                    Scene newScene = new Scene(adminView);
                    Stage stage = (Stage) getScene().getWindow();
                    stage.setScene(newScene);
                    stage.setTitle("Dashboard Admin - " + LoggedSeller.getInstance().getNomComplet());
                    
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de basculer vers l'interface Admin", 
                              "Détails: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            
            fadeOut.play();
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du changement d'interface", 
                      "Détails: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Configure le menu latéral pour qu'il soit indépendant du layout principal.
     * Cela évite les recalculs de mise en page lors de l'affichage/masquage du menu.
     */
    private void setupIndependentMenu() {
        // S'assurer que le menu est au premier plan
        sideMenu.setViewOrder(-10);
        
        // Désactiver les écouteurs de menuVisible pour updateDashboardLayout
        menuVisible.addListener((obs, oldVal, newVal) -> {
            // Ne rien faire ici - éviter les effets secondaires
        });
    }

    /**
     * Gère le redimensionnement de la fenêtre pour ajuster les éléments de l'interface utilisateur.
     * Ajuste la visibilité du menu et met à jour la disposition du tableau de bord si nécessaire.
     * Ajuste également la taille des polices.
     */
    private void handleWindowResize() {
        double width = getWidth();
        double height = getHeight();

        // Ajuster les composants si la fenêtre est trop petite
        if (width < MINIMUM_WINDOW_WIDTH) {
            if (menuVisible.get()) {
                toggleMenu(); // Fermer le menu si la fenêtre est trop petite
            }
            menuButton.setDisable(false);
        }

        // Mettre à jour le tableau de bord s'il est actuellement affiché
        if (dashboardTilePane != null && dashboardTilePane.getParent() == contentPane) {
            updateDashboardLayout();
        }

        // Ajuster la taille de police selon la largeur de la fenêtre
        adjustFontSizes(width);
    }

    /**
     * Ajuste la taille des polices des éléments textuels en fonction de la largeur de la fenêtre.
     * @param windowWidth La largeur actuelle de la fenêtre.
     */
    private void adjustFontSizes(double windowWidth) {
        double scaleFactor = Math.max(0.7, Math.min(1.0, windowWidth / 1280.0));

        headerTitle.setStyle(String.format("-fx-font-size: %.1fpx;", 24 * scaleFactor));
        userLabel.setStyle(String.format("-fx-font-size: %.1fpx;", 22 * scaleFactor));
    }

    /**
     * Vérifie si le rôle de l'utilisateur connecté est autorisé.
     * Actuellement, les rôles autorisés sont "PHARMACIEN_ADJOINT" et "APPRENTI".
     * @return true si le rôle est autorisé, false sinon.
     */
    private boolean isAuthorizedRole() {
        String role = LoggedSeller.getInstance().getRole();
        return "PHARMACIEN_ADJOINT".equals(role) || "APPRENTI".equals(role);
    }

    /**
     * Rafraîchit les informations de l'utilisateur affichées (nom complet).
     * Reconfigure également la visibilité du bouton de mode administrateur.
     */
    public void refreshUserInfo() {
        userLabel.setText(LoggedSeller.getInstance().getNomComplet());
        
        // Refresh admin button visibility when user info changes
        configureAdminButton();
    }

    /**
     * Affiche un message d'accès non autorisé dans la zone de contenu.
     * Cache le menu latéral et le bouton de menu.
     */
    private void showAccessDenied() {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(new Label("Accès non autorisé"));
        sideMenu.setVisible(false);
        menuButton.setVisible(false);
    }

    /**
     * Configure l'apparence d'un bouton du menu avec une icône et un texte.
     * @param button Le bouton à configurer.
     * @param iconName Le nom du fichier de l'icône (situé dans les ressources).
     * @param text Le texte à afficher sur le bouton.
     */
    private void configureMenuButton(Button button, String iconName, String text) {
        HBox content = new HBox(10);
        content.setAlignment(Pos.CENTER_LEFT);

        Image iconImage = ResourceLoader.loadImage("images/Icones/" + iconName);
        if (iconImage != null) {
            ImageView icon = new ImageView(iconImage);
            icon.setFitHeight(24);
            icon.setFitWidth(24);
            icon.setPreserveRatio(true);
            content.getChildren().add(icon);
        } else {
            System.out.println("Icône non trouvée: " + iconName);
            content.getChildren().add(new Label("•"));
        }

        Label label = new Label(text);
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        label.setFont(Font.font("Open Sans Semibold", 20));
        label.setWrapText(true);

        content.getChildren().add(label);
        button.setGraphic(content);
        button.setText("");
        button.setStyle("-fx-background-color: transparent; -fx-padding: 0 0 0 20;");
    }

    /**
     * Configure tous les boutons du menu latéral.
     * Appelle {@link #configureMenuButton(Button, String, String)} pour chaque bouton.
     */
    private void configureMenuButtons() {
        configureMenuButton(btnDashboard, "dashboard.png", "Tableau de Bord");
        configureMenuButton(btnSales, "ventes.png", "Ventes");
        configureMenuButton(btnMedics, "médicaments.png", "Médicaments");
        configureMenuButton(btnCommandes, "gestionCommande.png", "Commandes");
        configureMenuButton(btnSuppliers, "medecins.png", "Médecins"); // Use btnSuppliers but with médecins text
        configureMenuButton(btnAnalytics, "analyseventes.png", "Analyse des ventes");
    }

    /**
     * Gère la déconnexion de l'utilisateur.
     * Efface les informations de l'utilisateur connecté et retourne à l'écran de connexion.
     */
    private void handleLogout() {
        LoggedSeller.getInstance().clearUser();
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(App.getLoginScene());
        ((Login) App.getLoginScene().getRoot()).clearFields();
    }

    /**
     * Configure les actions associées à chaque bouton du menu latéral.
     * Chaque bouton charge une vue ou une fonctionnalité spécifique.
     */
    private void setupMenuActions() {
        btnDashboard.setOnAction(event -> {
            setActiveButton(btnDashboard);
            loadDashboard();
        });

        btnSales.setOnAction(event -> {
            setActiveButton(btnSales);
            loadContent("Ventes");
        });

        btnMedics.setOnAction(event -> {
            setActiveButton(btnMedics);
            loadContent("Médicaments");
        });

        btnCommandes.setOnAction(event -> {
            setActiveButton(btnCommandes);
            loadContent("Commandes");
        });

        btnSuppliers.setOnAction(event -> {
            setActiveButton(btnSuppliers);
            loadContent("Médecins"); // Changed from Fournisseurs to Médecins
        });

        btnAnalytics.setOnAction(event -> {
            setActiveButton(btnAnalytics);
            loadAnalytics();
        });
    }

    /**
     * Charge et affiche la vue principale du tableau de bord (avec les cartes d'indicateurs).
     * Récupère les données du tableau de bord via l'API et crée les cartes correspondantes.
     */
    private void loadDashboard() {
        setActiveButton(btnDashboard);
        updateHeaderTitle("Tableau de Bord");

        contentPane.getChildren().clear();

        dashboardTilePane = new TilePane();
        dashboardTilePane.setPadding(new Insets(20));
        dashboardTilePane.setHgap(CARD_SPACING);
        dashboardTilePane.setVgap(CARD_SPACING);

        // Configuration responsive du TilePane
        dashboardTilePane.prefWidthProperty().bind(contentPane.widthProperty());
        dashboardTilePane.prefHeightProperty().bind(contentPane.heightProperty());

        // Calculer dynamiquement le nombre de colonnes basé uniquement sur la largeur
        // sans tenir compte du menu
        dashboardTilePane.prefColumnsProperty().bind(
                Bindings.createIntegerBinding(
                        () -> calculateColumns(),
                        contentPane.widthProperty() // menuVisible supprimé ici
                ));

        String[] titles = { "CA", "Bénéfices", "Employés", "Clients", "Médecins", "Médicaments",
                "Médicaments en rupture de stock", "Médicaments périmés", "Médicaments Stock Faible",
                "Médicaments péremption - 1 mois" };
        List<String> values = new ArrayList<>();
        String[] colors = { "#1F82F2", "#1F82F2", "#E74C3C", "#9B59B6",
                "#F39C12", "#16A085", "#34495E", "#7F8C8D", "#E74C3C", "#16A085" };

        try {
            Dashboard dashboardValues = ApiRest.getDashboardRequest();
            values.addAll(List.of(
                    String.format("%.2f", dashboardValues.getCA()),
                    String.format("%.2f", dashboardValues.getBenefices()),
                    String.valueOf(dashboardValues.getNbEmployes()),
                    String.valueOf(dashboardValues.getNbClients()),
                    String.valueOf(dashboardValues.getNbMedecins()),
                    String.valueOf(dashboardValues.getNbMedicaments()),
                    String.valueOf(dashboardValues.getNbMedicamentsRuptureStock()),
                    String.valueOf(dashboardValues.getNbMedicamentsPerimes()),
                    String.valueOf(dashboardValues.getNbMedicamentsAlerte()),
                    String.valueOf(dashboardValues.getNbMedicamentsAlerteBientotPerimee())));

            for (int i = 0; i < titles.length; i++) {
                dashboardTilePane.getChildren().add(
                        createCard(titles[i], values.get(i), colors[i]));
            }

            contentPane.getChildren().setAll(dashboardTilePane);
            updateDashboardLayout();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erreur",
                    "Recherche impossible",
                    "Erreur serveur : " + e.getMessage());
        }

        if (menuVisible.get() && getWidth() < MINIMUM_WINDOW_WIDTH)
            toggleMenu();
    }

    /**
     * Met à jour la disposition du tableau de bord (TilePane) pour s'adapter à la taille
     * actuelle de la zone de contenu et à l'état du menu.
     * Ajuste le nombre de colonnes et la taille des cartes.
     */
    private void updateDashboardLayout() {
        if (dashboardTilePane == null)
            return;

        double availableWidth = contentPane.getWidth();
        // Prendre en compte le padding du contentPane qui est affecté par l'état du menu
        
        int columns = Math.max(1, (int) Math.floor((availableWidth - 40) / (CARD_WIDTH + CARD_SPACING)));

        // Ajuster la taille des cartes si l'espace est réduit
        if (columns == 1 || availableWidth / columns < CARD_WIDTH + CARD_SPACING) {
            double newCardWidth = Math.max(180, availableWidth / columns - CARD_SPACING - 10);
            dashboardTilePane.getChildren().forEach(card -> {
                if (card instanceof Region) {
                    ((Region) card).setPrefWidth(newCardWidth);
                    ((Region) card).setMaxWidth(newCardWidth);
                }
            });
        } else {
            // Revenir à la taille standard
            dashboardTilePane.getChildren().forEach(card -> {
                if (card instanceof Region) {
                    ((Region) card).setPrefWidth(CARD_WIDTH);
                    ((Region) card).setMaxWidth(CARD_WIDTH);
                }
            });
        }

        // Uniformiser le padding
        dashboardTilePane.setPadding(new Insets(20));
    }

    /**
     * Calcule le nombre de colonnes optimal pour le TilePane du tableau de bord
     * en fonction de la largeur disponible de la zone de contenu.
     * @return Le nombre de colonnes à afficher.
     */
    private int calculateColumns() {
        double contentWidth = contentPane.getWidth();
        if (contentWidth <= 0)
            return 1;

        // Suppression de la soustraction conditionnelle qui prenait en compte le menu
        double availableWidth = contentWidth;
        int columns = Math.max(1, (int) Math.floor((availableWidth - 40) / (CARD_WIDTH + CARD_SPACING)));
        return columns;
    }

    /**
     * Crée une carte (GridPane) pour afficher un indicateur du tableau de bord.
     * @param title Le titre de la carte.
     * @param value La valeur de l'indicateur.
     * @param color La couleur de fond de la carte.
     * @return Le GridPane configuré représentant la carte.
     */
    private GridPane createCard(String title, String value, String color) {
        GridPane card = new GridPane();
        card.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        card.setMaxSize(CARD_WIDTH, CARD_HEIGHT);
        card.setMinSize(180, CARD_HEIGHT);
        card.setStyle("-fx-background-color: " + color + ";"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;"
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
                
        // Make cards interactive with hover effect
        card.setCursor(javafx.scene.Cursor.HAND);
        
        // Store original color for hover effect
        card.getProperties().put("originalColor", color);
        
        // Add hover effects
        card.setOnMouseEntered(e -> {
            String originalColor = (String) card.getProperties().get("originalColor");
            // Create slightly darker color for hover effect
            card.setStyle("-fx-background-color: derive(" + originalColor + ", -10%);"
                    + "-fx-background-radius: 10;"
                    + "-fx-border-radius: 10;"
                    + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 12, 0, 2, 2);");
        });
        
        card.setOnMouseExited(e -> {
            String originalColor = (String) card.getProperties().get("originalColor");
            card.setStyle("-fx-background-color: " + originalColor + ";"
                    + "-fx-background-radius: 10;"
                    + "-fx-border-radius: 10;"
                    + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        });
        
        // Add click action based on card title
        card.setOnMouseClicked(e -> handleCardClick(title, value));

        // Configuration des contraintes
        ColumnConstraints column = new ColumnConstraints();
        column.setHgrow(Priority.SOMETIMES);
        column.setMaxWidth(Double.MAX_VALUE);
        column.setMinWidth(180);
        column.setPrefWidth(CARD_WIDTH);

        RowConstraints row1 = new RowConstraints();
        row1.setValignment(VPos.CENTER);
        row1.setVgrow(Priority.SOMETIMES);
        row1.setMinHeight(40);
        row1.setPrefHeight(50);

        RowConstraints row2 = new RowConstraints();
        row2.setValignment(VPos.CENTER);
        row2.setVgrow(Priority.SOMETIMES);
        row2.setMinHeight(40);
        row2.setPrefHeight(50);

        RowConstraints row3 = new RowConstraints();
        row3.setValignment(VPos.CENTER);
        row3.setVgrow(Priority.SOMETIMES);
        row3.setMinHeight(40);
        row3.setPrefHeight(50);

        card.getColumnConstraints().add(column);
        card.getRowConstraints().addAll(row1, row2, row3);

        // ImageView
        ImageView icon = new ImageView();
        Image image = ResourceLoader.loadImage("images/Icones/fournisseurs.png");
        if (image != null) {
            icon.setImage(image);
        } else {
            System.out.println("Icône non trouvée pour la carte");
        }
        icon.setFitHeight(40);
        icon.setFitWidth(40);
        icon.setPreserveRatio(true);
        GridPane.setHalignment(icon, HPos.CENTER);
        GridPane.setValignment(icon, VPos.CENTER);

        // Label de valeur
        Label valueLabel = new Label(value);
        valueLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        valueLabel.setFont(Font.font("Open Sans Semibold", 32));
        GridPane.setHalignment(valueLabel, HPos.CENTER);
        GridPane.setValignment(valueLabel, VPos.CENTER);

        // Ajuster la taille de police pour les valeurs longues
        if (value.length() > 6) {
            valueLabel.setFont(Font.font("Open Sans Semibold", 24));
        }

        // Label de titre
        Label titleLabel = new Label(title);
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        titleLabel.setFont(Font.font("Open Sans", 20));
        titleLabel.setWrapText(true);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        GridPane.setValignment(titleLabel, VPos.CENTER);

        // Ajuster la taille de police pour les titres longs
        if (title.length() > 15) {
            titleLabel.setFont(Font.font("Open Sans", 16));
        }

        // Ajout des éléments au GridPane
        card.add(icon, 0, 0);
        card.add(valueLabel, 0, 1);
        card.add(titleLabel, 0, 2);

        // Liaison pour que le titre s'adapte à la largeur de la carte
        titleLabel.prefWidthProperty().bind(card.widthProperty().subtract(10));

        return card;
    }
    
    /**
     * Gère les clics sur les cartes du tableau de bord.
     * En fonction du titre de la carte cliquée, navigue vers la section appropriée
     * (ex: Analyse des ventes, Gestion du personnel, Médicaments).
     * @param title Le titre de la carte qui a été cliquée.
     * @param value La valeur affichée sur la carte.
     */
    private void handleCardClick(String title, String value) {
        switch (title) {
            case "CA":
            case "Bénéfices":
                loadAnalytics();
                break;
            case "Employés":
                if ("ADMINISTRATEUR".equals(LoggedSeller.getInstance().getRole())) {
                    // If admin, navigate to personnel management
                    handleSwitchToAdmin();
                    // Allow time for the transition and then load the personnel page
                    new Thread(() -> {
                        try {
                            Thread.sleep(500);
                            javafx.application.Platform.runLater(() -> {
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/GestionPersonnel.fxml"));
                                    Parent view = loader.load();
                                    DashboardAdminController adminController = 
                                        (DashboardAdminController) ((Scene) view.getScene()).getUserData();
                                    if (adminController != null) {
                                        adminController.loadGestionPersonnel();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }).start();
                } else {
                    showAlert(Alert.AlertType.WARNING,
                            "Accès restreint",
                            "Autorisation requise",
                            "Vous devez avoir le rôle Administrateur pour accéder à cette page.");
                }
                break;
            case "Clients":
                showNotImplementedAlert("Gestion des clients");
                break;
            case "Médecins":
                loadContent("Médecins");
                break;
            case "Médicaments":
            case "Médicaments en rupture de stock":
            case "Médicaments périmés":
            case "Médicaments Stock Faible":
            case "Médicaments péremption - 1 mois":
                // All medication cards now lead to the same unfiltered medication page
                loadContent("Médicaments");
                break;
            default:
                // No specific action for unknown cards
                System.out.println("Card clicked: " + title + " with value: " + value);
        }
    }
    
    /**
     * Charge la page des médicaments sans filtres spécifiques.
     * Met à jour le titre de l'en-tête et affiche la vue des médicaments.
     */
    private void loadMedicaments() {
        try {
            updateHeaderTitle("Médicaments");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/médicaments.fxml"));
            Parent viewContent = loader.load();
            
            if (viewContent instanceof Region) {
                Region region = (Region) viewContent;
                region.prefWidthProperty().bind(contentPane.widthProperty());
                region.prefHeightProperty().bind(contentPane.heightProperty());
            }
            
            contentPane.getChildren().setAll(viewContent);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la vue",
                    "Détails: " + e.getMessage());
        }
    }

    /**
     * Affiche une boîte de dialogue informant que la fonctionnalité n'est pas encore implémentée.
     * @param featureName Le nom de la fonctionnalité.
     */
    private void showNotImplementedAlert(String featureName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fonctionnalité à venir");
        alert.setHeaderText(featureName);
        alert.setContentText("Cette fonctionnalité sera disponible dans une prochaine version.");
        alert.showAndWait();
    }

    /**
     * Affiche ou masque le menu latéral avec une animation de translation.
     * Ajuste le padding de la zone de contenu et de la barre supérieure en conséquence.
     * Met à jour la disposition du tableau de bord si celui-ci est affiché.
     */
    private void toggleMenu() {
        TranslateTransition slideTransition = new TranslateTransition(Duration.seconds(0.3), sideMenu);
        boolean newVisibility = !menuVisible.get();

        // Ne pas ouvrir le menu si la fenêtre est trop petite
        if (newVisibility && getWidth() < MINIMUM_WINDOW_WIDTH) {
            return;
        }

        // Ajuster la translation pour positionner le menu correctement
        slideTransition.setToX(newVisibility ? 0 : -MENU_WIDTH);
        
        slideTransition.setOnFinished(e -> {
            menuVisible.set(newVisibility);
            
            // Repositionner le menu en fonction de son état
            if (newVisibility) {
                // Quand le menu est visible, on ajuste le contenu principal
                contentPane.setPadding(new Insets(0, 0, 0, MENU_WIDTH));
                
                // Ajuster la position de la barre supérieure pour qu'elle commence à 300px
                topBar.setPadding(new Insets(0, 0, 0, MENU_WIDTH));
                
                // Mise à jour des contraintes de redimensionnement pour le header
                headerTitle.prefWidthProperty().bind(
                        widthProperty().subtract(MENU_WIDTH).subtract(250)
                );
            } else {
                // Quand le menu est caché, on restaure le contenu principal
                contentPane.setPadding(new Insets(0));
                
                // Restaurer la position de la barre supérieure
                topBar.setPadding(new Insets(0));
                
                // Mise à jour des contraintes de redimensionnement pour le header
                headerTitle.prefWidthProperty().bind(
                        widthProperty().subtract(250)
                );
            }
            
            // Mise à jour de la disposition si nécessaire
            if (dashboardTilePane != null && dashboardTilePane.getParent() == contentPane) {
                Platform.runLater(() -> updateDashboardLayout());
            }
        });
        
        slideTransition.play();
    }

    /**
     * Affiche une boîte de dialogue d'alerte.
     * @param type Le type d'alerte (ex: INFORMATION, ERROR).
     * @param title Le titre de la fenêtre d'alerte.
     * @param header Le texte d'en-tête de l'alerte.
     * @param content Le message principal de l'alerte.
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Met en surbrillance le bouton actif dans le menu latéral.
     * @param activeButton Le bouton qui doit être marqué comme actif.
     */
    private void setActiveButton(Button activeButton) {
        Button[] menuButtons = { btnDashboard, btnSales, btnMedics, btnCommandes,
                btnSuppliers, btnAnalytics }; // Changed btnMedecins back to btnSuppliers

        for (Button btn : menuButtons) {
            if (btn == activeButton) {
                btn.setStyle("-fx-background-color: #00693E; -fx-padding: 0 0 0 20;");
            } else {
                btn.setStyle("-fx-background-color: transparent; -fx-padding: 0 0 0 20;");
            }
        }
    }

    /**
     * Charge une vue spécifique dans la zone de contenu principale.
     * @param title Le titre de la section à charger, utilisé pour déterminer le fichier FXML.
     */
    private void loadContent(String title) {
        updateHeaderTitle(title);
        contentPane.getChildren().clear();

        try {
            Parent viewContent = null;
            if (title.equals("Médicaments")) {
                // Chemin ABSOLU depuis les resources
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/médicaments.fxml"));
                viewContent = loader.load();
            } else if (title.equals("Médecins")) { // Changed from Fournisseurs to Médecins
                // Chemin ABSOLU depuis les resources
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/MedecinsPage.fxml"));
                viewContent = loader.load();
            } else if (title.equals("Commandes")) {
                // Chemin ABSOLU depuis les resources
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/GestionCommande.fxml"));
                viewContent = loader.load();
            } else if (title.equals("Ventes")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/ventes.fxml"));
                viewContent = loader.load();
            } else {
                Label label = new Label(title);
                label.setStyle("-fx-font-size: 24px;");
                viewContent = label;
            }

            // Rendre la vue chargée responsive
            if (viewContent instanceof Region) {
                Region region = (Region) viewContent;
                region.prefWidthProperty().bind(contentPane.widthProperty());
                region.prefHeightProperty().bind(contentPane.heightProperty());
            }

            contentPane.getChildren().add(viewContent);
        } catch (IOException e) {
            e.printStackTrace();
            Label errorLabel = new Label("Erreur de chargement: " + e.getMessage());
            errorLabel.setWrapText(true);
            errorLabel.setMaxWidth(contentPane.getWidth() - 40);
            contentPane.getChildren().add(errorLabel);
        }

        if (menuVisible.get() && getWidth() < MINIMUM_WINDOW_WIDTH)
            toggleMenu();
    }

    /**
     * Met à jour le titre affiché dans l'en-tête de la page.
     * Ajuste la taille de la police si le titre est long.
     * @param newTitle Le nouveau titre à afficher.
     */
    private void updateHeaderTitle(String newTitle) {
        headerTitle.setText(newTitle);

        // Ajuster la taille de police pour les titres longs
        if (newTitle.length() > 20) {
            headerTitle.setStyle("-fx-font-size: 20px;");
        } else {
            headerTitle.setStyle("-fx-font-size: 24px;");
        }
        
        // S'assurer que le titre a suffisamment d'espace
        double minWidth = Math.max(300, newTitle.length() * 12); // Approximation: 12px par caractère
        headerTitle.setMinWidth(minWidth);
    }

    /**
     * Initialise les composants graphiques de base du tableau de bord.
     * Configure le bouton de menu, la position initiale du menu latéral,
     * et les liaisons pour la responsivité.
     */
    private void initializeComponents() {
        menuButton.setText("☰");
        sideMenu.setTranslateX(-MENU_WIDTH);

        // S'assurer que le menu se superpose au contenu, mais correctement positionné
        StackPane.setAlignment(sideMenu, Pos.CENTER_LEFT);
        sideMenu.setViewOrder(-1);  // Plus petit = plus en avant
        contentPane.setViewOrder(0); // Par défaut = plus en arrière

        // Ajouter des propriétés de liaison responsive
        contentPane.prefWidthProperty().bind(widthProperty());
        contentPane.prefHeightProperty().bind(heightProperty().subtract(100)); // Tenir compte de la hauteur de l'en-tête

        // Initialiser le padding du topBar à 0
        topBar.setPadding(new Insets(0));

        // S'assurer que tous les éléments de la barre supérieure sont correctement dimensionnés
        for (javafx.scene.Node node : topBar.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                // Empêcher le texte de s'afficher avec des points de suspension
                label.setTextOverrun(javafx.scene.control.OverrunStyle.CLIP);
                // S'assurer que le texte est entièrement visible
                if (!label.getText().equals("|") && label != headerTitle) {
                    label.setMinWidth(Label.USE_PREF_SIZE);
                }
            }
        }

        // Modifier la contrainte de redimensionnement pour le header pour utiliser plus d'espace disponible
        headerTitle.prefWidthProperty().bind(
                widthProperty().subtract(menuVisible.get() ? MENU_WIDTH : 0).subtract(240) // Ajusté pour les éléments de la barre
        );

        // Activer le wrapping de texte pour le headerTitle
        headerTitle.setWrapText(true);

        // S'assurer que les étiquettes de menu s'adaptent à l'espace
        Button[] menuButtons = { btnDashboard, btnSales, btnMedics, btnCommandes, btnSuppliers, btnAnalytics,
                btnLogout };
        for (Button btn : menuButtons) {
            HBox content = (HBox) btn.getGraphic();
            if (content != null) {
                Label label = (Label) content.getChildren().get(content.getChildren().size() - 1);
                label.prefWidthProperty().bind(sideMenu.widthProperty().subtract(80));
            }
        }
    }

    /**
     * Charge et affiche la vue d'analyse des ventes.
     * Met à jour le titre de l'en-tête et configure la responsivité de la vue chargée.
     */
    private void loadAnalytics() {
        setActiveButton(btnAnalytics);
        updateHeaderTitle("Analyse des ventes");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/analyse_ventes.fxml"));
            Parent analyticsView = loader.load();

            // Rendre la vue analytique responsive
            if (analyticsView instanceof Region) {
                Region region = (Region) analyticsView;
                region.prefWidthProperty().bind(contentPane.widthProperty());
                region.prefHeightProperty().bind(contentPane.heightProperty());
            }

            contentPane.getChildren().setAll(analyticsView);
        } catch (IOException e) {
            e.printStackTrace();
            Label errorLabel = new Label("Erreur de chargement: " + e.getMessage());
            errorLabel.setWrapText(true);
            errorLabel.setMaxWidth(contentPane.getWidth() - 40);
            contentPane.getChildren().add(errorLabel);
        }

        if (menuVisible.get() && getWidth() < MINIMUM_WINDOW_WIDTH)
            toggleMenu();
    }

    /**
     * Configure l'effet de survol pour un bouton.
     * Change la couleur de fond du bouton lorsque la souris entre et sort.
     * @param button Le bouton à configurer.
     * @param normalColor La couleur de fond normale du bouton.
     * @param hoverColor La couleur de fond du bouton au survol.
     */
    private void setupButtonHoverEffect(Button button, String normalColor, String hoverColor) {
        button.setStyle("-fx-background-color: " + normalColor + ";");
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: " + hoverColor + ";"));
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: " + normalColor + ";"));
    }
}