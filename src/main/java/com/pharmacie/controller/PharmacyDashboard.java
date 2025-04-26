package com.pharmacie.controller;

import java.io.IOException;
import java.io.InputStream;

import com.pharmacie.App;
import com.pharmacie.util.LoggedSeller;

import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PharmacyDashboard extends StackPane {
    private static final int MENU_WIDTH = 300;
    private static final int CARD_SPACING = 52;

    @FXML private Button menuButton;
    @FXML private VBox sideMenu;
    @FXML private StackPane contentPane;
    @FXML private Button btnDashboard;
    @FXML private Button btnSales;
    @FXML private Button btnMedics;
    @FXML private Button btnCommandes;
    @FXML private Button btnSuppliers;
    @FXML private Button btnAnalytics;
    @FXML private Button btnSettings;
    @FXML private Label headerTitle;
    @FXML private Label userLabel;
    @FXML private Button btnLogout;

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
        } catch (IOException e) {
            throw new RuntimeException("Erreur de chargement du FXML", e);
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
            if (iconStream == null) {
                iconStream = getClass().getResourceAsStream("../../../../../resources/com/pharmacie/images/Icones/" + iconName);
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
        configureMenuButton(btnSettings, "paramètres.png", "Paramètres");
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
            loadContent("Analyse des ventes");
        });
        
        btnSettings.setOnAction(event -> {
            setActiveButton(btnSettings);
            loadContent("Paramètres");
        });
    }

    private void loadDashboard() {
        setActiveButton(btnDashboard);
        updateHeaderTitle("Tableau de Bord");
        
        dashboardTilePane = new TilePane();
        dashboardTilePane.setPadding(new Insets(20, 20, 20, CARD_SPACING));
        dashboardTilePane.setHgap(CARD_SPACING);
        dashboardTilePane.setVgap(CARD_SPACING);
        dashboardTilePane.prefColumnsProperty().bind(Bindings.createIntegerBinding(() -> 
        calculateColumns(), 
        menuVisible, 
        contentPane.widthProperty()
    ));
        dashboardTilePane.paddingProperty().bind(Bindings.createObjectBinding(() -> 
        new Insets(20, 20, 20, menuVisible.get() ? MENU_WIDTH + CARD_SPACING : CARD_SPACING), 
        menuVisible
    ));

        String[] titles = {"Ventes", "Médicaments", "Clients", "Commandes", 
                          "Fournisseurs", "Alertes", "CA Journalier", "CA Mensuel"};
        String[] values = {"142", "358", "89", "23", "15", "9", "€2450", "€58900"};
        String[] colors = {"#1F82F2", "#1F82F2", "#E74C3C", "#9B59B6", 
                          "#F39C12", "#16A085", "#34495E", "#7F8C8D"};

        for(int i = 0; i < 8; i++) {
            dashboardTilePane.getChildren().add(
                createCard(titles[i], values[i], colors[i])
            );
        }

        contentPane.getChildren().setAll(dashboardTilePane);
        if(menuVisible.get()) toggleMenu();
    }
    private int calculateColumns() {
        double contentWidth = contentPane.getWidth();
        if(contentWidth <= 0) return 4; // Valeur par défaut
        
        double availableWidth = contentWidth - (menuVisible.get() ? MENU_WIDTH : 0) - CARD_SPACING;
        return (int) Math.floor(availableWidth / (246 + CARD_SPACING));
    }

    private GridPane createCard(String title, String value, String color) {
        GridPane card = new GridPane();
        card.setPrefSize(246, 150);
        card.setStyle("-fx-background-color: " + color + ";"
            + "-fx-background-radius: 10;"
            + "-fx-border-radius: 10;"
            + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        // Configuration des contraintes
        ColumnConstraints column = new ColumnConstraints();
        column.setHgrow(Priority.SOMETIMES);
        column.setMaxWidth(246);
        column.setMinWidth(246);
        column.setPrefWidth(246);
        
        RowConstraints row1 = new RowConstraints();
        row1.setValignment(VPos.CENTER);
        row1.setVgrow(Priority.SOMETIMES);
        row1.setMinHeight(50);
        row1.setPrefHeight(50);

        RowConstraints row2 = new RowConstraints();
        row2.setValignment(VPos.CENTER);
        row2.setVgrow(Priority.SOMETIMES);
        row2.setMinHeight(50);
        row2.setPrefHeight(50);

        RowConstraints row3 = new RowConstraints();
        row3.setValignment(VPos.CENTER);
        row3.setVgrow(Priority.SOMETIMES);
        row3.setMinHeight(50);
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
                iconStream = getClass().getResourceAsStream("../../../../../resources/com/pharmacie/images/Icones/fournisseurs.png");
            }
            
            if (iconStream != null) {
                icon.setImage(new Image(iconStream));
            } else {
                System.out.println("Icône non trouvée après plusieurs tentatives");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
        }
        icon.setFitHeight(50);
        icon.setFitWidth(50);
        icon.setPreserveRatio(true);
        GridPane.setHalignment(icon, HPos.CENTER);
        GridPane.setValignment(icon, VPos.CENTER);

        // Label de valeur
        Label valueLabel = new Label(value);
        valueLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        valueLabel.setFont(Font.font("Open Sans Semibold", 32));
        GridPane.setHalignment(valueLabel, HPos.CENTER);
        GridPane.setValignment(valueLabel, VPos.CENTER);

        // Label de titre
        Label titleLabel = new Label(title);
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        titleLabel.setFont(Font.font("Open Sans", 20));
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        GridPane.setValignment(titleLabel, VPos.CENTER);

        // Ajout des éléments au GridPane
        card.add(icon, 0, 0);
        card.add(valueLabel, 0, 1);
        card.add(titleLabel, 0, 2);

        return card;
    }

    private void toggleMenu() {
        TranslateTransition slideTransition = new TranslateTransition(Duration.seconds(0.3), sideMenu);
        boolean newVisibility = !menuVisible.get();
        
        slideTransition.setToX(newVisibility ? 0 : -300);
        slideTransition.setOnFinished(e -> menuVisible.set(newVisibility));
        slideTransition.play();
    }

    private void setActiveButton(Button activeButton) {
        Button[] menuButtons = {btnDashboard, btnSales, btnMedics, 
                              btnSuppliers, btnAnalytics, btnSettings};
        
        for(Button btn : menuButtons) {
            if(btn == activeButton) {
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
            if(title.equals("Médicaments")) {
                // Chemin ABSOLU depuis les resources
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/médicaments.fxml"));
                Parent medicamentsView = loader.load();
                contentPane.getChildren().add(medicamentsView);
            } 
            else if(title.equals("Fournisseurs")) {
                // Chemin ABSOLU depuis les resources
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/fournisseurs.fxml"));
                Parent fournisseursView = loader.load();
                contentPane.getChildren().add(fournisseursView);
            }
            else if(title.equals("Commandes")) {
                // Chemin ABSOLU depuis les resources
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/GestionCommande.fxml"));
                Parent commandesView = loader.load();
                contentPane.getChildren().add(commandesView);
            }
            else {
                contentPane.getChildren().add(new Label(title));
            }
        } catch (IOException e) {
            e.printStackTrace();
            contentPane.getChildren().add(new Label("Erreur de chargement: " + e.getMessage()));
        }
        
        if(menuVisible.get()) toggleMenu();
    }
    
    private void updateHeaderTitle(String newTitle) {
        headerTitle.setText(newTitle);
    }

    private void initializeComponents() {
        menuButton.setText("☰");
        sideMenu.setTranslateX(-300);
    }


}