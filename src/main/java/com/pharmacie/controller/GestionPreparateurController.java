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

/**
 * Contrôleur pour la gestion des préparateurs.
 * Permet d'afficher, rechercher, ajouter, modifier et supprimer des préparateurs.
 */
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
    @FXML private Button resetSearchButton;

    private ObservableList<Preparateur> preparateurData = FXCollections.observableArrayList();
    private DashboardAdminController parentController;
    private PreparateurApi preparateurService;

    /**
     * Initialise le contrôleur après le chargement du FXML.
     * Configure les colonnes de la table, la recherche, les boutons de modification/suppression
     * et charge la liste initiale des préparateurs.
     */
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

    /**
     * Configure les colonnes de la table des préparateurs avec les propriétés correspondantes.
     */
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

    /**
     * Gère la recherche de préparateurs en fonction du texte saisi dans le champ de recherche.
     * La recherche est effectuée si le texte est vide (pour tout afficher) ou a au moins 3 caractères.
     */
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

    /**
     * Gère l'action du bouton de réinitialisation de la recherche.
     * Efface le champ de recherche et recharge tous les préparateurs.
     */
    @FXML
    private void handleResetSearch() {
        searchField.clear();
        loadAllPreparateurs();
    }

    /**
     * Charge tous les préparateurs depuis l'API et les affiche dans la table.
     */
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
     * Ouvre une boîte de dialogue pour ajouter un nouveau préparateur.
     * Si l'ajout est confirmé, crée le préparateur via l'API et rafraîchit la liste.
     */
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

    /**
     * Gère l'action du bouton "Modifier".
     * Ouvre une boîte de dialogue pour modifier le préparateur sélectionné.
     * Si la modification est confirmée, met à jour le préparateur via l'API et rafraîchit la liste.
     */
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

    /**
     * Gère l'action du bouton "Supprimer".
     * Demande confirmation avant de supprimer le préparateur sélectionné.
     * Si la suppression est confirmée, supprime le préparateur via l'API et rafraîchit la liste.
     */
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
