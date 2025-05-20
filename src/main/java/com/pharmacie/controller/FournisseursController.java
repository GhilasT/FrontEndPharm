package com.pharmacie.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Contrôleur pour la gestion des fournisseurs.
 * Gère l'interface utilisateur pour afficher, ajouter, modifier, supprimer et voir les détails des fournisseurs.
 */
public class FournisseursController {

    @FXML private TableView<?> fournisseursTable;
    @FXML private TextField searchFieldFournisseur;
    @FXML private Button btnEditFournisseur;
    @FXML private Button btnDeleteFournisseur;
    @FXML private Button btnDetailsFournisseur;
    @FXML private Button btnCommanderFournisseur;

    /**
     * Initialise le contrôleur après le chargement du FXML.
     * Configure la sélection dans la table des fournisseurs.
     */
    @FXML
    public void initialize() {
        configureTableSelection();
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
            }
        );
    }

    /**
     * Gère l'action d'ajout d'un nouveau fournisseur.
     * (Actuellement non implémenté)
     */
    @FXML private void handleAdd() {}

    /**
     * Gère l'action de modification du fournisseur sélectionné.
     * (Actuellement non implémenté)
     */
    @FXML private void handleEdit() {}

    /**
     * Gère l'action de suppression du fournisseur sélectionné.
     * (Actuellement non implémenté)
     */
    @FXML private void handleDelete() {}

    /**
     * Gère l'action d'affichage des détails du fournisseur sélectionné.
     * (Actuellement non implémenté)
     */
    @FXML private void handleDetails() {}
}