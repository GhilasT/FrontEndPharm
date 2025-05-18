package com.pharmacie;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.pharmacie.controller.Login;
import com.pharmacie.controller.PharmacyDashboard;
import com.pharmacie.controller.PharmacyDashboardModifier;
import com.pharmacie.model.dto.LoginResponse;
import com.pharmacie.util.Global;
import com.pharmacie.util.LoggedSeller;

public class App extends Application {
    private static Stage primaryStage;
    private static Scene loginScene;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Scene getLoginScene() {
        return loginScene;
    }

    private static final String API_URL = Global.getBaseUrl() + "/auth/login";
    PharmacyDashboard dashboard = new PharmacyDashboard();
    private Parent dashboardAdmin;
    Scene adminScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        App.primaryStage = primaryStage;
        primaryStage.getIcons()
                .add(new Image(getClass().getResource("/com/pharmacie/images/Icones/logo16.png").toExternalForm()));
        primaryStage.getIcons()
                .add(new Image(getClass().getResource("/com/pharmacie/images/Icones/logo32.png").toExternalForm()));
        primaryStage.getIcons()
                .add(new Image(getClass().getResource("/com/pharmacie/images/Icones/logo48.png").toExternalForm()));
        primaryStage.getIcons()
                .add(new Image(getClass().getResource("/com/pharmacie/images/Icones/logo64.png").toExternalForm()));

        Login login = new Login();
        loginScene = new Scene(login);
        Scene dashBoardScene = new Scene(dashboard);
        try {
            dashboardAdmin = FXMLLoader.load(getClass().getResource("/com/pharmacie/view/DashboardAdmin.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger le tableau de bord administrateur.");
            return;
        }
        PharmacyDashboardModifier.modifyDashboard(dashboard);
        adminScene = new Scene(dashboardAdmin);

        primaryStage.setTitle("Pharmacie - Connexion");
        primaryStage.setScene(loginScene);
        primaryStage.show();

        login.getLoginButton().setOnAction(e -> handleLogin(login, primaryStage, dashBoardScene));
    }

    private void handleLogin(Login login, Stage primaryStage, Scene dashBoardScene) {
        String email = login.getEmail();
        String password = login.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erreur de saisie", "Veuillez remplir tous les champs");
            return;
        }

        Task<LoginResponse> loginTask = createLoginTask(email, password);
        setupLoginTaskHandlers(loginTask, primaryStage, dashBoardScene);
        new Thread(loginTask).start();
    }

    private Task<LoginResponse> createLoginTask(String email, String password) {
        return new Task<>() {
            @Override
            protected LoginResponse call() throws Exception {
                try {
                    String jsonBody = String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, password);

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(API_URL))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                            .build();

                    HttpResponse<String> response = HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

                    return new Gson().fromJson(response.body(), LoginResponse.class);

                } catch (Exception e) {
                    return new LoginResponse();
                }
            }
        };
    }

    private void setupLoginTaskHandlers(Task<LoginResponse> loginTask, Stage primaryStage, Scene dashBoardScene) {
        loginTask.setOnSucceeded(event -> {
            try {
                LoginResponse response = loginTask.getValue();

                if (response != null && response.isSuccess()) {
                    String role = response.getRole();

                    Global.setToken(response.getToken());
                    
                    if ("PHARMACIEN_ADJOINT".equalsIgnoreCase(role) 
                        || "APPRENTI".equalsIgnoreCase(role) 
                        || "ADMINISTRATEUR".equalsIgnoreCase(role)) {
                        
                        LoggedSeller.getInstance().setUser(
                            response.getId(),
                            response.getNom(),
                            response.getPrenom(),
                            role,
                            response.getToken()
                        );
                        
                        if ("ADMINISTRATEUR".equalsIgnoreCase(role)) {
                            primaryStage.setScene(adminScene);
                        } else {
                            dashboard.refreshUserInfo();
                            primaryStage.setScene(dashBoardScene);
                        }
                        
                        primaryStage.setTitle("Dashboard - " + LoggedSeller.getInstance().getNomComplet());
                    } else {
                        showAlert("Accès refusé", "Rôle non autorisé (" + role + ")");
                    }
                } else {
                    String errorMessage = response == null 
                        ? "Pas de réponse du serveur" 
                        : "Erreur d'authentification";
                    showAlert("Échec", errorMessage);
                }
            } catch (Exception e) {
                showAlert("Erreur", "Erreur de traitement: " + e.getMessage());
            }
        });

        loginTask.setOnFailed(event -> {
            showAlert("Connexion impossible",
                    "Erreur réseau: " + loginTask.getException().getMessage());
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}