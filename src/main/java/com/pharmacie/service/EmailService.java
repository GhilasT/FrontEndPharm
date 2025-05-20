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

/**
 * Service pour l'envoi d'e-mails, notamment pour les commandes aux fournisseurs.
 * Interagit avec une API backend pour l'envoi effectif des e-mails.
 */
public class EmailService {

    private final HttpClient client = HttpClient.newHttpClient();
    private static final String BASE_URL = Global.getBaseUrl();
    private static final String EMAIL_ENDPOINT = "/api/email";
    private static final String EMAIL_TO_FOURNISSEUR_ENDPOINT = "/sendCommandeAuFournisseur";
    private static final String EMAIL_URL = BASE_URL + EMAIL_ENDPOINT;
    private static final String EMAIL_URL_FOURNISSEUR = BASE_URL + EMAIL_TO_FOURNISSEUR_ENDPOINT;

    /**
     * Envoie un e-mail de commande à un fournisseur en utilisant la référence de la commande.
     * La méthode s'exécute de manière asynchrone et affiche des notifications sur le succès ou l'échec.
     *
     * @param reference La référence de la commande à inclure dans l'e-mail.
     */
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

    /**
     * Exécute une tâche {@link Task} dans un nouveau thread.
     * Utilisé pour effectuer des opérations réseau (comme l'envoi d'e-mails) de manière asynchrone.
     *
     * @param task La tâche à exécuter.
     */
    private void executerTache(Task<HttpResponse<String>> task) {
        new Thread(task).start();
    }

    /**
     * Affiche une boîte de dialogue d'alerte JavaFX.
     *
     * @param type Le type d'alerte (par exemple, INFORMATION, ERROR).
     * @param titre Le titre de la fenêtre d'alerte.
     * @param entete Le texte d'en-tête de l'alerte.
     * @param contenu Le message principal de l'alerte.
     */
    private void afficherMessage(AlertType type, String titre, String entete, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
