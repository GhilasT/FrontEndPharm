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
import com.pharmacie.util.ResourceLoader;

/**
 * Main application class for the pharmacy management system.
 * This class is responsible for initializing the JavaFX application,
 * handling user authentication, and managing scene transitions based on user roles.
 */
public class App extends Application {
    /** The primary stage of the application. */
    private static Stage primaryStage;
    
    /** The login scene that will be shown to users. */
    private static Scene loginScene;

    /**
     * Gets the primary stage of the application.
     * @return The primary stage.
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Gets the login scene of the application.
     * @return The login scene.
     */
    public static Scene getLoginScene() {
        return loginScene;
    }

    /** The API endpoint for user authentication. */
    private static final String API_URL = Global.getBaseUrl() + "/auth/login";
    
    /** Dashboard controller for pharmacy users. */
    PharmacyDashboard dashboard = new PharmacyDashboard();
    
    /** The scene for the admin dashboard. */
    Scene adminScene;

    /**
     * Main method that launches the JavaFX application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the application, sets up the UI components and event handlers.
     * This method is called automatically when the application starts.
     * @param primaryStage The primary stage for this application.
     */
    @Override
    public void start(Stage primaryStage) {
        App.primaryStage = primaryStage;
        
        // Chargement des icônes de l'application avec ResourceLoader
        addApplicationIcon(primaryStage, "images/Icones/logo16.png");
        addApplicationIcon(primaryStage, "images/Icones/logo32.png");
        addApplicationIcon(primaryStage, "images/Icones/logo48.png");
        addApplicationIcon(primaryStage, "images/Icones/logo64.png");

        Login login = new Login();
        loginScene = new Scene(login);
        Scene dashBoardScene = new Scene(dashboard);
        
        // We'll only create the admin scene when needed, not at startup

        PharmacyDashboardModifier.modifyDashboard(dashboard);

        primaryStage.setTitle("Pharmacie - Connexion");
        primaryStage.setScene(loginScene);
        primaryStage.show();

        login.getLoginButton().setOnAction(e -> handleLogin(login, primaryStage, dashBoardScene));
    }

    /**
     * Ajoute une icône à la fenêtre principale de l'application
     * @param stage La fenêtre qui doit recevoir l'icône
     * @param path Le chemin vers l'icône
     */
    private void addApplicationIcon(Stage stage, String path) {
        Image iconImage = ResourceLoader.loadImage(path);
        if (iconImage != null) {
            stage.getIcons().add(iconImage);
        }
    }

    /**
     * Creates a fresh instance of the admin dashboard scene.
     * @return A new Scene containing the admin dashboard.
     * @throws IOException If loading the FXML fails.
     */
    private Scene createAdminDashboardScene() throws IOException {
        Parent dashboardAdmin = FXMLLoader.load(getClass().getResource("/com/pharmacie/view/DashboardAdmin.fxml"));
        return new Scene(dashboardAdmin);
    }

    /**
     * Handles the login process when the user clicks the login button.
     * Validates input fields and initiates the authentication process.
     *
     * @param login The login controller containing user credentials.
     * @param primaryStage The main application window.
     * @param dashBoardScene The scene to show if login is successful for non-admin users.
     */
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

    /**
     * Creates a background task that performs the API call for user authentication.
     *
     * @param email User's email address.
     * @param password User's password.
     * @return A Task that will return the login response from the server.
     */
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

    /**
     * Sets up handlers for the login task completion or failure.
     * Handles UI updates based on login result and user role.
     *
     * @param loginTask The task performing the login operation.
     * @param primaryStage The main application window.
     * @param dashBoardScene The scene to show if login is successful for non-admin users.
     */
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
                            try {
                                // Always create a fresh admin dashboard scene
                                Scene freshAdminScene = createAdminDashboardScene();
                                primaryStage.setScene(freshAdminScene);
                            } catch (IOException e) {
                                showAlert("Erreur", "Impossible de charger le tableau de bord administrateur: " + e.getMessage());
                                e.printStackTrace();
                            }
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
                e.printStackTrace();
            }
        });

        loginTask.setOnFailed(event -> {
            showAlert("Connexion impossible",
                    "Erreur réseau: " + loginTask.getException().getMessage());
        });
    }

    /**
     * Displays an alert dialog with the specified title and content.
     * Used for showing error messages and notifications to the user.
     *
     * @param title The title of the alert dialog.
     * @param content The message to display in the alert dialog.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}