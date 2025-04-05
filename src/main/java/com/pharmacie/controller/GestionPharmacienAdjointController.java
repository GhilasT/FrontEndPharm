package com.pharmacie.controller;

import com.pharmacie.model.PharmacienAdjoint;
import com.pharmacie.model.dto.PharmacienAdjointCreateRequest;
import com.pharmacie.model.dto.PharmacienAdjointUpdateRequest;
import com.pharmacie.service.PharmacienAdjointApi;

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

public class GestionPharmacienAdjointController {
    @FXML private TableView<PharmacienAdjoint> pharmacienTable;
    @FXML private TableColumn<PharmacienAdjoint, UUID> idColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> nomColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> prenomColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> emailColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> telephoneColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> adresseColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> matriculeColumn;
    @FXML private TableColumn<PharmacienAdjoint, LocalDate> dateEmbaucheColumn;
    @FXML private TableColumn<PharmacienAdjoint, Double> salaireColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> statutContratColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> diplomeColumn;
    @FXML private TableColumn<PharmacienAdjoint, String> emailProColumn;

    @FXML private TextField searchField;
    @FXML private Button modifierButton, supprimerButton;

    private ObservableList<PharmacienAdjoint> pharmacienData = FXCollections.observableArrayList();
    private DashboardAdminController parentController;
    private PharmacienAdjointApi pharmacienService;

    @FXML
    public void initialize() {
        pharmacienService = new PharmacienAdjointApi();

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
        statutContratColumn.setCellValueFactory(new PropertyValueFactory<>("statutContrat"));
        diplomeColumn.setCellValueFactory(new PropertyValueFactory<>("diplome"));
        emailProColumn.setCellValueFactory(new PropertyValueFactory<>("emailPro"));

        pharmacienTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            modifierButton.setDisable(newVal == null);
            supprimerButton.setDisable(newVal == null);
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= 3 || newValue.isEmpty()) {
                handleSearch();
            }
        });

        loadAllPharmaciens();
    }

    private void loadAllPharmaciens() {
        Task<List<PharmacienAdjoint>> task = new Task<>() {
            @Override
            protected List<PharmacienAdjoint> call() throws Exception {
                return pharmacienService.getAllPharmaciensAdjoints();
            }
        };

        task.setOnSucceeded(event -> {
            pharmacienData.clear();
            pharmacienData.addAll(task.getValue());
            pharmacienTable.setItems(pharmacienData);
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            System.err.println("Erreur chargement pharmaciens: " + exception.getMessage());

            // Données de test
            pharmacienData.clear();
            PharmacienAdjoint pharma = new PharmacienAdjoint(
                UUID.randomUUID(),
                "Dupont",
                "Marie",
                "marie.dupont@example.com",
                "0678912345",
                "15 Rue des Pharmaciens",
                "PHARM123",
                LocalDate.now(),
                2500.0,
                "PHARMACIEN_ADJOINT",
                "CDI",
                "Doctorat en pharmacie",
                "marie.dupont@pharma.com"
            );
            pharmacienData.add(pharma);
            pharmacienTable.setItems(pharmacienData);
        });

        new Thread(task).start();
    }

    @FXML
    private void handleSearch() {
        String searchQuery = searchField.getText().trim();

        if (searchQuery.isEmpty()) {
            loadAllPharmaciens();
            return;
        }

        Task<List<PharmacienAdjoint>> task = new Task<>() {
            @Override
            protected List<PharmacienAdjoint> call() throws Exception {
                return pharmacienService.searchPharmaciensAdjoints(searchQuery);
            }
        };

        task.setOnSucceeded(event -> {
            pharmacienData.clear();
            pharmacienData.addAll(task.getValue());
            pharmacienTable.setItems(pharmacienData);
        });

        task.setOnFailed(event -> {
            System.err.println("Erreur recherche: " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    @FXML
    private void handleResetSearch() {
        searchField.clear();
        loadAllPharmaciens();
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
        AddPharmacienAdjointDialogController dialogController = new AddPharmacienAdjointDialogController();
        Optional<ButtonType> result = dialogController.showAndWait();

        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            PharmacienAdjointCreateRequest request = dialogController.getCreateRequest();

            try {
                boolean success = pharmacienService.createPharmacienAdjoint(request);

                if (success) {
                    loadAllPharmaciens();
                    showAlert(
                        Alert.AlertType.INFORMATION,
                        "Succès",
                        "Pharmacien adjoint ajouté\n" +
                        "Nom: " + request.getNom() + "\n" +
                        "Email pro: " + request.getEmailPro()
                    );
                } else {
                    showAlert(
                        Alert.AlertType.ERROR,
                        "Erreur",
                        "Échec de la création"
                    );
                }
            }
            catch (Exception e) {
                showAlert(
                    Alert.AlertType.ERROR,
                    "Erreur technique",
                    "Erreur serveur :\n" + e.getMessage()
                );
            }
        }
    }

    @FXML
    private void handleModifier() {
        PharmacienAdjoint selected = pharmacienTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            EditPharmacienAdjointDialogController dialogController = new EditPharmacienAdjointDialogController();
            dialogController.setPharmacienAdjoint(selected);

            Optional<ButtonType> result = dialogController.showAndWait();

            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                PharmacienAdjointUpdateRequest request = dialogController.getUpdateRequest();
                boolean success = pharmacienService.updatePharmacienAdjoint(selected.getIdPersonne(), request);
                if (success) {
                    loadAllPharmaciens();
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Modifications enregistrées");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour");
                }
            }
        }
    }

    @FXML
    private void handleSupprimer() {
        PharmacienAdjoint selected = pharmacienTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Supprimer " + selected.getNom() + " ?");
            confirmation.setContentText("Cette action est définitive !");

            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    boolean success = pharmacienService.deletePharmacienAdjoint(selected.getIdPersonne());

                    if (success) {
                        pharmacienData.remove(selected);
                        showAlert(
                            Alert.AlertType.INFORMATION,
                            "Succès",
                            "Pharmacien supprimé\nID: " + selected.getIdPersonne()
                        );
                    } else {
                        showAlert(
                            Alert.AlertType.WARNING,
                            "Avertissement",
                            "Suppression échouée"
                        );
                    }
                }
                catch (Exception e) {
                    showAlert(
                        Alert.AlertType.ERROR,
                        "Erreur",
                        "Erreur suppression :\n" + e.getMessage()
                    );
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
