package com.pharmacie.controller;

import com.pharmacie.model.Medicament;
import com.pharmacie.model.MedicamentPanier;
import com.pharmacie.model.Vente;
import com.pharmacie.model.VenteUpdateRequest;
import com.pharmacie.service.ApiRest;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModifierVenteController {

    private static final Logger LOGGER = Logger.getLogger(ModifierVenteController.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @FXML private TableView<MedicamentPanier> medicamentsTable;
    @FXML private TableColumn<MedicamentPanier, String> nomColumn;
    @FXML private TableColumn<MedicamentPanier, Integer> quantiteColumn;
    @FXML private TableColumn<MedicamentPanier, Double> prixColumn;
    
    @FXML private ComboBox<String> modePaiementCombo;
    @FXML private CheckBox ordonnanceCheckbox;
    @FXML private DatePicker dateVentePicker;
    
    @FXML private Button btnEnregistrer;
    @FXML private Button btnAnnuler;
    
    @FXML private Label labelTotalValue;
    @FXML private TextField rechercheField;
    @FXML private Button btnRecherche;
    @FXML private ListView<String> resultatsList;

    private Vente vente;
    private VentesController parentController;
    private ObservableList<MedicamentPanier> medicamentsData = FXCollections.observableArrayList();
    private ObservableList<Medicament> suggestions = FXCollections.observableArrayList();

    public void setVente(Vente vente) {
        this.vente = vente;
    }

    public void setVentesController(VentesController controller) {
        this.parentController = controller;
    }

    @FXML
    public void initialize() {
        if (vente == null) return;

        // Configuration des colonnes de la table
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomMedicament"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        prixColumn.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(
                cellData.getValue().getPrixUnitaire()).asObject());
        
        // Configuration des modes de paiement
        modePaiementCombo.setItems(FXCollections.observableArrayList(
            "Carte bancaire", "Espèces", "Chèque", "Virement"));
        
        // Chargement des données de la vente
        chargerDonneesVente();
        
        // Gestion de la recherche
        rechercheField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.length() >= 2) {
                rechercherMedicaments(newVal);
            } else {
                resultatsList.getItems().clear();
            }
        });
        
        resultatsList.setOnMouseClicked(event -> {
            String selected = resultatsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ajouterMedicament(selected);
                resultatsList.getItems().clear();
            }
        });
        
        // Mise à jour du total à chaque modification
        medicamentsData.addListener((javafx.collections.ListChangeListener<MedicamentPanier>) c -> {
            mettreAJourTotal();
        });
    }

    private void chargerDonneesVente() {
        try {
            // Mode de paiement
            modePaiementCombo.setValue(vente.getModePaiement());
            
            // Ordonnance ajoutée
            ordonnanceCheckbox.setSelected(vente.getNotification() != null && 
                                          vente.getNotification().contains("Ordonnance"));
            
            // Date de vente
            if (vente.getDateVente() != null) {
                LocalDateTime ldt = LocalDateTime.ofInstant(
                    vente.getDateVente().toInstant(), ZoneId.systemDefault());
                dateVentePicker.setValue(ldt.toLocalDate());
            }
            
            // Médicaments
            medicamentsData.clear();
            if (vente.getMedicaments() != null) {
                for (Medicament med : vente.getMedicaments()) {
                    MedicamentPanier mp = new MedicamentPanier();
                    mp.setCodeCip13(med.getCodeCip13());
                    mp.setNomMedicament(med.getDenomination() != null ? 
                                       med.getDenomination() : med.getLibelle());
                    
                    // La quantité est stockée dans le premier stock pour les ventes
                    if (!med.getStocks().isEmpty()) {
                        mp.setQuantite(med.getStocks().get(0).getQuantite());
                    } else {
                        mp.setQuantite(med.getQuantite());
                    }
                    
                    // Récupération du prix TTC
                    mp.setPrixUnitaire(med.getPrixTTC() != null ? 
                                      med.getPrixTTC().doubleValue() : 0.0);
                    
                    medicamentsData.add(mp);
                }
            }
            
            medicamentsTable.setItems(medicamentsData);
            mettreAJourTotal();
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement des données de la vente", e);
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur de chargement", 
                     "Impossible de charger les données de la vente: " + e.getMessage());
        }
    }
    
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

            resultatsList.setItems(results);
            suggestions.setAll(medicaments);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erreur",
                    "Recherche impossible",
                    "Erreur serveur : " + e.getMessage());
        }
    }
    
    /**
     * Méthode appelée par le bouton de recherche via onAction dans le FXML
     */
    @FXML
    private void rechercherMedicaments() {
        String searchTerm = rechercheField.getText().trim();
        if (!searchTerm.isEmpty()) {
            rechercherMedicaments(searchTerm);
        }
    }
    
    private void ajouterMedicament(String selected) {
        try {
            // Extraction du nom et du prix depuis la chaîne "Libellé - XX,XX€"
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

            // On retrouve l'objet Medicament correspondant
            Medicament med = suggestions.stream()
                    .filter(m -> {
                        String n = m.getDenomination() != null ? m.getDenomination() : m.getLibelle();
                        return selected.startsWith(n);
                    })
                    .findFirst()
                    .orElse(null);
                    
            if (med == null) {
                LOGGER.warning("Aucun médicament correspondant trouvé pour : " + nom);
                return;
            }

            // Vérification du stock
            if (med.getStock() == 0) {
                showAlert(Alert.AlertType.WARNING, "Rupture de stock", 
                         "Rupture de stock !",
                         "Le médicament est en rupture de stock");
                return;
            }

            // Création de l'objet pour le panier
            MedicamentPanier mp = new MedicamentPanier(med.getCodeCip13(), 1, prix);
            mp.setNomMedicament(nom);

            // Ajout au modèle
            medicamentsData.add(mp);
            medicamentsTable.refresh();
            mettreAJourTotal();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout du médicament", e);
        }
    }
    
    private void mettreAJourTotal() {
        double total = medicamentsData.stream()
                .mapToDouble(mp -> mp.getQuantite() * mp.getPrixUnitaire())
                .sum();
                
        labelTotalValue.setText(String.format("%.2f €", total));
    }

    @FXML
    private void handleEnregistrer(ActionEvent event) {
        try {
            if (medicamentsData.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation", 
                         "Panier vide", 
                         "Veuillez ajouter au moins un médicament");
                return;
            }
            
            // Création de la requête de mise à jour
            VenteUpdateRequest request = new VenteUpdateRequest();
            request.setIdVente(vente.getIdVente());
            
            // Date de vente
            if (dateVentePicker.getValue() != null) {
                LocalDateTime ldt = dateVentePicker.getValue().atTime(12, 0);
                request.setDateVente(Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()));
            } else {
                request.setDateVente(vente.getDateVente());
            }
            
            request.setModePaiement(modePaiementCombo.getValue());
            request.setOrdonnanceAjoutee(ordonnanceCheckbox.isSelected());
            request.setMedicaments(new ArrayList<>(medicamentsData));
            
            // Appel à l'API
            ApiRest.updateVente(vente.getIdVente(), request);
            
            showAlert(Alert.AlertType.INFORMATION, "Succès", 
                     "Vente mise à jour", 
                     "La vente a été modifiée avec succès");
                     
            // Fermeture de la fenêtre
            ((Stage) btnEnregistrer.getScene().getWindow()).close();
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de la vente", e);
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Échec de la mise à jour", 
                     "Impossible de mettre à jour la vente: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleAnnuler(ActionEvent event) {
        // Fermeture de la fenêtre sans enregistrer
        ((Stage) btnAnnuler.getScene().getWindow()).close();
    }
    
    @FXML
    private void handleSupprimerMedicament(ActionEvent event) {
        MedicamentPanier selected = medicamentsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            medicamentsData.remove(selected);
            medicamentsTable.refresh();
        } else {
            showAlert(Alert.AlertType.WARNING, "Sélection", 
                     "Aucun médicament sélectionné", 
                     "Veuillez sélectionner un médicament à supprimer");
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
