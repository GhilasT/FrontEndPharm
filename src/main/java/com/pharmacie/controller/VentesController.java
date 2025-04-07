package com.pharmacie.controller;

import com.pharmacie.model.Employe;
import com.pharmacie.model.Vente;
import com.pharmacie.service.ApiRest;
import com.pharmacie.util.SessionUtilisateur;
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
import javafx.scene.layout.HBox;
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
    public void initialize() {
        configureTableColumns();
        loadVentes();
    }

    private void configureTableColumns() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(tronquerUUID(cellData.getValue().getIdVente())));

        dateColumn.setCellValueFactory(cellData -> {
            Date date = cellData.getValue().getDateVente();
            String formattedDate = (date != null) ? DATE_FORMAT.format(date) : "";
            return new SimpleStringProperty(formattedDate);
        });
        clientColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(tronquerUUID(cellData.getValue().getClientId())));

        montantColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f €", cellData.getValue().getMontantTotal())));

        paiementColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getModePaiement()));

        // Configuration de la colonne d'actions
        actionsColumn.setCellFactory(createActionsColumnCellFactory());
    }

    private String tronquerUUID(UUID uuid) {
        if (uuid == null) return "???";
        return uuid.toString().substring(0, 8) + "...";
    }

    private Callback<TableColumn<Vente, Void>, TableCell<Vente, Void>> createActionsColumnCellFactory() {
        return param -> new TableCell<Vente, Void>() {
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

            // ✅ Transmettre aussi l'ID du pharmacien connecté
            if (SessionUtilisateur.getUtilisateurConnecte() != null) {
                venteCtrl.setPharmacienAdjointId(SessionUtilisateur.getUtilisateurConnecte().getIdPersonne());

            }

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
        // Exemple: tu peux reprendre le code existant pour afficher
        // la fenêtre de détails (table de médicaments, etc.)
        showAlert(Alert.AlertType.INFORMATION, "Détails Vente",
                "Détails de la vente",
                "Ici, tu peux montrer une popup de détails ou un nouvel écran.");
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
}
