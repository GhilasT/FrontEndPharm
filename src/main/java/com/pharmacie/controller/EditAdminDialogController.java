package com.pharmacie.controller;

/**
 * Contrôleur pour la boîte de dialogue de modification d'un administrateur.
 * Cette classe gère le formulaire permettant de modifier les informations d'un administrateur existant.
 * Elle effectue également la validation des données saisies avant leur soumission.
 */
import com.pharmacie.model.dto.AdminUpdateRequest;
import com.pharmacie.model.Admin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Contrôleur pour la boîte de dialogue de modification d'un administrateur.
 * Gère l'interface utilisateur pour la mise à jour des informations d'un administrateur
 * et la validation des entrées.
 */
public class EditAdminDialogController {
    @FXML private TextField nomField, prenomField, emailField, telephoneField, 
                          adresseField, salaireField, diplomeField, emailProField, roleField;
    @FXML private ComboBox<String> statutContratCombo;
    @FXML private Label errorLabel;
    
    private final Dialog<ButtonType> dialog = new Dialog<>();
    private AdminUpdateRequest updateRequest = new AdminUpdateRequest();
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    /**
     * Constructeur. Charge le fichier FXML de la boîte de dialogue, configure le contrôleur
     * et met en place la validation des champs.
     * @throws RuntimeException si le chargement du fichier FXML échoue.
     */
    public EditAdminDialogController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/EditAdminDialog.fxml"));
            loader.setController(this);
            DialogPane dialogPane = loader.load();
            dialog.setDialogPane(dialogPane);
            
            // Vérifier si les boutons existent déjà dans le FXML
            boolean hasButtonTypes = !dialog.getDialogPane().getButtonTypes().isEmpty();
            
            // N'ajouter les boutons que s'ils n'existent pas déjà
            if (!hasButtonTypes) {
                ButtonType okButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);
            }
            
            configureValidation();

        } catch (IOException e) {
            throw new RuntimeException("Erreur de chargement du formulaire", e);
        }
    }

    /**
     * Configure la validation des champs du formulaire.
     * Ajoute un filtre d'événement au bouton "OK" pour exécuter la validation
     * avant de fermer la boîte de dialogue.
     */
    private void configureValidation() {
        // Récupération du bon ButtonType
        ButtonType okButtonType = dialog.getDialogPane().getButtonTypes()
            .stream()
            .filter(bt -> bt.getButtonData() == ButtonBar.ButtonData.OK_DONE)
            .findFirst()
            .orElse(null);

        if (okButtonType != null) {
            Button okButton = (Button) dialog.getDialogPane().lookupButton(okButtonType);
            if (okButton != null) {
                okButton.addEventFilter(ActionEvent.ACTION, event -> {
                    if (!validateInputs()) {
                        event.consume();
                    }
                });
            } else {
                System.err.println("Bouton OK non trouvé dans le DialogPane");
            }
        } else {
            System.err.println("ButtonType OK_DONE non trouvé");
        }
    }

    /**
     * Initialise les composants du formulaire, notamment le ComboBox pour le statut du contrat.
     * Cette méthode est appelée automatiquement après le chargement du fichier FXML.
     */
    @FXML
    private void initialize() {
        statutContratCombo.getItems().addAll("CDI", "CDD", "STAGE", "APPRENTISSAGE");
    }

    /**
     * Valide les champs du formulaire (email et téléphone).
     * Affiche les messages d'erreur s'il y en a.
     * @return true si tous les champs sont valides, false sinon.
     */
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();
        
        // Validation email
        String email = emailField.getText().trim();
        if (!email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
            errors.append("• Format d'email invalide\n");
        }
        
        // Validation téléphone
        String telephone = telephoneField.getText().trim();
        if (!telephone.isEmpty() && !PHONE_PATTERN.matcher(telephone).matches()) {
            errors.append("• Téléphone doit contenir 10 chiffres\n");
        }
        
        if (errors.length() > 0) {
            errorLabel.setText(errors.toString());
            return false;
        }
        
        errorLabel.setText("");
        return true;
    }

    /**
     * Remplit les champs du formulaire avec les informations d'un administrateur existant.
     * @param admin L'administrateur dont les informations doivent être affichées.
     */
    public void setAdmin(Admin admin) {
        nomField.setText(admin.getNom());
        prenomField.setText(admin.getPrenom());
        emailField.setText(admin.getEmail());
        telephoneField.setText(admin.getTelephone());
        adresseField.setText(admin.getAdresse());
        salaireField.setText(String.valueOf(admin.getSalaire()));
        statutContratCombo.setValue(admin.getStatutContrat());
        diplomeField.setText(admin.getDiplome());
        emailProField.setText(admin.getEmailPro());
        roleField.setText(admin.getRole());
    }

    /**
     * Affiche la boîte de dialogue et attend que l'utilisateur interagisse avec elle.
     * @return Un {@link Optional} contenant le {@link ButtonType} sur lequel l'utilisateur a cliqué.
     */
    public Optional<ButtonType> showAndWait() {
        return dialog.showAndWait();
    }

    /**
     * Récupère les informations mises à jour de l'administrateur à partir des champs du formulaire.
     * @return Un objet {@link AdminUpdateRequest} contenant les données mises à jour.
     */
    public AdminUpdateRequest getUpdateRequest() {
        updateRequest.setNom(nomField.getText().trim());
        updateRequest.setPrenom(prenomField.getText().trim());
        updateRequest.setEmail(emailField.getText().trim());
        updateRequest.setTelephone(telephoneField.getText().trim());
        updateRequest.setAdresse(adresseField.getText().trim());
        updateRequest.setSalaire(parseDouble(salaireField.getText().trim()));
        updateRequest.setStatutContrat(statutContratCombo.getValue());
        updateRequest.setDiplome(diplomeField.getText().trim());
        updateRequest.setEmailPro(emailProField.getText().trim());
        updateRequest.setRole(roleField.getText().trim());
        return updateRequest;
    }

    /**
     * Convertit une chaîne de caractères en {@link Double}.
     * @param value La chaîne à convertir.
     * @return Le {@link Double} correspondant, ou null si la chaîne est vide ou n'est pas un nombre valide.
     */
    private Double parseDouble(String value) {
        try {
            return value.isEmpty() ? null : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}