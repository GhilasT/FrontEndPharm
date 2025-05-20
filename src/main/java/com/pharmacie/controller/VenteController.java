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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

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

import javafx.scene.image.ImageView;
//PARACETAMOL/CODEINE TEVA 500 mg/30 mg, comprimé pelliculé
//pharmacien.pro@example.com

/**
 * Contrôleur pour l'interface de création d'une nouvelle vente.
 * Gère la recherche de médicaments, l'ajout au panier, la gestion du panier,
 * l'ajout d'ordonnance et la finalisation du paiement.
 */
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
    private ImageView boutonTrash;

    @FXML
    private Button btnAjouterOrdonnance;

    private boolean ordonnanceAjoutee = false;

    private UUID clientId;
    private UUID pharmacienAdjointId;

    // Modèle du panier
    private final List<MedicamentPanier> panierItems = new ArrayList<>();

    private int rowCount = 1;
    private ObservableList<Medicament> suggestions = FXCollections.observableArrayList();
    private final Logger LOGGER = Logger.getLogger(VenteController.class.getName());
    @FXML private Button btnRetour;
    private VentesController parentController;

    /**
     * Définit le contrôleur parent (VentesController) pour permettre la navigation retour.
     * @param parent Le contrôleur VentesController.
     */
    public void setParentController(VentesController parent) {
        this.parentController = parent;
    }


    /**
     * Initialise le contrôleur après le chargement du FXML.
     * Configure les listeners pour la barre de recherche de médicaments et la liste de suggestions.
     */
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

    /**
     * Gère l'action du bouton "Retour".
     * Si un contrôleur parent est défini, appelle sa méthode pour revenir à la vue précédente.
     * Sinon, affiche une alerte.
     * @param event L'événement d'action.
     */
    @FXML
    private void handleRetour(ActionEvent event) {
        if (parentController != null) {
            parentController.returnToList();
        } else {
            showAlert(Alert.AlertType.WARNING,
                    "Navigation",
                    "Impossible de revenir en arrière",
                    "Le contrôleur parent n'a pas été initialisé.");
        }
    }



    /**
     * Définit l'ID du client pour la vente en cours.
     * @param clientId L'UUID du client.
     */
    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    /**
     * Gère l'action du bouton d'ajout de médicament (souvent implicite via la sélection).
     * Ajoute le premier médicament de la liste de suggestions au panier.
     * @param event L'événement d'action.
     */
    @FXML
    private void ajouterMedicament(ActionEvent event) {
        if (!listView.getItems().isEmpty()) {
            String selected = listView.getItems().get(0);
            if (selected.contains("null")) {
                showAlert(Alert.AlertType.WARNING, "Prix manquant",
                        "Impossible d'ajouter ce médicament", "Le prix est manquant (null).");
                return;
            }
            ajouterMedicament(selected);
            listView.getItems().clear();
        }
    }

    /**
     * Définit l'ID du pharmacien adjoint pour la vente en cours.
     * @param pharmId L'UUID du pharmacien adjoint.
     */
    public void setPharmacienAdjointId(UUID pharmId) {
        this.pharmacienAdjointId = pharmId;
    }

    /**
     * Recherche des médicaments en fonction du terme saisi.
     * Appelle l'API REST et met à jour la liste de suggestions.
     * @param searchTerm Le terme de recherche.
     */
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

    /**
     * Définit si une ordonnance a été ajoutée pour cette vente.
     * @param valeur true si une ordonnance a été ajoutée, false sinon.
     */
    public void setOrdonnanceAjoutee(boolean valeur) {
        this.ordonnanceAjoutee = valeur;
    }

    /**
     * Ajoute un médicament sélectionné au panier.
     * Extrait les informations du médicament, le crée en tant qu'objet MedicamentPanier,
     * l'ajoute à la liste du panier et rafraîchit l'affichage.
     * @param selected La chaîne représentant le médicament sélectionné (nom - prix).
     */
    @FXML
    public void ajouterMedicament(String selected) {
        try {
            // 1) Extraction du nom et du prix depuis la chaîne "Libellé - XX,XX€"
            String[] parts = selected.split(" - ");
            if (parts.length < 2) {
                LOGGER.warning("Format invalide : " + selected);
                return;
            }
            String nom = parts[0].trim();
            double prix = Double.parseDouble(parts[1]
                    .replace("€", "")
                    .replace(",", ".")
                    .trim());

            // 2) On retrouve l'objet Medicament correspondant
            Optional<Medicament> match = suggestions.stream()
                    .filter(m -> {
                        String n = m.getDenomination() != null ? m.getDenomination() : m.getLibelle();
                        return selected.startsWith(n);
                    })
                    .findFirst();
            if (match.isEmpty()) {
                LOGGER.warning("Aucun médicament correspondant trouvé pour : " + nom);
                return;
            }

            // 3) Vérification du code CIP13
            Medicament med = match.get();
            if (med.getStock() == 0 ){
                showAlert(Alert.AlertType.WARNING, "Rupture de stock", "Rupture de stock ! ",
                        "le médicament est en rupture de stock");
                return;
            }
            String codeCip13 = med.getCodeCip13();
            if (codeCip13 == null || codeCip13.isBlank()) {
                LOGGER.warning("Code CIP13 manquant pour : " + med.getLibelle());
                return;
            }

            // 4) Création et peuplement de l'objet métier
            MedicamentPanier mp = new MedicamentPanier(codeCip13, 1, prix);
            mp.setNomMedicament(nom);

            System.out.println("le stock est : "+med.getStock());

            // 5) Ajout au modèle
            panierItems.add(mp);

            // 6) Rafraîchissement complet de la grille et des totaux
            refreshGrid();

            // 7) Remise à zéro de la barre de recherche
            barDeRecherche.clear();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout du médicament", e);
        }

    }
    /*
     * @FXML
     * public void ajouterMedicament(String selected) {
     * try {
     * String[] parts = selected.split(" - ");
     * if (parts.length < 2) {
     * LOGGER.warning("Format invalide : " + selected);
     * return;
     * }
     *
     * String nom = parts[0].trim();
     * double prix = Double.parseDouble(parts[1].replace("€", "").replace(",",
     * ".").trim());
     *
     * Optional<Medicament> match = suggestions.stream()
     * .filter(m -> {
     * String nomMedoc = (m.getDenomination() != null) ? m.getDenomination() :
     * m.getLibelle();
     * return selected.startsWith(nomMedoc);
     * })
     * .findFirst();
     *
     * if (match.isEmpty()) {
     * LOGGER.warning("Aucun médicament correspondant trouvé pour : " + nom);
     * return;
     * }
     *
     * Medicament med = match.get();
     * String codeCip13 = med.getCodeCip13();
     * if (codeCip13 == null || codeCip13.isBlank()) {
     * LOGGER.warning("Code CIP13 manquant pour le médicament : " +
     * med.getLibelle());
     * return;
     * }
     *
     * Label labelNom = new Label(nom);
     * labelNom.setTextFill(Color.WHITE);
     * labelNom.setUserData(codeCip13);
     *
     * TextField qteField = new TextField("1");
     * qteField.
     * setStyle("-fx-text-fill: white; -fx-control-inner-background: rgba(0,122,255,1);"
     * );
     * qteField.textProperty().addListener((obs, oldVal, newVal) -> {
     * if (!newVal.matches("\\d*")) {
     * qteField.setText(newVal.replaceAll("[^\\d]", ""));
     * }
     * majInfosPanier();
     * });
     *
     * Label labelPrix = new Label(String.format("%.2f €", prix));
     * labelPrix.setTextFill(Color.WHITE);
     *
     * gridPanePanier.add(labelNom, 0, rowCount);
     * gridPanePanier.add(qteField, 1, rowCount);
     * gridPanePanier.add(labelPrix, 2, rowCount);
     *
     * rowCount++;
     * barDeRecherche.clear();
     * majInfosPanier();
     *
     * } catch (Exception e) {
     * LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout du médicament", e);
     * }
     * }
     *
     * private void majInfosPanier() {
     * double total = 0.0;
     * int sommeQte = 0;
     * for (int i = 1; i < rowCount; i++) {
     * Node qteNode = gridPanePanier.getChildren().get(i * 3 + 1);
     * Node prixNode = gridPanePanier.getChildren().get(i * 3 + 2);
     *
     * if (qteNode instanceof TextField && prixNode instanceof Label) {
     * TextField qteField = (TextField) qteNode;
     * Label prixLabel = (Label) prixNode;
     *
     * String qteText = qteField.getText().trim();
     * if (!qteText.isEmpty() && qteText.matches("\\d+")) {
     * int qte = Integer.parseInt(qteText);
     * double prixUnit = Double.parseDouble(prixLabel.getText().replace(" €",
     * "").replace(",", "."));
     * total += qte * prixUnit;
     * sommeQte += qte;
     * }
     * }
     * LabelQuantierValue.setText(String.valueOf(sommeQte));
     * LabelPrixValue.setText(String.format("%.2f €", total));
     * }
     *
     */

    /**
     * Gère l'action du bouton "Payer".
     * Crée une requête de vente avec les informations du client, du pharmacien,
     * des médicaments du panier et le mode de paiement.
     * Appelle l'API REST pour enregistrer la vente.
     * @param event L'événement d'action.
     */
    @FXML
    private void handlePayer(ActionEvent event) {
        List<MedicamentPanier> panier = new ArrayList<>(panierItems); // Utiliser directement panierItems

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
        request.setOrdonnanceAjoutee(ordonnanceAjoutee);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String requestJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            LOGGER.info("Payload de la vente :\n" + requestJson);

            ApiRest.createVente(request);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Vente créée", "La vente a bien été enregistrée.");
            // Plutôt que de fermer la fenêtre, on revient à la liste initiale :
            if (parentController != null) {
                parentController.returnToList();
            } else {
                // En cas de parent non initialisé, on ferme quand même :
                Stage stage = (Stage) btnPayer.getScene().getWindow();
                stage.close();
            }


        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Erreur de sérialisation JSON : " + e.getMessage(), e);
            showAlert(Alert.AlertType.ERROR,
                    "Erreur technique",
                    "Problème de format de données",
                    "Impossible de formater les données de la vente : " + e.getMessage());
        } catch (Exception e) {
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

    /**
     * Calcule le montant total des médicaments dans le panier.
     * @param panier La liste des médicaments dans le panier.
     * @return Le montant total.
     */
    private double calculerMontantTotal(List<MedicamentPanier> panier) {
        return panier.stream().mapToDouble(mp -> mp.getPrixUnitaire() * mp.getQuantite()).sum();
    }

    /**
     * Affiche une boîte de dialogue d'alerte.
     * @param type Le type d'alerte (ex: ERROR, INFORMATION).
     * @param title Le titre de la fenêtre d'alerte.
     * @param header Le texte d'en-tête de l'alerte.
     * @param content Le message principal de l'alerte.
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Récupère l'ID du client associé à cette vente.
     * @return L'UUID du client.
     */
    public UUID getClientId() {
        return clientId;
    }

    /**
     * Récupère les médicaments actuellement dans le panier.
     *
     * @return Une nouvelle liste contenant les objets {@link MedicamentPanier} du panier.
     */
    public List<MedicamentPanier> getMedicamentsPanier() {
        return new ArrayList<>(panierItems);

    }

    /**
     * Gère l'action du bouton "Ajouter Ordonnance".
     * Ouvre une fenêtre modale pour sélectionner un médecin.
     * @param event L'événement d'action.
     */
    @FXML
    private void handleAjouterOrdonnance(ActionEvent event) {
        try {
            // 1) Charger la vue de sélection du médecin
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/pharmacie/view/MedecinsPage.fxml"));
            Parent medecinRoot = loader.load();

            // 2) Créer un Stage modal UNIQUE
            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Sélection du Médecin");
            modal.setScene(new Scene(medecinRoot));

            // 3) Passer au contrôleur de la modale :
            MedecinsController mc = loader.getController();
            mc.setVenteController(this);
            mc.setModalStage(modal);

            // 4) Afficher ce modal
            modal.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erreur",
                    "Impossible d'ouvrir la fenêtre de sélection du médecin",
                    e.getMessage());
        }
    }

    /**
     * Méthode appelée par {@code OrdonnanceController} lorsque l'ordonnance est validée.
     * Met à jour l'ID du médecin et indique qu'une ordonnance a été ajoutée.
     * @param medecinId L'UUID du médecin sélectionné.
     */
    public void onOrdonnanceAjoutee(UUID medecinId) {
        this.pharmacienAdjointId = medecinId;
        this.ordonnanceAjoutee = true;
        // Vous pouvez aussi rafraîchir un label, etc.
    }

    /**
     * Met à jour les informations récapitulatives du panier (quantité totale et prix total).
     */
    private void majInfosPanier() {
        int sommeQte = 0;
        double total = 0.0;

        for (MedicamentPanier mp : panierItems) {
            sommeQte += mp.getQuantite();
            total += mp.getQuantite() * mp.getPrixUnitaire();
        }

        LabelQuantierValue.setText(String.valueOf(sommeQte));
        LabelPrixValue.setText(String.format("%.2f €", total));
    }

    /**
     * Rafraîchit l'affichage de la grille du panier.
     * Supprime les anciennes lignes de médicaments et reconstruit la grille
     * à partir de la liste actuelle {@code panierItems}.
     */
    private void refreshGrid() {
        // 1) Supprimer uniquement les nœuds de la grille ayant rowIndex > 0
        List<Node> toRemove = new ArrayList<>();
        for (Node node : gridPanePanier.getChildren()) {
            Integer r = GridPane.getRowIndex(node);
            // r peut être null (par défaut row=0),
            // ou explicite. On ne considère que r>0.
            if (r != null && r > 0) {
                toRemove.add(node);
            }
        }
        gridPanePanier.getChildren().removeAll(toRemove);

        // 2) Reconstruire les lignes de données à partir du modèle
        int row = 1;
        for (MedicamentPanier mp : panierItems) {
            // Nom
            Label labelNom = new Label(mp.getNomMedicament());
            labelNom.setTextFill(Color.BLACK);

            // Quantité
            TextField qteField = new TextField(String.valueOf(mp.getQuantite()));
            qteField.setStyle("-fx-text-fill: Black; -fx-control-inner-background: rgba(0,122,255,1);");
            qteField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.matches("\\d*")) {
                    qteField.setText(newVal.replaceAll("[^\\d]", ""));
                    return;
                }
                if (!newVal.isEmpty() && Integer.parseInt(newVal) == 0) {
                    panierItems.remove(mp);
                    refreshGrid();
                    return;
                }
                if (!newVal.isEmpty()) {
                    mp.setQuantite(Integer.parseInt(newVal));
                }
                majInfosPanier();
            });

            // Prix unitaire
            Label labelPrix = new Label(String.format("%.2f €", mp.getPrixUnitaire()));
            labelPrix.setTextFill(Color.BLACK);

            // Ajout des nœuds à la ligne `row`
            gridPanePanier.add(labelNom, 0, row);
            gridPanePanier.add(qteField, 1, row);
            gridPanePanier.add(labelPrix, 2, row);

            row++;
        }

        // 3) Mettre à jour les totaux
        majInfosPanier();
    }

    /**
     * Gère l'événement de clic sur l'icône de la poubelle pour vider le panier.
     * Efface tous les articles du modèle de panier et rafraîchit l'interface utilisateur.
     * @param event L'événement de souris déclenché par le clic.
     */
    @FXML
    private void handleEmptyCart(MouseEvent event) {
        // 1) Vide le modèle
        panierItems.clear();
        // 2) Reconstruit la grille vide
        refreshGrid();
        // 3) Remet les compteurs à zéro
        LabelQuantierValue.setText("0");
        LabelPrixValue.setText("0.00 €");
    }
}
