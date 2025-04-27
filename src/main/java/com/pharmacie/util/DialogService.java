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

public class DialogService {

    public static void afficherMessage(AlertType type, String titre, String entete, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    public static void afficherPopupFournisseur(Fournisseur fournisseur) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Informations sur le Fournisseur");

        VBox content = new VBox();
        content.setSpacing(15);
        content.setStyle("-fx-padding: 20; -fx-background-color: #f9f9f9;");

        Label titreLabel = new Label("Détails du Fournisseur");
        titreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        Label nomSocieteLabel = new Label("Nom Société : " + fournisseur.getNomSociete());
        Label adresseLabel = new Label("Adresse : " + fournisseur.getAdresse());
        Label telephoneLabel = new Label("Téléphone : " + fournisseur.getTelephone());
        Label emailLabel = new Label("Email : " + fournisseur.getEmail());
        Label sujetFonctionLabel = new Label("Sujet Fonction : " + fournisseur.getSujetFonction());
        Label faxLabel = new Label("Fax : " + fournisseur.getFax());
        Label idFournisseurLabel = new Label("ID Fournisseur : " + fournisseur.getIdFournisseur());

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
