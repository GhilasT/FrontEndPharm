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

public class AddMedecinController {

    @FXML private TextField civiliteField, nomField, prenomField, rppsField, professionField, modeExerciceField,
            qualificationsField, structureExerciceField, fonctionActiviteField, genreActiviteField;
    @FXML private TextField categorieProfessionnelleField;
    @FXML private RadioButton radioBtnMr, radioBtnMme;
    @FXML private Button btnAjouterMedecin;
    @FXML
    private MedecinsController medecinsController;

    @FXML
    private void initialize() {
        // Créer un ToggleGroup pour la civilité
        ToggleGroup civiliteGroup = new ToggleGroup();
        radioBtnMr.setToggleGroup(civiliteGroup);
        radioBtnMme.setToggleGroup(civiliteGroup);
    }



    @FXML
    private void handleAjouterMedecin(ActionEvent event) {
        // Récupérer les informations du formulaire
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

        // Créer l'objet MedecinCreateRequest
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

        // Appel à l'API pour créer le médecin
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Appeler la méthode qui vérifie si le médecin existe déjà
                    MedecinResponse response = ApiRest.checkMedecinByRpps(rpps);

                    // Si un médecin existe déjà
                    if (response != null) {
                        showAlert(Alert.AlertType.ERROR, "Médecin déjà existant",
                                "Le médecin avec ce numéro RPPS existe déjà",
                                "Veuillez vérifier le numéro RPPS.");
                    } else {
                        // Si aucun médecin n'existe, on le crée
                        ApiRest.createMedecin(request);
                        showAlert(Alert.AlertType.INFORMATION, "Succès", "Médecin créé",
                                "Le médecin a été ajouté avec succès.");

                        // Rafraîchir la liste des médecins dans le contrôleur principal
                        if (medecinsController != null) {
                            medecinsController.refreshMedecinsList(); // Rafraîchit la liste
                        }

                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.close();
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Problème de connexion",
                            "Une erreur s'est produite lors de la création du médecin.");
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setMedecinsController(MedecinsController medecinsController) {
        this.medecinsController = medecinsController;
    }
    @FXML
    private void handleAnnuler(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }




}