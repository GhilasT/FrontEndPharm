package com.pharmacie.controller;

import com.pharmacie.model.Medicament;
import com.pharmacie.model.MedicamentPanier;
import com.pharmacie.model.PageResponse;
import com.pharmacie.model.VenteCreateRequest;
import com.pharmacie.service.ApiRest;
import com.pharmacie.util.LoggedSeller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.json.JSONObject;

public class VenteController {

    @FXML
    private TextField barDeRecherche;
    @FXML
    private Button btnbarRecherche;
    @FXML
    private Button btnPayer;
    @FXML
    private GridPane gridPanePanier;
    @FXML
    private ListView<String> listView;
    @FXML
    private Label LabelQuantierValue;
    @FXML
    private Label LabelPrixValue;

    @FXML
    private Button btnAjouterOrdonnance;

    private boolean ordonnanceAjoutee = false;

    private UUID clientId;
    private UUID pharmacienAdjointId;

    private int rowCount = 1;
    private ObservableList<Medicament> suggestions = FXCollections.observableArrayList();
    private final Logger LOGGER = Logger.getLogger(VenteController.class.getName());

    @FXML
    public void initialize() {
        barDeRecherche.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() >= 2) {
                rechercherMedicaments(newVal);
            } else {
                listView.getItems().clear();
            }
        });

        listView.setOnMouseClicked(event -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ajouterMedicament(selected);
                listView.getItems().clear();
            }
        });

        barDeRecherche.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !listView.getItems().isEmpty()) {
                ajouterMedicament(listView.getItems().get(0));
                listView.getItems().clear();
            }
        });
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    @FXML
    private void ajouterMedicament(ActionEvent event) {
        if (!listView.getItems().isEmpty()) {
            String selected = listView.getItems().get(0);
            if (selected.contains("null")) {
                showAlert(Alert.AlertType.WARNING, "Prix manquant",
                        "Impossible d'ajouter ce médicament", "Le prix est manquant (null).");
                return;
            }
            ajouterMedicament(selected); // utilise la première suggestion
            listView.getItems().clear();
        }
    }

    public void setPharmacienAdjointId(UUID pharmId) {
        this.pharmacienAdjointId = pharmId;
    }

    @FXML
private void rechercherMedicaments(String searchTerm) {
    try {
        List<Medicament> medicaments = ApiRest.searchForVente(searchTerm);
        ObservableList<String> results = FXCollections.observableArrayList();
        
        for (Medicament m : medicaments) {
            String display = String.format("%s - %.2f€", 
                !m.getDenomination().isEmpty() ? m.getDenomination() : m.getLibelle(),
                m.getPrixTTC());
            results.add(display);
        }
        
        listView.setItems(results);
        suggestions.setAll(medicaments);
        
    } catch (Exception e) {
        showAlert(Alert.AlertType.ERROR, 
            "Erreur", 
            "Recherche impossible", 
            "Erreur serveur : " + e.getMessage());
    }
}

@FXML
public void ajouterMedicament(String selected) {
    try {
        String[] parts = selected.split(" - ");
        if (parts.length < 2) {
            LOGGER.warning("Format invalide : " + selected);
            return;
        }

        String nom = parts[0].trim();
        double prix = Double.parseDouble(parts[1].replace("€", "").replace(",", ".").trim());

        Optional<Medicament> match = suggestions.stream()
                .filter(m -> {
                    String nomMedoc = (m.getDenomination() != null) ? m.getDenomination() : m.getLibelle();
                    return selected.startsWith(nomMedoc);
                })
                .findFirst();

        if (match.isEmpty()) {
            LOGGER.warning("Aucun médicament correspondant trouvé pour : " + nom);
            return;
        }

        Medicament med = match.get();
        String codeCip13 = med.getCodeCip13();
        if (codeCip13 == null || codeCip13.isBlank()) {
            LOGGER.warning("Code CIP13 manquant pour le médicament : " + med.getLibelle());
            return;
        }
 
        Label labelNom = new Label(nom);
        labelNom.setTextFill(Color.WHITE);
        labelNom.setUserData(codeCip13); 

        TextField qteField = new TextField("1");
        qteField.setStyle("-fx-text-fill: white; -fx-control-inner-background: rgba(0,122,255,1);");
        qteField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                qteField.setText(newVal.replaceAll("[^\\d]", ""));
            }
            majInfosPanier();
        });

        Label labelPrix = new Label(String.format("%.2f €", prix));
        labelPrix.setTextFill(Color.WHITE);

        gridPanePanier.add(labelNom, 0, rowCount);
        gridPanePanier.add(qteField, 1, rowCount);
        gridPanePanier.add(labelPrix, 2, rowCount);

        rowCount++;
        barDeRecherche.clear();
        majInfosPanier();

    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout du médicament", e);
    }
}

    private void majInfosPanier() {
        double total = 0.0;
        int sommeQte = 0;
        for (int i = 1; i < rowCount; i++) {
            Node qteNode = gridPanePanier.getChildren().get(i * 3 + 1);
            Node prixNode = gridPanePanier.getChildren().get(i * 3 + 2);

            if (qteNode instanceof TextField && prixNode instanceof Label) {
                TextField qteField = (TextField) qteNode;
                Label prixLabel = (Label) prixNode;

                String qteText = qteField.getText().trim();
                if (!qteText.isEmpty() && qteText.matches("\\d+")) {
                    int qte = Integer.parseInt(qteText);
                    double prixUnit = Double.parseDouble(prixLabel.getText().replace(" €", "").replace(",", "."));
                    total += qte * prixUnit;
                    sommeQte += qte;
                }
            }
        }
        LabelQuantierValue.setText(String.valueOf(sommeQte));
        LabelPrixValue.setText(String.format("%.2f €", total));
    }

    @FXML
    private void handlePayer(ActionEvent event) {
        List<MedicamentPanier> panier = new ArrayList<>();

        for (int i = 1; i < rowCount; i++) {
            Label labelNom = (Label) gridPanePanier.getChildren().get(i * 3);
            TextField qteField = (TextField) gridPanePanier.getChildren().get(i * 3 + 1);
            Label prixLabel = (Label) gridPanePanier.getChildren().get(i * 3 + 2);

            Object userData = labelNom.getUserData();
            if (userData == null) {
                LOGGER.warning("Code CIS manquant pour la ligne : " + labelNom.getText());
                continue;
            }

            String codeCIS = userData.toString();
            int qte = Integer.parseInt(qteField.getText());
            double prixUnit = Double.parseDouble(prixLabel.getText().replace("€", "").replace(",", ".").trim());

            MedicamentPanier mp = new MedicamentPanier(codeCIS, qte, prixUnit);
            mp.setNomMedicament(labelNom.getText());
            panier.add(mp);
        }

        if (clientId == null || pharmacienAdjointId == null || panier.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Informations incomplètes",
                    "Vérifiez que le client, le pharmacien et les médicaments sont bien sélectionnés.");
            LOGGER.log(Level.SEVERE, "Échec de la création de vente - Détails :\n"
                    + "Client ID: " + clientId + "\n"
                    + "Pharmacien ID: " + pharmacienAdjointId + "\n"
                    + "Médicaments: " + panier.stream()
                    .map(m -> m.getCodeCip13() + " (x" + m.getQuantite() + ")")
                    .collect(Collectors.joining(", ")));
            return;
        }

        VenteCreateRequest request = new VenteCreateRequest();
        request.setPharmacienAdjointId(LoggedSeller.getInstance().getId());
        request.setClientId(clientId);
        request.setDateVente(new Date());
        request.setModePaiement("Carte bancaire");
        request.setMontantTotal(calculerMontantTotal(panier));
        request.setMontantRembourse(0.0);
        request.setMedicaments(panier);

        try {
            // Logging détaillé de la requête
            ObjectMapper mapper = new ObjectMapper();
            String requestJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            LOGGER.info("Payload de la vente :\n" + requestJson);

            ApiRest.createVente(request);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Vente créée", "La vente a bien été enregistrée.");
            ((Stage) btnPayer.getScene().getWindow()).close();
        } catch (JsonProcessingException e) {
            // Erreur de sérialisation
            LOGGER.log(Level.SEVERE, "Erreur de sérialisation JSON : " + e.getMessage(), e);
            showAlert(Alert.AlertType.ERROR,
                    "Erreur technique",
                    "Problème de format de données",
                    "Impossible de formater les données de la vente : " + e.getMessage());

        } catch (Exception e) {
            // Récupère et affiche le message d'erreur renvoyé par le back
            LOGGER.log(Level.SEVERE, "Échec de la création de vente", e);
            String raw = e.getMessage();
            String message = raw;

            try {
                int ix = raw.indexOf("{");
                if (ix >= 0) {
                    JSONObject obj = new JSONObject(raw.substring(ix));
                    message = obj.optString("message", raw);
                }
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Impossible de parser le message d'erreur", ex);
            }


            showAlert(Alert.AlertType.ERROR,
                    "Vente bloqué",
                    "Ordonnance requise",
                    "Veuillez ajouter une ordonnance");

        }
    }

    private double calculerMontantTotal(List<MedicamentPanier> panier) {
        return panier.stream().mapToDouble(mp -> mp.getPrixUnitaire() * mp.getQuantite()).sum();
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public UUID getClientId(){
        return clientId;
    }

    /**
     * Récupère les médicaments du panier.
     * @return Liste des médicaments ajoutés au panier sous forme de MedicamentPanier
     */
    public List<MedicamentPanier> getMedicamentsPanier() {
        List<MedicamentPanier> panier = new ArrayList<>();  // Liste pour contenir les médicaments du panier

        // Parcours de chaque ligne dans le panier
        for (int i = 1; i < rowCount; i++) {  // On commence à 1 car la première ligne est l'entête
            Label labelNom = (Label) gridPanePanier.getChildren().get(i * 3);  // Nom du médicament
            TextField qteField = (TextField) gridPanePanier.getChildren().get(i * 3 + 1);  // Quantité
            Label prixLabel = (Label) gridPanePanier.getChildren().get(i * 3 + 2);  // Prix

            // Vérification que le nom du médicament contient des informations
            Object userData = labelNom.getUserData();
            if (userData == null) {
                LOGGER.warning("Code CIP manquant pour la ligne : " + labelNom.getText());
                continue;  // Ignore cette ligne si le code CIP est manquant
            }

            // Récupère le codeCIP du médicament
            String codeCIS = userData.toString();

            // Récupère la quantité entrée
            int qte = Integer.parseInt(qteField.getText());

            // Récupère le prix unitaire du médicament
            double prixUnit = Double.parseDouble(prixLabel.getText().replace("€", "").replace(",", ".").trim());

            // Crée un objet MedicamentPanier avec les informations récupérées
            MedicamentPanier mp = new MedicamentPanier(codeCIS, qte, prixUnit);
            mp.setNomMedicament(labelNom.getText());  // Ajoute le nom du médicament

            panier.add(mp);  // Ajoute le médicament au panier
        }

        System.out.println("jai recuperer les medoc");

        return panier;
    }

    @FXML
    private void handleAjouterOrdonnance(ActionEvent event) {
        try {
            // Créer et charger la nouvelle fenêtre de médecins
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/MedecinsPage.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Page des Médecins");

            // Récupérer le contrôleur de la fenêtre et passer les informations si nécessaire
            MedecinsController medecinsController = loader.getController();
            // Passer l'instance de VenteController au MedecinsController
            medecinsController.setVenteController(this);

            // Afficher la nouvelle fenêtre
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la page des médecins", e.getMessage());
        }
    }
}
