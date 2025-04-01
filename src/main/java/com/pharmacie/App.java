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

import com.pharmacie.controller.Login;
import com.pharmacie.controller.PharmacyDashboard;
import com.pharmacie.controller.PharmacyDashboardModifier;

public class App extends Application {

    private static final String API_URL = "http://localhost:8080/api/auth/login";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Login login = new Login();
        Scene loginScene = new Scene(login);
        PharmacyDashboard dashboard = new PharmacyDashboard();
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

        Task<Boolean> loginTask = createLoginTask(email, password);
        setupLoginTaskHandlers(loginTask, primaryStage, dashBoardScene);
        new Thread(loginTask).start();
    }

    private Task<Boolean> createLoginTask(String email, String password) {
        return new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    String jsonBody = String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, password);
                    
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(API_URL))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                            .build();

                    HttpResponse<String> response = HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

                    return response.statusCode() == 200;
                } catch (Exception e) {
                    return false;
                }
            }
        };
    }

    private void setupLoginTaskHandlers(Task<Boolean> loginTask, Stage primaryStage, Scene dashBoardScene) {
        loginTask.setOnSucceeded(event -> {
            if (Boolean.TRUE.equals(loginTask.getValue())) {
                primaryStage.setScene(dashBoardScene);
            } else {
                showAlert("Échec de connexion", "Identifiants incorrects ou problème de connexion au serveur");
            }
        });

        loginTask.setOnFailed(event -> {
            showAlert("Erreur technique", "Impossible de contacter le serveur. Vérifiez votre connexion.");
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