package com.pharmacie;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.pharmacie.controller.Login;
import com.pharmacie.controller.PharmacyDashboard;
import com.pharmacie.controller.PharmacyDashboardModifier;
import com.pharmacie.model.dto.LoginResponse;
import com.pharmacie.util.LoggedSeller;

public class App extends Application {

    private static final String API_URL = "http://localhost:8080/api/auth/login";
    PharmacyDashboard dashboard = new PharmacyDashboard();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Login login = new Login();
        Scene loginScene = new Scene(login);
        Scene dashBoardScene = new Scene(dashboard);
        PharmacyDashboardModifier.modifyDashboard(dashboard);

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
                    
                    if("PHARMACIEN_ADJOINT".equalsIgnoreCase(role) || "APPRENTI".equalsIgnoreCase(role)) {
                        LoggedSeller.getInstance().setUser(
                            response.getId(),
                            response.getNom(),
                            response.getPrenom(), 
                            role
                        );
                        dashboard.refreshUserInfo();
                        primaryStage.setTitle("Dashboard - " + LoggedSeller.getInstance().getNomComplet());
                        primaryStage.setScene(dashBoardScene);
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