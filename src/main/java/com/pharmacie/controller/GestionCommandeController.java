package com.pharmacie.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import com.pharmacie.service.CommandeService;

import com.pharmacie.model.Medicament;
import com.pharmacie.model.Commande;
import com.pharmacie.model.Fournisseur;
import com.pharmacie.model.Employe;
import com.pharmacie.model.LigneCommande;
import com.pharmacie.util.Global;
import com.pharmacie.util.DialogService;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonType;

import org.json.JSONObject;
import org.json.JSONArray;

public class GestionCommandeController {
    //Constante
    private static final String CURRENCY_SYMBOL = " €";

    private static final String BASE_URL = Global.getBaseUrl();
    private static final String COMMANDES_URL = BASE_URL + "/commandes";
    private static final String FOURNISSEURS_URL = BASE_URL + "/fournisseurs";
    private static final String EMPLOYES_URL = BASE_URL + "/employes";

    //Interface Attribut
    @FXML private TableView<Commande> tableViewCommandes;
    @FXML private TableColumn<Commande,String> columnReference, columnDate, columnFournisseur, columnMontant, columnVendeur, columnStatus;
    @FXML private Button btnModifierStatus,btnInfosVendeur,btnInfosFournisseur,btnCommandeIncomp,btnInfoCommande,btnPasserCommande;
    private final ObservableList<Commande> commandesObservable = FXCollections.observableArrayList();

    //Attribut
    private List<Commande> commandes = new ArrayList<>();
    private Map<UUID,Fournisseur> fournisseurChargerMap = new HashMap<>();
    private Map<UUID,Employe> employeChargerMap = new HashMap<>();
    @FXML private Pane paneParent; 



    @FXML
    public void initialize() {
        configurationTable();
        chargeCommandeDonneAsync();
        btnInfosFournisseur.setOnAction(this::handleInfoFournisseur);
        btnInfosVendeur.setOnAction(this::handleInfoEmployer);
        btnModifierStatus.setOnAction(this::handleChangerStatusRecu);
        btnCommandeIncomp.setOnAction(this::handleCommandeIncomplet);
        btnInfoCommande.setOnAction(this::handleInfoCommande);
        btnPasserCommande.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/passer-commande.fxml"));
                Parent passerCommandeView = loader.load();
                paneParent.getChildren().clear(); 
                paneParent.getChildren().add(passerCommandeView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void configurationTable() {
        
        // Configuration des colonnes en fonction de la classe Commande
        columnReference.setCellValueFactory(cell -> {
            UUID reference = cell.getValue().getReference();
            return new SimpleStringProperty(reference != null ? reference.toString() : "");
        });
        
        columnDate.setCellValueFactory(cell -> {
            Date date = cell.getValue().getDateCommande();
            if (date != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                return new SimpleStringProperty(sdf.format(date));
            }
            return new SimpleStringProperty("");
        });
        
        columnFournisseur.setCellValueFactory(cell -> {
            UUID fournisseurId = cell.getValue().getFournisseurId();
            String nomFournisseur = getFournisseurById(fournisseurId).getNomSociete();
            return new SimpleStringProperty(nomFournisseur != null ? nomFournisseur : "");
        });
        
        columnMontant.setCellValueFactory(cell -> 
            new SimpleStringProperty(formaterPrix(cell.getValue().getMontantTotal())));
            
        columnVendeur.setCellValueFactory(cell -> {
            UUID employeId = cell.getValue().getPharmacienAdjointId();
            String nomEmployer = getEmployeById(employeId).getNom();
            return new SimpleStringProperty(nomEmployer != null ? nomEmployer : "");
        });
        
        columnStatus.setCellValueFactory(cell -> {
            String statut = cell.getValue().getStatut();
            return new SimpleStringProperty(statut != null ? statut : "");
        });
        
        tableViewCommandes.setItems(commandesObservable);
    }
    
    private void executerTache(Task<?> task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void chargeCommandeDonneAsync() {
        executerTache(new Task<List<Commande>>() {
            @Override 
            protected List<Commande> call() throws Exception {
                try {
                    return CommandeService.getCommandes();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            
            @Override 
            protected void succeeded() {
                List<Commande> listeCommandes = getValue();
                
                Platform.runLater(() -> {
                    commandes.clear();
                    commandes.addAll(listeCommandes);
                    
                    commandesObservable.clear();
                    commandesObservable.addAll(commandes);
                    
                    
                    // Forcer le rafraîchissement de la table
                    tableViewCommandes.refresh();
                    
                });
            }

            @Override 
            protected void failed() {
                Throwable exception = getException();
                System.err.println("Erreur lors du chargement des commandes: " + exception.getMessage());
                exception.printStackTrace();
                
                Platform.runLater(() -> {
                    DialogService.afficherMessage(AlertType.ERROR, "Erreur", 
                                   "Impossible de charger les commandes", 
                                   "Détails: " + exception.getMessage());
                });
            }
        });
    }

    private String formaterPrix(BigDecimal prix) {
        return prix != null ? prix + CURRENCY_SYMBOL : "";
    }

    @FXML
    private void handleInfoEmployer(ActionEvent event){
        Commande commandeSelectionner=tableViewCommandes.getSelectionModel().getSelectedItem();
        if (commandeSelectionner != null) {
            UUID employeId = commandeSelectionner.getPharmacienAdjointId();
            if (employeId != null) {
                Employe employe = getEmployeById(employeId);
                if (employe != null) {
                    DialogService.afficherPopupEmployer(employe);
                } else {
                    DialogService.afficherMessage(AlertType.ERROR, "Erreur", "Employer introuvable",
                            "Aucun Employer trouvé avec cet ID.");
                }
            } else {
                DialogService.afficherMessage(AlertType.WARNING, "Avertissement", "ID Employer NULL",
                        "L'ID du Employer est NULL.");
            }
        } else {
            DialogService.afficherMessage(AlertType.WARNING, "Avertissement", "Aucune commande sélectionnée",
                    "Veuillez sélectionner une commande dans la table.");
        }
    }

    @FXML
    private void handleInfoFournisseur(ActionEvent event) {
        Commande commandeSelectionner=tableViewCommandes.getSelectionModel().getSelectedItem();
        if (commandeSelectionner != null) {
            UUID fournisseurId = commandeSelectionner.getFournisseurId();
            if (fournisseurId != null) {
                Fournisseur fournisseur = getFournisseurById(fournisseurId);
                if (fournisseur != null) {
                    DialogService.afficherPopupFournisseur(fournisseur);
                } else {
                    DialogService.afficherMessage(AlertType.ERROR, "Erreur", "Fournisseur introuvable",
                            "Aucun fournisseur trouvé avec cet ID.");
                }
            } else {
                DialogService.afficherMessage(AlertType.WARNING, "Avertissement", "ID Fournisseur NULL",
                        "L'ID du fournisseur est NULL.");
            }
        } else {
            DialogService.afficherMessage(AlertType.WARNING, "Avertissement", "Aucune commande sélectionnée",
                    "Veuillez sélectionner une commande dans la table.");
        }
    }

    private Fournisseur getFournisseurById(UUID id) {
        if (fournisseurChargerMap.containsKey(id)) {
            return fournisseurChargerMap.get(id);
        }
        Fournisseur fournisseur = null;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEURS_URL+"/"+ id.toString()))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
    
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    
            if (response.statusCode() == 200) {
                JSONObject obj = new JSONObject(response.body());
    
                fournisseur = new Fournisseur(
                    UUID.fromString(obj.getString("idFournisseur")),
                    obj.optString("nomSociete", ""),
                    obj.optString("sujetFonction", ""),
                    obj.optString("fax", ""),
                    obj.optString("email", ""),
                    obj.optString("telephone", ""),
                    obj.optString("adresse", "")
                );
            } else {
                System.out.println("Aucun fournisseur trouvé avec cet ID.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération du fournisseur: " + e.getMessage());
            e.printStackTrace();
        }
        fournisseurChargerMap.put(id, fournisseur);
        return fournisseur;
    }
    
    private Employe getEmployeById(UUID employeId) {//Recupere l'employe par son id dans la BDD
        if (employeChargerMap.containsKey(employeId)) {
            return employeChargerMap.get(employeId);
        }
        Employe employe = null;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(EMPLOYES_URL+"/"+employeId.toString()))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
    
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    
            if (response.statusCode() == 200) {
                JSONObject obj = new JSONObject(response.body());
    
                employe = new Employe();
                employe.setIdPersonne(UUID.fromString(obj.getString("idPersonne"))); // Assuming `setIdPersonne` exists in `Personne`
                employe.setNom(obj.optString("nom", ""));
                employe.setPrenom(obj.optString("prenom", ""));
                employe.setPoste(obj.optString("poste", ""));
                employe.setEmail(obj.optString("email", ""));
                employe.setTelephone(obj.optString("telephone", ""));
                employe.setAdresse(obj.optString("adresse", ""));
                employe.setMatricule(obj.optString("matricule", ""));
                employe.setEmailPro(obj.optString("emailPro", ""));
            } else {
                System.out.println("Aucun employé trouvé avec cet ID.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération de l'employé: " + e.getMessage());
            e.printStackTrace();
        }
        employeChargerMap.put(employeId, employe);
        return employe;
    }

    @FXML
    private void handleChangerStatusRecu(ActionEvent event) {
        Commande commandeSelectionner = tableViewCommandes.getSelectionModel().getSelectedItem();
        if (commandeSelectionner != null && !commandeSelectionner.getStatut().equals("Reçu")) {
            UUID idCommande = commandeSelectionner.getReference();
            try {
                HttpResponse<String> response = CommandeService.mettreAJourStatutCommandeRecu(idCommande);
                
                if (response.statusCode() == 200) {
                    DialogService.afficherMessage(AlertType.INFORMATION, "Succès", "Statut mis à jour",
                            "Le statut de la commande a été mis à jour avec succès.");
                    // Mettre à jour la table de commandes
                    rafraichirTableCommandes();
                } else {
                    System.err.println("Erreur lors de la mise à jour du statut : " + response.statusCode());
                    System.err.println("Détails de l'erreur : " + response.body());
                    DialogService.afficherMessage(AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour du statut",
                            "Une erreur s'est produite lors de la mise à jour du statut de la commande. Code: " + response.statusCode());
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la mise à jour du statut de la commande: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            DialogService.afficherMessage(AlertType.WARNING, "Avertissement", "Aucune commande sélectionnée ou commande sélectionnée déjà reçue",
                    "Veuillez sélectionner une commande dans la table.");    
        }
    }
        
    private void AfficherPopupEditerQteCommandeIncomplet(Commande commande) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Page Commande Incomplete");
        
        VBox content = new VBox();
        content.setSpacing(15);
        content.setStyle("-fx-padding: 20; -fx-background-color: #f9f9f9;");
        
        Label label = new Label("Commande Incomplete");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        content.getChildren().add(label);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        List<TextField> quantiteFields = new ArrayList<>();
        
        int row = 0;
        for (LigneCommande ligneCommande : commande.getLigneCommandes()) {
            Label labelMedicament = new Label("Medicament : " + ligneCommande.getMedicament().getDenomination());
            labelMedicament.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495E;");
            
            TextField textFieldQuantite = new TextField(String.valueOf(ligneCommande.getQuantite()));
            quantiteFields.add(textFieldQuantite);
            
            grid.add(labelMedicament, 0, row); 
            grid.add(textFieldQuantite, 1, row); 
            
            row++; 
        }
        
        content.getChildren().add(grid);
        
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonType.OK.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType);
        
        Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 15;");
    
        dialog.getDialogPane().setContent(content);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                List<Integer> nouvellesQuantites = quantiteFields.stream()
                    .map(textField -> Integer.parseInt(textField.getText()))
                    .collect(Collectors.toList());
                
                envoyerQuantitesMiseAJour(commande.getReference(), nouvellesQuantites);
            }
            return null;
        });
    
        dialog.setOnCloseRequest(event -> dialog.close());
        dialog.showAndWait();
    }
    
    private void envoyerQuantitesMiseAJour(UUID referenceCommande, List<Integer> nouvellesQuantites) {
        try {
            JSONArray jsonQuantites = new JSONArray(nouvellesQuantites);
            String fullUrl = COMMANDES_URL + "/updateIncomplete/" + referenceCommande.toString();
            System.out.println("URL de la requête: " + fullUrl); // Log l'URL
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fullUrl))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonQuantites.toString()))
                    .build();
            
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                DialogService.afficherMessage(AlertType.INFORMATION, "Succès", "Commande mise à jour",
                        "La commande a été mise à jour avec succès.");
                rafraichirTableCommandes();
            } else {
                // Ajouter les détails du corps de la réponse
                String errorDetails = "Code: " + response.statusCode() + "\nRéponse du serveur: " + response.body();
                System.err.println("Erreur lors de la mise à jour: " + errorDetails);
                
                DialogService.afficherMessage(
                    AlertType.ERROR, 
                    "Erreur", 
                    "Erreur lors de la mise à jour de la commande", 
                    errorDetails // Affiche le corps de la réponse
                );
            }
        } catch (Exception e) {
            String errorMessage = "Erreur technique: " + e.getMessage();
            System.err.println(errorMessage);
            e.printStackTrace();
            
            DialogService.afficherMessage(
                AlertType.ERROR, 
                "Erreur", 
                "Erreur de communication", 
                errorMessage
            );
        }
    }
    
    @FXML
    private void handleCommandeIncomplet(ActionEvent event){
        Commande commandeSelectionner=tableViewCommandes.getSelectionModel().getSelectedItem();
        if (commandeSelectionner != null && commandeSelectionner.getStatut().equals("En attente")) {
            AfficherPopupEditerQteCommandeIncomplet(commandeSelectionner);
        } else {
            DialogService.afficherMessage(AlertType.WARNING, "Avertissement", "Aucune commande sélectionnée ou commande avec statut incompatible ",
                    "Veuillez sélectionner une commande dans la table.");    
        }
    }

    @FXML
    private void handleInfoCommande(ActionEvent event ){
        Commande commandeSelectionner=tableViewCommandes.getSelectionModel().getSelectedItem();
        if (commandeSelectionner != null) {
            DialogService.afficherPopupCommandeInfo(commandeSelectionner);
        } else {
            DialogService.afficherMessage(AlertType.WARNING, "Avertissement", "Aucune commande sélectionnée",
                    "Veuillez sélectionner une commande dans la table.");    
        }
    }

    @FXML
    public void rafraichirTableCommandes() {
        commandesObservable.clear();
        chargeCommandeDonneAsync();
    }
}
