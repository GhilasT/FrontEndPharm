package com.pharmacie.controller;

import com.pharmacie.model.Medicament;
import com.pharmacie.model.MedicamentPanier;
import com.pharmacie.model.PageResponse;
import com.pharmacie.model.Vente;
import com.pharmacie.model.VenteCreateRequest;
import com.pharmacie.service.ApiRest;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RechercheMedicamentsController {

    private static final Logger LOGGER = Logger.getLogger(RechercheMedicamentsController.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @FXML
    private TextField searchField;

    @FXML
    private Button btnRechercher;

    @FXML
    private TableView<Medicament> medicamentsTable;

    @FXML
    private TableColumn<Medicament, Long> idColumn;

    @FXML
    private TableColumn<Medicament, String> libelleColumn;
    
    @FXML
    private TableColumn<Medicament, String> denominationColumn;

    @FXML
    private TableColumn<Medicament, String> prixColumn;

    @FXML
    private TableColumn<Medicament, Integer> quantiteColumn;

    @FXML
    private TableColumn<Medicament, Void> actionColumn;

    @FXML
    private ListView<MedicamentPanier> panierListView;

    @FXML
    private Label totalLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Button btnAnnuler;

    @FXML
    private Button btnValider;

    private ObservableList<Medicament> medicamentsData = FXCollections.observableArrayList();
    private ObservableList<MedicamentPanier> panierData = FXCollections.observableArrayList();
    
    private UUID clientId;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String adresse;
    
    private double total = 0.0;

    @FXML
    public void initialize() {
        try {
            // Initialiser les listes avant de configurer les colonnes
            medicamentsData = FXCollections.observableArrayList();
            panierData = FXCollections.observableArrayList();
            
            // Configurer les colonnes et le panier
            configureTableColumns();
            configurePanierListView();
            
            // Configurer le bouton de recherche
            btnRechercher.setOnAction(event -> {
                try {
                    String searchTerm = searchField.getText().trim();
                    if (!searchTerm.isEmpty()) {
                        rechercherMedicaments(searchTerm);
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors de la recherche via le bouton", e);
                    errorLabel.setText("Erreur: " + e.getMessage());
                }
            });
            
            // Ajouter un listener sur le champ de recherche pour rechercher automatiquement
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (newValue.length() >= 3) {
                        rechercherMedicaments(newValue);
                    } else if (newValue.isEmpty()) {
                        medicamentsData.clear();
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors de la recherche automatique", e);
                    errorLabel.setText("Erreur: " + e.getMessage());
                }
            });
            
            // S'assurer que le champ de recherche est initialisé correctement
            searchField.setPromptText("Rechercher un médicament...");
            searchField.setFocusTraversable(true);
            
            // Initialiser les tableaux avec les listes
            medicamentsTable.setItems(medicamentsData);
            panierListView.setItems(panierData);
            
            // Initialiser le label d'erreur
            errorLabel.setText("");
            
            // Mettre à jour le total
            updateTotal();
            
            LOGGER.log(Level.INFO, "Initialisation de RechercheMedicamentsController terminée avec succès");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'initialisation de RechercheMedicamentsController", e);
            errorLabel.setText("Erreur d'initialisation: " + e.getMessage());
        }
    }
    
    public void setClientInfo(UUID clientId, String nom, String prenom, String telephone, String email, String adresse) {
        this.clientId = clientId;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
    }

    private void configureTableColumns() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("codeCIS"));
        libelleColumn.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        
        // Vérifier si la colonne dénomination existe avant de la configurer
        if (denominationColumn != null) {
            denominationColumn.setCellValueFactory(new PropertyValueFactory<>("denomination"));
        } else {
            LOGGER.log(Level.WARNING, "La colonne dénomination est null, impossible de la configurer");
        }
        
        prixColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(String.format("%.2f €", cellData.getValue().getPrixTTC())));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        
        // Configuration de la colonne d'actions
        actionColumn.setCellFactory(createActionColumnCellFactory());
    }
    
    private void configurePanierListView() {
        panierListView.setCellFactory(param -> new ListCell<MedicamentPanier>() {
            @Override
            protected void updateItem(MedicamentPanier item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    
                    Label nomLabel = new Label(item.getNomMedicament());
                    nomLabel.setPrefWidth(150);
                    
                    Label quantiteLabel = new Label("Qté: " + item.getQuantite());
                    quantiteLabel.setPrefWidth(60);
                    
                    Label prixLabel = new Label(String.format("%.2f €", item.getPrixUnitaire()));
                    prixLabel.setPrefWidth(70);
                    
                    Label totalLabel = new Label(String.format("Total: %.2f €", item.getPrixTotal()));
                    totalLabel.setPrefWidth(100);
                    
                    Button btnSupprimer = new Button("X");
                    btnSupprimer.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");
                    btnSupprimer.setOnAction(event -> {
                        panierData.remove(item);
                        updateTotal();
                    });
                    
                    hbox.getChildren().addAll(nomLabel, quantiteLabel, prixLabel, totalLabel, btnSupprimer);
                    setGraphic(hbox);
                }
            }
        });
    }

    private Callback<TableColumn<Medicament, Void>, TableCell<Medicament, Void>> createActionColumnCellFactory() {
        return new Callback<TableColumn<Medicament, Void>, TableCell<Medicament, Void>>() {
            @Override
            public TableCell<Medicament, Void> call(final TableColumn<Medicament, Void> param) {
                return new TableCell<Medicament, Void>() {
                    private final Button btnAjouter = new Button("Ajouter");

                    {
                        // Style du bouton
                        btnAjouter.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white;");
                        
                        // Action du bouton
                        btnAjouter.setOnAction(event -> {
                            Medicament medicament = getTableView().getItems().get(getIndex());
                            ajouterAuPanier(medicament);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btnAjouter);
                    }
                };
            }
        };
    }

    @FXML
    private void handleRechercher(ActionEvent event) {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            rechercherMedicaments(searchTerm);
        }
    }
    
    private void rechercherMedicaments(String searchTerm) {
        try {
            // Vérifier d'abord si le backend est accessible
            if (!ApiRest.isBackendAccessible()) {
                errorLabel.setText("Le serveur backend n'est pas accessible. Veuillez réessayer plus tard.");
                return;
            }
            
            // Récupérer les médicaments correspondant au terme de recherche avec pagination
            PageResponse<Medicament> pageResponse = ApiRest.getMedicamentsPagines(0, searchTerm);
            List<Medicament> medicaments = pageResponse.getContent();
            
            // Filtrer les médicaments qui ne nécessitent pas d'ordonnance
            List<Medicament> medicamentsSansOrdonnance = new ArrayList<>();
            for (Medicament medicament : medicaments) {
                if ("Sans".equals(medicament.getSurOrdonnance())) {
                    medicamentsSansOrdonnance.add(medicament);
                }
            }
            
            // Mettre à jour la liste des médicaments
            medicamentsData.clear();
            medicamentsData.addAll(medicamentsSansOrdonnance);
            
            if (medicamentsSansOrdonnance.isEmpty()) {
                errorLabel.setText("Aucun médicament sans ordonnance trouvé pour \"" + searchTerm + "\"");
            } else {
                errorLabel.setText("");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de médicaments", e);
            errorLabel.setText("Erreur lors de la recherche: " + e.getMessage());
        }
    }
    
    private void ajouterAuPanier(Medicament medicament) {
        // Vérifier si le médicament est déjà dans le panier
        for (MedicamentPanier item : panierData) {
            if (item.getMedicamentId() != null && 
                medicament.getCodeCIS() != null && 
                item.getMedicamentId().toString().equals(medicament.getCodeCIS())) {
                // Incrémenter la quantité
                item.setQuantite(item.getQuantite() + 1);
                panierListView.refresh();
                updateTotal();
                return;
            }
        }
        
        // Convertir codeCIS en Long si possible, sinon utiliser un ID par défaut
        Long medicamentId;
        try {
            medicamentId = Long.parseLong(medicament.getCodeCIS());
        } catch (NumberFormatException e) {
            // Si codeCIS n'est pas un nombre, utiliser un ID par défaut
            medicamentId = 1L; // ID par défaut
            LOGGER.log(Level.WARNING, "Impossible de convertir codeCIS en Long: " + medicament.getCodeCIS());
        }
        
        // Ajouter le médicament au panier
        MedicamentPanier item = new MedicamentPanier(
            medicamentId,
            1,
            medicament.getPrixTTC().doubleValue()
        );
        item.setNomMedicament(medicament.getLibelle());
        panierData.add(item);
        updateTotal();
    }
    
    private void updateTotal() {
        total = 0.0;
        for (MedicamentPanier item : panierData) {
            total += item.getPrixTotal();
        }
        totalLabel.setText(String.format("%.2f €", total));
    }

    @FXML
    private void handleAnnuler(ActionEvent event) {
        // Fermer la fenêtre
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleValider(ActionEvent event) {
        if (validatePanier()) {
            try {
                // Créer la requête de création de vente
                VenteCreateRequest request = new VenteCreateRequest();
                request.setClientId(clientId.toString());
                // Utiliser un ID de pharmacien adjoint fixe pour éviter les problèmes
                request.setPharmacienAdjointId("178ee63d-fcf4-4db1-a63c-bfdfa84bdd6e");
                request.setDateVente(DATE_FORMAT.format(new Date()));
                request.setModePaiement("Carte bancaire");
                request.setMontantTotal(total);
                request.setMontantRembourse(0.0); // Par défaut, pas de remboursement
                
                // Vérifier et nettoyer les médicaments du panier avant de les ajouter à la requête
                List<MedicamentPanier> medicamentsValides = new ArrayList<>();
                for (MedicamentPanier item : panierData) {
                    // S'assurer que l'ID du médicament n'est pas null
                    if (item.getMedicamentId() != null) {
                        medicamentsValides.add(item);
                    } else {
                        LOGGER.log(Level.WARNING, "Médicament ignoré car son ID est null");
                    }
                }
                request.setMedicaments(medicamentsValides);
                
                // Envoyer la requête à l'API
                Vente vente = ApiRest.createVente(request);
                
                // Afficher un message de succès
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Vente effectuée", 
                        "La vente a été enregistrée avec succès.");
                
                // Fermer la fenêtre
                Stage stage = (Stage) btnValider.getScene().getWindow();
                stage.close();
                
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de la création de la vente", e);
                errorLabel.setText("Erreur lors de la création de la vente: " + e.getMessage());
            }
        }
    }
    
    private boolean validatePanier() {
        if (panierData.isEmpty()) {
            errorLabel.setText("Le panier est vide. Veuillez ajouter au moins un médicament.");
            return false;
        }
        
        errorLabel.setText("");
        return true;
    }
    
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
