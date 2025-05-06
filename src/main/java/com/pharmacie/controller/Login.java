package com.pharmacie.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Login extends BorderPane {
    private final TextField username = new TextField();
    private final PasswordField password = new PasswordField();
    private final Button loginBtn = new Button("LOG IN");
    
    private AnchorPane leftPane;
    private VBox rightPane;
    
    // Dimensions pour un ratio 16:9
    private double minWidth = 1280; // 16 unités de largeur
    private double minHeight = 720; // 9 unités de hauteur

    public Login() {
        leftPane = createLeftPane();
        rightPane = createRightPane();
        
        setLeft(leftPane);
        setCenter(rightPane);
        
        // Configuration initiale des proportions
        leftPane.setPrefWidth(minWidth * 0.55); // 55% de la largeur initiale
        rightPane.setPrefWidth(minWidth * 0.45); // 45% de la largeur initiale
        
        // Add listeners for responsive behavior
        widthProperty().addListener((obs, oldVal, newVal) -> {
            handleResize();
        });
        
        heightProperty().addListener((obs, oldVal, newVal) -> {
            handleResize();
        });
        
        // Set minimum size avec ratio 16:9
        setMinSize(minWidth, minHeight);
        setPrefSize(minWidth, minHeight);
    }
    
    private void handleResize() {
        double width = getWidth();
        double height = getHeight();
        
        // If screen gets too small, adjust components
        if (width < 1000) {
            leftPane.setPrefWidth(width * 0.4);
            rightPane.setPrefWidth(width * 0.6);
        } else {
            leftPane.setPrefWidth(width * 0.55);
            rightPane.setPrefWidth(width * 0.45);
        }
        
        // Adjust font sizes based on screen size
        double fontScale = Math.max(0.7, Math.min(1.0, width / 1280.0));
        
        // Update label font sizes
        for (Label label : findLabels(leftPane)) {
            if (label.getText().equals("PharmaPlus")) {
                label.setFont(Font.font("Open Sans", 90 * fontScale));
            }
        }
        
        // Also update form title font size
        for (Label label : findLabels(rightPane)) {
            if (label.getText().equals("Connexion")) {
                label.setFont(Font.font("Open Sans", 40 * fontScale));
            }
        }
    }
    
    private java.util.List<Label> findLabels(Pane pane) {
        java.util.List<Label> labels = new java.util.ArrayList<>();
        for (javafx.scene.Node node : pane.getChildren()) {
            if (node instanceof Label) {
                labels.add((Label) node);
            } else if (node instanceof Pane) {
                labels.addAll(findLabels((Pane) node));
            }
        }
        return labels;
    }

    private AnchorPane createLeftPane() {
        AnchorPane pane = new AnchorPane();
        pane.setStyle("-fx-background-color: #007B3D;");

        // Images with responsive positioning
        ImageView img1 = createImageView("/com/pharmacie/images/MedocBois1.png", 360, 360);
        AnchorPane.setTopAnchor(img1, -91.0);
        AnchorPane.setLeftAnchor(img1, -39.0);

        ImageView img2 = createImageView("/com/pharmacie/images/img.png", 375, 674);
        AnchorPane.setTopAnchor(img2, 117.0);
        AnchorPane.setRightAnchor(img2, 0.0);

        ImageView img3 = createImageView("/com/pharmacie/images/Pomade.png", 468, 533);
        AnchorPane.setBottomAnchor(img3, 0.0);
        AnchorPane.setLeftAnchor(img3, -26.0);
        img3.setBlendMode(javafx.scene.effect.BlendMode.SRC_ATOP);

        // Créer un conteneur StackPane pour centrer le titre
        StackPane titleContainer = new StackPane();
        AnchorPane.setTopAnchor(titleContainer, 0.0);
        AnchorPane.setRightAnchor(titleContainer, 0.0);
        AnchorPane.setLeftAnchor(titleContainer, 0.0);
        AnchorPane.setBottomAnchor(titleContainer, 0.0);
        
        Label title = new Label("PharmaPlus");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Open Sans", 90));
        
        titleContainer.getChildren().add(title);
        titleContainer.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(img1, img2, img3, titleContainer);
        return pane;
    }

    private ImageView createImageView(String resourcePath, double width, double height) {
        try {
            Image image = new Image(getClass().getResourceAsStream(resourcePath), 
                                   width, height, true, true);
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            
            // Ajuster la taille des images en fonction de la largeur de la fenêtre
            imageView.fitWidthProperty().bind(widthProperty().multiply(0.25));
            return imageView;
        } catch (NullPointerException e) {
            System.err.println("Image non trouvée: " + resourcePath);
            return new ImageView();
        }
    }

    private VBox createRightPane() {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: #FFFFFF;");
        container.setPadding(new Insets(50));
        
        // Center everything inside the right pane
        VBox loginContainer = createLoginForm();
        // Make the login container responsive
        loginContainer.prefWidthProperty().bind(container.widthProperty().multiply(0.8));
        loginContainer.maxWidthProperty().bind(container.widthProperty().multiply(0.9));
        
        container.getChildren().add(loginContainer);
        
        return container;
    }

    private VBox createLoginForm() {
        VBox container = new VBox();
        container.setSpacing(30);
        container.setAlignment(Pos.CENTER);
        
        Label title = new Label("Connexion");
        title.setFont(Font.font("Open Sans", 40));
        title.setAlignment(Pos.CENTER);
        
        VBox fieldsBox = createFieldsBox();
        VBox buttonsBox = createButtonsBox();
        
        container.getChildren().addAll(title, fieldsBox, buttonsBox);
        return container;
    }

    private VBox createFieldsBox() {
        VBox box = new VBox();
        box.setSpacing(20);
        box.setAlignment(Pos.CENTER);
        box.setMaxWidth(Double.MAX_VALUE);
        
        // Ajout de padding latéral pour les champs de texte
        box.setPadding(new Insets(0, 45, 0, 45));
        
        String fieldStyle = "-fx-background-color: #EDEDED; -fx-padding: 15; -fx-font-size: 18;";
        
        username.setPrefHeight(55);
        username.setMaxWidth(Double.MAX_VALUE);
        username.setPromptText("Identifiant");
        username.setStyle(fieldStyle);

        password.setPrefHeight(55);
        password.setMaxWidth(Double.MAX_VALUE);
        password.setPromptText("Mot de Passe");
        password.setStyle(fieldStyle);
        
        box.getChildren().addAll(username, password);
        return box;
    }

    public void clearFields() {
        username.clear();
        password.clear();
    }
    
    private VBox createButtonsBox() {
        VBox box = new VBox();
        box.setSpacing(20);
        box.setAlignment(Pos.CENTER);
        box.setMaxWidth(Double.MAX_VALUE);
        
        // Ajout de padding latéral plus important pour le bouton de connexion
        box.setPadding(new Insets(0, 70, 0, 70));

        String buttonStyle = "-fx-background-radius: 5; -fx-cursor: hand; -fx-font-size: 18;";

        loginBtn.setStyle(buttonStyle + "-fx-background-color: #007B3D;");
        loginBtn.setTextFill(Color.WHITE);
        loginBtn.setFont(Font.font("Open Sans Semibold", 18));
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(40);
        
        box.getChildren().add(loginBtn);
        return box;
    }

    public Button getLoginButton() {
        return loginBtn;
    }

    public String getEmail() {
        return username.getText();
    }

    public String getPassword() {
        return password.getText();
    }
}