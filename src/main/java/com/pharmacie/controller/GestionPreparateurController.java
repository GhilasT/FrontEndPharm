package com.pharmacie.controller;

import com.pharmacie.model.Preparateur;
import com.pharmacie.model.dto.PreparateurCreateRequest;
import com.pharmacie.model.dto.PreparateurUpdateRequest;
import com.pharmacie.service.PreparateurApi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GestionPreparateurController {
    @FXML private TableView<Preparateur> preparateurTable;
    @FXML private TableColumn<Preparateur, UUID> idColumn;
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
    @FXML private TextField searchField;
    @FXML private Button resetSearchButton; // Ajouté

    private ObservableList<Preparateur> preparateurData = FXCollections.observableArrayList();
    private DashboardAdminController parentController;
    private PreparateurApi preparateurService;

    @FXML
    public void initialize() {
        preparateurService = new PreparateurApi();

        // Configuration des colonnes
        configureColumns();

        // Configuration de la recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= 3 || newValue.isEmpty()) {
                handleSearch();
            }
        });

        preparateurTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            modifierButton.setDisable(newVal == null);
            supprimerButton.setDisable(newVal == null);
        });

        loadAllPreparateurs();
    }

    private void configureColumns() {
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
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();

        if (query.isEmpty()) {
            loadAllPreparateurs();
            return;
        }

        Task<List<Preparateur>> task = new Task<>() {
            @Override
            protected List<Preparateur> call() throws Exception {
                return preparateurService.searchPreparateurs(query);
            }
        };

        task.setOnSucceeded(event -> {
            preparateurData.clear();
            preparateurData.addAll(task.getValue());
            preparateurTable.setItems(preparateurData);
        });

        task.setOnFailed(event -> {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Recherche échouée : " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    @FXML
    private void handleResetSearch() {
        searchField.clear();
        loadAllPreparateurs();
    }

    private void loadAllPreparateurs() {
        Task<List<Preparateur>> task = new Task<>() {
            @Override
            protected List<Preparateur> call() throws Exception {
                return preparateurService.getAllPreparateurs();
            }
        };

        task.setOnSucceeded(event -> {
            preparateurData.clear();
            preparateurData.addAll(task.getValue());
            preparateurTable.setItems(preparateurData);
        });

        task.setOnFailed(event -> {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Chargement échoué : " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    public void setParentController(DashboardAdminController controller) {
        this.parentController = controller;
    }

    @FXML
    private void handleBack() {
        if (parentController != null) {
            parentController.loadGestionPersonnel();
        }
    }

    @FXML
    private void handleAjouter() {
        AddPreparateurDialogController dialogController = new AddPreparateurDialogController();
        Optional<ButtonType> result = dialogController.showAndWait();

        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            PreparateurCreateRequest request = dialogController.getCreateRequest();

            try {
                boolean success = preparateurService.createPreparateur(request);
                if (success) {
                    loadAllPreparateurs();
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Préparateur ajouté");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la création : " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleModifier() {
        Preparateur selected = preparateurTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            EditPreparateurDialogController dialogController = new EditPreparateurDialogController();
            dialogController.setPreparateur(selected);

            Optional<ButtonType> result = dialogController.showAndWait();

            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                PreparateurUpdateRequest request = dialogController.getUpdateRequest();
                boolean success = preparateurService.updatePreparateur(selected.getIdPersonne(), request);
                if (success) {
                    loadAllPreparateurs();
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Modifications enregistrées");
                }
            }
        }
    }

    @FXML
    private void handleSupprimer() {
        Preparateur selected = preparateurTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Supprimer " + selected.getNom() + " ?");
            confirmation.setContentText("Cette action est irréversible !");

            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    boolean success = preparateurService.deletePreparateur(selected.getIdPersonne());
                    if (success) {
                        preparateurData.remove(selected);
                        showAlert(Alert.AlertType.INFORMATION, "Succès", "Préparateur supprimé");
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression : " + e.getMessage());
                }
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
