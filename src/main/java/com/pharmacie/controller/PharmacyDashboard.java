package com.pharmacie.controller;

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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.pharmacie.App;
import com.pharmacie.model.Dashboard;
import com.pharmacie.service.ApiRest;
import com.pharmacie.util.LoggedSeller;

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
    private Button btnSuppliers;
    @FXML
    private Button btnAnalytics;
    @FXML
    private Label headerTitle;
    @FXML
    private Label userLabel;
    @FXML
    private Button btnLogout;
    @FXML
    private HBox topBar; // Ajout de la référence à la barre supérieure
    
    // Nouvelle propriété pour garantir un espacement adéquat
    private final double HEADER_SPACING = 20.0;

    private BooleanProperty menuVisible = new SimpleBooleanProperty(false);
    private TilePane dashboardTilePane;

    public PharmacyDashboard() {
        loadFXML();
        initializeComponents();
        initialize();
    }

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

    public void initialize() {
        configureMenuButtons();
        menuButton.setOnAction(event -> toggleMenu());
        setupMenuActions();
        btnLogout.setOnAction(event -> handleLogout());
        configureMenuButton(btnLogout, "logout.png", "Déconnexion");
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
     * Configure le menu pour qu'il soit complètement indépendant et ne provoque aucun recalcul de layout
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
     * Gère le redimensionnement de la fenêtre pour ajuster les éléments UI
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
     * Ajuste la taille des polices en fonction de la largeur de la fenêtre
     */
    private void adjustFontSizes(double windowWidth) {
        double scaleFactor = Math.max(0.7, Math.min(1.0, windowWidth / 1280.0));

        headerTitle.setStyle(String.format("-fx-font-size: %.1fpx;", 24 * scaleFactor));
        userLabel.setStyle(String.format("-fx-font-size: %.1fpx;", 22 * scaleFactor));
    }

    private boolean isAuthorizedRole() {
        String role = LoggedSeller.getInstance().getRole();
        return "PHARMACIEN_ADJOINT".equals(role) || "APPRENTI".equals(role);
    }

    public void refreshUserInfo() {
        userLabel.setText(LoggedSeller.getInstance().getNomComplet());
    }

    private void showAccessDenied() {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(new Label("Accès non autorisé"));
        sideMenu.setVisible(false);
        menuButton.setVisible(false);
    }

    private void configureMenuButton(Button button, String iconName, String text) {
        HBox content = new HBox(10);
        content.setAlignment(Pos.CENTER_LEFT);

        try {
            // Essayons plusieurs chemins possibles
            InputStream iconStream = getClass().getResourceAsStream("/Icones/" + iconName);
            if (iconStream == null) {
                iconStream = getClass().getResourceAsStream("/images/Icones/" + iconName);
            }
            if (iconStream == null) {
                iconStream = getClass().getResourceAsStream("/com/pharmacie/Icones/" + iconName);
            }
            if (iconStream == null) {
                iconStream = getClass().getResourceAsStream("/com/pharmacie/images/Icones/" + iconName);
            }

            if (iconStream != null) {
                ImageView icon = new ImageView(new Image(iconStream));
                icon.setFitHeight(24);
                icon.setFitWidth(24);
                icon.setPreserveRatio(true);
                content.getChildren().add(icon);
            } else {
                System.out.println("Icône non trouvée après plusieurs tentatives: " + iconName);
                content.getChildren().add(new Label("•"));
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
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

    private void configureMenuButtons() {
        configureMenuButton(btnDashboard, "dashboard.png", "Tableau de Bord");
        configureMenuButton(btnSales, "ventes.png", "Ventes");
        configureMenuButton(btnMedics, "médicaments.png", "Médicaments");
        configureMenuButton(btnCommandes, "gestionCommande.png", "Commandes");
        configureMenuButton(btnSuppliers, "fournisseurs.png", "Fournisseurs");
        configureMenuButton(btnAnalytics, "analyseventes.png", "Analyse des ventes");
    }

    private void handleLogout() {
        LoggedSeller.getInstance().clearUser();
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(App.getLoginScene());
        ((Login) App.getLoginScene().getRoot()).clearFields();
    }

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
            loadContent("Fournisseurs");
        });

        btnAnalytics.setOnAction(event -> {
            setActiveButton(btnAnalytics);
            loadAnalytics();
        });
    }

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
     * Met à jour la mise en page du tableau de bord pour qu'il s'adapte à la taille
     * actuelle et à l'état du menu
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

    private int calculateColumns() {
        double contentWidth = contentPane.getWidth();
        if (contentWidth <= 0)
            return 1;

        // Suppression de la soustraction conditionnelle qui prenait en compte le menu
        double availableWidth = contentWidth;
        int columns = Math.max(1, (int) Math.floor((availableWidth - 40) / (CARD_WIDTH + CARD_SPACING)));
        return columns;
    }

    private GridPane createCard(String title, String value, String color) {
        GridPane card = new GridPane();
        card.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        card.setMaxSize(CARD_WIDTH, CARD_HEIGHT);
        card.setMinSize(180, CARD_HEIGHT);
        card.setStyle("-fx-background-color: " + color + ";"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;"
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

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
        try {
            // Essayons plusieurs chemins possibles
            InputStream iconStream = getClass().getResourceAsStream("/Icones/fournisseurs.png");
            if (iconStream == null) {
                iconStream = getClass().getResourceAsStream("/images/Icones/fournisseurs.png");
            }
            if (iconStream == null) {
                iconStream = getClass().getResourceAsStream("/com/pharmacie/Icones/fournisseurs.png");
            }
            if (iconStream == null) {
                iconStream = getClass().getResourceAsStream("/com/pharmacie/images/Icones/fournisseurs.png");
            }
            if (iconStream == null) {
                iconStream = getClass()
                        .getResourceAsStream("../../../../../resources/com/pharmacie/images/Icones/fournisseurs.png");
            }

            if (iconStream != null) {
                icon.setImage(new Image(iconStream));
            } else {
                System.out.println("Icône non trouvée après plusieurs tentatives");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
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

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void setActiveButton(Button activeButton) {
        Button[] menuButtons = { btnDashboard, btnSales, btnMedics, btnCommandes,
                btnSuppliers, btnAnalytics };

        for (Button btn : menuButtons) {
            if (btn == activeButton) {
                btn.setStyle("-fx-background-color: #00693E; -fx-padding: 0 0 0 20;");
            } else {
                btn.setStyle("-fx-background-color: transparent; -fx-padding: 0 0 0 20;");
            }
        }
    }

    private void loadContent(String title) {
        updateHeaderTitle(title);
        contentPane.getChildren().clear();

        try {
            Parent viewContent = null;
            if (title.equals("Médicaments")) {
                // Chemin ABSOLU depuis les resources
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/médicaments.fxml"));
                viewContent = loader.load();
            } else if (title.equals("Fournisseurs")) {
                // Chemin ABSOLU depuis les resources
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/fournisseurs.fxml"));
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
}