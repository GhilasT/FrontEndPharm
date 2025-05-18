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



    // 1) Nouveau champ pour conserver le médecin créé
    private MedecinResponse createdMedecin;

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

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // 2) Vérifier si le médecin existe
                    MedecinResponse existing = ApiRest.checkMedecinByRpps(rpps);
                    if (existing != null) {
                        showAlert(Alert.AlertType.ERROR,
                                "Médecin déjà existant",
                                "Le médecin avec ce numéro RPPS existe déjà",
                                null);
                    } else {
                        // 3) Créer le médecin et récupérer la réponse
                        createdMedecin = ApiRest.createMedecin(request);

                        showAlert(Alert.AlertType.INFORMATION,
                                "Succès",
                                "Médecin créé",
                                "Le médecin a été ajouté avec succès.");

                        // 4) Rafraîchir la liste principale
                        if (medecinsController != null) {
                            medecinsController.refreshMedecinsList();
                        }

                        // 5) Fermer le formulaire
                        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        stage.close();
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR,
                            "Erreur",
                            "Problème de connexion",
                            e.getMessage());
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    /**
     * Exposé pour que MedecinsController sache quel médecin vient d'être créé.
     */
    public MedecinResponse getCreatedMedecin() {
        return createdMedecin;
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