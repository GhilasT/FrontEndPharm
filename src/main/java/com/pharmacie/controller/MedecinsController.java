package com.pharmacie.controller;

import com.pharmacie.model.Medecin;
import com.pharmacie.model.MedicamentPanier;
import com.pharmacie.model.PageResponse;
import com.pharmacie.service.ApiRest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.pharmacie.controller.TestUtils.showAlert;

/**
 * Contrôleur pour la gestion de l'affichage et des interactions avec la liste des médecins.
 * Permet la recherche, l'ajout, la modification (implicite via détails/prescription),
 * la suppression et l'affichage des détails des médecins.
 * Gère également la pagination et l'ouverture de la vue de prescription.
 */
public class MedecinsController {
    private static final Logger LOGGER = Logger.getLogger(MedecinsController.class.getName());

    @FXML private TableView<Medecin> medecinsTable;
    @FXML private TableColumn<Medecin, String> colCivilite;
    @FXML private TableColumn<Medecin, String> colNomExercice;
    @FXML private TableColumn<Medecin, String> colPrenomExercice;
    @FXML private TableColumn<Medecin, String> colRppsMedecin;
    @FXML private TableColumn<Medecin, String> colProfession;
    @FXML private TableColumn<Medecin, String> colModeExercice;
    @FXML private TableColumn<Medecin, String> colQualifications;
    @FXML private TableColumn<Medecin, String> colStructureExercice;
    @FXML private TableColumn<Medecin, String> colFonctionActivite;
    @FXML private TableColumn<Medecin, String> colGenreActivite;
    @FXML private TableColumn<Medecin, String> colSupprimer;
    @FXML private Pagination pagination;
    @FXML private TextField searchField; // Changé de searchFieldMedecin à searchField pour correspondre au FXML
    @FXML private Button btnEditMedecin;
    @FXML private Button btnDeleteMedecin;
    @FXML private Button btnDetailsMedecin;
    @FXML private Button btnAddMedecin;
    @FXML private Button btnSearch;
    @FXML private Label statusLabel;
    @FXML private HBox topPane;

    private final ObservableList<Medecin> medecins = FXCollections.observableArrayList();
    private VenteController venteController;
    private Stage modalStage;

    private int currentPage = 0;
    private int totalPages = 1;
    private long totalElements = 0;

    /**
     * Initialise le contrôleur après le chargement du FXML.
     * Configure les colonnes de la table, la sélection, la recherche, la pagination,
     * les boutons, la colonne de suppression et charge la première page de médecins.
     */
    @FXML
    public void initialize() {
        configureTableColumns();
        configureTableSelection();
        configureSearch();
        configurePagination();
        configureButtons();
        configureDeleteButtonColumn();
        loadMedecins(0, searchField.getText()); // Changé de searchFieldMedecin à searchField

        // Désactive le bouton de recherche si le champ est vide
        btnSearch.setDisable(true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> // Changé de searchFieldMedecin à searchField
                btnSearch.setDisable(newVal.trim().isEmpty())
        );

        // Clic sur ligne ouvre Prescription dans le même modal
        medecinsTable.setRowFactory(tv -> {
            TableRow<Medecin> row = new TableRow<>();
            row.setOnMouseClicked(evt -> {
                if (!row.isEmpty() && evt.getClickCount() == 1) {
                    String rpps = row.getItem().getRppsMedecin();
                    openPrescriptionFor(rpps);
                }
            });
            return row;
        });

        // Créer un bouton supprimer
        Button btnDelete = new Button("Supprimer");
        btnDelete.getStyleClass().add("button-red");
        btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnDelete.setOnAction(e -> handleDeleteMedecin());

        // Chercher le parent de btnAddMedecin
        Parent parent = btnAddMedecin.getParent();

        if (parent instanceof HBox) {
            // Si c'est déjà un HBox, il suffit d'ajouter le bouton
            ((HBox) parent).getChildren().add(btnDelete);
        } else if (parent instanceof Pane) {
            // Sinon, créer un nouveau HBox
            HBox actionsBox = new HBox(10);

            // Obtenir l'index de btnAdd dans son parent
            int index = ((Pane) parent).getChildren().indexOf(btnAddMedecin);

            // Retirer btnAdd de son parent
            ((Pane) parent).getChildren().remove(btnAddMedecin);

            // Ajouter les deux boutons à la HBox
            actionsBox.getChildren().addAll(btnAddMedecin, btnDelete);

            // Ajouter la HBox au parent à la position de btnAdd
            ((Pane) parent).getChildren().add(index, actionsBox);
        }
    }

    /**
     * Configure les colonnes de la table des médecins avec les propriétés correspondantes des objets Medecin.
     */
    private void configureTableColumns() {
        colCivilite.setCellValueFactory(cd -> cd.getValue().civiliteProperty());
        colNomExercice.setCellValueFactory(cd -> cd.getValue().nomExerciceProperty());
        colPrenomExercice.setCellValueFactory(cd -> cd.getValue().prenomExerciceProperty());
        colRppsMedecin.setCellValueFactory(cd -> cd.getValue().rppsMedecinProperty());
        colProfession.setCellValueFactory(cd -> cd.getValue().professionProperty());
        colModeExercice.setCellValueFactory(cd -> cd.getValue().modeExerciceProperty());
        colQualifications.setCellValueFactory(cd -> cd.getValue().qualificationsProperty());
        colStructureExercice.setCellValueFactory(cd -> cd.getValue().structureExerciceProperty());
        colFonctionActivite.setCellValueFactory(cd -> cd.getValue().fonctionActiviteProperty());
        colGenreActivite.setCellValueFactory(cd -> cd.getValue().genreActiviteProperty());
    }

    /**
     * Configure la gestion de la sélection dans la table des médecins.
     * Active ou désactive les boutons d'édition, de suppression et de détails en fonction de la sélection.
     */
    private void configureTableSelection() {
        medecinsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean sel = newSel != null;
            btnEditMedecin.setDisable(!sel);
            btnDeleteMedecin.setDisable(!sel);
            btnDetailsMedecin.setDisable(!sel);
        });
    }

    /**
     * Configure la fonctionnalité de recherche de médecins.
     * La recherche est déclenchée lors de la saisie (après 3 caractères) ou en cliquant sur le bouton de recherche.
     */
    private void configureSearch() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.trim().isEmpty() && newVal.trim().length() >= 3) {
                currentPage = 0; // Réinitialiser à la première page lors d'une nouvelle recherche
                searchMedecins(newVal.trim());
            } else if (newVal == null || newVal.trim().isEmpty()) {
                currentPage = 0;
                loadMedecins(currentPage, "");
            }
        });
        
        btnSearch.setOnAction(e -> {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                currentPage = 0; // Réinitialiser à la première page lors d'une recherche via bouton
                searchMedecins(searchTerm);
            } else {
                loadMedecins(0, "");
            }
        });
    }

    /**
     * Effectue une recherche de médecins en fonction du terme fourni.
     * Met à jour la table avec les résultats et désactive la pagination.
     * @param searchTerm Le terme à rechercher.
     */
    private void searchMedecins(String searchTerm) {
        statusLabel.setText("Recherche en cours...");
        Task<List<Medecin>> searchTask = new Task<>() {
            @Override 
            protected List<Medecin> call() throws Exception {
                return ApiRest.searchMedecins(searchTerm);
            }
        };
        
        searchTask.setOnSucceeded(e -> {
            List<Medecin> results = searchTask.getValue();
            if (results != null) {
                medecins.clear(); // Vider la liste actuelle
                medecins.addAll(results); // Ajouter seulement les résultats de la recherche
                medecinsTable.setItems(medecins);
                statusLabel.setText(String.format("Recherche: %d médecin(s) trouvé(s) pour '%s'", 
                                                results.size(), searchTerm));
            } else {
                medecins.clear();
                statusLabel.setText("Aucun résultat trouvé pour '" + searchTerm + "'");
            }
            
            // Désactiver la pagination pendant la recherche
            pagination.setDisable(true);
            pagination.setVisible(false);
        });
        
        searchTask.setOnFailed(e -> {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de médecins", searchTask.getException());
            showAlert(Alert.AlertType.ERROR, 
                    "Erreur de recherche", 
                    "La recherche a échoué", 
                    "Détails: " + searchTask.getException().getMessage());
            statusLabel.setText("Erreur lors de la recherche");
        });
        
        new Thread(searchTask).start();
    }

    /**
     * Gère l'action du bouton de recherche ou la réinitialisation de la recherche si le champ est vide.
     * Si le champ de recherche est vide, recharge la liste paginée des médecins.
     * Sinon, lance une recherche spécifique.
     */
    @FXML
    private void handleSearchMedecin() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            currentPage = 0;
            loadMedecins(0, "");
            pagination.setDisable(false);
            pagination.setVisible(true);
        } else {
            currentPage = 0;
            searchMedecins(searchTerm);
        }
    }

    /**
     * Configure le contrôle de pagination pour naviguer entre les pages de médecins.
     */
    private void configurePagination() {
        pagination.setPageCount(1);
        pagination.setCurrentPageIndex(0);
        pagination.currentPageIndexProperty().addListener((obs, oldIdx, newIdx) -> {
            if (newIdx.intValue() != currentPage) {
                loadMedecins(newIdx.intValue(), searchField.getText()); // Changé de searchFieldMedecin à searchField
            }
        });
    }

    /**
     * Configure les actions des boutons, notamment le bouton "Ajouter Médecin".
     */
    private void configureButtons() {
        btnAddMedecin.setOnAction(e -> handleAddMedecin());
    }

    /**
     * Charge une page spécifique de médecins, potentiellement filtrée par un terme de recherche.
     * Met à jour la table et les contrôles de pagination.
     * @param page Le numéro de la page à charger.
     * @param searchTerm Le terme de recherche (peut être vide pour charger tous les médecins).
     */
    private void loadMedecins(int page, String searchTerm) {
        Task<PageResponse<Medecin>> task = new Task<>() {
            @Override protected PageResponse<Medecin> call() throws Exception {
                return ApiRest.getMedecinsPagines(page, searchTerm);
            }
        };
        task.setOnSucceeded(e -> {
            PageResponse<Medecin> pr = task.getValue();
            List<Medecin> list = pr.getContent();
            medecins.setAll(list);
            medecinsTable.setItems(medecins);
            currentPage = pr.getCurrentPage();
            totalPages = pr.getTotalPages();
            totalElements = pr.getTotalElements();
            updatePaginationControls();
        });
        task.setOnFailed(e -> LOGGER.log(Level.SEVERE, "Erreur chargement médecins", task.getException()));
        new Thread(task).start();
    }

    /**
     * Met à jour les contrôles de pagination (nombre de pages, page actuelle) et le label de statut.
     */
    private void updatePaginationControls() {
        javafx.application.Platform.runLater(() -> {
            pagination.setPageCount(totalPages);
            pagination.setCurrentPageIndex(currentPage);
            statusLabel.setText(String.format("Page %d sur %d (total %d)", currentPage+1, totalPages, totalElements));
        });
    }

    /**
     * Gère l'action du bouton "Ajouter Médecin".
     * Ouvre une fenêtre modale pour ajouter un nouveau médecin.
     */
    @FXML
    private void handleAddMedecin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/AddMedecinForm.fxml"));
            Parent root = loader.load();
            AddMedecinController ctrl = loader.getController();
            ctrl.setMedecinsController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Formulaire d'ajout de médecin");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire", e.getMessage());
        }
    }

    /**
     * Configure la colonne "Supprimer" de la table avec un bouton de suppression pour chaque ligne.
     */
    private void configureDeleteButtonColumn() {
        colSupprimer.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("Supprimer");
            { deleteBtn.setOnAction(evt -> {
                Medecin m = getTableView().getItems().get(getIndex());
                handleDeleteMedecin(evt, m);
            }); }
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
    }

    /**
     * Gère la suppression d'un médecin spécifique, généralement appelée par le bouton dans la ligne de la table.
     * @param event L'événement d'action.
     * @param m Le médecin à supprimer.
     */
    @FXML
    private void handleDeleteMedecin(ActionEvent event, Medecin m) {
        if (m == null) return;
        try {
            ApiRest.deleteMedecin(m.getRppsMedecin());
            loadMedecins(currentPage, searchField.getText()); // Changé de searchFieldMedecin à searchField
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer le médecin", e.getMessage());
        }
    }

    /**
     * Gère la suppression du médecin actuellement sélectionné dans la table.
     * Affiche une boîte de dialogue de confirmation avant la suppression.
     */
    private void handleDeleteMedecin() {
        Medecin selectedMedecin = medecinsTable.getSelectionModel().getSelectedItem();

        if (selectedMedecin == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Aucun médecin sélectionné",
                    "Veuillez sélectionner un médecin à supprimer");
            return;
        }

        // Confirmation avant suppression
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmer la suppression");
        confirmDialog.setHeaderText("Vous êtes sur le point de supprimer ce médecin");
        confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer le médecin ? ");
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                ApiRest.deleteMedecin(selectedMedecin.getRppsMedecin());
                medecins.remove(selectedMedecin);
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Médecin supprimé",
                        "Le médecin a été supprimé avec succès");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression",
                        "Une erreur est survenue lors de la suppression: " + ex.getMessage());
            }
        }
    }

    /**
     * Ouvre la vue de prescription pour le médecin spécifié par son RPPS.
     * Remplace le contenu de la fenêtre modale actuelle par la vue de prescription.
     * @param rpps Le numéro RPPS du médecin pour lequel ouvrir la prescription.
     */
    private void openPrescriptionFor(String rpps) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/Prescription.fxml"));
            Parent root = loader.load();
            PrescriptionController ctrl = loader.getController();

            // Passage des données
            ctrl.setClientId(venteController.getClientId());
            ctrl.setMedecinRpps(rpps);
            ctrl.setMedicamentsPanier(venteController.getMedicamentsPanier());
            ctrl.setVenteController(venteController);

            // Remplacement du contenu du même modal
            modalStage.getScene().setRoot(root);
            modalStage.setTitle("Prescription pour RPPS " + rpps);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la page de prescription", e.getMessage());
        }
    }

    /**
     * Définit le contrôleur de vente parent.
     * @param venteController Le contrôleur de vente.
     */
    public void setVenteController(VenteController venteController) {
        this.venteController = venteController;
    }

    /**
     * Définit la fenêtre modale unique utilisée pour afficher les formulaires (ajout médecin, prescription).
     * @param modalStage La fenêtre modale.
     */
    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

    /**
     * Permet de rafraîchir la liste des médecins après un ajout ou une édition.
     * Réactive la pagination et recharge la page courante.
     */
    public void refreshMedecinsList() {
        pagination.setDisable(false);
        pagination.setVisible(true);
        loadMedecins(currentPage, "");
    }
}
