package com.pharmacie.controller;

import com.pharmacie.model.Client;
import com.pharmacie.model.Employe;
import com.pharmacie.model.PharmacienAdjoint;
import com.pharmacie.model.Vente;
import com.pharmacie.service.ApiRest;
import com.pharmacie.util.LoggedSeller;
import com.pharmacie.util.SessionUtilisateur;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VentesController {

    private static final Logger LOGGER = Logger.getLogger(VentesController.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @FXML
    private TableView<Vente> ventesTable;

    @FXML
    private TableColumn<Vente, String> idColumn;

    @FXML
    private TableColumn<Vente, String> dateColumn;

    @FXML
    private TableColumn<Vente, String> clientColumn;
    private UUID utilisateurConnecte;

    @FXML
    private TableColumn<Vente, String> montantColumn;

    @FXML
    private TableColumn<Vente, String> paiementColumn;

    @FXML
    private TableColumn<Vente, Void> actionsColumn;

    @FXML
    private Button btnEffectuerVente;

    private ObservableList<Vente> ventesData = FXCollections.observableArrayList();

    @FXML
    private Button btnToggleVentes;
    private boolean showMySales = false;

    @FXML
    public void initialize() {
        configureTableColumns();
        loadVentes();
    }

    private void configureTableColumns() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(tronquerUUID(cellData.getValue().getIdVente())));

        dateColumn.setCellValueFactory(cellData -> {
            Date date = cellData.getValue().getDateVente();
            String formattedDate = (date != null) ? DATE_FORMAT.format(date) : "";
            return new SimpleStringProperty(formattedDate);
        });
        clientColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(tronquerUUID(cellData.getValue().getClientId())));

        montantColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.format("%.2f €", cellData.getValue().getMontantTotal())));

        paiementColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModePaiement()));

        // Configuration de la colonne d'actions
        actionsColumn.setCellFactory(createActionsColumnCellFactory());
    }

    private String tronquerUUID(UUID uuid) {
        if (uuid == null)
            return "???";
        return uuid.toString().substring(0, 8) + "...";
    }

    private Callback<TableColumn<Vente, Void>, TableCell<Vente, Void>> createActionsColumnCellFactory() {
        return param -> new TableCell<Vente, Void>() {
            private final Button btnSupprimer = new Button("Supprimer");
            private final Button btnModifier = new Button("Modifier");
            private final Button btnDetails = new Button("Détails");
            private final Button btnClient = new Button("Ventes Client");
            private final Button btnPharmacien = new Button("Ventes Pharmacien");
            private final HBox pane = new HBox(5, btnDetails, btnModifier, btnSupprimer, btnClient, btnPharmacien);
            {
                // Style des boutons
                btnSupprimer.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");
                btnModifier.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white;");
                btnDetails.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white;");
                btnClient.setStyle("-fx-background-color: #9B59B6; -fx-text-fill: white;");
                btnPharmacien.setStyle("-fx-background-color: #E67E22; -fx-text-fill: white;");
                pane.setAlignment(Pos.CENTER);

                // Actions des boutons
                btnSupprimer.setOnAction(event -> {
                    Vente vente = getTableView().getItems().get(getIndex());
                    handleSupprimerVente(vente);
                });

                btnModifier.setOnAction(event -> {
                    Vente vente = getTableView().getItems().get(getIndex());
                    handleModifierVente(vente);
                });

                btnDetails.setOnAction(event -> {
                    Vente vente = getTableView().getItems().get(getIndex());
                    handleDetailsVente(vente);
                });
                btnClient.setOnAction(event -> {
                    Vente vente = getTableView().getItems().get(getIndex());
                    loadVentesClient(vente.getClientId());
                });

                btnPharmacien.setOnAction(event -> {
                    Vente vente = getTableView().getItems().get(getIndex());
                    loadVentesPharmacien(vente.getPharmacienAdjointId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        };
    }

    private void loadVentes() {
        try {
            // Charger la liste des ventes via l'API REST
            List<Vente> ventes = ApiRest.getVentes();
            ventesData.clear();
            ventesData.addAll(ventes);
            ventesTable.setItems(ventesData);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement des ventes", e);
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des ventes",
                    "Impossible de charger les ventes: " + e.getMessage());
        }
    }

    @FXML
    private void handleEffectuerVente(ActionEvent event) {
        try {
            // 1) Ouvrir le formulaire client en modal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/formulaire-client.fxml"));
            Parent root = loader.load();
            FormulaireClientController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Formulaire Client");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // 2) Récupérer l'ID client depuis le contrôleur
            UUID clientId = controller.getClientId();
            if (clientId == null) {
                System.out.println("pas de id");
                return;
            }

            // 3) Ouvrir la page de vente (panier)
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/com/pharmacie/view/vente.fxml"));
            Parent root2 = loader2.load();
            System.out.println("vente.fxml chargé avec succès");

            VenteController venteCtrl = loader2.getController();
            venteCtrl.setClientId(clientId);

            LoggedSeller loggedSeller = LoggedSeller.getInstance();
            venteCtrl.setPharmacienAdjointId(loggedSeller.getId());

            Stage stage2 = new Stage();
            stage2.setTitle("Nouvelle Vente - Panier");
            stage2.setScene(new Scene(root2));
            stage2.initModality(Modality.APPLICATION_MODAL);
            stage2.showAndWait();

            // 4) Rafraîchir les ventes
            loadVentes();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ouverture du formulaire de vente", e);
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ouverture du formulaire client",
                    "Impossible d'ouvrir le formulaire: " + e.getMessage());
        }
    }

    private void handleSupprimerVente(Vente vente) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de suppression");
        confirmDialog.setHeaderText("Supprimer la vente");
        confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer cette vente ?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                ApiRest.deleteVente(vente.getIdVente());
                ventesData.remove(vente);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Vente supprimée",
                        "La vente a été supprimée avec succès.");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de la vente", e);
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression",
                        "Impossible de supprimer la vente: " + e.getMessage());
            }
        }
    }

    private void handleModifierVente(Vente vente) {
        // Exemple : fonctionnalité non encore implémentée
        showAlert(Alert.AlertType.INFORMATION, "Information",
                "Fonctionnalité non disponible",
                "La modification de vente n'est pas encore implémentée.");
    }

    private void handleDetailsVente(Vente vente) {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle("Détails de la vente " + tronquerUUID(vente.getIdVente()));

    Button btnPharmacien = new Button("Détails du Pharmacien");
    Button btnClient = new Button("Détails du Client");
    Button btnMedicaments = new Button("Liste des médicaments");

    // Style cohérent avec l'application
    String buttonStyle = "-fx-pref-width: 200px; -fx-padding: 8px;";
    btnPharmacien.setStyle(buttonStyle + "-fx-background-color: #3498DB; -fx-text-fill: white;");
    btnClient.setStyle(buttonStyle + "-fx-background-color: #2ECC71; -fx-text-fill: white;");
    btnMedicaments.setStyle(buttonStyle + "-fx-background-color: #9B59B6; -fx-text-fill: white;");

    // Gestion des clics
    btnPharmacien.setOnAction(e -> {
        try {
            PharmacienAdjoint pharmacien = ApiRest.getPharmacienById(vente.getPharmacienAdjointId());
            afficherDetailsPharmacien(pharmacien);
        } catch (Exception ex) {
            gererErreurApi("pharmacien", ex);
        }
    });

    btnClient.setOnAction(e -> {
        try {
            Client client = ApiRest.getClientById(vente.getClientId());
            afficherDetailsClient(client);
        } catch (Exception ex) {
            gererErreurApi("client", ex);
        }
    });

    btnMedicaments.setOnAction(e -> 
        showNotImplementedAlert("Liste des médicaments"));

    VBox layout = new VBox(15);
    layout.setAlignment(Pos.CENTER);
    layout.setPadding(new Insets(20));
    layout.getChildren().addAll(btnPharmacien, btnClient, btnMedicaments);

    dialog.setScene(new Scene(layout, 350, 250));
    dialog.showAndWait();
}


private void afficherDetailsClient(Client client) {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle("📋 Détails Client");

    GridPane grid = new GridPane();
    grid.setPadding(new Insets(20));
    grid.setHgap(15);
    grid.setVgap(10);
    grid.getStyleClass().add("details-dialog");

    // Header
    Label header = new Label("Fiche Client");
    header.getStyleClass().add("details-header");
    grid.add(header, 0, 0, 2, 1);

    // Lignes d'information
    addFormRow(grid, 1, "👤", "Nom Complet:", client.getPrenom() + " " + client.getNom());
    addFormRow(grid, 2, "📧", "Email:", client.getEmail());
    addFormRow(grid, 3, "📱", "Téléphone:", client.getTelephone());
    addFormRow(grid, 4, "🏠", "Adresse:", client.getAdresse());
    
    if(client.getNumeroSecu() != null) {
        addFormRow(grid, 5, "🆔", "Numéro Sécu:", client.getNumeroSecu());
    }
    if(client.getMutuelle() != null) {
        addFormRow(grid, 6, "🏥", "Mutuelle:", client.getMutuelle());
    }

    Scene scene = new Scene(grid);
    scene.getStylesheets().add(getClass().getResource("/com/pharmacie/css/styles.css").toExternalForm());
    dialog.setScene(scene);
    dialog.sizeToScene();
    dialog.show();
}

private void afficherDetailsPharmacien(PharmacienAdjoint pharmacien) {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle("💼 Détails Pharmacien");

    GridPane grid = new GridPane();
    grid.setPadding(new Insets(20));
    grid.setHgap(15);
    grid.setVgap(10);
    grid.getStyleClass().add("details-dialog");

    // Header
    Label header = new Label("Profil Professionnel");
    header.getStyleClass().add("details-header");
    grid.add(header, 0, 0, 2, 1);

    // Lignes d'information
    addFormRow(grid, 1, "👤", "Nom Complet:", pharmacien.getPrenom() + " " + pharmacien.getNom());
    addFormRow(grid, 2, "🆔", "Matricule:", pharmacien.getMatricule());
    addFormRow(grid, 3, "📧", "Email Pro:", pharmacien.getEmailPro());
    addFormRow(grid, 4, "📅", "Date Embauche:", pharmacien.getDateEmbauche().toString());
    addFormRow(grid, 5, "💰", "Salaire:", String.format("%.2f €", pharmacien.getSalaire()));
    addFormRow(grid, 6, "🏢", "Poste:", pharmacien.getPoste());
    addFormRow(grid, 7, "📝", "Statut Contrat:", pharmacien.getStatutContrat());

    Scene scene = new Scene(grid);
    scene.getStylesheets().add(getClass().getResource("/com/pharmacie/css/styles.css").toExternalForm());
    dialog.setScene(scene);
    dialog.sizeToScene();
    dialog.show();
}
private void addFormRow(GridPane grid, int row, String icon, String labelText, String value) {
    Label iconLabel = new Label(icon);
    iconLabel.getStyleClass().add("detail-icon");
    
    Label label = new Label(labelText);
    label.getStyleClass().add("detail-label");
    
    Label valueLabel = new Label(value != null ? value : "N/A");
    valueLabel.getStyleClass().add("detail-value");
    valueLabel.setWrapText(true);

    HBox hbox = new HBox(5, iconLabel, label);
    hbox.setAlignment(Pos.CENTER_LEFT);

    grid.add(hbox, 0, row);
    grid.add(valueLabel, 1, row);
}




// Gestion des erreurs d'API
private void gererErreurApi(String entite, Exception ex) {
    LOGGER.log(Level.SEVERE, "Erreur API - " + entite, ex);
    showAlert(Alert.AlertType.ERROR, "Erreur", 
        "Erreur de communication", 
        String.format("Impossible de récupérer les détails du %s: %s", 
            entite, ex.getMessage()));
}

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setUtilisateurConnecte(UUID utilisateurConnecte) {
        this.utilisateurConnecte = utilisateurConnecte;
    }

    private void loadVentesClient(UUID clientId) {
        try {
            List<Vente> ventes = ApiRest.getVentesByClientId(clientId);
            ventesData.setAll(ventes);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Chargement impossible",
                    "Erreur lors du chargement des ventes client: " + e.getMessage());
        }
    }

    private void loadVentesPharmacien(UUID pharmacienId) {
        try {
            List<Vente> ventes = ApiRest.getVentesByPharmacienId(pharmacienId);
            ventesData.setAll(ventes);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Chargement impossible",
                    "Erreur lors du chargement des ventes pharmacien: " + e.getMessage());
        }
    }

    @FXML
    private void handleToggleVentes(ActionEvent event) {
        showMySales = !showMySales;

        if (showMySales) {
            btnToggleVentes.setText("Afficher toutes les ventes");
            btnToggleVentes.setStyle("-fx-background-color: #1F82F2; -fx-background-radius: 5;");
            loadMyVentes();
        } else {
            btnToggleVentes.setText("Afficher mes ventes");
            btnToggleVentes.setStyle("-fx-background-color: #007B3D; -fx-background-radius: 5;");
            loadVentes();
        }
    }

    private void loadMyVentes() {
        try {
            UUID pharmacienId = LoggedSeller.getInstance().getId();
            List<Vente> ventes = ApiRest.getVentesByPharmacienId(pharmacienId);
            ventesData.setAll(ventes);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Chargement impossible",
                    "Erreur lors du chargement des ventes: " + e.getMessage());
        }
    }

    @FXML
    private void handleReset(ActionEvent event) {
        showMySales = false;
    btnToggleVentes.setText("Afficher mes ventes");
    btnToggleVentes.setStyle("-fx-background-color: #007B3D; -fx-background-radius: 5;");
        loadVentes();
    }
    private void showNotImplementedAlert(String featureName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Fonctionnalité non disponible");
        alert.setContentText("L'option '" + featureName + "' n'est pas encore implémentée.");
        alert.showAndWait();
    }
}
