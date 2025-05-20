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

/**
 * Contrôleur pour la gestion des fournisseurs.
 * Permet d'afficher, rechercher, ajouter, modifier et supprimer des fournisseurs.
 */
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

    /**
     * Initialise le contrôleur après le chargement du FXML.
     * Configure les colonnes de la table, la sélection, la recherche,
     * et charge la liste initiale des fournisseurs.
     */
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
    
    /**
     * Charge tous les fournisseurs depuis l'API et les affiche dans la table.
     * En cas d'échec du chargement depuis l'API, des données de test sont affichées.
     */
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

    /**
     * Gère la recherche de fournisseurs en fonction du texte saisi.
     * Si le champ de recherche est vide, recharge tous les fournisseurs.
     * Sinon, effectue une recherche spécifique.
     */
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
    
    /**
     * Gère l'action du bouton de réinitialisation de la recherche.
     * Efface le champ de recherche et recharge tous les fournisseurs.
     */
    @FXML
    private void handleResetSearch() {
        searchField.clear();
        loadAllFournisseurs();
    }

    /**
     * Définit le contrôleur parent (DashboardAdminController).
     * @param controller Le contrôleur parent.
     */
    public void setParentController(DashboardAdminController controller) {
        this.parentController = controller;
    }

    /**
     * Gère l'action du bouton "Retour".
     * Revient à la vue principale du tableau de bord de l'administrateur.
     */
    @FXML
    private void handleBack() {
        if (parentController != null) {
            parentController.showDashboard();
        }
    }

    /**
     * Gère l'action du bouton "Ajouter".
     * Ouvre une boîte de dialogue pour ajouter un nouveau fournisseur.
     * Si l'ajout est confirmé, crée le fournisseur via l'API et rafraîchit la liste.
     */
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

    /**
     * Gère l'action du bouton "Modifier".
     * Ouvre une boîte de dialogue pour modifier le fournisseur sélectionné.
     * Si la modification est confirmée, met à jour le fournisseur via l'API et rafraîchit la liste.
     */
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

    /**
     * Gère l'action du bouton "Supprimer".
     * Demande confirmation avant de supprimer le fournisseur sélectionné.
     * Si la suppression est confirmée, supprime le fournisseur via l'API et rafraîchit la liste.
     */
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

    /**
     * Affiche une boîte de dialogue d'alerte.
     * @param type Le type d'alerte (Erreur, Information, etc.).
     * @param title Le titre de la fenêtre d'alerte.
     * @param message Le message principal de l'alerte.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}