package com.pharmacie.controller;

import com.pharmacie.model.Apprenti;
import com.pharmacie.model.dto.ApprentiCreateRequest;
import com.pharmacie.model.dto.ApprentiUpdateRequest;
import com.pharmacie.service.ApprentiApi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Contrôleur pour la gestion des apprentis.
 * Gère l'affichage, l'ajout, la modification, la suppression et la recherche des apprentis.
 */
public class GestionApprentiController {
    @FXML private TableView<Apprenti> apprentiTable;
    @FXML private TableColumn<Apprenti, UUID> idColumn;
    @FXML private TableColumn<Apprenti, String> nomColumn;
    @FXML private TableColumn<Apprenti, String> prenomColumn;
    @FXML private TableColumn<Apprenti, String> emailColumn;
    @FXML private TableColumn<Apprenti, String> telephoneColumn;
    @FXML private TableColumn<Apprenti, String> adresseColumn;
    @FXML private TableColumn<Apprenti, String> matriculeColumn;
    @FXML private TableColumn<Apprenti, LocalDate> dateEmbaucheColumn;
    @FXML private TableColumn<Apprenti, Double> salaireColumn;
    @FXML private TableColumn<Apprenti, String> posteColumn;
    @FXML private TableColumn<Apprenti, String> statutContratColumn;
    @FXML private TableColumn<Apprenti, String> diplomeColumn;
    @FXML private TableColumn<Apprenti, String> ecoleColumn;
    @FXML private TableColumn<Apprenti, String> emailProColumn;

    @FXML private TextField searchField;
    @FXML private Button modifierButton, supprimerButton;
    
    private ObservableList<Apprenti> apprentiData = FXCollections.observableArrayList();
    private DashboardAdminController parentController;
    private ApprentiApi apprentiService;

    /**
     * Initialise le contrôleur après le chargement du FXML.
     * Configure les colonnes de la table, les écouteurs d'événements et charge les données initiales.
     */
    @FXML
    public void initialize() {
        apprentiService = new ApprentiApi();
        
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idPersonne"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        matriculeColumn.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        dateEmbaucheColumn.setCellValueFactory(new PropertyValueFactory<>("dateEmbauche"));
        salaireColumn.setCellValueFactory(new PropertyValueFactory<>("salaire"));
        posteColumn.setCellValueFactory(new PropertyValueFactory<>("poste"));
        statutContratColumn.setCellValueFactory(new PropertyValueFactory<>("statutContrat"));
        diplomeColumn.setCellValueFactory(new PropertyValueFactory<>("diplome"));
        ecoleColumn.setCellValueFactory(new PropertyValueFactory<>("ecole"));
        emailProColumn.setCellValueFactory(new PropertyValueFactory<>("emailPro"));

        apprentiTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            modifierButton.setDisable(newVal == null);
            supprimerButton.setDisable(newVal == null);
        });
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= 3 || newValue.isEmpty()) {
                handleSearch();
            }
        });
        
        loadAllApprentis();
    }
    
    /**
     * Charge tous les apprentis depuis le service et les affiche dans la table.
     * Gère les cas de succès et d'échec du chargement, y compris l'affichage de données de test en cas d'erreur.
     */
    private void loadAllApprentis() {
        Task<List<Apprenti>> task = new Task<>() {
            @Override
            protected List<Apprenti> call() throws Exception {
                return apprentiService.getAllApprentis();
            }
        };
        
        task.setOnSucceeded(event -> {
            apprentiData.clear();
            apprentiData.addAll(task.getValue());
            apprentiTable.setItems(apprentiData);
        });
        
        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            System.err.println("Erreur chargement apprentis: " + exception.getMessage());
            
            // Données de test
            apprentiData.clear();
            Apprenti app = new Apprenti(
                UUID.randomUUID(),
                "Leroy",
                "Paul",
                "paul.leroy@example.com",
                "0678912345",
                "10 Rue des Apprentis",
                "APPR123",
                LocalDate.now(),
                1200.0,
                "Apprenti pharmacie",
                "Contrat pro",
                "Bac Général",
                "Lycée PharmaPlus",
                "paul.leroy@apprenti.com"
            );
            apprentiData.add(app);
            apprentiTable.setItems(apprentiData);
        });
        
        new Thread(task).start();
    }

    /**
     * Gère la recherche d'apprentis en fonction du texte saisi dans le champ de recherche.
     * Si le champ de recherche est vide, charge tous les apprentis.
     */
    @FXML
    private void handleSearch() {
        String searchQuery = searchField.getText().trim();
        
        if (searchQuery.isEmpty()) {
            loadAllApprentis();
            return;
        }
        
        Task<List<Apprenti>> task = new Task<>() {
            @Override
            protected List<Apprenti> call() throws Exception {
                return apprentiService.searchApprentis(searchQuery);
            }
        };
        
        task.setOnSucceeded(event -> {
            apprentiData.clear();
            apprentiData.addAll(task.getValue());
            apprentiTable.setItems(apprentiData);
        });
        
        task.setOnFailed(event -> {
            System.err.println("Erreur recherche: " + task.getException().getMessage());
        });
        
        new Thread(task).start();
    }
    
    /**
     * Réinitialise la recherche en effaçant le champ de recherche et en rechargeant tous les apprentis.
     */
    @FXML
    private void handleResetSearch() {
        searchField.clear();
        loadAllApprentis();
    }

    /**
     * Définit le contrôleur parent pour permettre la navigation.
     * @param controller Le contrôleur parent (DashboardAdminController).
     */
    public void setParentController(DashboardAdminController controller) {
        this.parentController = controller;
    }

    /**
     * Gère l'action de retour vers la vue de gestion du personnel.
     */
    @FXML
    private void handleBack() {
        if (parentController != null) {
            parentController.loadGestionPersonnel();
        }
    }

    /**
     * Gère l'ajout d'un nouvel apprenti.
     * Ouvre une boîte de dialogue pour saisir les informations du nouvel apprenti,
     * puis appelle le service pour créer l'apprenti et met à jour la table.
     */
    @FXML
    private void handleAjouter() {
        AddApprentiDialogController dialogController = new AddApprentiDialogController();
        Optional<ButtonType> result = dialogController.showAndWait();
        
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            ApprentiCreateRequest request = dialogController.getCreateRequest();
            
            try {
                // Appel du service avec vérification explicite
                boolean success = apprentiService.createApprenti(request);
                
                if (success) {
                    loadAllApprentis(); // Rafraîchir les données
                    showAlert(
                        Alert.AlertType.INFORMATION,
                        "Succès",
                        "Apprenti ajouté avec succès\n" +
                        "Nom: " + request.getNom() + "\n" +
                        "École: " + request.getEcole()
                    );
                } else {
                    showAlert(
                        Alert.AlertType.ERROR,
                        "Erreur critique",
                        "Échec de la création en base de données\n" +
                        "Vérifiez les logs serveur"
                    );
                }
            } 
            catch (Exception e) { // Capturer les exceptions inattendues
                showAlert(
                    Alert.AlertType.ERROR,
                    "Erreur technique",
                    "Erreur lors de la communication avec le serveur :\n" +
                    e.getMessage()
                );
            }
        }
    }

    /**
     * Gère la modification d'un apprenti sélectionné.
     * Ouvre une boîte de dialogue pré-remplie avec les informations de l'apprenti,
     * puis appelle le service pour mettre à jour l'apprenti et rafraîchit la table.
     */
    @FXML
    private void handleModifier() {
        Apprenti selected = apprentiTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            EditApprentiDialogController dialogController = new EditApprentiDialogController();
            dialogController.setApprenti(selected);
            
            Optional<ButtonType> result = dialogController.showAndWait();
            
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                ApprentiUpdateRequest request = dialogController.getUpdateRequest();
                boolean success = apprentiService.updateApprenti(selected.getIdPersonne(), request);
                if (success) {
                    loadAllApprentis();
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Apprenti mis à jour");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour");
                }
            }
        }
    }

    /**
     * Gère la suppression d'un apprenti sélectionné.
     * Demande une confirmation à l'utilisateur, puis appelle le service pour supprimer l'apprenti
     * et met à jour la table.
     */
    @FXML
    private void handleSupprimer() {
        Apprenti selected = apprentiTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Supprimer l'apprenti " + selected.getNom() + " ?");
            confirmation.setContentText("Cette action est irréversible !");
            
            Optional<ButtonType> result = confirmation.showAndWait();
            
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    boolean success = apprentiService.deleteApprenti(selected.getIdPersonne());
                    
                    if (success) {
                        apprentiData.remove(selected); // Mise à jour immédiate du tableau
                        showAlert(
                            Alert.AlertType.INFORMATION,
                            "Succès",
                            "Apprenti supprimé \n" +
                            "ID: " + selected.getIdPersonne().toString().substring(0, 8) + "..."
                        );
                    } else {
                        showAlert(
                            Alert.AlertType.WARNING,
                            "Avertissement",
                            "La suppression a échoué mais la raison est inconnue \n" +
                            "L'élément peut avoir déjà été supprimé"
                        );
                    }
                } 
                catch (Exception e) {
                    showAlert(
                        Alert.AlertType.ERROR,
                        "Erreur base de données",
                        "Erreur lors de la suppression :\n" +
                        e.getLocalizedMessage()
                    );
                }
            }
        }
    }

    /**
     * Affiche une boîte de dialogue d'alerte.
     * @param type Le type d'alerte (par exemple, INFORMATION, ERROR).
     * @param title Le titre de l'alerte.
     * @param message Le message contenu dans l'alerte.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}