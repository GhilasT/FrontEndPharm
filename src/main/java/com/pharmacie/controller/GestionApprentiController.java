package com.pharmacie.controller;

import com.pharmacie.model.Apprenti;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.UUID;

public class GestionApprentiController {

    @FXML private TableView<Apprenti> apprentiTable;
    @FXML private TableColumn<Apprenti, UUID> idColumn;
    @FXML private TableColumn<Apprenti, String> nomColumn;
    @FXML private TableColumn<Apprenti, String> prenomColumn;
    @FXML private TableColumn<Apprenti, String> emailColumn;
    @FXML private TableColumn<Apprenti, String> telephoneColumn;
    @FXML private TableColumn<Apprenti, String> adresseColumn;
    @FXML private TableColumn<Apprenti, String> matriculeColumn;
    @FXML private TableColumn<Apprenti, LocalDate> dateEmbaucheColumn;
    @FXML private TableColumn<Apprenti, Double> salaireColumn;
    @FXML private TableColumn<Apprenti, String> posteColumn;
    @FXML private TableColumn<Apprenti, String> statutContratColumn;
    @FXML private TableColumn<Apprenti, String> diplomeColumn;
    @FXML private TableColumn<Apprenti, String> ecoleColumn; // Colonne supplémentaire
    @FXML private TableColumn<Apprenti, String> emailProColumn;

    private ObservableList<Apprenti> apprentiData = FXCollections.observableArrayList();
    private DashboardAdminController parentController;

    @FXML
    public void initialize() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idPersonne"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        matriculeColumn.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        dateEmbaucheColumn.setCellValueFactory(new PropertyValueFactory<>("dateEmbauche"));
        salaireColumn.setCellValueFactory(new PropertyValueFactory<>("salaire"));
        posteColumn.setCellValueFactory(new PropertyValueFactory<>("poste"));
        statutContratColumn.setCellValueFactory(new PropertyValueFactory<>("statutContrat"));
        diplomeColumn.setCellValueFactory(new PropertyValueFactory<>("diplome"));
        ecoleColumn.setCellValueFactory(new PropertyValueFactory<>("ecole"));
        emailProColumn.setCellValueFactory(new PropertyValueFactory<>("emailPro"));

        // Donnée de test
        apprentiData.add(new Apprenti(
            UUID.randomUUID(), "Leroy", "Paul", "paul.leroy@example.com", "0678912345",
            "10 Rue des Apprentis", "APPR123", LocalDate.now(), 1200.0,
            "Apprenti pharmacie", "Contrat pro", "Bac Général",
            "Lycée PharmaPlus", "paul.leroy@apprenti.com"
        ));
        apprentiTable.setItems(apprentiData);
    }

    public void setParentController(DashboardAdminController controller) {
        this.parentController = controller;
    }

    @FXML
    private void handleBack() {
        parentController.loadGestionPersonnel();
    }

    @FXML
    private void handleAjouter() {
        // Logique d'ajout d'un nouvel apprenti
    }

    @FXML
    private void handleModifier() {
        Apprenti selected = apprentiTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Logique de modification de l'apprenti sélectionné
        }
    }

    @FXML
    private void handleSupprimer() {
        Apprenti selected = apprentiTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            apprentiData.remove(selected);
        }
    }
}
