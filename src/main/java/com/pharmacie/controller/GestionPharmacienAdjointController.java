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

/**
 * Contrôleur pour la gestion des pharmaciens adjoints.
 * Permet d'afficher, rechercher, ajouter, modifier et supprimer des pharmaciens adjoints.
 */
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

    /**
     * Initialise le contrôleur après le chargement du FXML.
     * Configure les colonnes de la table, la sélection, la recherche,
     * et charge la liste initiale des pharmaciens adjoints.
     */
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

    /**
     * Charge tous les pharmaciens adjoints depuis l'API et les affiche dans la table.
     * En cas d'échec du chargement depuis l'API, des données de test sont affichées.
     */
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

    /**
     * Gère la recherche de pharmaciens adjoints en fonction du texte saisi.
     * Si le champ de recherche est vide, recharge tous les pharmaciens.
     * Sinon, effectue une recherche spécifique.
     */
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

    /**
     * Gère l'action du bouton de réinitialisation de la recherche.
     * Efface le champ de recherche et recharge tous les pharmaciens adjoints.
     */
    @FXML
    private void handleResetSearch() {
        searchField.clear();
        loadAllPharmaciens();
    }

    /**
     * Définit le contrôleur parent (DashboardAdminController).
     * @param controller Le contrôleur parent.
     */
    public void setParentController(DashboardAdminController controller) {
        this.parentController = controller;
    }

    /**
     * Gère l'action du bouton "Retour".
     * Revient à la vue de gestion du personnel dans le tableau de bord de l'administrateur.
     */
    @FXML
    private void handleBack() {
        if (parentController != null) {
            parentController.loadGestionPersonnel();
        }
    }

    /**
     * Gère l'action du bouton "Ajouter".
     * Ouvre une boîte de dialogue pour ajouter un nouveau pharmacien adjoint.
     * Si l'ajout est confirmé, crée le pharmacien adjoint via l'API et rafraîchit la liste.
     */
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

    /**
     * Gère l'action du bouton "Modifier".
     * Ouvre une boîte de dialogue pour modifier le pharmacien adjoint sélectionné.
     * Si la modification est confirmée, met à jour le pharmacien adjoint via l'API et rafraîchit la liste.
     */
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

    /**
     * Gère l'action du bouton "Supprimer".
     * Demande confirmation avant de supprimer le pharmacien adjoint sélectionné.
     * Si la suppression est confirmée, supprime le pharmacien adjoint via l'API et rafraîchit la liste.
     */
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

    /**
     * Affiche une boîte de dialogue d'alerte.
     * @param type Le type d'alerte (Erreur, Information, etc.).
     * @param title Le titre de la fenêtre d'alerte.
     * @param message Le message principal de l'alerte.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
