package com.pharmacie.controller;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Login extends FlowPane {
    private final TextField username = new TextField();
    private final PasswordField password = new PasswordField();
    private final Button loginBtn = new Button("LOG IN");

    public Login() {
        Pane leftPane = createLeftPane();
        GridPane rightPane = createRightGridPane();
        setPrefSize(1280, 820);
        getChildren().addAll(leftPane, rightPane);
    }

    private Pane createLeftPane() {
        Pane pane = new Pane();
        pane.setPrefSize(695, 820);
        pane.setStyle("-fx-background-color: #007B3D;");

        // Correction des chemins d'accès aux images
        ImageView img1 = createImageView("/com/pharmacie/images/MedocBois1.png", 360, 360);
        img1.setLayoutX(-39);
        img1.setLayoutY(-91);

        ImageView img2 = createImageView("/com/pharmacie/images/img.png", 375, 674);
        img2.setLayoutX(321);
        img2.setLayoutY(117);

        ImageView img3 = createImageView("/com/pharmacie/images/Pomade.png", 468, 533);
        img3.setLayoutX(-26);
        img3.setLayoutY(322);
        img3.setBlendMode(javafx.scene.effect.BlendMode.SRC_ATOP);

        Label title = new Label("Pharmacie");
        title.setLayoutX(67);
        title.setLayoutY(269);
        title.setPrefSize(586, 174);
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Open Sans", 90));

        pane.getChildren().addAll(img1, img2, img3, title);
        return pane;
    }

    private ImageView createImageView(String resourcePath, double width, double height) {
        try {
            Image image = new Image(getClass().getResourceAsStream(resourcePath));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            return imageView;
        } catch (NullPointerException e) {
            System.err.println("Image non trouvée: " + resourcePath);
            return new ImageView(); // Retourne une ImageView vide si l'image est introuvable
        }
    }

    private GridPane createRightGridPane() {
        GridPane grid = new GridPane();
        grid.setPrefSize(585, 820);
        grid.setStyle("-fx-background-color: #FFFFFF;");

        // Configuration des colonnes
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints(255, 255, 313);
        ColumnConstraints col3 = new ColumnConstraints();
        col1.setMinWidth(85);
        col3.setMinWidth(85);
        grid.getColumnConstraints().addAll(col1, col2, col3);

        // Configuration des lignes
        RowConstraints row1 = new RowConstraints(170);
        RowConstraints row2 = new RowConstraints(396);
        RowConstraints row3 = new RowConstraints(255);
        grid.getRowConstraints().addAll(row1, row2, row3);

        GridPane loginGrid = createLoginForm();
        grid.add(loginGrid, 1, 1);

        return grid;
    }

    private GridPane createLoginForm() {
        GridPane grid = new GridPane();
        grid.setMaxWidth(330);
        grid.setMinWidth(330);
        
        RowConstraints titleRow = new RowConstraints(105);
        RowConstraints fieldsRow = new RowConstraints(181.67);
        RowConstraints buttonsRow = new RowConstraints(125);
        grid.getRowConstraints().addAll(titleRow, fieldsRow, buttonsRow);

        Label title = new Label("Connexion");
        title.setPrefSize(204, 57);
        title.setFont(Font.font("Open Sans", 40));
        GridPane.setHalignment(title, javafx.geometry.HPos.CENTER);
        grid.add(title, 0, 0);

        grid.add(createFieldsGrid(), 0, 1);
        grid.add(createButtonsGrid(), 0, 2);

        return grid;
    }

    private GridPane createFieldsGrid() {
        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setPadding(new Insets(10, 0, 10, 0));

        ColumnConstraints column = new ColumnConstraints(330, 330, 330);
        grid.getColumnConstraints().add(column);

        RowConstraints row1 = new RowConstraints(85);
        RowConstraints row2 = new RowConstraints(85);
        grid.getRowConstraints().addAll(row1, row2);

        String fieldStyle = "-fx-background-color: #EDEDED; -fx-padding: 15; -fx-font-size: 18;";
        
        username.setPrefHeight(55);
        username.setMaxWidth(330);
        username.setPromptText("Identifiant");
        username.setStyle(fieldStyle);

        password.setPrefHeight(55);
        password.setMaxWidth(330);
        password.setPromptText("Mot de Passe");
        password.setStyle(fieldStyle);

        grid.add(username, 0, 0);
        grid.add(password, 0, 1);

        return grid;
    }

    private GridPane createButtonsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(20);

        ColumnConstraints col1 = new ColumnConstraints(70);
        ColumnConstraints col2 = new ColumnConstraints(175, 175, 183);
        ColumnConstraints col3 = new ColumnConstraints(70);
        grid.getColumnConstraints().addAll(col1, col2, col3);

        String buttonStyle = "-fx-background-radius: 5; -fx-cursor: hand; -fx-pref-width: 173; -fx-pref-height: 34;";

        loginBtn.setStyle(buttonStyle + "-fx-background-color: #007B3D;");
        loginBtn.setTextFill(Color.WHITE);
        loginBtn.setFont(Font.font("Open Sans Semibold", 18));

        Button signupBtn = new Button("SIGN UP");
        signupBtn.setStyle(buttonStyle + "-fx-background-color: #0F8AE1;");
        signupBtn.setTextFill(Color.WHITE);
        signupBtn.setFont(Font.font("Open Sans Semibold", 18));
        
        grid.add(loginBtn, 1, 1);
        grid.add(signupBtn, 1, 2);

        return grid;
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