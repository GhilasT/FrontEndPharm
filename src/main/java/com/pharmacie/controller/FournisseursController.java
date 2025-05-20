package com.pharmacie.controller;

import com.pharmacie.model.Fournisseur;
import com.pharmacie.model.dto.FournisseurCreateRequest;
import com.pharmacie.model.dto.FournisseurUpdateRequest;
import com.pharmacie.service.FournisseurApi;
import com.pharmacie.util.DialogService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Contrôleur pour la gestion des fournisseurs.
 * Gère l'interface utilisateur pour afficher, ajouter, modifier, supprimer et voir les détails des fournisseurs.
 */
public class FournisseursController {

    @FXML private TableView<Fournisseur> fournisseursTable;
    @FXML private TableColumn<Fournisseur, String> nomSocieteColumn;
    @FXML private TableColumn<Fournisseur, String> emailColumn;
    @FXML private TableColumn<Fournisseur, String> telephoneColumn;
    @FXML private TableColumn<Fournisseur, String> adresseColumn;
    @FXML private TextField searchFieldFournisseur;
    @FXML private Button btnEditFournisseur;
    @FXML private Button btnDeleteFournisseur;
    @FXML private Button btnDetailsFournisseur;
    @FXML private Button btnCommanderFournisseur;
    @FXML private Button btnAddFournisseur;

    private ObservableList<Fournisseur> fournisseurData = FXCollections.observableArrayList();
    private FournisseurApi fournisseurService = new FournisseurApi();

    /**
     * Initialise le contrôleur après le chargement du FXML.
     * Configure la sélection dans la table des fournisseurs.
     */
    @FXML
    public void initialize() {
        configureTableColumns();
        configureTableSelection();
        loadAllFournisseurs();
        
        searchFieldFournisseur.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= 3 || newValue.isEmpty()) {
                handleSearch();
            }
        });
    }

    /**
     * Configure les colonnes de la table des fournisseurs.
     */
    private void configureTableColumns() {
        nomSocieteColumn.setCellValueFactory(new PropertyValueFactory<>("nomSociete"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        
        fournisseursTable.setItems(fournisseurData);
    }

    /**
     * Configure l'écouteur de sélection sur la table des fournisseurs.
     * Active ou désactive les boutons d'édition, de suppression et de détails en fonction de la sélection.
     */
    private void configureTableSelection() {
        fournisseursTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                boolean itemSelected = newSelection != null;
                btnEditFournisseur.setDisable(!itemSelected);
                btnDeleteFournisseur.setDisable(!itemSelected);
                btnDetailsFournisseur.setDisable(!itemSelected);
                btnCommanderFournisseur.setDisable(!itemSelected);
            }
        );
    }

    /**
     * Charge tous les fournisseurs depuis l'API.
     */
    private void loadAllFournisseurs() {
        Task<List<Fournisseur>> task = new Task<>() {
            @Override
            protected List<Fournisseur> call() throws Exception {
                return fournisseurService.getAllFournisseurs();
            }
        };
        
        task.setOnSucceeded(event -> {
            List<Fournisseur> fournisseurs = task.getValue();
            fournisseurData.clear();
            if (fournisseurs != null && !fournisseurs.isEmpty()) {
                fournisseurData.addAll(fournisseurs);
            } else {
                showAlert(AlertType.INFORMATION, "Information", "Aucun fournisseur trouvé", 
                         "Il n'y a aucun fournisseur enregistré ou une erreur s'est produite lors du chargement.");
            }
        });
        
        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            showAlert(AlertType.ERROR, "Erreur", "Erreur lors du chargement des fournisseurs", 
                     exception.getMessage());
        });
        
        new Thread(task).start();
    }

    /**
     * Méthode de recherche de fournisseurs.
     */
    @FXML
    private void handleSearch() {
        String searchQuery = searchFieldFournisseur.getText().trim();
        
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
            List<Fournisseur> fournisseurs = task.getValue();
            fournisseurData.clear();
            if (fournisseurs != null && !fournisseurs.isEmpty()) {
                fournisseurData.addAll(fournisseurs);
            } else {
                showAlert(AlertType.INFORMATION, "Recherche", "Aucun résultat", 
                         "Aucun fournisseur ne correspond à votre recherche.");
            }
        });
        
        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            showAlert(AlertType.ERROR, "Erreur", "Erreur lors de la recherche", 
                     exception.getMessage());
        });
        
        new Thread(task).start();
    }

    /**
     * Gère l'action d'ajout d'un nouveau fournisseur.
     */
    @FXML 
    private void handleAdd() {
        AddFournisseurDialogController dialogController = new AddFournisseurDialogController();
        Optional<ButtonType> result = dialogController.showAndWait();
        
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            FournisseurCreateRequest request = dialogController.getCreateRequest();
            
            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    return fournisseurService.createFournisseur(request);
                }
            };
            
            task.setOnSucceeded(event -> {
                Boolean success = task.getValue();
                if (success) {
                    showAlert(AlertType.INFORMATION, "Succès", "Fournisseur ajouté", 
                             "Le fournisseur " + request.getNomSociete() + " a été ajouté avec succès.");
                    loadAllFournisseurs();
                } else {
                    showAlert(AlertType.ERROR, "Erreur", "Échec de l'ajout", 
                             "Le fournisseur n'a pas pu être ajouté.");
                }
            });
            
            task.setOnFailed(event -> {
                Throwable exception = task.getException();
                showAlert(AlertType.ERROR, "Erreur", "Erreur lors de l'ajout", 
                         exception.getMessage());
            });
            
            new Thread(task).start();
        }
    }

    /**
     * Gère l'action de modification du fournisseur sélectionné.
     */
    @FXML 
    private void handleEdit() {
        Fournisseur selectedFournisseur = fournisseursTable.getSelectionModel().getSelectedItem();
        if (selectedFournisseur == null) {
            showAlert(AlertType.WARNING, "Sélection", "Aucun fournisseur sélectionné", 
                     "Veuillez sélectionner un fournisseur à modifier.");
            return;
        }
        
        EditFournisseurDialogController dialogController = new EditFournisseurDialogController();
        dialogController.setFournisseur(selectedFournisseur);
        Optional<ButtonType> result = dialogController.showAndWait();
        
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            FournisseurUpdateRequest request = dialogController.getUpdateRequest();
            
            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    return fournisseurService.updateFournisseur(selectedFournisseur.getIdFournisseur(), request);
                }
            };
            
            task.setOnSucceeded(event -> {
                Boolean success = task.getValue();
                if (success) {
                    showAlert(AlertType.INFORMATION, "Succès", "Fournisseur modifié", 
                             "Le fournisseur " + request.getNomSociete() + " a été modifié avec succès.");
                    loadAllFournisseurs();
                } else {
                    showAlert(AlertType.ERROR, "Erreur", "Échec de la modification", 
                             "Le fournisseur n'a pas pu être modifié.");
                }
            });
            
            task.setOnFailed(event -> {
                Throwable exception = task.getException();
                showAlert(AlertType.ERROR, "Erreur", "Erreur lors de la modification", 
                         exception.getMessage());
            });
            
            new Thread(task).start();
        }
    }

    /**
     * Gère l'action de suppression du fournisseur sélectionné.
     */
    @FXML 
    private void handleDelete() {
        Fournisseur selectedFournisseur = fournisseursTable.getSelectionModel().getSelectedItem();
        if (selectedFournisseur == null) {
            showAlert(AlertType.WARNING, "Sélection", "Aucun fournisseur sélectionné", 
                     "Veuillez sélectionner un fournisseur à supprimer.");
            return;
        }
        
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer le fournisseur " + selectedFournisseur.getNomSociete() + " ?");
        confirmation.setContentText("Cette action est irréversible.");
        
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    return fournisseurService.deleteFournisseur(selectedFournisseur.getIdFournisseur());
                }
            };
            
            task.setOnSucceeded(event -> {
                Boolean success = task.getValue();
                if (success) {
                    showAlert(AlertType.INFORMATION, "Succès", "Fournisseur supprimé", 
                             "Le fournisseur a été supprimé avec succès.");
                    fournisseurData.remove(selectedFournisseur);
                } else {
                    showAlert(AlertType.ERROR, "Erreur", "Échec de la suppression", 
                             "Le fournisseur n'a pas pu être supprimé.");
                }
            });
            
            task.setOnFailed(event -> {
                Throwable exception = task.getException();
                showAlert(AlertType.ERROR, "Erreur", "Erreur lors de la suppression", 
                         exception.getMessage());
            });
            
            new Thread(task).start();
        }
    }

    /**
     * Gère l'action d'affichage des détails du fournisseur sélectionné.
     */
    @FXML 
    private void handleDetails() {
        Fournisseur selectedFournisseur = fournisseursTable.getSelectionModel().getSelectedItem();
        if (selectedFournisseur == null) {
            showAlert(AlertType.WARNING, "Sélection", "Aucun fournisseur sélectionné", 
                     "Veuillez sélectionner un fournisseur pour voir ses détails.");
            return;
        }
        
        DialogService.afficherPopupFournisseur(selectedFournisseur);
    }
    
    /**
     * Affiche une alerte avec les paramètres spécifiés.
     *
     * @param type Le type d'alerte
     * @param title Le titre de l'alerte
     * @param header L'en-tête de l'alerte
     * @param content Le contenu de l'alerte
     */
    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}