package com.pharmacie.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.pharmacie.model.Fournisseur;
import com.pharmacie.model.Employe;
import com.pharmacie.model.Commande;
import com.pharmacie.model.LigneCommande;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service utilitaire pour afficher divers types de boîtes de dialogue et popups JavaFX.
 * Fournit des méthodes pour afficher des messages d'alerte, des informations sur les fournisseurs,
 * les employés et les détails des commandes.
 */
public class DialogService {

    /**
     * Affiche une boîte de dialogue d'alerte standard.
     *
     * @param type Le type d'alerte (par exemple, {@link AlertType#INFORMATION}, {@link AlertType#WARNING}).
     * @param titre Le titre de la fenêtre de dialogue.
     * @param entete Le texte d'en-tête de la boîte de dialogue.
     * @param contenu Le message principal de la boîte de dialogue.
     */
    public static void afficherMessage(AlertType type, String titre, String entete, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    /**
     * Affiche une popup avec les informations détaillées d'un fournisseur.
     *
     * @param fournisseur L'objet {@link Fournisseur} dont les informations doivent être affichées.
     */
    public static void afficherPopupFournisseur(Fournisseur fournisseur) {
        if (fournisseur == null) {
            afficherMessage(AlertType.WARNING, "Avertissement", "Fournisseur non valide",
                    "Aucun fournisseur valide fourni.");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Informations sur le Fournisseur");

        VBox content = new VBox();
        content.setSpacing(15);
        content.setStyle("-fx-padding: 20; -fx-background-color: #f9f9f9;");

        Label titreLabel = new Label("Détails du Fournisseur");
        titreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        Label nomSocieteLabel = new Label("Nom Société : " + (fournisseur.getNomSociete() != null ? fournisseur.getNomSociete() : "Non renseigné"));
        Label adresseLabel = new Label("Adresse : " + (fournisseur.getAdresse() != null ? fournisseur.getAdresse() : "Non renseignée"));
        Label telephoneLabel = new Label("Téléphone : " + (fournisseur.getTelephone() != null ? fournisseur.getTelephone() : "Non renseigné"));
        Label emailLabel = new Label("Email : " + (fournisseur.getEmail() != null ? fournisseur.getEmail() : "Non renseigné"));
        Label sujetFonctionLabel = new Label("Sujet Fonction : " + (fournisseur.getSujetFonction() != null ? fournisseur.getSujetFonction() : "Non renseigné"));
        Label faxLabel = new Label("Fax : " + (fournisseur.getFax() != null ? fournisseur.getFax() : "Non renseigné"));
        Label idFournisseurLabel = new Label("ID Fournisseur : " + (fournisseur.getIdFournisseur() != null ? fournisseur.getIdFournisseur() : "Non renseigné"));

        for (Label label : new Label[]{nomSocieteLabel, adresseLabel, telephoneLabel, emailLabel, sujetFonctionLabel, faxLabel, idFournisseurLabel}) {
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495E;");
        }

        content.getChildren().addAll(
            titreLabel, nomSocieteLabel, adresseLabel, telephoneLabel, emailLabel, sujetFonctionLabel, faxLabel, idFournisseurLabel
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    /**
     * Affiche une popup avec les informations détaillées d'un employé.
     *
     * @param employe L'objet {@link Employe} dont les informations doivent être affichées.
     */
    public static void afficherPopupEmployer(Employe employe) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Informations sur l'employé Responsable");

        VBox content = new VBox();
        content.setSpacing(15);
        content.setStyle("-fx-padding: 20; -fx-background-color: #f9f9f9;");

        Label titreLabel = new Label("Détails de l'Employé");
        titreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        Label nomLabel = new Label("Nom : " + employe.getNom());
        Label prenomLabel = new Label("Prénom : " + employe.getPrenom());
        Label posteLabel = new Label("Poste : " + employe.getPoste());
        Label emailLabel = new Label("Email : " + employe.getEmail());
        Label telephoneLabel = new Label("Téléphone : " + employe.getTelephone());
        Label adresseLabel = new Label("Adresse : " + employe.getAdresse());
        Label matriculeLabel = new Label("Matricule : " + employe.getMatricule());
        Label emailProLabel = new Label("Email Professionnel : " + employe.getEmailPro());

        for (Label label : new Label[]{nomLabel, prenomLabel, posteLabel, emailLabel, telephoneLabel, adresseLabel, matriculeLabel, emailProLabel}) {
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495E;");
        }

        content.getChildren().addAll(
            titreLabel, nomLabel, prenomLabel, posteLabel, emailLabel, telephoneLabel, adresseLabel, matriculeLabel, emailProLabel
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    /**
     * Affiche une popup avec les informations détaillées d'une commande, y compris ses lignes de commande.
     * Si la commande est null, un message d'avertissement est affiché.
     *
     * @param commande L'objet {@link Commande} dont les informations doivent être affichées.
     */
    public static void afficherPopupCommandeInfo(Commande commande) {
        if (commande == null) {
            afficherMessage(AlertType.WARNING, "Avertissement", "Commande non valide",
                    "Aucune commande valide fournie.");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Page Commande Info");

        VBox content = new VBox();
        content.setSpacing(15);
        content.setStyle("-fx-padding: 20; -fx-background-color: #f9f9f9;");

        Label label = new Label("Commande Info");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        content.getChildren().add(label);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        int row = 0;
        for (LigneCommande ligneCommande : commande.getLigneCommandes()) {
            String nomMedicament = ligneCommande.getMedicament().getDenomination();
            BigDecimal prixUnitaire = ligneCommande.getPrixUnitaire();
            int quantite = ligneCommande.getQuantite();
            BigDecimal prixTotalLigne = ligneCommande.getMontantLigne();

            Label labelNom = new Label("Nom : " + nomMedicament);
            Label labelQuantite = new Label("Quantité : " + quantite);
            Label labelPrixUnitaire = new Label("Prix Unitaire : " + prixUnitaire + " €");
            Label labelPrixTotal = new Label("Prix Total : " + prixTotalLigne + " €");

            for (Label l : new Label[]{labelNom, labelQuantite, labelPrixUnitaire, labelPrixTotal}) {
                l.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495E;");
            }

            grid.add(labelNom, 0, row);
            grid.add(labelQuantite, 1, row);
            grid.add(labelPrixUnitaire, 2, row);
            grid.add(labelPrixTotal, 3, row);

            row++;
        }

        content.getChildren().add(grid);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.setOnCloseRequest(event -> dialog.close());
        dialog.showAndWait();
    }
}
