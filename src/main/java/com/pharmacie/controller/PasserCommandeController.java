package com.pharmacie.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.pharmacie.model.Medicament;
import com.pharmacie.model.PageResponse;
import com.pharmacie.service.ApiRest;
import com.pharmacie.service.EmailService;
import com.pharmacie.util.Global;
import com.pharmacie.util.LoggedSeller;
import com.pharmacie.model.Fournisseur;
import com.pharmacie.model.LigneCommande;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.Alert.AlertType;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import org.json.JSONObject;
import org.json.JSONArray;

/**
* Contrôleur pour l'interface de passation de commandes de médicaments auprès des fournisseurs.
* Permet de rechercher des médicaments, de les ajouter à un panier de commande,
* de sélectionner un fournisseur et de valider la commande.
*/
public class PasserCommandeController {

    // Constantes URL vers les services du serveur 
    private static final String BASE_URL = Global.getBaseUrl();
    private static final String FOURNISSEURS_URL = BASE_URL+"/fournisseurs";
    private static final String COMMANDES_URL = BASE_URL+ "/commandes";
    
    // Constantes 
    private static final int LIMITE_MEDICAMENTS = 500;
    private static final String CURRENCY_SYMBOL = " €";
    private static final HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    // Services
    private EmailService emailService = new EmailService(); // Service pour envoie de mail 

    // Éléments de l'interface
    @FXML private TableView<Medicament> tableViewMedicament;
    @FXML private TableColumn<Medicament, String> columnLibelle, columnCodeCIS, columnPrix;
    @FXML private TextField textFieldNomMedicament, textFieldQte, textFieldNewQTE;
    @FXML private Label labelNomMedicament, labelPrixTotal;
    @FXML private ChoiceBox<Fournisseur> ChoiceBoxFournisseur;
    @FXML private Button buttonRecherche, buttonAjouterPanier, buttonValiderCommande,btnRetour, buttonModifierQte;
    @FXML private TableView<PanierItem> tableViewPanier;
    @FXML private TableColumn<PanierItem, String> columnMedicamentPanier, columnPrixPanier;
    @FXML private TableColumn<PanierItem, Integer> columnQuantitePanier;
    @FXML private HBox paginationBar;
    @FXML private Pane paneParent;
    
    // Données
    private LoggedSeller vendeur;
    private UUID fournisseurId;
    private Long idLignecommande = 0L;
    private final ObservableList<Fournisseur> fournisseurs = FXCollections.observableArrayList();
    private final ObservableList<Medicament> medicaments = FXCollections.observableArrayList();
    private final ObservableList<PanierItem> panierItems = FXCollections.observableArrayList();
    private final List<LigneCommande> ligneCommandes = new ArrayList<>();

    // Pagination
    private int pageActuelle = 0;
    private int totalPages = 0;
    private static final int TAILLE_PAGE = 50;

    /**
     * Initialise le contrôleur après le chargement du FXML.
     * Configure les tables, les contrôles, charge les données initiales (fournisseurs, médicaments)
     * et met à jour le montant total.
     */
    @FXML
    public void initialize() {
        tableViewPanier.setEditable(true);
        btnRetour.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/pharmacie/view/GestionCommande.fxml")
                );
                Parent retourView = loader.load();
                paneParent.getChildren().setAll(retourView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        configurerTables();
        configurerControles();
        chargerDonnees();
        mettreAJourMontantTotal(); // Initialiser le montant total dès le démarrage
    }

    /**
     * Configure les colonnes des tables (médicaments et panier).
     * Définit les fabriques de cellules pour afficher les données et gérer l'édition de la quantité dans le panier.
     */
    private void configurerTables() {
        // Table des médicaments
        columnLibelle.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDenomination()));
        columnCodeCIS.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCodeCIS()));
        columnPrix.setCellValueFactory(cell -> new SimpleStringProperty(formaterPrix(cell.getValue().getPrixTTC())));
        tableViewMedicament.setItems(medicaments);
        
        // Table du panier
        columnMedicamentPanier.setCellValueFactory(cell -> cell.getValue().medicamentProperty());
        
        columnQuantitePanier.setCellValueFactory(cell -> cell.getValue().quantiteProperty().asObject());
        columnQuantitePanier.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        columnQuantitePanier.setOnEditCommit(event -> {
            PanierItem item = event.getRowValue();
            int newQuantiter = event.getNewValue();
        
            if (newQuantiter == 0) {
                // Supprimer l'item du panier si la quantité est zéro
                panierItems.remove(item);
            } else {
                item.quantiteProperty().set(newQuantiter);
        
                // Recalculer le prix total pour l'item
                Medicament medicament = getMedicamentByName(item.medicamentProperty().get());
                if (medicament != null) {
                    BigDecimal prixUnitaire = medicament.getPrixTTC() != null ? medicament.getPrixTTC() : BigDecimal.ZERO;
                    BigDecimal nouveauPrixTotal = prixUnitaire.multiply(new BigDecimal(newQuantiter));
                    
                    // Mettre à jour le prix total de l'item dans le panier
                    item.prixFormateProperty().set(nouveauPrixTotal.toString() + CURRENCY_SYMBOL);
                }
            }
        
            mettreAJourMontantTotal();
        });
        
        columnPrixPanier.setCellValueFactory(cell -> cell.getValue().prixFormateProperty());
        tableViewPanier.setItems(panierItems);
        
        // Écouteur pour mise à jour du montant total à chaque modification du panier
        panierItems.addListener((javafx.collections.ListChangeListener<PanierItem>) c -> mettreAJourMontantTotal());
    }

    /**
     * Configure les contrôles de l'interface (boutons, champ de recherche, sélection de médicament, choix du fournisseur).
     * Attribue les actions aux boutons et met en place les listeners.
     */
    private void configurerControles() {
        // Configuration des boutons
        buttonRecherche.setOnAction(this::handleRecherche);
        buttonAjouterPanier.setOnAction(this::handleAjouterAuPanier);
        buttonValiderCommande.setOnAction(this::handleValiderCommande);
        buttonModifierQte.setOnAction(this::handlesupprimerPanier);
        
        // Configuration de la sélection de médicament
        tableViewMedicament.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> labelNomMedicament.setText(
                newVal != null ? newVal.getDenomination() : "Aucun médicament sélectionné"
            )
        );
        
        // Configuration du choix de fournisseur
        ChoiceBoxFournisseur.setConverter(new StringConverter<Fournisseur>() {
            @Override public String toString(Fournisseur f) { return f != null ? f.getNomSociete() : ""; }
            @Override public Fournisseur fromString(String s) { return null; }
        });
        
        ChoiceBoxFournisseur.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fournisseurId = newVal.getIdFournisseur();
                System.out.println("Fournisseur sélectionné: " + newVal.getNomSociete() + " (ID: " + fournisseurId + ")");
            }
        });
        
        ChoiceBoxFournisseur.setItems(fournisseurs);
    }

    /**
     * Charge les données initiales nécessaires au fonctionnement de l'interface.
     * Appelle les méthodes pour charger les fournisseurs et les médicaments.
     */
    private void chargerDonnees() {
        chargerFournisseurs();
        chargerMedicaments();
    }

    /**
     * Récupère la liste des fournisseurs depuis l'API.
     * @return Une liste d'objets {@link Fournisseur}.
     */
    public static List<Fournisseur> getFournisseurs() {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEURS_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + Global.getToken()) // Ajout du token d'autorisation
                    .GET()
                    .build();
    
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // A supprimer pour test réponse serveur pour les fournisseurs
            System.out.println("Réponse du serveur: " + response.body());
    
            if (response.statusCode() == 200) {
                JSONArray array = new JSONArray(response.body());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    
                    // Créer un fournisseur avec le constructeur complet
                    Fournisseur f = new Fournisseur(
                        UUID.fromString(obj.getString("idFournisseur")),
                        obj.optString("nomSociete", ""),
                        obj.optString("sujetFonction", ""),
                        obj.optString("fax", ""),
                        obj.optString("email", ""),
                        obj.optString("telephone", ""),
                        obj.optString("adresse", "")
                    );
                    
                    fournisseurs.add(f);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des fournisseurs: " + e.getMessage());
            e.printStackTrace();
        }
        return fournisseurs;
    }
    
    /**
     * Charge la liste des fournisseurs de manière asynchrone et met à jour le ChoiceBox.
     * Gère les cas de succès et d'échec du chargement.
     */
    private void chargerFournisseurs() {
        executerTache(new Task<List<Fournisseur>>() {
            @Override protected List<Fournisseur> call() throws Exception {
                List<Fournisseur> listeFournisseurs = getFournisseurs();
                return listeFournisseurs;
            }
            
            @Override protected void succeeded() {
                List<Fournisseur> liste = getValue();
                Platform.runLater(() -> {
                    fournisseurs.setAll(liste);
                    if (!liste.isEmpty()) {
                        ChoiceBoxFournisseur.getSelectionModel().select(0);
                        fournisseurId = liste.get(0).getIdFournisseur();
                    }
                });
            }
            
            @Override protected void failed() {
                Throwable exception = getException();
                System.err.println("Erreur lors du chargement des fournisseurs: " + exception.getMessage());
                exception.printStackTrace();
                
                Platform.runLater(() -> {
                    afficherMessage(AlertType.ERROR, "Erreur", "Impossible de charger les fournisseurs", 
                                   "Détails: " + exception.getMessage());
                });
            }
        });
    }

    /**
     * Charge la liste des médicaments de manière asynchrone (première page par défaut) et met à jour la table.
     * Gère les cas de succès et d'échec, y compris l'inaccessibilité du backend.
     */
    private void chargerMedicaments() {
        medicaments.clear();
        executerTache(new Task<List<Medicament>>() {
            @Override protected List<Medicament> call() throws Exception {
                if (!ApiRest.isBackendAccessible()) {
                    throw new Exception("Le backend n'est pas accessible.");
                }
                return chargerMedicamentsDepuisAPI("", pageActuelle);
            }
            
            @Override protected void succeeded() {
                List<Medicament> liste = getValue();
                Platform.runLater(() -> {
                    if (!liste.isEmpty()) {
                        medicaments.setAll(liste);
                    } else {
                        afficherMessage(
                            AlertType.WARNING,
                            "Aucun médicament", 
                            "Aucun médicament n'a été récupéré", 
                            "Veuillez vérifier la base de données."
                        );
                    }
                });
            }
            
            @Override protected void failed() {
                Platform.runLater(() -> afficherMessage(
                    AlertType.ERROR,
                    "Erreur de connexion", 
                    "Impossible de récupérer les médicaments", 
                    "Détails: " + getException().getMessage()
                ));
            }
        });
    }

    /**
     * Charge une page de médicaments depuis l'API en fonction d'un terme de recherche et d'un numéro de page.
     * @param terme Le terme de recherche (peut être vide pour tous les médicaments).
     * @param page Le numéro de la page à récupérer.
     * @return Une liste de {@link Medicament}.
     * @throws Exception Si une erreur survient lors de l'appel à l'API.
     */
    private List<Medicament> chargerMedicamentsDepuisAPI(String terme, int page) throws Exception {
        List<Medicament> listeMedicaments = new ArrayList<>();
        PageResponse<Medicament> response;
        
        if (terme.isEmpty()) {
            response = ApiRest.getMedicamentsPagines(page);
        } else {
            response = ApiRest.getMedicamentsPagines(page, terme);
        }
        
        if (response != null && response.getContent() != null) {
            listeMedicaments.addAll(response.getContent());
            totalPages = response.getTotalPages(); // Récupérer le nombre total de pages
        }
        
        return listeMedicaments;
    }

    /**
     * Récupère un objet {@link Medicament} de la liste chargée en fonction de son nom (dénomination).
     * @param nomMedicament Le nom du médicament à rechercher.
     * @return Le {@link Medicament} correspondant, ou null s'il n'est pas trouvé.
     */
    private Medicament getMedicamentByName(String nomMedicament) {
        for (Medicament medicament : medicaments) {
            if (medicament.getDenomination().equals(nomMedicament)) {
                return medicament;
            }
        }
        return null;
    }

    /**
     * Gère l'événement de clic sur le bouton de recherche de médicaments.
     * Lance une recherche asynchrone de médicaments en utilisant le terme saisi.
     * @param event L'événement d'action.
     */
    @FXML
    private void handleRecherche(ActionEvent event) {
        String nomMedicament = textFieldNomMedicament.getText().trim();
        medicaments.clear();
        
        if (nomMedicament.isEmpty()) {
            chargerMedicaments();
            return;
        }

        executerTache(new Task<List<Medicament>>() {
            @Override protected List<Medicament> call() throws Exception {
                if (!ApiRest.isBackendAccessible()) {
                    throw new Exception("Le backend n'est pas accessible.");
                }
                List<Medicament> resultats = chargerMedicamentsDepuisAPI(nomMedicament,pageActuelle);
                
                if (!resultats.isEmpty()) {
                    trierResultatsParPertinence(resultats, nomMedicament);
                }
                
                return resultats;
            }
            
            @Override protected void succeeded() {
                List<Medicament> resultats = getValue();
                Platform.runLater(() -> {
                    if (!resultats.isEmpty()) {
                        medicaments.setAll(resultats);
                    } else {
                        afficherMessage(
                            AlertType.INFORMATION,
                            "Recherche", 
                            "Aucun résultat", 
                            "Aucun médicament ne correspond à \"" + nomMedicament + "\""
                        );
                    }
                });
            }
            
            @Override protected void failed() {
                Platform.runLater(() -> afficherMessage(
                    AlertType.ERROR,
                    "Erreur de recherche", 
                    "Impossible d'effectuer la recherche", 
                    "Détails: " + getException().getMessage()
                ));
            }
        });
    }

    /**
     * Trie une liste de médicaments par pertinence par rapport à un terme de recherche.
     * Privilégie les médicaments dont le nom commence par le terme, puis ceux qui contiennent le terme comme mot entier,
     * puis ceux qui contiennent simplement le terme.
     * @param resultats La liste des médicaments à trier.
     * @param terme Le terme de recherche.
     */
    private void trierResultatsParPertinence(List<Medicament> resultats, String terme) {
        String termeRecherche = terme.toLowerCase();
        
        resultats.sort((med1, med2) -> {
            String nom1 = med1.getDenomination() != null ? med1.getDenomination().toLowerCase() : "";
            String nom2 = med2.getDenomination() != null ? med2.getDenomination().toLowerCase() : "";
            
            boolean med1CommencePar = nom1.startsWith(termeRecherche);
            boolean med2CommencePar = nom2.startsWith(termeRecherche);
            if (med1CommencePar != med2CommencePar) return med1CommencePar ? -1 : 1;
            
            boolean med1ContientMot = nom1.contains(" " + termeRecherche + " ") || 
                                    nom1.equals(termeRecherche) || 
                                    nom1.startsWith(termeRecherche + " ") || 
                                    nom1.endsWith(" " + termeRecherche);
            
            boolean med2ContientMot = nom2.contains(" " + termeRecherche + " ") || 
                                    nom2.equals(termeRecherche) || 
                                    nom2.startsWith(termeRecherche + " ") || 
                                    nom2.endsWith(" " + termeRecherche);
            
            if (med1ContientMot != med2ContientMot) return med1ContientMot ? -1 : 1;
            
            boolean med1Contient = nom1.contains(termeRecherche);
            boolean med2Contient = nom2.contains(termeRecherche);
            if (med1Contient != med2Contient) return med1Contient ? -1 : 1;
            
            if (med1Contient && med2Contient) {
                int pos1 = nom1.indexOf(termeRecherche);
                int pos2 = nom2.indexOf(termeRecherche);
                if (pos1 != pos2) return Integer.compare(pos1, pos2);
            }
            
            return nom1.compareTo(nom2);
        });
    }

    /**
     * Gère l'événement de clic sur le bouton "Ajouter au panier".
     * Ajoute le médicament sélectionné dans la table des médicaments au panier de commande
     * avec la quantité spécifiée.
     * @param event L'événement d'action.
     */
    @FXML
    private void handleAjouterAuPanier(ActionEvent event) {
        Medicament selectedMedicament = tableViewMedicament.getSelectionModel().getSelectedItem();
        if (selectedMedicament == null) {
            afficherMessage(
                AlertType.ERROR,
                "Aucune sélection", 
                "Aucun médicament sélectionné", 
                "Veuillez sélectionner un médicament avant d'ajouter au panier."
            );
            return;
        }
        
        try {
            int quantite = Integer.parseInt(textFieldQte.getText().trim());
            if (quantite <= 0) {
                afficherMessage(
                    AlertType.ERROR,
                    "Quantité invalide", 
                    "La quantité doit être positive", 
                    "Veuillez entrer une quantité supérieure à 0."
                );
                return;
            }

            ajouterOuMettreAJourPanier(selectedMedicament, quantite);
            textFieldQte.clear();
            
        } catch (NumberFormatException e) {
            afficherMessage(
                AlertType.ERROR,
                "Format invalide", 
                "Format de quantité invalide", 
                "Veuillez entrer un nombre entier valide."
            );
        }
    }

    /**
     * Ajoute un médicament au panier ou met à jour sa quantité s'il y est déjà présent.
     * Met également à jour la liste des lignes de commande et le montant total.
     * @param medicament Le médicament à ajouter/mettre à jour.
     * @param quantite La quantité à ajouter.
     */
    private void ajouterOuMettreAJourPanier(Medicament medicament, int quantite) {
        String nomMedicament = medicament.getDenomination();
        
        // Rechercher si le médicament est déjà dans le panier
        for (int i = 0; i < panierItems.size(); i++) {
            PanierItem item = panierItems.get(i);
            if (item.medicamentProperty().get().equals(nomMedicament)) {
                // Mise à jour de l'item existant
                int nouvelleQuantite = item.quantiteProperty().get() + quantite;
                BigDecimal prixUnitaire = medicament.getPrixTTC() != null ? medicament.getPrixTTC() : BigDecimal.ZERO;
                BigDecimal nouveauPrixTotal = prixUnitaire.multiply(new BigDecimal(nouvelleQuantite));
                
                panierItems.set(i, new PanierItem(
                    nomMedicament,
                    nouvelleQuantite,
                    nouveauPrixTotal.toString() + CURRENCY_SYMBOL
                ));
                
                // Mise à jour de la ligne de commande
                for (LigneCommande lc : ligneCommandes) {
                    if (lc.getId().equals(idLignecommande - 1)) {
                        lc.setQuantite(nouvelleQuantite);
                        lc.calculerMontantLigneAvantSauvegarde();
                        break;
                    }
                }
                
                // Mettre à jour le montant total après modification
                mettreAJourMontantTotal();
                return;
            }
        }
        
        // Ajout d'un nouvel item
        BigDecimal prixHT = medicament.getPrixHT() != null ? medicament.getPrixHT() : BigDecimal.ZERO;
        BigDecimal prixTTC = medicament.getPrixTTC() != null ? medicament.getPrixTTC() : BigDecimal.ZERO;
        
        // Créer la ligne de commande
        LigneCommande ligneCommande = new LigneCommande(idLignecommande++, quantite, prixHT);
        ligneCommande.setMedicament(medicament);
        ligneCommande.calculerMontantLigneAvantSauvegarde();
        ligneCommandes.add(ligneCommande);
        
        // Ajouter au panier
        BigDecimal prixTotal = prixTTC.multiply(new BigDecimal(quantite));
        panierItems.add(new PanierItem(
            nomMedicament,
            quantite,
            prixTotal.toString() + CURRENCY_SYMBOL
        ));
        
        mettreAJourMontantTotal();
    }

    /**
     * Gère l'événement de clic sur le bouton pour modifier/supprimer un article du panier.
     * Réduit la quantité de l'article sélectionné dans le panier ou le supprime si la quantité
     * à retirer est supérieure ou égale à la quantité actuelle.
     * @param event L'événement d'action.
     */
    @FXML
    private void handlesupprimerPanier(ActionEvent event) {
        // Récupérer l'élément sélectionné dans le tableau du panier
        PanierItem selectedItem = tableViewPanier.getSelectionModel().getSelectedItem();
        
        if (selectedItem == null) {
            afficherMessage(
                AlertType.ERROR,
                "Aucune sélection", 
                "Aucun article sélectionné dans le panier", 
                "Veuillez sélectionner un article dans le panier avant de le retirer."
            );
            return;
        }
        String nomMedicament = selectedItem.medicamentProperty().get();
        
        // Vérifier si la quantité à supprimer est valide
        try {
            int quantiteASupprimer = Integer.parseInt(textFieldNewQTE.getText().trim());
            if (quantiteASupprimer <= 0) {
                afficherMessage(
                    AlertType.ERROR,
                    "Quantité invalide", 
                    "La quantité doit être positive", 
                    "Veuillez entrer une quantité supérieure à 0."
                );
                return;
            }
            
            // Rechercher l'article dans le panier
            for (int i = 0; i < panierItems.size(); i++) {
                PanierItem item = panierItems.get(i);
                if (item.medicamentProperty().get().equals(nomMedicament)) {
                    int quantiteActuelle = item.quantiteProperty().get();
    
                    // Trouver la ligne de commande correspondante
                    LigneCommande ligneAModifier = null;
                    for (LigneCommande lc : ligneCommandes) {
                        if (lc.getMedicament() != null && 
                            lc.getMedicament().getDenomination() != null && 
                            lc.getMedicament().getDenomination().equals(nomMedicament)) {
                            ligneAModifier = lc;
                            break;
                        }
                    }
                    
                    if (ligneAModifier == null) {
                        afficherMessage(
                            AlertType.ERROR,
                            "Erreur ", 
                            "Ligne de commande introuvable", 
                            "Impossible de trouver la ligne de commande correspondant à cet article."
                        );
                        return;
                    }
                    
                    if (quantiteASupprimer < quantiteActuelle) {
                        int nouvelleQuantite = quantiteActuelle - quantiteASupprimer;
                        
                        BigDecimal prixUnitaire = ligneAModifier.getPrixUnitaire();
                        if (prixUnitaire == null) {
                            prixUnitaire = BigDecimal.ZERO;
                        }
                        BigDecimal nouveauPrixTotal = prixUnitaire.multiply(new BigDecimal(nouvelleQuantite));
                        
                        // Mettre à jour l'item du panier
                        panierItems.set(i, new PanierItem(
                            nomMedicament,
                            nouvelleQuantite,
                            nouveauPrixTotal.toString() + CURRENCY_SYMBOL
                        ));

                        // Mettre à jour la ligne de commande
                        ligneAModifier.setQuantite(nouvelleQuantite);
                        ligneAModifier.calculerMontantLigneAvantSauvegarde();
                    } 
                    // Quantité à supprimer égale ou supérieure à la quantité actuelle
                    else {
                        // Supprimer l'item du panier
                        panierItems.remove(i);
                        
                        // Supprimer la ligne de commande
                        ligneCommandes.remove(ligneAModifier);
                        
                        // Réorganiser les IDs des lignes de commande restantes
                        reorganiserIdsLigneCommande();
                        
                    }
                    
                    // Mettre à jour le montant total
                    mettreAJourMontantTotal();
                    textFieldNewQTE.setText("Saisir la quantiter");
                    return;
                }
            }         
        } catch (NumberFormatException e) {
            afficherMessage(
                AlertType.ERROR,
                "Format invalide", 
                "Format de quantité invalide", 
                "Veuillez entrer un nombre entier valide pour la quantité."
            );
        }
    }
    
    /**
     * Réorganise les identifiants des lignes de commande après une suppression.
     * Assure que les IDs sont séquentiels à partir de 0.
     */
    private void reorganiserIdsLigneCommande() {
        // Réinitialiser le compteur d'ID
        idLignecommande = 0L;
        for (LigneCommande lc : ligneCommandes) {
            lc.setId(idLignecommande++);
        }
    }

    /**
     * Met à jour l'affichage du montant total de la commande.
     * Calcule le montant total à partir des articles du panier.
     */
    private void mettreAJourMontantTotal() {
        BigDecimal montantTotal = getMontantTotal();
        if (labelPrixTotal != null) { 
            Platform.runLater(() -> labelPrixTotal.setText(montantTotal.toString() + CURRENCY_SYMBOL));
        }
    }

    /**
     * Gère l'événement de clic sur le bouton "Valider Commande".
     * Vérifie si le panier n'est pas vide et si un fournisseur est sélectionné,
     * puis lance l'envoi de la commande.
     * @param event L'événement d'action.
     */
    @FXML
    private void handleValiderCommande(ActionEvent event) {
        if (panierItems.isEmpty()) {
            afficherMessage(
                AlertType.ERROR,
                "Panier vide", 
                "Le panier est vide", 
                "Veuillez ajouter des médicaments au panier avant de valider."
            );
            return;
        }
        
        // Vérifier si un fournisseur est sélectionné
        Fournisseur fournisseurSelectionne = ChoiceBoxFournisseur.getSelectionModel().getSelectedItem();
        if (fournisseurSelectionne == null) {
            if (!fournisseurs.isEmpty()) {
                fournisseurSelectionne = fournisseurs.get(0);
                ChoiceBoxFournisseur.getSelectionModel().select(fournisseurSelectionne);
            } else {
                afficherMessage(
                    AlertType.ERROR,
                    "Aucun fournisseur", 
                    "Aucun fournisseur disponible", 
                    "Impossible de valider la commande sans fournisseur."
                );
                return;
            }
        }
        
        fournisseurId = fournisseurSelectionne.getIdFournisseur();
        System.out.println("Validation commande pour fournisseur: " + fournisseurSelectionne.getNomSociete() + " (ID: " + fournisseurId + ")");
        
        envoyerCommande();
    }

    /**
     * Prépare et envoie la requête de création de commande à l'API.
     * Construit l'objet JSON de la commande avec les informations du fournisseur, du pharmacien
     * et des lignes de commande. Gère la réponse de l'API.
     */
    private void envoyerCommande() {
        try {
            Fournisseur fournisseurSelectionne = ChoiceBoxFournisseur.getSelectionModel().getSelectedItem();
            if (fournisseurSelectionne == null) {
                afficherMessage(AlertType.ERROR, "Erreur Fournisseur", "Aucun fournisseur sélectionné", "Veuillez sélectionner un fournisseur avant de valider la commande.");
                return;
            }
            fournisseurId = fournisseurSelectionne.getIdFournisseur();
    
            UUID vendeurId = vendeur != null ? vendeur.getId() : LoggedSeller.getInstance().getId();
            if (vendeurId == null) {
                afficherMessage(AlertType.ERROR, "Erreur Vendeur", "Aucun vendeur identifié", "Veuillez vous connecter avant de valider la commande.");
                return;
            }
    
            JSONObject commandeRequest = new JSONObject();
            commandeRequest.put("fournisseurId", fournisseurId.toString());
            commandeRequest.put("pharmacienAdjointId", vendeurId.toString()); 
            //commandeRequest.put("pharmacienAdjointId", "cc0efc97-ddcb-49b2-8b3a-241141585701");// a modifier car pour teste 
    
            JSONArray lignesArray = new JSONArray();
            int i = 1;
            for (LigneCommande ligne : ligneCommandes) {

                if (ligne.getMedicament() != null && ligne.getMedicament().getId() > 0) {// a modifier pr test 
                    JSONObject ligneJson = new JSONObject();
                    ligneJson.put("stockMedicamentId", ligne.getMedicament().getId());
                    ligneJson.put("quantite", ligne.getQuantite());
                    ligneJson.put("prixUnitaire", ligne.getPrixUnitaire() != null ? ligne.getPrixUnitaire().toString() : "0");
                    lignesArray.put(ligneJson);
                    System.out.println("ligne \n"+ i++);
                    System.out.println(ligneJson);
                    System.out.println("stock id > "+ ligne.getMedicament().getId());
                } else {
                    afficherMessage(AlertType.WARNING, "Attention Médicament", "Médicament sans identifiant de stock", "Un médicament sans identifiant de stock a été ignoré.");
                }
            }
            if (lignesArray.isEmpty()) {
                afficherMessage(AlertType.ERROR, "Erreur Panier", "Panier invalide", "Aucun médicament valide dans le panier.");
                return;
            }
    
            commandeRequest.put("ligneCommandes", lignesArray);
    
            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(COMMANDES_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Global.getToken())
                .timeout(Duration.ofSeconds(15))
                .POST(HttpRequest.BodyPublishers.ofString(commandeRequest.toString()))
                .build();
    
            executerTache(new Task<HttpResponse<String>>() {
                @Override protected HttpResponse<String> call() throws Exception {
                    return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                }
    
                @Override protected void succeeded() {
                    HttpResponse<String> response = getValue();
                    Platform.runLater(() -> {
                        if (response.statusCode() >= 200 && response.statusCode() < 300) {
                            afficherMessage(AlertType.INFORMATION, "Commande validée", "Commande validée avec succès", "Votre commande a été validée avec succès.");
                            JSONObject jsonObject = new JSONObject(response.body());
                            // Accéder directement aux éléments du JSON
                            String reference = jsonObject.getString("reference");
                            emailService.envoyerMailFournisseur(reference);
                            reinitialiserPanier();
                        } else {
                            afficherMessage(AlertType.ERROR, "Erreur de validation", "Impossible de valider la commande", "Code: " + response.statusCode() + "\nDétails: " + response.body());
                        }
                    });
                }
    
                @Override protected void failed() {
                    Throwable exception = getException();
                    Platform.runLater(() -> afficherMessage(AlertType.ERROR, "Erreur de connexion", "Impossible d'envoyer la commande", "Détails: " + exception.getMessage()));
                }
            });
    
        } catch (Exception e) {
            afficherMessage(AlertType.ERROR, "Erreur", "Erreur lors de la préparation de la commande", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Réinitialise le panier de commande après validation.
     * Vide la liste des articles du panier, les lignes de commande et remet le montant total à zéro.
     */
    private void reinitialiserPanier() {
        panierItems.clear();
        ligneCommandes.clear();
        idLignecommande = 0L;
        mettreAJourMontantTotal(); // Réinitialiser l'affichage du montant total
    }

    /**
     * Exécute une tâche ({@link Task}) dans un nouveau thread.
     * @param task La tâche à exécuter.
     */
    private void executerTache(Task<?> task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Affiche une boîte de dialogue d'alerte.
     * @param type Le type d'alerte (ex: INFORMATION, ERROR).
     * @param titre Le titre de la fenêtre d'alerte.
     * @param entete Le texte d'en-tête de l'alerte.
     * @param contenu Le message principal de l'alerte.
     */
    private void afficherMessage(AlertType type, String titre, String entete, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    /**
     * Formate un prix (BigDecimal) en chaîne de caractères avec le symbole monétaire.
     * @param prix Le prix à formater.
     * @return Le prix formaté, ou une chaîne vide si le prix est null.
     */
    private String formaterPrix(BigDecimal prix) {
        return prix != null ? prix + CURRENCY_SYMBOL : "";
    }

    /**
     * Calcule le montant total de la commande à partir des articles présents dans le panier.
     * @return Le montant total sous forme de {@link BigDecimal}.
     */
    public BigDecimal getMontantTotal() {
        BigDecimal montantTotal = BigDecimal.ZERO;
        for (PanierItem item : panierItems) {
            String prixString = item.prixFormateProperty().get().replace(CURRENCY_SYMBOL, "");
            try {
                montantTotal = montantTotal.add(new BigDecimal(prixString));
            } catch (NumberFormatException e) {
                System.err.println("Erreur de format pour le prix: " + prixString);
            }
        }
        return montantTotal;
    }
    
    /**
     * Définit le vendeur (pharmacien adjoint) qui passe la commande.
     * @param vendeur L'objet {@link LoggedSeller} représentant le vendeur.
     */
    public void setVendeur(LoggedSeller vendeur) {
        this.vendeur = vendeur;
    }
    
    /**
     * Définit l'identifiant du fournisseur pour la commande.
     * Sélectionne également le fournisseur correspondant dans le ChoiceBox.
     * @param fournisseurId L'UUID du fournisseur.
     */
    public void setFournisseurId(UUID fournisseurId) {
        this.fournisseurId = fournisseurId;
        
        // Sélectionner le fournisseur correspondant dans le ChoiceBox
        if (ChoiceBoxFournisseur != null && fournisseurId != null) {
            Platform.runLater(() -> {
                for (Fournisseur f : fournisseurs) {
                    if (f.getIdFournisseur().equals(fournisseurId)) {
                        ChoiceBoxFournisseur.getSelectionModel().select(f);
                        break;
                    }
                }
            });
        }
    }

    /**
     * Classe interne représentant un article dans le panier de commande.
     * Contient des propriétés observables pour le nom du médicament, la quantité et le prix formaté.
     */
    public static class PanierItem {
        private final SimpleStringProperty medicament;
        private final SimpleIntegerProperty quantite;
        private final SimpleStringProperty prixFormate;
        
        /**
         * Constructeur pour un PanierItem.
         * @param medicament Le nom du médicament.
         * @param quantite La quantité commandée.
         * @param prixFormate Le prix total formaté pour cet article.
         */
        public PanierItem(String medicament, int quantite, String prixFormate) {
            this.medicament = new SimpleStringProperty(medicament);
            this.quantite = new SimpleIntegerProperty(quantite);
            this.prixFormate = new SimpleStringProperty(prixFormate);
        }
        
        /** @return La propriété observable du nom du médicament. */
        public SimpleStringProperty medicamentProperty() { return medicament; }
        /** @return La propriété observable de la quantité. */
        public SimpleIntegerProperty quantiteProperty() { return quantite; }
        /** @return La propriété observable du prix formaté. */
        public SimpleStringProperty prixFormateProperty() { return prixFormate; }
    }
    
    /**
     * Charge la page précédente de la liste des médicaments.
     * Met à jour la variable {@code pageActuelle} et recharge les médicaments.
     */
    @FXML
    private void chargerPagePrecedente() {
        if (pageActuelle > 0) {
            pageActuelle--;
            chargerMedicaments();
        }
    }

    /**
     * Charge la page suivante de la liste des médicaments.
     * Met à jour la variable {@code pageActuelle} et recharge les médicaments.
     */
    @FXML
    private void chargerPageSuivante() {
        if (pageActuelle < totalPages - 1) {
            pageActuelle++;
            chargerMedicaments();
        }
    }
}