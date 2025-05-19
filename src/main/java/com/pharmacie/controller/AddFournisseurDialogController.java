package com.pharmacie.controller;

import com.pharmacie.model.dto.FournisseurCreateRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

public class AddFournisseurDialogController {

    // Champs du formulaire liés au FXML
    @FXML
    private TextField nomSocieteField, sujetFonctionField, faxField, emailField, telephoneField, adresseField;
    @FXML
    private Label errorLabel; // Label pour afficher les messages d'erreur

    // Dialog JavaFX utilisé pour afficher la boîte de dialogue
    private Dialog<ButtonType> dialog;

    // Expressions régulières pour valider email et téléphone
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    // Constructeur : initialise la boîte de dialogue
    public AddFournisseurDialogController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/AddFournisseurDialog.fxml"));
            loader.setController(this); // Lier ce contrôleur au FXML
            dialog = new Dialog<>();
            dialog.setTitle("Ajouter un fournisseur");
            dialog.setDialogPane(loader.load()); // Charger le contenu
            configureValidation(); // Ajouter la logique de validation
        } catch (IOException e) {
            throw new RuntimeException("Erreur chargement dialog", e);
        }
    }

    // Configure la validation avant de fermer le formulaire
    private void configureValidation() {
        ButtonType okButtonType = dialog.getDialogPane().getButtonTypes()
                .stream()
                .filter(bt -> bt.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                .findFirst()
                .orElse(null);

        if (okButtonType != null) {
            Button okButton = (Button) dialog.getDialogPane().lookupButton(okButtonType);
            okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                if (!validateInputs()) // Si invalide, annule la fermeture
                    event.consume();
            });
        }
    }

    // Valide les champs du formulaire et affiche les erreurs
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

        errorLabel.setText(errors.toString()); // Affiche les erreurs
        return errors.length() == 0; // Retourne vrai si aucun message d'erreur
    }

    // Crée et retourne un objet FournisseurCreateRequest avec les données saisies
    public FournisseurCreateRequest getCreateRequest() {
        FournisseurCreateRequest req = new FournisseurCreateRequest();
        req.setNomSociete(nomSocieteField.getText().trim());
        req.setEmail(emailField.getText().trim());
        req.setTelephone(telephoneField.getText().trim());
        req.setAdresse(adresseField.getText().trim());
        req.setSujetFonction(sujetFonctionField.getText().trim());
        req.setFax(faxField.getText().trim());
        return req;
    }

    // Affiche la boîte de dialogue et attend l'action de l'utilisateur
    public Optional<ButtonType> showAndWait() {
        return dialog.showAndWait();
    }
}
