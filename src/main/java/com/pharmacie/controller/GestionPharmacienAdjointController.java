package com.pharmacie.controller;

import java.time.LocalDate;

import com.pharmacie.model.PharmacienAdjoint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class GestionPharmacienAdjointController {

    @FXML private TableView<PharmacienAdjoint> pharmacienTable;
    @FXML private TableColumn<PharmacienAdjoint, Integer> idColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> nomColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> prenomColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> emailColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> telephoneColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> adresseColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> matriculeColumn;
    @FXML private TableColumn<PharmacienAdjoint, LocalDate> dateEmbaucheColumn;
    @FXML private TableColumn<PharmacienAdjoint, Double> salaireColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> posteColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> statutContratColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> diplomeColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> emailProColumn;

    private ObservableList<PharmacienAdjoint> pharmacienData = FXCollections.observableArrayList();
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
        emailProColumn.setCellValueFactory(new PropertyValueFactory<>("emailPro"));

        // Données de test
        pharmacienData.add(new PharmacienAdjoint(
            1, "Dubois", "Marie", "marie.dubois@pharma.com", "0787654321",
            "22 Rue des Médicaments", "PHARM002", LocalDate.now(), 2800.0,
            "Pharmacien Adjoint", "CDI", "Doctorat en pharmacie", "marie.dubois@pharma-pro.com"
        ));
        pharmacienTable.setItems(pharmacienData);
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
        // Logique d'ajout d'un nouvel pharmacien adjoint
    }

    @FXML
    private void handleModifier() {
        PharmacienAdjoint selected = pharmacienTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Logique de modification de l'pharmacien adjoint sélectionné
        }
    }

    @FXML
    private void handleSupprimer() {
        PharmacienAdjoint selected = pharmacienTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            pharmacienData.remove(selected);
        }
    }
}
