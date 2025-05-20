package com.pharmacie.controller;

import com.pharmacie.model.dto.FournisseurCreateRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Contrôleur de la fenêtre permettant d'ajouter un nouveau fournisseur.
 * Gère la validation et la collecte des informations du fournisseur via un dialogue JavaFX.
 */
public class AddFournisseurDialogController {
    @FXML
    private TextField nomSocieteField, sujetFonctionField, faxField, emailField, telephoneField, adresseField;
    @FXML
    private Label errorLabel;
    
    private Dialog<ButtonType> dialog;
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    /**
     * Initialise le contrôleur du dialogue, charge le fichier FXML et configure la fenêtre.
     * Configure la validation du formulaire lors de la soumission.
     *
     * @throws RuntimeException si une erreur survient lors du chargement du FXML
     */
    public AddFournisseurDialogController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/AddFournisseurDialog.fxml"));
            loader.setController(this);
            dialog = new Dialog<>();
            dialog.setTitle("Ajouter un fournisseur");
            dialog.setDialogPane(loader.load());
            
            // Vérifier si les boutons existent déjà dans le FXML
            boolean hasOkButton = dialog.getDialogPane().getButtonTypes().stream()
                .anyMatch(bt -> bt.getButtonData() == ButtonBar.ButtonData.OK_DONE);
            boolean hasCancelButton = dialog.getDialogPane().getButtonTypes().stream()
                .anyMatch(bt -> bt.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE);
                
            // N'ajouter les boutons que s'ils n'existent pas déjà
            if (!hasOkButton) {
                ButtonType okButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().add(okButtonType);
            }
            
            if (!hasCancelButton) {
                ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().add(cancelButtonType);
            }
            
            configureValidation();
        } catch (IOException e) {
            throw new RuntimeException("Erreur chargement dialog", e);
        }
    }

    /**
     * Configure la validation lorsque le bouton OK est cliqué.
     * Empêche la fermeture du dialogue si la validation échoue.
     */
    private void configureValidation() {
        ButtonType okButtonType = dialog.getDialogPane().getButtonTypes()
                .stream()
                .filter(bt -> bt.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                .findFirst()
                .orElse(null);

        if (okButtonType != null) {
            Button okButton = (Button) dialog.getDialogPane().lookupButton(okButtonType);
            okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                if (!validateInputs()) {
                    event.consume();
                }
            });
        }
    }

    /**
     * Valide toutes les saisies utilisateur et affiche les messages d'erreur si la validation échoue.
     * Vérifie que les champs obligatoires sont remplis et que les formats d'email et de téléphone sont valides.
     *
     * @return true si toutes les saisies sont valides, false sinon
     */
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();
        
        if (nomSocieteField.getText().trim().isEmpty())
            errors.append("Le nom de société est obligatoire\n");
        
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            errors.append("L'email est obligatoire\n");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.append("Format email invalide\n");
        }
        
        String telephone = telephoneField.getText().trim();
        if (telephone.isEmpty()) {
            errors.append("Le téléphone est obligatoire\n");
        } else if (!PHONE_PATTERN.matcher(telephone).matches()) {
            errors.append("Téléphone doit contenir 10 chiffres\n");
        }
        
        if (adresseField.getText().trim().isEmpty())
            errors.append("L'adresse est obligatoire\n");

        errorLabel.setText(errors.toString());
        return errors.length() == 0;
    }

    /**
     * Crée et renvoie un objet de requête de création de fournisseur contenant les données du formulaire.
     *
     * @return FournisseurCreateRequest rempli avec les saisies utilisateur
     */
    public FournisseurCreateRequest getCreateRequest() {
        FournisseurCreateRequest req = new FournisseurCreateRequest(
            nomSocieteField.getText().trim(),
            emailField.getText().trim(),
            telephoneField.getText().trim(),
            adresseField.getText().trim(),
            sujetFonctionField.getText().trim(),
            faxField.getText().trim()
        );
        return req;
    }

    /**
     * Affiche le dialogue et attend la réponse de l'utilisateur.
     *
     * @return un Optional contenant le ButtonType cliqué par l'utilisateur
     */
    public Optional<ButtonType> showAndWait() {
        return dialog.showAndWait();
    }
}