package com.pharmacie.controller;

import com.pharmacie.model.Client;
import com.pharmacie.service.ApiRest;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableRow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Contrôleur pour la gestion des clients.
 * 
 * Cette classe est responsable de:
 * - Afficher la liste des clients dans un TableView
 * - Permettre la sélection d'un client pour une vente
 * - Déclencher l'ajout d'un nouveau client
 * 
 * Ce contrôleur est généralement utilisé comme une fenêtre modale depuis
 * le contrôleur des ventes pour sélectionner un client existant ou en créer
 * un nouveau lors d'une vente.
 */
public class ClientsController {

    @FXML private TableView<Client> clientsTable;
    @FXML private TableColumn<Client,String> colNom;
    @FXML private TableColumn<Client,String> colPrenom;
    @FXML private TableColumn<Client,String> colTelephne;
    @FXML private TableColumn<Client,String> colEmail;
    @FXML private TableColumn<Client,String> colAdresse;
    @FXML private Button btnAddClient;

    private final ObservableList<Client> clients = FXCollections.observableArrayList();
    private VentesController venteController;
    private Stage modalStage;

    /**
     * Initialise le contrôleur après le chargement de son élément racine.
     * Configure les colonnes du TableView, charge les clients et définit le comportement
     * lors d'un clic sur une ligne de la table.
     */
    @FXML
    public void initialize() {
        // 1) Lier les colonnes à vos getters
        colNom   .setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getNom()));
        colPrenom.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getPrenom()));
        colTelephne.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getTelephone()));
        colEmail .setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getEmail()));
        colAdresse.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getAdresse()));

        // 2) Charger la liste des clients
        loadClients();

        // 3) Clic simple sur une ligne → renvoyer l’UUID et fermer la modale
        clientsTable.setRowFactory(tv -> {
            TableRow<Client> row = new TableRow<>();
            row.setOnMouseClicked(evt -> {
                if (!row.isEmpty()) {
                    Client c = row.getItem();
                    if (venteController != null) {
                        venteController.setClientId(c.getIdPersonne());
                    }
                    if (modalStage != null) {
                        modalStage.close();
                    }
                }
            });
            return row;
        });
        // **Plus de binding manuel ici** : on utilise handleAddClient()
    }

    /**
     * Charge la liste des clients de manière asynchrone depuis l'API.
     * Met à jour le TableView avec les clients récupérés ou affiche une alerte en cas d'erreur.
     */
    private void loadClients() {
        Task<List<Client>> task = new Task<>() {
            @Override
            protected List<Client> call() throws Exception {
                return ApiRest.getAllClients();
            }
        };
        task.setOnSucceeded(e -> {
            clients.setAll(task.getValue());
            clientsTable.setItems(clients);
        });
        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur chargement clients");
            alert.setHeaderText("Erreur chargement clients");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        });
        new Thread(task).start();
    }

    /** 
     * Gère l'action du bouton "Ajouter Client".
     * Ouvre une nouvelle fenêtre modale pour le formulaire d'ajout de client.
     * Si un nouveau client est ajouté, met à jour la liste des clients, le sélectionne
     * et transmet son ID au contrôleur des ventes, puis ferme la modale.
     * @param event L'événement d'action déclenché par le clic sur le bouton.
     */
    @FXML
    private void handleAddClient(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/pharmacie/view/formulaire-client.fxml"));
            Parent root = loader.load();
            FormulaireClientController formCtrl = loader.getController();

            Stage formStage = new Stage();
            formStage.initModality(Modality.APPLICATION_MODAL);
            formStage.setTitle("Ajouter un client");
            formStage.setScene(new Scene(root));
            formStage.showAndWait();

            UUID newId = formCtrl.getClientId();
            if (newId != null) {
                loadClients();
                clients.stream()
                        .filter(c -> newId.equals(c.getIdPersonne()))
                        .findFirst()
                        .ifPresent(c -> {
                            clientsTable.getSelectionModel().select(c);
                            if (venteController != null) {
                                venteController.setClientId(newId);
                            }
                            if (modalStage != null) {
                                modalStage.close();
                            }
                        });
            }
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Impossible d’ouvrir le formulaire client");
            alert.setHeaderText("Erreur chargement du formulaire client");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    /** 
     * Gère l'action du bouton "Fermer".
     * Ferme la fenêtre modale si elle est ouverte.
     * @param event L'événement d'action déclenché par le clic sur le bouton.
     */
    @FXML
    private void handleClose(ActionEvent event) {
        if (modalStage != null) {
            modalStage.close();
        }
    }

    /** 
     * Définit le contrôleur des ventes ({@link VentesController}) qui utilisera cette fenêtre
     * pour sélectionner un client.
     * Permet de renvoyer l'UUID du client sélectionné au {@link VentesController}.
     * @param vc Le contrôleur des ventes.
     */
    public void setVenteController(VentesController vc) {
        this.venteController = vc;
    }
    /** 
     * Définit la {@link Stage} de cette fenêtre modale.
     * Permet à ce contrôleur de fermer sa propre fenêtre.
     * @param stage La {@link Stage} de la fenêtre modale.
     */
    public void setModalStage(Stage stage) {
        this.modalStage = stage;
    }

}
