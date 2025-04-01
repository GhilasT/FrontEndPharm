package com.pharmacie.controller;

import com.pharmacie.model.Medicament;
import com.pharmacie.model.Vente;
import com.pharmacie.service.ApiRest;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
    public void initialize() {
        configureTableColumns();
        loadVentes();
    }

    private void configureTableColumns() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getIdVente().toString().substring(0, 8) + "..."));
        
        dateColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(DATE_FORMAT.format(cellData.getValue().getDateVente())));
        
        clientColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getClientId().toString().substring(0, 8) + "..."));
        
        montantColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(String.format("%.2f €", cellData.getValue().getMontantTotal())));
        
        paiementColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getModePaiement()));
        
        // Configuration de la colonne d'actions
        actionsColumn.setCellFactory(createActionsColumnCellFactory());
    }

    private Callback<TableColumn<Vente, Void>, TableCell<Vente, Void>> createActionsColumnCellFactory() {
        return new Callback<TableColumn<Vente, Void>, TableCell<Vente, Void>>() {
            @Override
            public TableCell<Vente, Void> call(final TableColumn<Vente, Void> param) {
                return new TableCell<Vente, Void>() {
                    private final Button btnSupprimer = new Button("Supprimer");
                    private final Button btnModifier = new Button("Modifier");
                    private final Button btnDetails = new Button("Détails");
                    private final HBox pane = new HBox(5, btnDetails, btnModifier, btnSupprimer);

                    {
                        // Style des boutons
                        btnSupprimer.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");
                        btnModifier.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white;");
                        btnDetails.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white;");
                        
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
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : pane);
                    }
                };
            }
        };
    }

    private void loadVentes() {
        try {
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
            // Ouvrir directement le formulaire client pour une vente sans ordonnance
            // sans passer par un popup de sélection
            ouvrirFormulaireClient();
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ouverture du formulaire de vente", e);
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ouverture du formulaire de vente", 
                    "Impossible d'ouvrir le formulaire: " + e.getMessage());
        }
    }

    private void ouvrirFormulaireClient() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/formulaire-client.fxml"));
            Parent root = loader.load();
            
            FormulaireClientController controller = loader.getController();
            
            Stage stage = new Stage();
            stage.setTitle("Formulaire Client");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            // Rafraîchir la liste des ventes après la création d'une nouvelle vente
            loadVentes();
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ouverture du formulaire client", e);
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
        // Cette fonctionnalité sera implémentée ultérieurement
        showAlert(Alert.AlertType.INFORMATION, "Information", 
                "Fonctionnalité non disponible", 
                "La modification de vente n'est pas encore implémentée.");
    }

    private void handleDetailsVente(Vente vente) {
        try {
            // Création d'une boîte de dialogue pour afficher les détails
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Détails de la vente");
            dialog.setHeaderText("Détails de la vente #" + vente.getIdVente().toString().substring(0, 8) + "...");
            
            // Création du contenu
            VBox content = new VBox(10);
            content.getChildren().add(new Label("ID: " + vente.getIdVente()));
            content.getChildren().add(new Label("Date: " + DATE_FORMAT.format(vente.getDateVente())));
            content.getChildren().add(new Label("Client ID: " + vente.getClientId()));
            content.getChildren().add(new Label("Pharmacien ID: " + vente.getPharmacienAdjointId()));
            content.getChildren().add(new Label("Mode de paiement: " + vente.getModePaiement()));
            content.getChildren().add(new Label("Montant total: " + String.format("%.2f €", vente.getMontantTotal())));
            content.getChildren().add(new Label("Montant remboursé: " + String.format("%.2f €", vente.getMontantRembourse())));
            
            // Ajout de la liste des médicaments
            content.getChildren().add(new Label("Médicaments:"));
            
            TableView<Medicament> medicamentsTable = new TableView<>();
            TableColumn<Medicament, String> libelleCol = new TableColumn<>("Libellé");
            libelleCol.setCellValueFactory(new PropertyValueFactory<>("libelle"));
            
            TableColumn<Medicament, String> quantiteCol = new TableColumn<>("Quantité");
            quantiteCol.setCellValueFactory(new PropertyValueFactory<>("quantite"));
            
            TableColumn<Medicament, String> prixCol = new TableColumn<>("Prix");
            prixCol.setCellValueFactory(cellData -> 
                new SimpleStringProperty(String.format("%.2f €", cellData.getValue().getPrix())));
            
            medicamentsTable.getColumns().addAll(libelleCol, quantiteCol, prixCol);
            medicamentsTable.setItems(FXCollections.observableArrayList(vente.getMedicaments()));
            medicamentsTable.setPrefHeight(200);
            
            content.getChildren().add(medicamentsTable);
            
            dialog.getDialogPane().setContent(content);
            dialog.getDialogPane().setPrefWidth(500);
            dialog.getDialogPane().setPrefHeight(500);
            
            ButtonType fermerType = new ButtonType("Fermer", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().add(fermerType);
            
            dialog.showAndWait();
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'affichage des détails de la vente", e);
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'affichage des détails", 
                    "Impossible d'afficher les détails: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
