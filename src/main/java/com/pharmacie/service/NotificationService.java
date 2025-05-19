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
    
    private NotificationService() {
        // Constructeur privé pour le singleton
    }
    
    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }
    
    /**
     * Démarre le service de vérification périodique pour les notifications
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
     * Arrête le service de notifications
     */
    public void stopNotificationService() {
        if (checkTimer != null) {
            checkTimer.cancel();
            checkTimer = null;
        }
    }
    
    /**
     * Vérifie s'il y a de nouvelles notifications à afficher
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
     * Ajoute une notification à la liste
     * @param message Le message de notification
     */
    public void addNotification(String message) {
        synchronized (notifications) {
            notifications.add(message);
            notificationCount++;
        }
    }
    
    /**
     * Affiche une notification temporaire à l'utilisateur
     * @param message Le message à afficher
     * @param type Le type de notification (info, warning, error, success)
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
     * Ferme une notification avec animation
     * @param notificationStage La fenêtre de notification à fermer
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
     * Réorganise les positions des notifications actives après fermeture d'une notification
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
     * Renvoie le nombre de notifications non lues
     * @return Nombre de notifications
     */
    public int getNotificationCount() {
        return notificationCount;
    }
    
    /**
     * Récupère la liste des notifications
     * @return Liste des messages de notification
     */
    public List<String> getNotifications() {
        return new ArrayList<>(notifications);
    }
    
    /**
     * Marque toutes les notifications comme lues
     */
    public void markAllAsRead() {
        synchronized(notifications) {
            notificationCount = 0;
        }
    }
    
    /**
     * Efface toutes les notifications
     */
    public void clearAllNotifications() {
        synchronized(notifications) {
            notifications.clear();
            notificationCount = 0;
        }
    }
}
