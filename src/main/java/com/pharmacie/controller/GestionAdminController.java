package com.pharmacie.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import com.pharmacie.model.Admin;
import com.pharmacie.model.dto.AdminUpdateRequest;
import com.pharmacie.model.dto.AdministrateurCreateRequest;
import com.pharmacie.service.AdminApi;
import javafx.collections.*;
import javafx.concurrent.Task;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Contrôleur pour la gestion des administrateurs.
 * Gère l'affichage, l'ajout, la modification, la suppression et la recherche des administrateurs.
 */
public class GestionAdminController {
    @FXML private TableView<Admin> adminTable;
    @FXML private TableColumn<Admin, UUID> idColumn;
    @FXML private TableColumn<Admin, String> nomColumn;
    @FXML private TableColumn<Admin, String> prenomColumn;
    @FXML private TableColumn<Admin, String> emailColumn;
    @FXML private TableColumn<Admin, String> telephoneColumn;
    @FXML private TableColumn<Admin, String> adresseColumn;
    @FXML private TableColumn<Admin, String> matriculeColumn;
    @FXML private TableColumn<Admin, LocalDate> dateEmbaucheColumn;
    @FXML private TableColumn<Admin, Double> salaireColumn;
    @FXML private TableColumn<Admin, String> posteColumn;
    @FXML private TableColumn<Admin, String> statutContratColumn;
    @FXML private TableColumn<Admin, String> diplomeColumn;
    @FXML private TableColumn<Admin, String> emailProColumn;

    @FXML private TextField searchField;
    @FXML private Button modifierButton, supprimerButton;
    
    private ObservableList<Admin> adminData = FXCollections.observableArrayList();
    private DashboardAdminController parentController;
    private AdminApi adminService;

    /**
     * Initialise le contrôleur après le chargement du FXML.
     * Configure les colonnes de la table, les écouteurs d'événements et charge les données initiales.
     */
    @FXML
    public void initialize() {
        // Initialiser le service
        adminService = new AdminApi();
        
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
        emailProColumn.setCellValueFactory(new PropertyValueFactory<>("emailPro"));

        adminTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            modifierButton.setDisable(newVal == null);
            supprimerButton.setDisable(newVal == null);
        });
        
        // Configurer la recherche en temps réel (optionnel)
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= 3 || newValue.isEmpty()) {
                handleSearch();
            }
        });
        
        // Charger les données initiales
        loadAllAdmins();
    }
    
    /**
     * Charge tous les administrateurs depuis le service et les affiche dans la table.
     * Gère les cas de succès et d'échec du chargement, y compris l'affichage de données de test en cas d'erreur.
     */
    private void loadAllAdmins() {
        Task<List<Admin>> task = new Task<>() {
            @Override
            protected List<Admin> call() throws Exception {
                return adminService.getAllAdmins();
            }
        };
        
        task.setOnSucceeded(event -> {
            adminData.clear();
            adminData.addAll(task.getValue());
            adminTable.setItems(adminData);
        });
        
        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            System.err.println("Erreur lors du chargement des administrateurs: " + exception.getMessage());
            exception.printStackTrace();
            
            // Ajouter des données de test en cas d'échec de connexion à l'API
            adminData.clear();
            Admin adm = new Admin(
                UUID.randomUUID(),
                "Dupont",
                "Jean",
                "jean.dupont@example.com",
                "0123456789",
                "123 Rue Exemple",
                "MAT123",
                LocalDate.now(),
                3000.0,
                "Développeur",
                "CDI",
                "Bac+5",
                "jean.dupont@pro.com",
                "Administrateur"
            );
            adminData.add(adm);
            adminTable.setItems(adminData);
        });
        
        new Thread(task).start();
    }

    /**
     * Gère la recherche d'administrateurs en fonction du texte saisi dans le champ de recherche.
     * Si le champ de recherche est vide, charge tous les administrateurs.
     */
    @FXML
    private void handleSearch() {
        String searchQuery = searchField.getText().trim();
        
        if (searchQuery.isEmpty()) {
            loadAllAdmins();
            return;
        }
        
        Task<List<Admin>> task = new Task<>() {
            @Override
            protected List<Admin> call() throws Exception {
                return adminService.searchAdmins(searchQuery);
            }
        };
        
        task.setOnSucceeded(event -> {
            adminData.clear();
            adminData.addAll(task.getValue());
            adminTable.setItems(adminData);
        });
        
        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            System.err.println("Erreur lors de la recherche d'administrateurs: " + exception.getMessage());
            exception.printStackTrace();
        });
        
        new Thread(task).start();
    }
    
    /**
     * Réinitialise la recherche en effaçant le champ de recherche et en rechargeant tous les administrateurs.
     */
    @FXML
    private void handleResetSearch() {
        searchField.clear();
        loadAllAdmins();
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
     * Gère l'ajout d'un nouvel administrateur.
     * Ouvre une boîte de dialogue pour saisir les informations du nouvel administrateur,
     * puis appelle le service pour créer l'administrateur et met à jour la table.
     */
    @FXML
    private void handleAjouter() {
        AddAdminDialogController dialogController = new AddAdminDialogController();
        
        Optional<ButtonType> result = dialogController.showAndWait();
        
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            AdministrateurCreateRequest request = dialogController.getCreateRequest();
            boolean success = adminService.createAdmin(request);
            if (success) {
                loadAllAdmins();
                new Alert(Alert.AlertType.INFORMATION, "Administrateur ajouté avec succès").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Échec de l'ajout de l'administrateur").show();
            }
        }
    }

    /**
     * Gère la modification d'un administrateur sélectionné.
     * Ouvre une boîte de dialogue pré-remplie avec les informations de l'administrateur,
     * puis appelle le service pour mettre à jour l'administrateur et rafraîchit la table.
     */
    @FXML
    private void handleModifier() {
        Admin selected = adminTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            EditAdminDialogController dialogController = new EditAdminDialogController();
            dialogController.setAdmin(selected);
            
            Optional<ButtonType> result = dialogController.showAndWait();
            
            if (result.isPresent() && result.get().getButtonData() == ButtonData.OK_DONE) {
                AdminUpdateRequest request = dialogController.getUpdateRequest();
                boolean success = adminService.updateAdmin(selected.getIdPersonne(), request);
                if (success) {
                    loadAllAdmins();
                    new Alert(Alert.AlertType.INFORMATION, "Administrateur mis à jour").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Échec de la mise à jour").show();
                }
            }
        }
    }

    /**
     * Gère la suppression d'un administrateur sélectionné.
     * Demande une confirmation à l'utilisateur, puis appelle le service pour supprimer l'administrateur
     * et met à jour la table.
     */
    @FXML
    private void handleSupprimer() {
        Admin selected = adminTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Vérifier que l'UUID est valide
            if (selected.getIdPersonne() == null) {
                new Alert(Alert.AlertType.ERROR, "ID de l'administrateur manquant").show();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer " + selected.getNom() + "?");
            alert.setContentText("ID : " + selected.getIdPersonne());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean success = adminService.deleteAdmin(selected.getIdPersonne());
                if (success) {
                    loadAllAdmins();
                    new Alert(Alert.AlertType.INFORMATION, "Suppression réussie").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Échec de la suppression").show();
                }
            }
        }
    }
}