// GestionFournisseurController.java
package com.pharmacie.controller;

import com.pharmacie.model.Fournisseur;
import com.pharmacie.model.dto.FournisseurCreateRequest;
import com.pharmacie.model.dto.FournisseurUpdateRequest;
import com.pharmacie.service.FournisseurApi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GestionFournisseurController {
    @FXML private TableView<Fournisseur> fournisseurTable;
    @FXML private TableColumn<Fournisseur, UUID> idFournisseurColumn;
    @FXML private TableColumn<Fournisseur, String> nomSocieteColumn;
    @FXML private TableColumn<Fournisseur, String> sujetFonctionColumn;
    @FXML private TableColumn<Fournisseur, String> faxColumn;
    @FXML private TableColumn<Fournisseur, String> emailColumn;
    @FXML private TableColumn<Fournisseur, String> telephoneColumn;
    @FXML private TableColumn<Fournisseur, String> adresseColumn;

    @FXML private TextField searchField;
    @FXML private Button modifierButton, supprimerButton;
    
    private ObservableList<Fournisseur> fournisseurData = FXCollections.observableArrayList();
    private DashboardAdminController parentController;
    private FournisseurApi fournisseurService;

    @FXML
    public void initialize() {
        fournisseurService = new FournisseurApi();
        
        // Configuration des colonnes
        idFournisseurColumn.setCellValueFactory(new PropertyValueFactory<>("idFournisseur"));
        nomSocieteColumn.setCellValueFactory(new PropertyValueFactory<>("nomSociete"));
        sujetFonctionColumn.setCellValueFactory(new PropertyValueFactory<>("sujetFonction"));
        faxColumn.setCellValueFactory(new PropertyValueFactory<>("fax"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        fournisseurTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            modifierButton.setDisable(newVal == null);
            supprimerButton.setDisable(newVal == null);
        });
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= 3 || newValue.isEmpty()) {
                handleSearch();
            }
        });
        
        loadAllFournisseurs();
    }
    
    private void loadAllFournisseurs() {
        Task<List<Fournisseur>> task = new Task<>() {
            @Override
            protected List<Fournisseur> call() throws Exception {
                return fournisseurService.getAllFournisseurs();
            }
        };
        
        task.setOnSucceeded(event -> {
            fournisseurData.clear();
            fournisseurData.addAll(task.getValue());
            fournisseurTable.setItems(fournisseurData);
        });
        
        task.setOnFailed(event -> {
            // Données de test
            fournisseurData.clear();
            fournisseurData.add(new Fournisseur(
                UUID.randomUUID(),
                "PharmaStock",
                "Approvisionnement médicaments",
                "0123456789",
                "contact@pharmastock.com",
                "0612345678",
                "22 Rue des Grossistes, Paris"
            ));
            fournisseurTable.setItems(fournisseurData);
        });
        
        new Thread(task).start();
    }

    @FXML
    private void handleSearch() {
        String searchQuery = searchField.getText().trim();
        
        if (searchQuery.isEmpty()) {
            loadAllFournisseurs();
            return;
        }
        
        Task<List<Fournisseur>> task = new Task<>() {
            @Override
            protected List<Fournisseur> call() throws Exception {
                return fournisseurService.searchFournisseurs(searchQuery);
            }
        };
        
        task.setOnSucceeded(event -> {
            fournisseurData.clear();
            fournisseurData.addAll(task.getValue());
            fournisseurTable.setItems(fournisseurData);
        });
        
        new Thread(task).start();
    }
    
    @FXML
    private void handleResetSearch() {
        searchField.clear();
        loadAllFournisseurs();
    }

    public void setParentController(DashboardAdminController controller) {
        this.parentController = controller;
    }

    @FXML
    private void handleBack() {
        if (parentController != null) {
            parentController.showDashboard();
        }
    }

    @FXML
    private void handleAjouter() {
        AddFournisseurDialogController dialogController = new AddFournisseurDialogController();
        Optional<ButtonType> result = dialogController.showAndWait();
        
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            FournisseurCreateRequest request = dialogController.getCreateRequest();
            
            try {
                boolean success = fournisseurService.createFournisseur(request);
                if (success) {
                    loadAllFournisseurs();
                    showAlert(Alert.AlertType.INFORMATION, 
                        "Succès", 
                        "Fournisseur ajouté:\n" + request.getNomSociete()
                    );
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, 
                    "Erreur", 
                    "Échec de la création:\n" + e.getMessage()
                );
            }
        }
    }

    @FXML
    private void handleModifier() {
        Fournisseur selected = fournisseurTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            EditFournisseurDialogController dialogController = new EditFournisseurDialogController();
            dialogController.setFournisseur(selected);
            
            Optional<ButtonType> result = dialogController.showAndWait();
            
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                FournisseurUpdateRequest request = dialogController.getUpdateRequest();
                boolean success = fournisseurService.updateFournisseur(
                    selected.getIdFournisseur(), 
                    request
                );
                
                if (success) {
                    loadAllFournisseurs();
                    showAlert(Alert.AlertType.INFORMATION, 
                        "Succès", 
                        "Fournisseur mis à jour"
                    );
                }
            }
        }
    }

    @FXML
    private void handleSupprimer() {
        Fournisseur selected = fournisseurTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmer suppression");
            confirmation.setHeaderText("Supprimer " + selected.getNomSociete() + " ?");
            confirmation.setContentText("Cette action est irréversible !");
            
            Optional<ButtonType> result = confirmation.showAndWait();
            
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    boolean success = fournisseurService.deleteFournisseur(
                        selected.getIdFournisseur()
                    );
                    
                    if (success) {
                        fournisseurData.remove(selected);
                        showAlert(Alert.AlertType.INFORMATION, 
                            "Succès", 
                            "Fournisseur supprimé"
                        );
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, 
                        "Erreur", 
                        "Échec de la suppression:\n" + e.getMessage()
                    );
                }
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}