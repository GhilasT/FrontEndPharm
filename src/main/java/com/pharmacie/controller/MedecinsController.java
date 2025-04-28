package com.pharmacie.controller;

import com.pharmacie.model.Medecin;
import com.pharmacie.model.MedicamentPanier;
import com.pharmacie.service.ApiRest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.concurrent.Task;
import com.pharmacie.model.PageResponse;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static com.pharmacie.controller.TestUtils.showAlert;
import static com.pharmacie.service.ApiRest.LOGGER;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;



public class MedecinsController {

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
        loadMedecins(0);

        // Désactive le bouton de recherche si le champ est vide
        searchFieldMedecin.textProperty().addListener((obs, o, n) ->
                btnSearch.setDisable(n.trim().isEmpty())
        );

        // ←––– NOUVEAU : ouvrir la page de prescription au clic sur une ligne
        medecinsTable.setRowFactory(tv -> {
            TableRow<Medecin> row = new TableRow<>();
            row.setOnMouseClicked(evt -> {
                if (!row.isEmpty() && evt.getClickCount() == 1) {
                    Medecin sel = row.getItem();
                    // Appelle ta méthode qui ouvre Prescription.fxml
                    openPrescriptionFor(sel.getRppsMedecin());
                }
            });
            return row;
        });
    }

    private void configureTableColumns() {
        colCivilite.setCellValueFactory(cellData -> cellData.getValue().civiliteProperty());
        colNomExercice.setCellValueFactory(cellData -> cellData.getValue().nomExerciceProperty());
        colPrenomExercice.setCellValueFactory(cellData -> cellData.getValue().prenomExerciceProperty());
        colRppsMedecin.setCellValueFactory(cellData -> cellData.getValue().rppsMedecinProperty());
        colProfession.setCellValueFactory(cellData -> cellData.getValue().professionProperty());
        colModeExercice.setCellValueFactory(cellData -> cellData.getValue().modeExerciceProperty());
        colQualifications.setCellValueFactory(cellData -> cellData.getValue().qualificationsProperty());
        colStructureExercice.setCellValueFactory(cellData -> cellData.getValue().structureExerciceProperty());
        colFonctionActivite.setCellValueFactory(cellData -> cellData.getValue().fonctionActiviteProperty());
        colGenreActivite.setCellValueFactory(cellData -> cellData.getValue().genreActiviteProperty());
    }

    private void configureTableSelection() {
        medecinsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    boolean itemSelected = newSelection != null;
                    btnEditMedecin.setDisable(!itemSelected);
                    btnDeleteMedecin.setDisable(!itemSelected);
                    btnDetailsMedecin.setDisable(!itemSelected);
                }
        );
    }

    private void configureSearch() {
        searchFieldMedecin.textProperty().addListener((observable, oldValue, newValue) -> {
            currentPage = 0;  // Reset to first page when search term changes
            loadMedecins(currentPage, newValue);
        });
    }

    private void configurePagination() {
        pagination.setPageCount(1);
        pagination.setCurrentPageIndex(0);

        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            if (newIndex.intValue() != currentPage) {
                loadMedecins(newIndex.intValue());
            }
        });
    }

    private void configureButtons() {
        btnAddMedecin.setOnAction(e -> handleAddMedecin());
    }

    private void loadMedecins(int page) {
        loadMedecins(page, searchFieldMedecin.getText());
    }
    // Ajoutez la méthode setVenteController
    public void setVenteController(VenteController venteController) {
        this.venteController = venteController;
    }

    @FXML
    private void loadMedecins(int page, String searchTerm) {
        Task<PageResponse<Medecin>> task = new Task<>() {
            @Override
            protected PageResponse<Medecin> call() throws Exception {
                return ApiRest.getMedecinsPagines(page, searchTerm);  // Recherche avec le terme
            }
        };

        task.setOnSucceeded(e -> {
            PageResponse<Medecin> pageResponse = task.getValue();
            List<Medecin> medecinsList = pageResponse.getContent();
            LOGGER.log(Level.INFO, "Médecins récupérés : " + medecinsList);
            medecins.setAll(medecinsList);
            medecinsTable.setItems(medecins);
            currentPage = pageResponse.getCurrentPage();
            totalPages = pageResponse.getTotalPages();
            totalElements = pageResponse.getTotalElements();
            updatePaginationControls(currentPage, totalPages, totalElements);
        });

        task.setOnFailed(e -> {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des médecins");
        });

        new Thread(task).start();
    }

    private void updatePaginationControls(int currentPage, int totalPages, long totalElements) {
        javafx.application.Platform.runLater(() -> {
            pagination.setPageCount(totalPages);
            pagination.setCurrentPageIndex(currentPage);
        });
    }

    @FXML
    private void handleAddMedecin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/AddMedecinForm.fxml"));
            Parent root = loader.load();

            // Passer le contrôleur principal à la fenêtre modale
            AddMedecinController addMedecinController = loader.getController();
            addMedecinController.setMedecinsController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Formulaire d'ajout de médecin");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire",
                    "Une erreur s'est produite lors du chargement du formulaire.");
        }
    }

    @FXML
    private void handleEdit() {}

    @FXML
    private void handleDelete() {}

    @FXML
    private void handleDetails() {}
    public void refreshMedecinsList() {
        loadMedecins(currentPage);
    }

    private void configureDeleteButtonColumn() {
        // Assurez-vous que vous avez accès à la colonne colSupprimer
        colSupprimer.setCellFactory(param -> {
            TableCell<Medecin, String> cell = new TableCell<Medecin, String>() {
                final Button deleteButton = new Button("Supprimer");

                {
                    // Ajouter une action au bouton de suppression
                    deleteButton.setOnAction(event -> {
                        Medecin selectedMedecin = getTableView().getItems().get(getIndex());
                        if (selectedMedecin != null) {
                            // Appeler la méthode de suppression dans le contrôleur
                            handleDeleteMedecin(event, selectedMedecin);
                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null); // Ne rien afficher si la cellule est vide
                    } else {
                        setGraphic(deleteButton); // Afficher le bouton de suppression
                    }
                }
            };
            return cell; // Retourne la cellule contenant le bouton
        });
    }

    @FXML
    private void handleDeleteMedecin(ActionEvent event, Medecin selectedMedecin) {
        if (selectedMedecin != null) {
            try {
                // Extraire le RPPS à partir de la StringProperty
                String rpps = selectedMedecin.rppsMedecinProperty().get();
                // Appeler la méthode deleteMedecin d'ApiRest pour supprimer le médecin via le RPPS
                ApiRest.deleteMedecin(rpps);

                // Rafraîchir la liste des médecins après suppression
                loadMedecins(currentPage);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer le médecin",
                        "Une erreur est survenue lors de la suppression du médecin.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner un médecin",
                    "Pour supprimer un médecin, sélectionnez d'abord un élément.");
        }
    }

    @FXML
    private void handleSearchMedecin() {
        String searchTerm = searchFieldMedecin.getText();  // Récupérer le texte du champ de recherche

        if (searchTerm != null && !searchTerm.isEmpty()) {
            // Appel à l'API pour récupérer les médecins correspondant au terme de recherche
            Task<List<Medecin>> task = new Task<>() {
                @Override
                protected List<Medecin> call() throws Exception {
                    return ApiRest.searchMedecins(searchTerm);  // Appel à l'API pour rechercher les médecins
                }
            };

            task.setOnSucceeded(e -> {
                List<Medecin> medecinsList = task.getValue();  // Récupérer les résultats de la recherche
                medecins.setAll(medecinsList);  // Mettre à jour la liste des médecins dans la table
                medecinsTable.setItems(medecins);
            });

            task.setOnFailed(e -> {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la recherche",
                        "Une erreur est survenue lors de la recherche des médecins.");
            });

            new Thread(task).start();  // Démarrer la tâche dans un thread séparé
        }
    }

    private void openPrescriptionFor(String rpps) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/pharmacie/view/Prescription.fxml"));
            Parent root = loader.load();
            PrescriptionController ctrl = loader.getController();
    
            // S'assurer que venteController n'est pas null
            if (venteController != null) {
                // 1) Passe le clientId récupéré depuis VenteController
                ctrl.setClientId(venteController.getClientId());
                
                // 2) Passe le RPPS
                ctrl.setMedecinRpps(rpps);
                
                // 3) Passe la référence au venteController pour avoir accès aux méthodes
                ctrl.venteController = venteController;
                
                // 4) Passe la liste des MedicamentPanier
                List<MedicamentPanier> panier = venteController.getMedicamentsPanier();
                System.out.println("Taille du panier récupéré: " + (panier != null ? panier.size() : 0));
                ctrl.setMedicamentsPanier(panier);
            } else {
                System.err.println("VenteController non initialisé!");
            }
    
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Prescription pour RPPS " + rpps);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erreur", "Impossible d'ouvrir la page de prescription", e.getMessage());
        }
    }



}