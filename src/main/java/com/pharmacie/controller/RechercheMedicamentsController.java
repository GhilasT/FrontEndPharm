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
import java.util.stream.Collectors;

/**
 * Contrôleur pour la fonctionnalité de recherche de médicaments et la gestion du panier.
 * Permet de rechercher des médicaments, de les ajouter à un panier et de valider la vente.
 */
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

    /**
     * Initialise le contrôleur après le chargement du FXML.
     * Configure les colonnes de la table, le panier, les listeners et les boutons.
     */
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

    /**
     * Définit les informations du client pour la vente en cours.
     * @param clientId L'identifiant unique du client.
     * @param nom Le nom du client.
     * @param prenom Le prénom du client.
     * @param telephone Le numéro de téléphone du client.
     * @param email L'adresse e-mail du client.
     * @param adresse L'adresse postale du client.
     */
    public void setClientInfo(UUID clientId, String nom, String prenom, String telephone, String email, String adresse) {
        this.clientId = clientId;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
    }

    /**
     * Configure les colonnes de la table des médicaments.
     * Définit les fabriques de cellules pour chaque colonne (ID, libellé, dénomination, prix, quantité, actions).
     */
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

    /**
     * Configure l'affichage de la liste des médicaments dans le panier.
     * Définit une fabrique de cellules personnalisée pour afficher les détails de chaque médicament du panier
     * et un bouton pour le supprimer.
     */
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

    /**
     * Crée une fabrique de cellules pour la colonne d'actions de la table des médicaments.
     * Chaque cellule contiendra un bouton "Ajouter" pour ajouter le médicament correspondant au panier.
     * @return Une instance de Callback pour la création des cellules d'action.
     */
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

    /**
     * Gère l'événement de clic sur le bouton de recherche.
     * Lance la recherche de médicaments en utilisant le terme saisi dans le champ de recherche.
     * @param event L'événement d'action déclenché par le clic.
     */
    @FXML
    private void handleRechercher(ActionEvent event) {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            rechercherMedicaments(searchTerm);
        }
    }

    /**
     * Effectue la recherche de médicaments via l'API.
     * Récupère les médicaments correspondant au terme de recherche, filtre ceux sans ordonnance
     * et met à jour la table des médicaments.
     * @param searchTerm Le terme à utiliser pour la recherche.
     */
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

    /**
     * Ajoute un médicament sélectionné au panier.
     * Crée un nouvel objet {@link MedicamentPanier} et l'ajoute à la liste des données du panier.
     * Met à jour le total du panier.
     * @param medicament Le médicament à ajouter au panier.
     */
    private void ajouterAuPanier(Medicament medicament) {
        String codeCIS = medicament.getCodeCIS();

        if (codeCIS == null || codeCIS.isBlank()) {
            LOGGER.warning("Code CIS manquant pour le médicament : " + medicament);
            return;
        }

        double prix = (medicament.getPrixTTC() != null) ? medicament.getPrixTTC().doubleValue() : 0.0;

        MedicamentPanier nouvelItem = new MedicamentPanier(codeCIS, 1, prix);
        nouvelItem.setNomMedicament(medicament.getLibelle());

        panierData.add(nouvelItem);
        updateTotal();
    }

    /**
     * Met à jour le montant total affiché du panier.
     * Calcule la somme des prix totaux de tous les articles dans le panier.
     */
    private void updateTotal() {
        total = 0.0;
        for (MedicamentPanier item : panierData) {
            total += item.getPrixTotal();
        }
        totalLabel.setText(String.format("%.2f €", total));
    }

    /**
     * Gère l'événement de clic sur le bouton "Annuler".
     * Ferme la fenêtre actuelle de recherche de médicaments.
     * @param event L'événement d'action déclenché par le clic.
     */
    @FXML
    private void handleAnnuler(ActionEvent event) {
        // Fermer la fenêtre
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    /**
     * Gère l'événement de clic sur le bouton "Valider".
     * Crée une nouvelle vente avec les médicaments présents dans le panier.
     * Affiche une alerte de succès et ferme la fenêtre si la vente est créée.
     * @param event L'événement d'action déclenché par le clic.
     */
    @FXML
    private void handleValider(ActionEvent event) {
        if (panierData.isEmpty()) {
            errorLabel.setText("Le panier est vide. Veuillez ajouter au moins un médicament.");
            return;
        }

        try {
            VenteCreateRequest request = new VenteCreateRequest();
            request.setClientId(clientId);
            request.setPharmacienAdjointId(UUID.fromString("178ee63d-fcf4-4db1-a63c-bfdfa84bdd6e")); // à adapter
            request.setDateVente(new Date());
            request.setModePaiement("Carte bancaire");
            request.setMontantTotal(total);
            request.setMontantRembourse(0.0);

            List<MedicamentPanier> medicamentsValides = panierData.stream()
                    .filter(item -> item.getCodeCip13() != null && !item.getCodeCip13().isBlank())
                    .collect(Collectors.toList());

            request.setMedicaments(medicamentsValides);

            ApiRest.createVente(request);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Vente effectuée",
                    "La vente a été enregistrée avec succès.");

            ((Stage) btnValider.getScene().getWindow()).close();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création de la vente", e);
            errorLabel.setText("Erreur : " + e.getMessage());
        }
    }

    /**
     * Valide si le panier contient des articles avant de procéder à la vente.
     * @return true si le panier n'est pas vide, false sinon.
     */
    private boolean validatePanier() {
        if (panierData.isEmpty()) {
            errorLabel.setText("Le panier est vide. Veuillez ajouter au moins un médicament.");
            return false;
        }

        errorLabel.setText("");
        return true;
    }

    /**
     * Affiche une boîte de dialogue d'alerte.
     * @param type Le type d'alerte (ex: INFORMATION, ERROR).
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
}
