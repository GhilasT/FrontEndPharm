package com.pharmacie.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.pharmacie.model.Admin;
import com.pharmacie.service.AdminService;
import javafx.collections.*;
import javafx.concurrent.Task;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    private AdminService adminService;

    @FXML
    public void initialize() {
        // Initialiser le service
        adminService = new AdminService();
        
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
                "jean.dupont@pro.com"
            );
            adminData.add(adm);
            adminTable.setItems(adminData);
        });
        
        new Thread(task).start();
    }

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
    
    @FXML
    private void handleResetSearch() {
        searchField.clear();
        loadAllAdmins();
    }

    public void setParentController(DashboardAdminController controller) {
        this.parentController = controller;
    }

    @FXML
    private void handleBack() {
        if (parentController != null) {
            parentController.loadGestionPersonnel();
        }
    }

    @FXML
    private void handleAjouter() {
        // Logique d'ajout d'un nouvel administrateur
        // Vous pouvez ouvrir une nouvelle fenêtre ou un dialogue pour saisir les informations
    }

    @FXML
    private void handleModifier() {
        Admin selected = adminTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Logique de modification de l'administrateur sélectionné
            // Vous pouvez ouvrir une fenêtre ou un dialogue pour modifier les informations
        }
    }

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