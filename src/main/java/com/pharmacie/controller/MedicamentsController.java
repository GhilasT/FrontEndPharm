package com.pharmacie.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import com.pharmacie.model.Medicament;
import com.pharmacie.model.PageResponse;
import com.pharmacie.service.ApiRest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class MedicamentsController {

    @FXML private TableView<Medicament> medicamentsTable;
    @FXML private TableColumn<Medicament, String> colCodeCIS;
    @FXML private TableColumn<Medicament, String> colLibelle;
    @FXML private TableColumn<Medicament, String> colDenomination; // Nouvelle colonne pour la dénomination
    @FXML private TableColumn<Medicament, String> colDosage;
    @FXML private TableColumn<Medicament, String> colReference;
    @FXML private TableColumn<Medicament, String> colOrdonnance;
    @FXML private TableColumn<Medicament, Integer> colStock;
    @FXML private TableColumn<Medicament, java.math.BigDecimal> colPrixHT; // Nouvelle colonne pour le prix HT
    @FXML private TableColumn<Medicament, java.math.BigDecimal> colPrixTTC; // Nouvelle colonne pour le prix TTC
    
    @FXML private TextField searchField;
    @FXML private Button btnSearch;
    @FXML private Button btnModifierStock;
    @FXML private Button btnDetails;
    
    // Nouveaux composants
    @FXML private ComboBox<String> filterOrdonnance;
    @FXML private ComboBox<String> filterStock;
    @FXML private Button btnApplyFilters;
    @FXML private Button btnResetFilters;
    @FXML private Pagination pagination;
    @FXML private Label statusLabel;
    @FXML private Label totalItemsLabel;
    
    private final ObservableList<Medicament> medicaments = FXCollections.observableArrayList();
    private FilteredList<Medicament> filteredMedicaments;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final Logger LOGGER = Logger.getLogger(MedicamentsController.class.getName());
    
    // Variables pour la pagination côté serveur
    private int currentPage = 0;
    private int totalPages = 1;
    private long totalElements = 0;
    private String currentSearchTerm = "";
    
    // Constantes pour la pagination
    private static final int ITEMS_PER_PAGE = 50; // Correspond à la taille de page du backend

    @FXML
    public void initialize() {
        configureTableColumns();
        configureSearch();
        configureFilters();
        configurePagination();
        configureButtons();
        loadMedicaments(0, "");
    }
    
    private void showError(String title, String message) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    
    private void configureTableColumns() {
        colCodeCIS.setCellValueFactory(new PropertyValueFactory<>("codeCIS"));
        colLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colDenomination.setCellValueFactory(new PropertyValueFactory<>("denomination"));
        colDosage.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        colReference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        colOrdonnance.setCellValueFactory(new PropertyValueFactory<>("surOrdonnance"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colPrixHT.setCellValueFactory(new PropertyValueFactory<>("prixHT"));
        colPrixTTC.setCellValueFactory(new PropertyValueFactory<>("prixTTC"));
        
        // Appliquer un style conditionnel pour la colonne Stock
        colStock.setCellFactory(column -> new TableCell<Medicament, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.valueOf(item));
                    
                    // Appliquer un style selon la valeur du stock
                    if (item == 0) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else if (item < 5) {
                        setStyle("-fx-text-fill: orange;");
                    } else {
                        setStyle("-fx-text-fill: green;");
                    }
                }
            }
        });
        
        // Appliquer un style pour la colonne Sur Ordonnance
        colOrdonnance.setCellFactory(column -> new TableCell<Medicament, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    
                    if ("Avec".equals(item)) {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #2ecc71;");
                    }
                }
            }
        });
        
        // Initialiser la liste filtrée
        filteredMedicaments = new FilteredList<>(medicaments);
        
        // Activer les boutons lorsqu'un médicament est sélectionné
        medicamentsTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                boolean itemSelected = newSelection != null;
                btnModifierStock.setDisable(!itemSelected);
                btnDetails.setDisable(!itemSelected);
                
                if (itemSelected) {
                    statusLabel.setText("Médicament sélectionné : " + newSelection.getLibelle());
                } else {
                    statusLabel.setText("Prêt");
                }
            }
        );
    }
    
    private void configureSearch() {
        // Activer le bouton de recherche
        btnSearch.setDisable(false);
        
        // Configurer l'action du bouton de recherche
        btnSearch.setOnAction(e -> {
            currentPage = 0; // Réinitialiser à la première page lors d'une nouvelle recherche
            String searchTerm = searchField.getText().trim();
            statusLabel.setText("Recherche en cours pour: " + searchTerm);
            loadMedicaments(currentPage, searchTerm);
        });
        
        // Configurer la recherche en temps réel avec un délai (debounce)
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (scheduler != null) {
                scheduler.schedule(() -> {
                    Platform.runLater(() -> {
                        currentPage = 0; // Réinitialiser à la première page lors d'une nouvelle recherche
                        loadMedicaments(currentPage, newValue.trim());
                    });
                }, 500, TimeUnit.MILLISECONDS);
            }
        });
        
        // Configurer l'action pour la touche Entrée dans le champ de recherche
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                currentPage = 0;
                String searchTerm = searchField.getText().trim();
                loadMedicaments(currentPage, searchTerm);
            }
        });
    }
    
    private void configureFilters() {
        // Initialiser les valeurs des filtres
        filterOrdonnance.getSelectionModel().selectFirst();
        filterStock.getSelectionModel().selectFirst();
        
        // Configurer l'action du bouton d'application des filtres
        btnApplyFilters.setOnAction(e -> applyFilters());
        
        // Configurer l'action du bouton de réinitialisation des filtres
        btnResetFilters.setOnAction(e -> resetFilters());
    }
    
    private void configurePagination() {
        // Configurer la pagination
        pagination.setPageCount(1);
        pagination.setCurrentPageIndex(0);
        
        // Configurer l'action de changement de page
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            if (newIndex.intValue() != currentPage) {
                loadMedicaments(newIndex.intValue(), currentSearchTerm);
            }
        });
    }
    
    private void configureButtons() {
        btnModifierStock.setOnAction(e -> showModifierStockDialog());
        btnDetails.setOnAction(e -> showDetailsDialog());
    }

    private void loadMedicaments(int page, String searchTerm) {
        statusLabel.setText("Chargement des médicaments...");
        currentSearchTerm = searchTerm;
        
        Task<Boolean> checkTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    return ApiRest.isBackendAccessible();
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de l'accessibilité du backend", e);
                    return false;
                }
            }
        };
        
        checkTask.setOnSucceeded(e -> {
            Boolean isAccessible = checkTask.getValue();
            if (Boolean.TRUE.equals(isAccessible)) {
                fetchMedicamentsFromBackend(page, searchTerm);
            } else {
                showError("Serveur inaccessible", 
                    "Impossible de se connecter au serveur backend.\n\n" +
                    "Détails:\n" +
                    "- Vérifiez que le serveur est en cours d'exécution\n" +
                    "- URL du serveur: " + ApiRest.getApiBaseUrl() + "\n" +
                    "- Assurez-vous que le port 8080 est ouvert");
                statusLabel.setText("Serveur backend inaccessible");
                medicaments.clear();
                filteredMedicaments = new FilteredList<>(medicaments);
                updatePaginationControls(0, 0, 0);
                totalItemsLabel.setText("Total: 0 médicament");
            }
        });
        
        checkTask.setOnFailed(e -> {
            Throwable exception = checkTask.getException();
            showError("Erreur de vérification", 
                "Impossible de vérifier l'état du serveur backend.\n\n" +
                "Détails de l'erreur: " + (exception != null ? exception.getMessage() : "Erreur inconnue"));
            statusLabel.setText("Erreur de vérification du serveur");
            
            // Log détaillé de l'exception
            if (exception != null) {
                LOGGER.log(Level.SEVERE, "Détails complets de l'exception:", exception);
            }
        });
        
        new Thread(checkTask).start();
    }
    
    private void fetchMedicamentsFromBackend(int page, String searchTerm) {
        Task<PageResponse<Medicament>> task = new Task<>() {
            @Override
            protected PageResponse<Medicament> call() throws Exception {
                try {
                    // Utilisation du service ApiRest pour récupérer les données paginées depuis le backend
                    if (searchTerm == null || searchTerm.isEmpty()) {
                        return ApiRest.getMedicamentsPagines(page);
                    } else {
                        // Utiliser la bonne méthode et le bon paramètre de recherche
                        return ApiRest.getMedicamentsPagines(page, searchTerm);
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des médicaments: {0}", e.getMessage());
                    throw e;
                }
            }
        };

        task.setOnSucceeded(e -> {
            PageResponse<Medicament> pageResponse = task.getValue();
            
            // Mettre à jour les données et les contrôles de pagination
            medicaments.setAll(pageResponse.getContent());
            filteredMedicaments = new FilteredList<>(medicaments);
            medicamentsTable.setItems(filteredMedicaments);
            
            // Mettre à jour les variables de pagination
            currentPage = pageResponse.getCurrentPage();
            totalPages = pageResponse.getTotalPages();
            totalElements = pageResponse.getTotalElements();
            
            // Mettre à jour les contrôles de pagination
            updatePaginationControls(currentPage, totalPages, totalElements);
            
            // Appliquer les filtres
            applyFilters();
            
            // Mettre à jour les labels
            totalItemsLabel.setText(String.format("Total: %d médicaments", totalElements));
            statusLabel.setText("Prêt");
            
            LOGGER.log(Level.INFO, "Page {0}/{1} chargée avec succès, {2} médicaments au total, recherche: {3}", 
                    new Object[]{currentPage + 1, totalPages, totalElements, searchTerm});
        });
        
        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            String errorMessage = exception != null ? exception.getMessage() : "Erreur inconnue";
            showError("Erreur de connexion au backend", 
                "Impossible de récupérer les données depuis le serveur.\n\n" +
                "Détails de l'erreur: " + errorMessage + "\n\n" +
                "Veuillez vérifier que le serveur backend est en cours d'exécution");
            statusLabel.setText("Erreur de connexion au backend");
            
            // Ne pas afficher de données par défaut, mais vider la liste pour montrer clairement qu'il y a un problème
            medicaments.clear();
            filteredMedicaments = new FilteredList<>(medicaments);
            updatePaginationControls(0, 0, 0);
            totalItemsLabel.setText("Total: 0 médicament");
            
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement de la page {0}: {1}", 
                    new Object[]{page, errorMessage});
        });

        new Thread(task).start();
    }
    
    private void updatePaginationControls(int currentPage, int totalPages, long totalElements) {
        javafx.application.Platform.runLater(() -> {
            pagination.setPageCount(Math.max(1, totalPages));
            pagination.setCurrentPageIndex(currentPage);
            
            // Mettre à jour le texte du label de pagination
            if (totalElements > 0) {
                int startItem = currentPage * ITEMS_PER_PAGE + 1;
                int endItem = Math.min((currentPage + 1) * ITEMS_PER_PAGE, (int) totalElements);
                statusLabel.setText(String.format("Affichage des éléments %d à %d sur %d", 
                        startItem, endItem, totalElements));
            } else {
                statusLabel.setText("Aucun médicament trouvé");
            }
        });
    }
    
    private void applyFilters() {
        String ordonnanceFilter = filterOrdonnance.getValue();
        String stockFilter = filterStock.getValue();
        
        Predicate<Medicament> predicate = medicament -> true; // Par défaut, aucun filtre
        
        // Appliquer le filtre d'ordonnance
        if (!"Tous".equals(ordonnanceFilter)) {
            predicate = predicate.and(m -> ordonnanceFilter.equals(m.getSurOrdonnance()));
        }
        
        // Appliquer le filtre de stock
        if ("En stock".equals(stockFilter)) {
            predicate = predicate.and(m -> m.getStock() > 0);
        } else if ("Rupture".equals(stockFilter)) {
            predicate = predicate.and(m -> m.getStock() == 0);
        }
        
        // Appliquer le prédicat à la liste filtrée
        filteredMedicaments.setPredicate(predicate);
        
        // Mettre à jour le tableau
        medicamentsTable.setItems(filteredMedicaments);
        
        // Mettre à jour le label de statut
        statusLabel.setText(String.format("Filtres appliqués: %d médicaments affichés", filteredMedicaments.size()));
    }
    
    private void resetFilters() {
        filterOrdonnance.getSelectionModel().selectFirst();
        filterStock.getSelectionModel().selectFirst();
        
        // Réinitialiser le prédicat
        filteredMedicaments.setPredicate(medicament -> true);
        
        // Mettre à jour le tableau
        medicamentsTable.setItems(filteredMedicaments);
        
        // Mettre à jour le label de statut
        statusLabel.setText("Filtres réinitialisés");
    }

    /**
     * Applies a predefined filter to the medicaments list.
     * This method is called from the dashboard when a card is clicked.
     * It's been modified to no longer apply filters.
     *
     * @param filterType
     */
    public void applyFilter(String filterType) {
        // The method is kept for backward compatibility
        Platform.runLater(() -> {
            statusLabel.setText("Liste complète des médicaments");
        });
    }
    
    private void showDetailsDialog() {
        Medicament selectedMedicament = medicamentsTable.getSelectionModel().getSelectedItem();
        if (selectedMedicament == null) return;
        
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Détails du médicament");
        dialog.setHeaderText("Détails pour " + selectedMedicament.getLibelle());
        
        // Créer un layout principal avec un menu vertical à gauche et un contenu à droite
        BorderPane mainLayout = new BorderPane();
        
        // Créer le menu vertical avec les catégories
        VBox menuVertical = new VBox(10);
        menuVertical.setPadding(new Insets(10));
        menuVertical.setStyle("-fx-background-color: #f0f0f0;");
        menuVertical.setPrefWidth(200);
        
        // Créer les boutons du menu
        Button btnInfosAdmin = new Button("Informations administratives");
        Button btnInfosDispo = new Button("Informations disponibilité");
        Button btnInfosPrescription = new Button("Informations prescription");
        Button btnInfosGeneriques = new Button("Informations génériques");
        
        // Configurer les boutons pour qu'ils prennent toute la largeur
        btnInfosAdmin.setMaxWidth(Double.MAX_VALUE);
        btnInfosDispo.setMaxWidth(Double.MAX_VALUE);
        btnInfosPrescription.setMaxWidth(Double.MAX_VALUE);
        btnInfosGeneriques.setMaxWidth(Double.MAX_VALUE);
        
        // Ajouter les boutons au menu vertical
        menuVertical.getChildren().addAll(btnInfosAdmin, btnInfosDispo, btnInfosPrescription, btnInfosGeneriques);
        
        // Créer le contenu qui changera selon la catégorie sélectionnée
        StackPane contentPane = new StackPane();
        contentPane.setPadding(new Insets(10));
        
        // Créer les différents panneaux de contenu
        GridPane infoAdminGrid = createInfoAdminPanel(selectedMedicament);
        GridPane infoDispoGrid = createInfoDispoPanel(selectedMedicament);
        GridPane infoPrescriptionGrid = createInfoPrescriptionPanel(selectedMedicament);
        GridPane infoGeneriquesGrid = createInfoGeneriquesPanel(selectedMedicament);
        
        // Par défaut, afficher les informations administratives
        contentPane.getChildren().add(infoAdminGrid);
        
        // Configurer les actions des boutons du menu
        btnInfosAdmin.setOnAction(e -> {
            contentPane.getChildren().clear();
            contentPane.getChildren().add(infoAdminGrid);
        });
        
        btnInfosDispo.setOnAction(e -> {
            contentPane.getChildren().clear();
            contentPane.getChildren().add(infoDispoGrid);
        });
        
        btnInfosPrescription.setOnAction(e -> {
            contentPane.getChildren().clear();
            contentPane.getChildren().add(infoPrescriptionGrid);
        });
        
        btnInfosGeneriques.setOnAction(e -> {
            contentPane.getChildren().clear();
            contentPane.getChildren().add(infoGeneriquesGrid);
        });
        
        // Assembler le layout principal
        mainLayout.setLeft(menuVertical);
        mainLayout.setCenter(contentPane);
        
        // Définir la taille de la boîte de dialogue
        dialog.getDialogPane().setPrefSize(800, 500);
        dialog.getDialogPane().setContent(mainLayout);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        
        dialog.showAndWait();
    }
    
    // Méthode pour créer le panneau d'informations administratives
    private GridPane createInfoAdminPanel(Medicament medicament) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        int row = 0;
        
        grid.add(new Label("Code CIS:"), 0, row);
        grid.add(new Label(medicament.getCodeCIS()), 1, row++);
        
        grid.add(new Label("Dénomination:"), 0, row);
        grid.add(new Label(medicament.getDenomination() != null ? medicament.getDenomination() : "Non disponible"), 1, row++);
        
        grid.add(new Label("Libellé:"), 0, row);
        grid.add(new Label(medicament.getLibelle()), 1, row++);
        
        // Récupérer les informations administratives depuis l'API
        JSONObject infoAdmin = ApiRest.getMedicamentInfosAdmin(medicament.getCodeCIS());
        
        if (infoAdmin != null) {
            grid.add(new Label("Forme pharmaceutique:"), 0, row);
            grid.add(new Label(infoAdmin.optString("formePharmaceutique", "Non disponible")), 1, row++);
            
            grid.add(new Label("Voies d'administration:"), 0, row);
            grid.add(new Label(infoAdmin.optString("voiesAdministration", "Non disponible")), 1, row++);
            
            grid.add(new Label("Statut AMM:"), 0, row);
            grid.add(new Label(infoAdmin.optString("statutAMM", "Non disponible")), 1, row++);
            
            grid.add(new Label("Type procédure AMM:"), 0, row);
            grid.add(new Label(infoAdmin.optString("typeProcedureAMM", "Non disponible")), 1, row++);
            
            grid.add(new Label("État commercialisation:"), 0, row);
            grid.add(new Label(infoAdmin.optString("etatCommercialisation", "Non disponible")), 1, row++);
            
            grid.add(new Label("Date AMM:"), 0, row);
            grid.add(new Label(infoAdmin.optString("dateAMM", "Non disponible")), 1, row++);
            
            grid.add(new Label("Titulaires:"), 0, row);
            grid.add(new Label(infoAdmin.optString("titulaires", "Non disponible")), 1, row++);
            
            grid.add(new Label("Surveillance renforcée:"), 0, row);
            grid.add(new Label(infoAdmin.optBoolean("surveillanceRenforcee") ? "Oui" : "Non"), 1, row++);
        } else {
            grid.add(new Label("Informations détaillées:"), 0, row);
            grid.add(new Label("Aucune information dans cette section n'est disponible pour ce médicament"), 1, row++);
        }
        
        return grid;
    }
    
    // Méthode pour créer le panneau d'informations de disponibilité
    private GridPane createInfoDispoPanel(Medicament medicament) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        int row = 0;
        
        grid.add(new Label("Code CIS:"), 0, row);
        grid.add(new Label(medicament.getCodeCIS()), 1, row++);
        
        // Récupérer les informations de disponibilité depuis l'API
        JSONObject infoDispo = ApiRest.getMedicamentInfosDispo(medicament.getCodeCIS());
        
        if (infoDispo != null) {
            grid.add(new Label("Code CIP13:"), 0, row);
            grid.add(new Label(infoDispo.optString("codeCIP13", "Non disponible")), 1, row++);
            
            grid.add(new Label("Libellé présentation:"), 0, row);
            grid.add(new Label(medicament.getLibelle()), 1, row++);
            
            grid.add(new Label("Statut administratif:"), 0, row);
            grid.add(new Label(infoDispo.optString("statutAdministratif", "Non disponible")), 1, row++);
            
            grid.add(new Label("État commercialisation:"), 0, row);
            grid.add(new Label(infoDispo.optString("etatCommercialisation", "Non disponible")), 1, row++);
            
            grid.add(new Label("Date déclaration commercialisation:"), 0, row);
            grid.add(new Label(infoDispo.optString("dateDeclarationCommercialisation", "Non disponible")), 1, row++);
        } else {
            grid.add(new Label("Informations détaillées:"), 0, row);
            grid.add(new Label("Aucune information dans cette section n'est disponible pour ce médicament"), 1, row++);
        }
        
        grid.add(new Label("Stock:"), 0, row);
        grid.add(new Label(String.valueOf(medicament.getStock())), 1, row++);
        
        grid.add(new Label("Prix HT:"), 0, row);
        grid.add(new Label(medicament.getPrixHT() != null ? medicament.getPrixHT() + " €" : "Non disponible"), 1, row++);
        
        grid.add(new Label("Prix TTC:"), 0, row);
        grid.add(new Label(medicament.getPrixTTC() != null ? medicament.getPrixTTC() + " €" : "Non disponible"), 1, row++);
        
        grid.add(new Label("Taxe:"), 0, row);
        grid.add(new Label(medicament.getTaxe() != null ? medicament.getTaxe() + " €" : "Non disponible"), 1, row++);
        
        grid.add(new Label("Agrément collectivités:"), 0, row);
        grid.add(new Label(medicament.getAgrementCollectivites() != null ? medicament.getAgrementCollectivites() : "Non disponible"), 1, row++);
        
        grid.add(new Label("Taux remboursement:"), 0, row);
        grid.add(new Label(medicament.getTauxRemboursement() != null ? medicament.getTauxRemboursement() : "Non disponible"), 1, row++);
        
        return grid;
    }
    
    // Méthode pour créer le panneau d'informations de prescription
    private GridPane createInfoPrescriptionPanel(Medicament medicament) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        int row = 0;
        
        grid.add(new Label("Code CIS:"), 0, row);
        grid.add(new Label(medicament.getCodeCIS()), 1, row++);
        
        grid.add(new Label("Sur ordonnance:"), 0, row);
        grid.add(new Label(medicament.getSurOrdonnance()), 1, row++);
        
        // Récupérer les informations de prescription depuis l'API
        JSONObject infoPrescription = ApiRest.getMedicamentInfosPrescription(medicament.getCodeCIS());
        
        if (infoPrescription != null) {
            grid.add(new Label("Conditions de prescription:"), 0, row);
            grid.add(new Label(infoPrescription.optString("conditionsPrescription", "Non disponible")), 1, row++);
            
            grid.add(new Label("Conditions de délivrance:"), 0, row);
            grid.add(new Label(infoPrescription.optString("conditionsDelivrance", "Non disponible")), 1, row++);
            
            grid.add(new Label("Prescription restreinte:"), 0, row);
            grid.add(new Label(infoPrescription.optString("prescriptionRestreinte", "Non disponible")), 1, row++);
            
            grid.add(new Label("Prescription exceptionnelle:"), 0, row);
            grid.add(new Label(infoPrescription.optString("prescriptionExceptionnelle", "Non disponible")), 1, row++);
        } else {
            grid.add(new Label("Informations détaillées:"), 0, row);
            grid.add(new Label("Aucune information dans cette section n'est disponible pour ce médicament"), 1, row++);
        }
        
        return grid;
    }
    
    // Méthode pour créer le panneau d'informations génériques
    private GridPane createInfoGeneriquesPanel(Medicament medicament) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        int row = 0;
        
        grid.add(new Label("Code CIS:"), 0, row);
        grid.add(new Label(medicament.getCodeCIS()), 1, row++);
        
        // Récupérer les informations génériques depuis l'API
        JSONObject infoGeneriques = ApiRest.getMedicamentInfosGeneriques(medicament.getCodeCIS());
        
        if (infoGeneriques != null) {
            grid.add(new Label("Groupe générique:"), 0, row);
            grid.add(new Label(infoGeneriques.optString("groupeGenerique", "Non disponible")), 1, row++);
            
            grid.add(new Label("Type de générique:"), 0, row);
            grid.add(new Label(infoGeneriques.optString("typeGenerique", "Non disponible")), 1, row++);
            
            grid.add(new Label("Numéro tri:"), 0, row);
            grid.add(new Label(infoGeneriques.optString("numeroTri", "Non disponible")), 1, row++);
        } else {
            grid.add(new Label("Informations détaillées:"), 0, row);
            grid.add(new Label("Aucune information dans cette section n'est disponible pour ce médicament"), 1, row++);
        }
        
        return grid;
    }
    
    
    private void showModifierStockDialog() {
        Medicament selectedMedicament = medicamentsTable.getSelectionModel().getSelectedItem();
        if (selectedMedicament == null) return;
        
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Modifier le stock");
        dialog.setHeaderText("Modifier le stock pour " + selectedMedicament.getLibelle());
        
        // Créer une grille pour organiser les champs
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        // Créer un spinner pour le stock
        Spinner<Integer> stockSpinner = new Spinner<>(0, 1000, selectedMedicament.getStock());
        stockSpinner.setEditable(true);
        
        grid.add(new Label("Stock actuel:"), 0, 0);
        grid.add(new Label(String.valueOf(selectedMedicament.getStock())), 1, 0);
        grid.add(new Label("Nouveau stock:"), 0, 1);
        grid.add(stockSpinner, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        
        ButtonType buttonTypeOk = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                return stockSpinner.getValue();
            }
            return null;
        });
        
        Optional<Integer> result = dialog.showAndWait();
        
        result.ifPresent(newStock -> {
            // Mettre à jour le stock avec l'API
            boolean success = ApiRest.updateMedicamentStock(selectedMedicament.getCodeCIS(), newStock);
            
            if (success) {
                // Mettre à jour le modèle local
                selectedMedicament.setStock(newStock);
                medicamentsTable.refresh();
                
                // Afficher un message de confirmation
                statusLabel.setText("Stock mis à jour pour " + selectedMedicament.getLibelle());
            } else {
                // Afficher un message d'erreur
                statusLabel.setText("Erreur lors de la mise à jour du stock pour " + selectedMedicament.getLibelle());
                
                // Afficher une alerte
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de mise à jour");
                alert.setHeaderText(null);
                alert.setContentText("Impossible de mettre à jour le stock dans la base de données. Veuillez réessayer.");
                alert.showAndWait();
            }
        });
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
