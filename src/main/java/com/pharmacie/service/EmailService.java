package com.pharmacie.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.pharmacie.util.Global;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import com.pharmacie.model.dto.EmailCommandeRequest ;


public class EmailService {

    private final HttpClient client = HttpClient.newHttpClient();
    private static final String BASE_URL = Global.getBaseUrl();
    private static final String EMAIL_ENDPOINT = "/api/email";
    private static final String EMAIL_TO_FOURNISSEUR_ENDPOINT = "/sendCommandeAuFournisseur";
    private static final String EMAIL_URL = BASE_URL + EMAIL_ENDPOINT;
    private static final String EMAIL_URL_FOURNISSEUR = BASE_URL + EMAIL_TO_FOURNISSEUR_ENDPOINT;


    public void envoyerMailFournisseur(String reference) {
        EmailCommandeRequest emailCommandeRequest = new EmailCommandeRequest();
        emailCommandeRequest.setCommandeReference(reference);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(EMAIL_URL_FOURNISSEUR))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(15))
                .POST(HttpRequest.BodyPublishers.ofString(emailCommandeRequest.toJson()))
                .build();

        executerTache(new Task<>() {
            @Override
            protected HttpResponse<String> call() throws Exception {
                return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            }

            @Override
            protected void succeeded() {
                HttpResponse<String> response = getValue();
                Platform.runLater(() -> {
                    if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        afficherMessage(AlertType.INFORMATION, "Email envoyé", "Email envoyé avec succès", "L'email a été envoyé avec succès.");
                    } else {
                        afficherMessage(AlertType.ERROR, "Erreur d'envoi", "Impossible d'envoyer l'email", "Code: " + response.statusCode() + "\nDétails: " + response.body());
                    }
                });
            }

            @Override
            protected void failed() {
                Throwable exception = getException();
                Platform.runLater(() -> afficherMessage(AlertType.ERROR, "Erreur de connexion", "Impossible d'envoyer l'email", "Détails: " + exception.getMessage()));
            }
        });
    }

    private void executerTache(Task<HttpResponse<String>> task) {
        new Thread(task).start();
    }

    private void afficherMessage(AlertType type, String titre, String entete, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
