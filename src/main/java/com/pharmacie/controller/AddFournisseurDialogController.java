package com.pharmacie.controller;

import com.pharmacie.model.dto.FournisseurCreateRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Controller for the dialog that allows adding a new supplier (fournisseur).
 * This class manages the validation and collection of supplier information through a JavaFX dialog.
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
     * Initializes the dialog controller, loads the FXML and sets up the dialog.
     * Configures form validation to be triggered when the user attempts to submit the form.
     *
     * @throws RuntimeException if there's an error loading the dialog FXML
     */
    public AddFournisseurDialogController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/AddFournisseurDialog.fxml"));
            loader.setController(this);
            dialog = new Dialog<>();
            dialog.setTitle("Ajouter un fournisseur");
            dialog.setDialogPane(loader.load());
            configureValidation();
        } catch (IOException e) {
            throw new RuntimeException("Erreur chargement dialog", e);
        }
    }

    /**
     * Configures validation to run when the OK button is clicked.
     * Prevents dialog closing if validation fails.
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
                if (!validateInputs())
                    event.consume();
            });
        }
    }

    /**
     * Validates all user inputs and displays error messages if validation fails.
     * Checks that required fields are filled and that email and phone formats are valid.
     *
     * @return true if all inputs are valid, false otherwise
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
     * Creates and returns a supplier creation request object with the data entered in the form.
     *
     * @return FournisseurCreateRequest populated with the user's input
     */
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

    /**
     * Displays the dialog and waits for user response.
     *
     * @return an Optional containing the ButtonType clicked by the user
     */
    public Optional<ButtonType> showAndWait() {
        return dialog.showAndWait();
    }
}