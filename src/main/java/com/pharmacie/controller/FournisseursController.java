package com.pharmacie.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class FournisseursController {

    @FXML private TableView<?> fournisseursTable;
    @FXML private TextField searchFieldFournisseur;
    @FXML private Button btnEditFournisseur;
    @FXML private Button btnDeleteFournisseur;
    @FXML private Button btnDetailsFournisseur;
    @FXML private Button btnCommanderFournisseur;

    @FXML
    public void initialize() {
        configureTableSelection();
    }

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

    @FXML private void handleAdd() {}
    @FXML private void handleEdit() {}
    @FXML private void handleDelete() {}
    @FXML private void handleDetails() {}
}