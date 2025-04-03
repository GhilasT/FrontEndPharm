package com.pharmacie.controller;

import com.pharmacie.model.Preparateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;

public class GestionPreparateurController {

    @FXML private TableView<Preparateur> preparateurTable;
    @FXML private TableColumn<Preparateur, Integer> idColumn;
    @FXML private TableColumn<Preparateur, String> nomColumn;
    @FXML private TableColumn<Preparateur, String> prenomColumn;
    @FXML private TableColumn<Preparateur, String> emailColumn;
    @FXML private TableColumn<Preparateur, String> telephoneColumn;
    @FXML private TableColumn<Preparateur, String> adresseColumn;
    @FXML private TableColumn<Preparateur, String> matriculeColumn;
    @FXML private TableColumn<Preparateur, LocalDate> dateEmbaucheColumn;
    @FXML private TableColumn<Preparateur, Double> salaireColumn;
    @FXML private TableColumn<Preparateur, String> posteColumn;
    @FXML private TableColumn<Preparateur, String> statutContratColumn;
    @FXML private TableColumn<Preparateur, String> diplomeColumn;
    @FXML private TableColumn<Preparateur, String> emailProColumn;
    
    @FXML private Button modifierButton, supprimerButton;
    private ObservableList<Preparateur> preparateurData = FXCollections.observableArrayList();
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

        preparateurTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            modifierButton.setDisable(newVal == null);
            supprimerButton.setDisable(newVal == null);
        });

        // Données de test
        preparateurData.add(new Preparateur(
            1, "Martin", "Lucie", "lucie.martin@example.com", "0612345678",
            "45 Rue des Préparateurs", "PREP001", LocalDate.now(), 2500.0,
            "Préparateur en chef", "CDI", "Bac+3", "lucie.martin@pharma.com"
        ));
        preparateurTable.setItems(preparateurData);
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
        // Logique d'ajout
    }

    @FXML
    private void handleModifier() {
        Preparateur selected = preparateurTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Logique de modification
        }
    }

    @FXML
    private void handleSupprimer() {
        Preparateur selected = preparateurTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            preparateurData.remove(selected);
        }
    }
}