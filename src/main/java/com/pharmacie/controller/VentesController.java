package com.pharmacie.controller;

import com.pharmacie.model.Client;
import com.pharmacie.model.Employe;
import com.pharmacie.model.Medicament;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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

/**
 * Contrôleur pour la gestion de l'affichage et des interactions avec la liste des ventes.
 * Permet d'effectuer des ventes, de les modifier, supprimer, et de visualiser leurs détails.
 */
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
    @FXML private BorderPane rootPane;
    private Node listeInitiale;

    private UUID clientId;
    /**
     * Définit l'ID du client pour une nouvelle vente.
     * @param id L'UUID du client.
     */
    public void setClientId(UUID id) {
        this.clientId = id;
    }

    /**
     * Initialise le contrôleur après le chargement du FXML.
     * Configure les colonnes de la table et charge les données initiales des ventes.
     */
    @FXML
    public void initialize() {
        listeInitiale = rootPane.getCenter();
        configureTableColumns();
        loadVentes();
    }

    /**
     * Configure les colonnes de la TableView des ventes, y compris les usines de cellules
     * pour l'affichage des données et des boutons d'action.
     */
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

    /**
     * Tronque un UUID pour un affichage plus concis.
     * @param uuid L'UUID à tronquer.
     * @return Une chaîne représentant les 8 premiers caractères de l'UUID suivis de "...".
     *         Retourne "???" si l'UUID est nul.
     */
    private String tronquerUUID(UUID uuid) {
        if (uuid == null)
            return "???";
        return uuid.toString().substring(0, 8) + "...";
    }

    /**
     * Crée une usine de cellules pour la colonne d'actions de la table des ventes.
     * Cette colonne contient des boutons pour détailler, modifier, supprimer une vente,
     * et filtrer les ventes par client ou pharmacien.
     * @return Un Callback pour la création des cellules d'action.
     */
    private Callback<TableColumn<Vente, Void>, TableCell<Vente, Void>> createActionsColumnCellFactory() {
        return param -> new TableCell<Vente, Void>() {
            private final Button btnSupprimer = new Button("Supprimer");
            private final Button btnModifier = new Button("Modifier");
            private final Button btnDetails = new Button("Détails");
            private final Button btnClient = new Button("Ventes Client");
            private final Button btnPharmacien = new Button("Ventes Pharmacien");
            private final HBox pane = new HBox(10, btnDetails, btnModifier, btnSupprimer, btnClient, btnPharmacien);
            {
                pane.setAlignment(Pos.CENTER);
                pane.setPadding(new Insets(0, 8, 0, 8));


                btnDetails   .setMinWidth( 70);
                btnModifier  .setMinWidth( 80);
                btnSupprimer .setMinWidth( 90);
                btnClient    .setMinWidth(120);
                btnPharmacien.setMinWidth(140);
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

    /**
     * Charge la liste de toutes les ventes depuis l'API REST et les affiche dans la table.
     * Gère les exceptions potentielles lors de l'appel à l'API.
     */
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

    /**
     * Gère l'action du bouton "Effectuer une vente".
     * Ouvre une fenêtre modale pour sélectionner ou créer un client, puis charge la vue de vente.
     * @param event L'événement d'action déclenché par le bouton.
     */
    @FXML
    private void handleEffectuerVente(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/ClientsPage.fxml"));
            Parent root = loader.load();
            ClientsController ctrl = loader.getController();

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Sélectionner ou créer un client");
            modal.setScene(new Scene(root));

            ctrl.setVenteController(this);
            ctrl.setModalStage(modal);

            modal.showAndWait();

            if (this.clientId == null) {

                return;
            }

            // 2) On charge la vue “vente.fxml” DANS LE même rootPane
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/com/pharmacie/view/vente.fxml"));
            Parent ventePane = loader2.load();
            VenteController venteCtrl = loader2.getController();
            venteCtrl.setParentController(this);
            venteCtrl.setClientId(clientId);
            venteCtrl.setPharmacienAdjointId(LoggedSeller.getInstance().getId());

            rootPane.setCenter(ventePane);

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d’ouvrir le formulaire client ou la page de vente", e.getMessage());
        }
    }

    /**
     * Rétablit l'affichage central du BorderPane à la liste initiale des ventes.
     */
    public void returnToList() {
        rootPane.setCenter(listeInitiale);
    }

    /**
     * Gère la suppression d'une vente.
     * Affiche une boîte de dialogue de confirmation avant de supprimer la vente via l'API REST.
     * @param vente La vente à supprimer.
     */
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

    /**
     * Gère la modification d'une vente.
     * Ouvre une fenêtre modale avec le formulaire de modification de la vente.
     * @param vente La vente à modifier.
     */
    private void handleModifierVente(Vente vente) {
        try {
            // Chargement du formulaire de modification de vente
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/modifier-vente.fxml"));
            Parent root = loader.load();
            
            // Initialisation du contrôleur
            ModifierVenteController controller = loader.getController();
            controller.setVente(vente);
            controller.setVentesController(this);
            controller.initialize();
            
            // Affichage dans une nouvelle fenêtre
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifier la vente - " + tronquerUUID(vente.getIdVente()));
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            // Après la fermeture de la fenêtre, rafraîchir la liste des ventes
            loadVentes();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement du formulaire de modification", e);
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur d'interface",
                    "Impossible de charger le formulaire de modification: " + e.getMessage());
        }
    }

    /**
     * Affiche une boîte de dialogue avec des options pour voir les détails
     * du pharmacien, du client ou des médicaments associés à une vente.
     * @param vente La vente pour laquelle afficher les détails.
     */
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

        btnMedicaments.setOnAction(e -> {
            Stage medicamentStage = new Stage();
            medicamentStage.initModality(Modality.APPLICATION_MODAL);
            medicamentStage.setTitle("Médicaments de la vente - " + tronquerUUID(vente.getIdVente()));

            VBox vbox = new VBox(10);
            vbox.setPadding(new Insets(15));
            vbox.setStyle("-fx-background-color: #f5f5f5;");

            for (Medicament medicament : vente.getMedicaments()) {
                GridPane grid = new GridPane();
                grid.setMaxWidth(580);
                grid.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-radius: 5;");

                // Configuration des colonnes (3/5 - 1/5 - 1/5)
                ColumnConstraints col1 = new ColumnConstraints();
                col1.setPercentWidth(60); // 3/5 de l'espace
                ColumnConstraints col2 = new ColumnConstraints();
                col2.setPercentWidth(20); // 1/5
                ColumnConstraints col3 = new ColumnConstraints();
                col3.setPercentWidth(20); // 1/5
                grid.getColumnConstraints().addAll(col1, col2, col3);

                // 1ère colonne: Dénomination
                String denomination = medicament.getDenomination();
                int maxLength = 50;
                if (denomination.length() > maxLength) {
                    denomination = denomination.substring(0, maxLength) + "...";
                }
                Label nomLabel = new Label(denomination);
                nomLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
                grid.add(nomLabel, 0, 0);

                // 2ème colonne: Quantité
                Medicament.Stock stock = !medicament.getStocks().isEmpty() ?
                        medicament.getStocks().get(0) : null;
                String quantite = stock != null ? String.valueOf(stock.getQuantite()) : "N/A";
                Label qteLabel = new Label("• Quantité: " + quantite);
                qteLabel.setStyle("-fx-text-fill: #666; -fx-alignment: CENTER;");
                grid.add(qteLabel, 1, 0);

                // 3ème colonne: Bouton
                Button detailsBtn = new Button("Détails");
                detailsBtn.setMaxWidth(Double.MAX_VALUE);
                detailsBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                detailsBtn.setOnAction(event -> {
                    try {
                        Medicament fullMed = ApiRest.getMedicamentByCodeCip13(medicament.getCodeCip13());
                        showMedicamentDetails(fullMed);
                    } catch (Exception ex) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Détails indisponibles",
                                ex.getMessage());
                    }
                });

                // Alignement du bouton
                HBox btnContainer = new HBox(detailsBtn);
                btnContainer.setAlignment(Pos.CENTER_RIGHT);
                grid.add(btnContainer, 2, 0);

                vbox.getChildren().add(grid);
            }

            ScrollPane scrollPane = new ScrollPane(vbox);
            scrollPane.setFitToWidth(true);
            medicamentStage.setScene(new Scene(scrollPane, 600, 400));
            medicamentStage.showAndWait();
        });

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(btnPharmacien, btnClient, btnMedicaments);

        dialog.setScene(new Scene(layout, 350, 250));
        dialog.showAndWait();
    }

    /**
     * Affiche les détails d'un médicament dans une nouvelle fenêtre modale.
     * @param medicament Le médicament dont les détails doivent être affichés.
     */
    private void showMedicamentDetails(Medicament medicament) {
        Stage detailsStage = new Stage();
        detailsStage.initModality(Modality.APPLICATION_MODAL);
        detailsStage.setTitle("Détails médicament - " + medicament.getCodeCip13());

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: #ffffff;");

        // Style commun
        String labelStyle = "-fx-text-fill: #666; -fx-font-weight: bold;";
        String valueStyle = "-fx-text-fill: #333;";

        int row = 0;

        // Informations générales
        addStyledRow(grid, row++, "Dénomination:", medicament.getDenomination(), labelStyle, valueStyle);
        addStyledRow(grid, row++, "Libelle:", medicament.getLibelle(), labelStyle, valueStyle);
        addStyledRow(grid, row++, "Code CIP13:", medicament.getCodeCip13(), labelStyle, valueStyle);
        addStyledRow(grid, row++, "Prix TTC:", medicament.getPrixTTC() + " €", labelStyle, valueStyle);

        // Informations du premier stock
        if (!medicament.getStocks().isEmpty()) {
            Medicament.Stock stock = medicament.getStocks().get(0);
            addStyledRow(grid, row++, "Lot:", stock.getNumeroLot(), labelStyle, valueStyle);
            addStyledRow(grid, row++, "Péremption:", stock.getDatePeremption(), labelStyle, valueStyle);
            addStyledRow(grid, row++, "Emplacement:", stock.getEmplacement(), labelStyle, valueStyle);
            addStyledRow(grid, row++, "Stock actuel:", String.valueOf(stock.getQuantite()), labelStyle, valueStyle);
            addStyledRow(grid, row++, "Seuil d'alerte:", String.valueOf(stock.getSeuilAlerte()), labelStyle,
                    valueStyle);
        } else {
            addStyledRow(grid, row++, "Stock:", "Aucune information disponible", labelStyle, valueStyle);
        }

        Scene scene = new Scene(grid);
        detailsStage.setScene(scene);
        detailsStage.sizeToScene();
        detailsStage.showAndWait();
    }

    /**
     * Ajoute une ligne stylisée à un GridPane, typiquement pour afficher une paire label-valeur.
     * @param grid Le GridPane auquel ajouter la ligne.
     * @param row L'index de la ligne où ajouter les éléments.
     * @param labelText Le texte du label.
     * @param value La valeur à afficher.
     * @param labelStyle Le style CSS pour le label.
     * @param valueStyle Le style CSS pour la valeur.
     */
    private void addStyledRow(GridPane grid, int row, String labelText, String value, String labelStyle,
                              String valueStyle) {
        Label label = new Label(labelText);
        label.setStyle(labelStyle);

        Label valueLabel = new Label(value != null ? value : "N/A");
        valueLabel.setStyle(valueStyle);
        valueLabel.setWrapText(true);

        grid.add(label, 0, row);
        grid.add(valueLabel, 1, row);
    }

    /**
     * Affiche les détails d'un client dans une nouvelle fenêtre modale.
     * @param client Le client dont les détails doivent être affichés.
     */
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

        if (client.getNumeroSecu() != null) {
            addFormRow(grid, 5, "🆔", "Numéro Sécu:", client.getNumeroSecu());
        }
        if (client.getMutuelle() != null) {
            addFormRow(grid, 6, "🏥", "Mutuelle:", client.getMutuelle());
        }

        Scene scene = new Scene(grid);
        scene.getStylesheets().add(getClass().getResource("/com/pharmacie/css/styles.css").toExternalForm());
        dialog.setScene(scene);
        dialog.sizeToScene();
        dialog.show();
    }

    /**
     * Affiche les détails d'un pharmacien adjoint dans une nouvelle fenêtre modale.
     * @param pharmacien Le pharmacien adjoint dont les détails doivent être affichés.
     */
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

    /**
     * Ajoute une ligne de formulaire (icône, label, valeur) à un GridPane.
     * Utilisé pour afficher les détails d'une entité de manière structurée.
     * @param grid Le GridPane auquel ajouter la ligne.
     * @param row L'index de la ligne.
     * @param icon Le caractère ou la chaîne représentant l'icône.
     * @param labelText Le texte du label.
     * @param value La valeur à afficher.
     */
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

    /**
     * Gère les erreurs survenant lors des appels à l'API REST.
     * Enregistre l'erreur et affiche une alerte à l'utilisateur.
     * @param entite Le nom de l'entité concernée par l'erreur (ex: "client", "pharmacien").
     * @param ex L'exception survenue.
     */
    private void gererErreurApi(String entite, Exception ex) {
        LOGGER.log(Level.SEVERE, "Erreur API - " + entite, ex);
        showAlert(Alert.AlertType.ERROR, "Erreur",
                "Erreur de communication",
                String.format("Impossible de récupérer les détails du %s: %s",
                        entite, ex.getMessage()));
    }

    /**
     * Affiche une boîte de dialogue d'alerte.
     * @param type Le type d'alerte (ex: ERROR, INFORMATION).
     * @param title Le titre de la fenêtre d'alerte.
     * @param header Le texte d'en-tête de l'alerte.
     * @param content Le message principal de l'alerte.
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Définit l'UUID de l'utilisateur actuellement connecté.
     * @param utilisateurConnecte L'UUID de l'utilisateur.
     */
    public void setUtilisateurConnecte(UUID utilisateurConnecte) {
        this.utilisateurConnecte = utilisateurConnecte;
    }

    /**
     * Charge et affiche les ventes associées à un ID client spécifique.
     * @param clientId L'UUID du client.
     */
    private void loadVentesClient(UUID clientId) {
        try {
            List<Vente> ventes = ApiRest.getVentesByClientId(clientId);
            ventesData.setAll(ventes);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Chargement impossible",
                    "Erreur lors du chargement des ventes client: " + e.getMessage());
        }
    }

    /**
     * Charge et affiche les ventes associées à un ID pharmacien spécifique.
     * @param pharmacienId L'UUID du pharmacien.
     */
    private void loadVentesPharmacien(UUID pharmacienId) {
        try {
            List<Vente> ventes = ApiRest.getVentesByPharmacienId(pharmacienId);
            ventesData.setAll(ventes);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Chargement impossible",
                    "Erreur lors du chargement des ventes pharmacien: " + e.getMessage());
        }
    }

    /**
     * Gère l'action du bouton pour basculer entre l'affichage de toutes les ventes
     * et uniquement les ventes du pharmacien connecté.
     * @param event L'événement d'action.
     */
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

    /**
     * Charge les ventes effectuées par le pharmacien actuellement connecté.
     */
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

    /**
     * Gère l'action du bouton de réinitialisation des filtres d'affichage des ventes.
     * Affiche à nouveau toutes les ventes.
     * @param event L'événement d'action.
     */
    @FXML
    private void handleReset(ActionEvent event) {
        showMySales = false;
        btnToggleVentes.setText("Afficher mes ventes");
        btnToggleVentes.setStyle("-fx-background-color: #007B3D; -fx-background-radius: 5;");
        loadVentes();
    }

    /**
     * Affiche une alerte informant que la fonctionnalité demandée n'est pas encore implémentée.
     * @param featureName Le nom de la fonctionnalité non implémentée.
     */
    private void showNotImplementedAlert(String featureName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Fonctionnalité non disponible");
        alert.setContentText("L'option '" + featureName + "' n'est pas encore implémentée.");
        alert.showAndWait();
    }
}
