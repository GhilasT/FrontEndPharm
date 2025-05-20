package com.pharmacie.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import com.pharmacie.model.dto.MedecinCreateRequest;
import com.pharmacie.model.dto.MedecinResponse;
import com.pharmacie.service.ApiRest;
import javafx.concurrent.Task;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contrôleur pour l'ajout d'un nouveau médecin.
 * 
 * Cette classe permet de :
 * - Saisir les informations d'un nouveau médecin (RPPS, civilité, nom, etc.).
 * - Vérifier si un médecin avec le même RPPS existe déjà.
 * - Envoyer une requête de création au serveur.
 * - Notifier le contrôleur parent de la création réussie.
 * 
 * Le contrôleur effectue les opérations réseau dans un thread séparé pour ne pas
 * bloquer l'interface utilisateur. Il communique avec le contrôleur principal des
 * médecins pour rafraîchir la liste après un ajout réussi.
 */
public class AddMedecinController {

    @FXML
    private TextField civiliteField, nomField, prenomField, rppsField, professionField, modeExerciceField,
            qualificationsField, structureExerciceField, fonctionActiviteField, genreActiviteField;
    @FXML
    private TextField categorieProfessionnelleField;
    @FXML
    private RadioButton radioBtnMr, radioBtnMme;
    @FXML
    private Button btnAjouterMedecin;
    @FXML
    private MedecinsController medecinsController;

    /**
     * Initialise les composants de l'interface utilisateur.
     * Configure les boutons radio pour la civilité.
     */
    @FXML
    private void initialize() {
        // Créer un ToggleGroup pour la civilité
        ToggleGroup civiliteGroup = new ToggleGroup();
        radioBtnMr.setToggleGroup(civiliteGroup);
        radioBtnMme.setToggleGroup(civiliteGroup);
    }

    // Nouveau champ pour conserver le médecin créé
    private MedecinResponse createdMedecin;

    /**
     * Gère l'ajout d'un médecin en validant les données et en envoyant une requête au serveur.
     * 
     * @param event L'événement déclenché par le clic sur le bouton "Ajouter".
     */
    @FXML
    private void handleAjouterMedecin(ActionEvent event) {
        String rpps = rppsField.getText().trim();
        String civilite = radioBtnMr.isSelected() ? "M." : "Mme";
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String profession = professionField.getText().trim();
        String categorieProfessionnelle = categorieProfessionnelleField.getText().trim();
        String modeExercice = modeExerciceField.getText().trim();
        String qualifications = qualificationsField.getText().trim();
        String structureExercice = structureExerciceField.getText().trim();
        String fonctionActivite = fonctionActiviteField.getText().trim();
        String genreActivite = genreActiviteField.getText().trim();

        MedecinCreateRequest request = new MedecinCreateRequest(
                civilite,
                nom,
                prenom,
                rpps,
                categorieProfessionnelle,
                profession,
                modeExercice,
                qualifications,
                structureExercice,
                fonctionActivite,
                genreActivite
        );

        Task<MedecinResponse> task = new Task<>() {
            @Override
            protected MedecinResponse call() throws Exception {
                // Vérifier si le médecin existe
                MedecinResponse existing = ApiRest.checkMedecinByRpps(rpps);
                if (existing != null) {
                    throw new Exception("Le médecin avec ce numéro RPPS existe déjà");
                }
                // Créer le médecin et récupérer la réponse
                return ApiRest.createMedecin(request);
            }
        };

        task.setOnSucceeded(e -> {
            createdMedecin = task.getValue();
            
            // Afficher le message de succès
            javafx.application.Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Médecin ajouté avec succès");
                alert.setContentText("Le médecin " + nom + " " + prenom + " a été ajouté avec succès.");
                alert.showAndWait();
            });
            
            // Rafraîchir la liste principale
            if (medecinsController != null) {
                medecinsController.refreshMedecinsList();
            }

            // Fermer le formulaire
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();
        });

        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            javafx.application.Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Impossible d'ajouter le médecin");
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
            });
        });

        new Thread(task).start();
    }

    /**
     * Retourne le médecin créé après une création réussie.
     * 
     * @return Un objet MedecinResponse représentant le médecin créé.
     */
    public MedecinResponse getCreatedMedecin() {
        return createdMedecin;
    }

    /**
     * Affiche une alerte avec le type, le titre, l'en-tête et le contenu spécifiés.
     * 
     * @param type    Le type de l'alerte (INFORMATION, ERREUR, etc.).
     * @param title   Le titre de l'alerte.
     * @param header  L'en-tête de l'alerte.
     * @param content Le contenu de l'alerte.
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Définit le contrôleur parent pour permettre la communication après l'ajout d'un médecin.
     * 
     * @param medecinsController Le contrôleur parent des médecins.
     */
    public void setMedecinsController(MedecinsController medecinsController) {
        this.medecinsController = medecinsController;
    }

    /**
     * Gère l'annulation de l'ajout d'un médecin en fermant la fenêtre actuelle.
     * 
     * @param event L'événement déclenché par le clic sur le bouton "Annuler".
     */
    @FXML
    private void handleAnnuler(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}