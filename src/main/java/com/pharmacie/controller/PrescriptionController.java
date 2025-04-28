package com.pharmacie.controller;

import com.pharmacie.model.MedicamentPanier;
import com.pharmacie.model.Prescription;
import com.pharmacie.model.dto.OrdonnanceCreateRequest;
import com.pharmacie.model.dto.PrescriptionCreateRequest;
import com.pharmacie.service.ApiRest;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PrescriptionController {

    @FXML
    private TableView<Prescription> prescriptionTable; // Tableau pour afficher les prescriptions
    @FXML
    private TableColumn<Prescription, String> medicamentColumn; // Colonne des médicaments
    @FXML
    private TableColumn<Prescription, Integer> quantiteColumn; // Colonne de la quantité
    @FXML
    private TableColumn<Prescription, Integer> dureeColumn; // Colonne de la durée
    @FXML
    private TableColumn<Prescription, String> posologieColumn; // Colonne de la posologie
    private UUID clientId;
    @FXML
    private TextField dureeField;  // Champ pour entrer la durée
    @FXML
    private TextField posologieField;  // Champ pour entrer la posologie

    @FXML
    private Button btnValider;// Bouton pour valider la prescription
    @FXML
    private Button btnAnnuler;
    @FXML private Label rppsLabel;
    private String rppsMedecin;

    public VenteController venteController;

    private ObservableList<Prescription> prescriptionsList = FXCollections.observableArrayList();  // Liste des prescriptions
    private List<MedicamentPanier> medicamentsPanier;  // Liste des médicaments récupérés du panier

    @FXML
    private void initialize() {
        // 1) Configuration des colonnes
        medicamentColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getMedicament()));
        quantiteColumn.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getQuantitePrescrite()).asObject());
        dureeColumn.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getDuree()).asObject());
        posologieColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getPosologie()));

        // 2.a Médicament (String)
        medicamentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        medicamentColumn.setOnEditCommit(e ->
                e.getRowValue().setMedicament(e.getNewValue()));

        // 2.b Quantité (Integer)
        quantiteColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantiteColumn.setOnEditCommit(e ->
                e.getRowValue().setQuantitePrescrite(e.getNewValue()));

        // 2.c Durée (Integer)
        dureeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        dureeColumn.setOnEditCommit(e ->
                e.getRowValue().setDuree(e.getNewValue()));

        // 2.d Posologie (String)
        posologieColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        posologieColumn.setOnEditCommit(e ->
                e.getRowValue().setPosologie(e.getNewValue()));

        // 3) Lier la TableView à ton ObservableList et activer l’édition
        prescriptionTable.setItems(prescriptionsList);
        prescriptionTable.setEditable(true);
    }


    public void  setClientId(UUID clientId){
        this.clientId = clientId;

    }

    // Méthode pour charger les médicaments du panier dans la liste des prescriptions
    public void loadMedicamentsPanier() {
        for (MedicamentPanier mp : medicamentsPanier) {
            Prescription prescription = new Prescription(
                    "",  // Posologie vide par défaut
                    mp.getQuantite(),  // Quantité du panier
                    0,  // Durée vide par défaut
                    mp.getNomMedicament()  // Nom du médicament
            );
            prescriptionsList.add(prescription);  // Ajouter à la liste
        }
        prescriptionTable.setItems(prescriptionsList);  // Afficher dans la table
    }

    // Méthode pour valider les prescriptions (ajouter à l'ordonnance)
    @FXML
    private void handleValiderPrescription(ActionEvent event) {
        // 1) Construis la liste DTO des prescriptions via le constructeur manuel
        List<PrescriptionCreateRequest> dtoPrescriptions = prescriptionsList.stream()
                .map(p -> new PrescriptionCreateRequest(
                        p.getMedicament(),
                        p.getQuantitePrescrite(),
                        p.getDuree(),
                        p.getPosologie()
                ))
                .collect(Collectors.toList());

        // 2) Monte le DTO global d’ordonnance
        // (Adapte ici si OrdonnanceCreateRequest a aussi perdu son builder)
        OrdonnanceCreateRequest dto = new OrdonnanceCreateRequest(
                new Date(),      // dateEmission
                rppsMedecin,     // RPPS
                clientId,        // clientId
                dtoPrescriptions // prescriptions
        );

        // 3) Envoie la requête HTTP comme avant…
        Task<UUID> task = new Task<>() {
            @Override
            protected UUID call() throws Exception {
                return ApiRest.createOrdonnance(dto);
            }
        };
        task.setOnSucceeded(e -> {
            UUID ordonnanceId = task.getValue();
            showAlert(Alert.AlertType.INFORMATION,
                    "Ordonnance créée",
                    "ID Ordonnance : " + ordonnanceId,
                    "Votre ordonnance a bien été enregistrée.");
                    if (venteController != null) {
                        venteController.setOrdonnanceAjoutee(true);
                    }
            ((Stage) btnValider.getScene().getWindow()).close();
        });
        task.setOnFailed(e -> {
            showAlert(Alert.AlertType.ERROR,
                    "Erreur",
                    "Impossible de créer l’ordonnance",
                    task.getException().getMessage());
        });
        new Thread(task).start();
    }

    // Méthode pour afficher un message d'alerte
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setMedicamentsPanier(List<MedicamentPanier> list) {
        // 1) Définit toujours une liste non-nulle
        this.medicamentsPanier = (list != null ? list : Collections.emptyList());

        // 2) Vide d’abord l’ObservableList
        prescriptionsList.clear();

        // 3) Si le panier contient des médicaments, on crée les Prescription
        for (MedicamentPanier mp : this.medicamentsPanier) {
            prescriptionsList.add(
                    new Prescription(
                            "",               // posologie par défaut
                            mp.getQuantite(), // quantité
                            0,                // durée par défaut
                            mp.getNomMedicament()
                    )
            );
        }
    }

    public void setMedecinRpps(String rpps) {
        // 1) Mémoriser si besoin
        this.rppsMedecin = rpps;
        // 2) Afficher dans le Label
        rppsLabel.setText("RPPS du médecin : " + rpps);
        // 3) (Re)charger ta liste de prescriptions éventuelles pour ce médecin, si tu en as
        //    sinon tu laisses la table vide pour une nouvelle ordonnance
    }


    @FXML
    private void handleAnnulerPrescription(ActionEvent event) {
        // Récupère la fenêtre par le source de l'événement et ferme-la
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}