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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.pharmacie.controller.TestUtils.showAlert;

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
    @FXML private TextField searchFieldMedecin;
    @FXML private Button btnEditMedecin;
    @FXML private Button btnDeleteMedecin;
    @FXML private Button btnDetailsMedecin;
    @FXML private Button btnAddMedecin;
    @FXML private Button btnSearch;
    @FXML private Label statusLabel;

    private final ObservableList<Medecin> medecins = FXCollections.observableArrayList();
    private VenteController venteController;
    private Stage modalStage;

    private int currentPage = 0;
    private int totalPages = 1;
    private long totalElements = 0;

    @FXML
    public void initialize() {
        configureTableColumns();
        configureTableSelection();
        configureSearch();
        configurePagination();
        configureButtons();
        configureDeleteButtonColumn();
        loadMedecins(0, searchFieldMedecin.getText());

        // Désactive le bouton de recherche si le champ est vide
        btnSearch.setDisable(true);
        searchFieldMedecin.textProperty().addListener((obs, oldVal, newVal) ->
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
    }

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

    private void configureTableSelection() {
        medecinsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean sel = newSel != null;
            btnEditMedecin.setDisable(!sel);
            btnDeleteMedecin.setDisable(!sel);
            btnDetailsMedecin.setDisable(!sel);
        });
    }

    private void configureSearch() {
        searchFieldMedecin.textProperty().addListener((obs, oldVal, newVal) -> {
            currentPage = 0;
            loadMedecins(currentPage, newVal);
        });
        btnSearch.setOnAction(e -> loadMedecins(0, searchFieldMedecin.getText()));
    }

    private void configurePagination() {
        pagination.setPageCount(1);
        pagination.setCurrentPageIndex(0);
        pagination.currentPageIndexProperty().addListener((obs, oldIdx, newIdx) -> {
            if (newIdx.intValue() != currentPage) {
                loadMedecins(newIdx.intValue(), searchFieldMedecin.getText());
            }
        });
    }

    private void configureButtons() {
        btnAddMedecin.setOnAction(e -> handleAddMedecin());
    }

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

    private void updatePaginationControls() {
        javafx.application.Platform.runLater(() -> {
            pagination.setPageCount(totalPages);
            pagination.setCurrentPageIndex(currentPage);
            statusLabel.setText(String.format("Page %d sur %d (total %d)", currentPage+1, totalPages, totalElements));
        });
    }

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

    @FXML
    private void handleDeleteMedecin(ActionEvent event, Medecin m) {
        if (m == null) return;
        try {
            ApiRest.deleteMedecin(m.getRppsMedecin());
            loadMedecins(currentPage, searchFieldMedecin.getText());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer le médecin", e.getMessage());
        }
    }

    @FXML
    private void handleSearchMedecin() {
        loadMedecins(0, searchFieldMedecin.getText());
    }

    /**
     * Surcharge de l'étape suivante : Prescription. Remplace le root du même modalStage
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

    // Setters pour lier VenteController et le Stage modal unique
    public void setVenteController(VenteController venteController) {
        this.venteController = venteController;
    }
    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

    /** Permet de rafraîchir la liste après ajout/édition */
    public void refreshMedecinsList() {
        loadMedecins(currentPage, searchFieldMedecin.getText());
    }
}
