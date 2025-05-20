package com.pharmacie.service;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Service de gestion des notifications pour l'application.
 * Permet d'afficher des notifications à l'utilisateur pour différents événements
 * comme les alertes de stock ou les médicaments périmés.
 * Cette classe utilise le modèle de conception Singleton.
 */
public class NotificationService {
    
    private static NotificationService instance;
    private final List<String> notifications = new ArrayList<>();
    private final List<Stage> activeNotifications = new ArrayList<>();
    private int notificationCount = 0;
    private Timer checkTimer;
    
    // Couleurs pour les différents types de notifications
    private static final String INFO_COLOR = "#2196F3";
    private static final String WARNING_COLOR = "#FF9800";
    private static final String ERROR_COLOR = "#F44336";
    private static final String SUCCESS_COLOR = "#4CAF50";
    
    /**
     * Constructeur privé pour implémenter le pattern Singleton.
     */
    private NotificationService() {
        // Constructeur privé pour le singleton
    }
    
    /**
     * Fournit le point d'accès global à l'instance unique de NotificationService.
     *
     * @return L'instance unique de NotificationService.
     */
    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }
    
    /**
     * Démarre le service de vérification périodique pour les notifications.
     * Si un minuteur de vérification existe déjà, il est annulé et un nouveau est créé.
     * La vérification est effectuée toutes les minutes après un délai initial de 5 secondes.
     */
    public void startNotificationService() {
        if (checkTimer != null) {
            checkTimer.cancel();
        }
        
        checkTimer = new Timer(true);
        checkTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkForNotifications();
            }
        }, 5000, 60000); // Vérifier toutes les minutes après un délai initial de 5 secondes
    }
    
    /**
     * Arrête le service de notifications.
     * Annule le minuteur de vérification s'il est actif.
     */
    public void stopNotificationService() {
        if (checkTimer != null) {
            checkTimer.cancel();
            checkTimer = null;
        }
    }
    
    /**
     * Vérifie s'il y a de nouvelles notifications à afficher.
     * Cette méthode est conçue pour être appelée périodiquement.
     * Actuellement, elle simule la réception de notifications.
     */
    private void checkForNotifications() {
        // Cette méthode pourrait interroger l'API ou une base de données locale
        // Pour démonstration, on va ajouter quelques notifications de test périodiques
        if (Math.random() < 0.3) { // 30% de chance d'avoir une notification
            String[] possibleNotifications = {
                "Paracétamol 1g: Stock faible (5 restants)",
                "Amoxicilline 500mg: Date de péremption proche (15 jours)",
                "Ibuprofène 400mg: En rupture de stock",
                "Aspirine 500mg: Commande confirmée, livraison prévue demain",
                "Oméprazole 20mg: 3 commandes déposées aujourd'hui"
            };
            
            int index = (int) (Math.random() * possibleNotifications.length);
            String notification = possibleNotifications[index];
            
            // Ajouter et afficher la notification
            addNotification(notification);
            
            // Pour les 3 premiers types, on les affiche comme alertes
            if (index <= 2) {
                String type = index == 0 ? "warning" : (index == 1 ? "info" : "error");
                Platform.runLater(() -> showNotification(notification, type));
            }
        }
    }
    
    /**
     * Ajoute une notification à la liste des notifications.
     * Incrémente également le compteur de notifications.
     * Cette méthode est synchronisée pour gérer l'accès concurrentiel.
     *
     * @param message Le message de la notification à ajouter.
     */
    public void addNotification(String message) {
        synchronized (notifications) {
            notifications.add(message);
            notificationCount++;
        }
    }
    
    /**
     * Affiche une notification temporaire à l'utilisateur.
     * La notification est stylisée en fonction de son type (info, warning, error, success)
     * et s'affiche en haut à droite de l'écran. Elle disparaît automatiquement après un délai.
     *
     * @param message Le message à afficher dans la notification.
     * @param type Le type de notification (par exemple, "info", "warning", "error", "success").
     *             Détermine la couleur de l'indicateur de la notification.
     */
    public void showNotification(String message, String type) {
        Platform.runLater(() -> {
            // Déterminer la couleur selon le type
            String colorHex;
            switch (type.toLowerCase()) {
                case "warning":
                    colorHex = WARNING_COLOR;
                    break;
                case "error":
                    colorHex = ERROR_COLOR;
                    break;
                case "success":
                    colorHex = SUCCESS_COLOR;
                    break;
                default:
                    colorHex = INFO_COLOR;
                    break;
            }
            
            // Créer la scène de notification
            Stage notificationStage = new Stage();
            notificationStage.initStyle(StageStyle.TRANSPARENT);
            notificationStage.setAlwaysOnTop(true);
            notificationStage.initModality(Modality.NONE);
            
            // Créer un indicateur de type
            Circle indicator = new Circle(8);
            indicator.setFill(Color.web(colorHex));
            
            // Créer le label pour le message
            Label messageLabel = new Label(message);
            messageLabel.setWrapText(true);
            messageLabel.setMaxWidth(300);
            messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
            
            // Créer le bouton de fermeture
            Button closeButton = new Button("×");
            closeButton.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-font-size: 16px; " +
                "-fx-text-fill: #888888; " +
                "-fx-padding: 0 5 0 5;"
            );
            closeButton.setOnAction(e -> {
                closeNotification(notificationStage);
            });
            
            // Layout horizontal pour l'indicateur et le message
            HBox contentBox = new HBox(10, indicator, messageLabel);
            contentBox.setAlignment(Pos.CENTER_LEFT);
            
            // Layout principal
            HBox mainBox = new HBox(10, contentBox, closeButton);
            mainBox.setAlignment(Pos.CENTER_LEFT);
            mainBox.setStyle(
                "-fx-background-color: white; " +
                "-fx-padding: 10 15 10 15; " +
                "-fx-background-radius: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);"
            );
            
            Scene scene = new Scene(mainBox);
            scene.setFill(Color.TRANSPARENT);
            
            notificationStage.setScene(scene);
            notificationStage.sizeToScene();
            
            // Positionner en haut à droite de l'écran principal
            double offsetY = 20;
            for (Stage existingNotification : activeNotifications) {
                offsetY += existingNotification.getHeight() + 10;
            }
            
            notificationStage.setX(javafx.stage.Screen.getPrimary().getVisualBounds().getMaxX() - notificationStage.getWidth() - 20);
            notificationStage.setY(javafx.stage.Screen.getPrimary().getVisualBounds().getMinY() + offsetY);
            
            // Ajouter à la liste des notifications actives
            activeNotifications.add(notificationStage);
            
            // Montrer la notification avec animation
            notificationStage.show();
            
            // Animation d'entrée
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(500), mainBox);
            slideIn.setFromX(100);
            slideIn.setToX(0);
            slideIn.play();
            
            // Fermeture automatique après un délai
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> closeNotification(notificationStage));
                }
            }, 10000); // 10 secondes
        });
    }
    
    /**
     * Ferme une notification spécifiée avec une animation de fondu.
     * Si la notification n'est pas affichée, la méthode ne fait rien.
     * Après la fermeture, la notification est retirée de la liste des notifications actives
     * et les notifications restantes sont réorganisées.
     *
     * @param notificationStage La fenêtre (Stage) de la notification à fermer.
     */
    private void closeNotification(Stage notificationStage) {
        if (!notificationStage.isShowing()) {
            return;
        }
        
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), notificationStage.getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            notificationStage.close();
            activeNotifications.remove(notificationStage);
            // Réorganiser les notifications restantes
            reorganizeNotifications();
        });
        fadeOut.play();
    }
    
    /**
     * Réorganise les positions verticales des notifications actives à l'écran.
     * Cette méthode est typiquement appelée après la fermeture d'une notification
     * pour éviter les superpositions ou les espaces vides.
     */
    private void reorganizeNotifications() {
        double offsetY = 20;
        for (Stage notification : activeNotifications) {
            TranslateTransition slide = new TranslateTransition(Duration.millis(300), notification.getScene().getRoot());
            notification.setY(javafx.stage.Screen.getPrimary().getVisualBounds().getMinY() + offsetY);
            offsetY += notification.getHeight() + 10;
        }
    }
    
    /**
     * Renvoie le nombre actuel de notifications non lues.
     *
     * @return Le nombre de notifications.
     */
    public int getNotificationCount() {
        return notificationCount;
    }
    
    /**
     * Récupère une copie de la liste des messages de notification.
     *
     * @return Une nouvelle liste contenant tous les messages de notification.
     */
    public List<String> getNotifications() {
        return new ArrayList<>(notifications);
    }
    
    /**
     * Marque toutes les notifications comme lues en réinitialisant le compteur de notifications à zéro.
     * Cette méthode est synchronisée pour gérer l'accès concurrentiel.
     */
    public void markAllAsRead() {
        synchronized(notifications) {
            notificationCount = 0;
        }
    }
    
    /**
     * Efface toutes les notifications de la liste et réinitialise le compteur de notifications.
     * Cette méthode est synchronisée pour gérer l'accès concurrentiel.
     */
    public void clearAllNotifications() {
        synchronized(notifications) {
            notifications.clear();
            notificationCount = 0;
        }
    }
}
