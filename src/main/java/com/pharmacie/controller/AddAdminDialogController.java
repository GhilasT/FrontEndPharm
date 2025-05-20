package com.pharmacie.controller;

import com.pharmacie.model.dto.AdministrateurCreateRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.StringConverter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Controller for the dialog that allows adding a new administrator.
 * Manages form validation and data collection for creating new admin users in the system.
 * Includes validation for personal information, professional details, and account credentials.
 */
public class AddAdminDialogController {
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField adresseField;
    @FXML
    private DatePicker dateEmbauchePicker;
    @FXML
    private TextField salaireField;
    @FXML
    private ComboBox<String> statutContratCombo;
    @FXML
    private TextField diplomeField;
    @FXML
    private TextField emailProField;
    @FXML
    private TextField roleField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label errorLabel;

    private Dialog<ButtonType> dialog;
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    /**
     * Initializes the dialog controller, loads the FXML and sets up the dialog.
     * Sets up form validation and initialization of form components.
     *
     * @throws RuntimeException if there's an error loading the dialog FXML
     */
    public AddAdminDialogController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/AddAdminDialog.fxml"));
            loader.setController(this);

            dialog = new Dialog<>();
            dialog.setTitle("Ajouter un administrateur");
            dialog.setDialogPane(loader.load());

            // Configuration après chargement du FXML
            initialize();

            // Validation avant confirmation
            Button okButton = (Button) dialog.getDialogPane().lookupButton(
                    dialog.getDialogPane().getButtonTypes().stream()
                            .filter(buttonType -> buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                            .findFirst().orElse(null));

            if (okButton != null) {
                okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                    if (!validateInputs()) {
                        event.consume();
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du chargement de la boîte de dialogue", e);
        }
    }

    /**
     * Initializes form components with default values.
     * - Populates the contract status combo box
     * - Sets the default hire date to today
     * - Adds input validation for salary field to ensure only numeric values
     */
    private void initialize() {
        // Initialisation des valeurs du ComboBox
        statutContratCombo.getItems().addAll("CDI", "CDD", "STAGE", "ALTERNANCE");

        // Date par défaut = aujourd'hui
        dateEmbauchePicker.setValue(LocalDate.now());

        // Convertir les valeurs numériques pour le salaire
        salaireField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                salaireField.setText(oldValue);
            }
        });
    }

    /**
     * Displays the dialog and waits for user response.
     *
     * @return an Optional containing the ButtonType clicked by the user
     */
    public Optional<ButtonType> showAndWait() {
        return dialog.showAndWait();
    }

    /**
     * Validates all user inputs and displays error messages if validation fails.
     * Checks required fields, email format, phone format, salary format, matching passwords.
     *
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();

        // Vérification des champs obligatoires
        if (nomField.getText().trim().isEmpty())
            errors.append("Le nom est obligatoire.\n");
        if (prenomField.getText().trim().isEmpty())
            errors.append("Le prénom est obligatoire.\n");

        // Validation de l'email
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            errors.append("L'email est obligatoire.\n");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.append("Format d'email invalide.\n");
        }

        // Validation du téléphone
        String telephone = telephoneField.getText().trim();
        if (telephone.isEmpty()) {
            errors.append("Le téléphone est obligatoire.\n");
        } else if (!PHONE_PATTERN.matcher(telephone).matches()) {
            errors.append("Format de téléphone invalide (10 chiffres attendus).\n");
        }

        // Vérification de la date d'embauche
        if (dateEmbauchePicker.getValue() == null) {
            errors.append("La date d'embauche est obligatoire.\n");
        }

        // Vérification du salaire
        if (salaireField.getText().trim().isEmpty()) {
            errors.append("Le salaire est obligatoire.\n");
        } else {
            try {
                Double.parseDouble(salaireField.getText().trim());
            } catch (NumberFormatException e) {
                errors.append("Le salaire doit être un nombre valide.\n");
            }
        }

        // Vérification du statut de contrat
        if (statutContratCombo.getValue() == null) {
            errors.append("Le statut de contrat est obligatoire.\n");
        }

        // Validation de l'email pro
        String emailPro = emailProField.getText().trim();
        if (emailPro.isEmpty()) {
            errors.append("L'email professionnel est obligatoire.\n");
        } else if (!EMAIL_PATTERN.matcher(emailPro).matches()) {
            errors.append("Format d'email professionnel invalide.\n");
        }

        // Vérification du rôle
        if (roleField.getText().trim().isEmpty()) {
            errors.append("Le rôle est obligatoire.\n");
        }

        // Vérification du mot de passe
        if (passwordField.getText().isEmpty()) {
            errors.append("Le mot de passe est obligatoire.\n");
        } else if (passwordField.getText().length() < 6) {
            errors.append("Le mot de passe doit contenir au moins 6 caractères.\n");
        }

        // Vérification de la confirmation du mot de passe
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            errors.append("Les mots de passe ne correspondent pas.\n");
        }

        if (errors.length() > 0) {
            errorLabel.setText(errors.toString());
            return false;
        }

        errorLabel.setText("");
        return true;
    }

    /**
     * Creates and returns an administrator creation request object with the data entered in the form.
     * All form field values are trimmed and converted to appropriate formats.
     *
     * @return AdministrateurCreateRequest populated with the user's input
     */
    public AdministrateurCreateRequest getCreateRequest() {
        AdministrateurCreateRequest request = new AdministrateurCreateRequest();
        request.setNom(nomField.getText().trim());
        request.setPrenom(prenomField.getText().trim());
        request.setEmail(emailField.getText().trim());
        request.setTelephone(telephoneField.getText().trim());
        request.setAdresse(adresseField.getText().trim());
        LocalDate date = dateEmbauchePicker.getValue();
        String formattedDate = date.toString();
        request.setDateEmbauche(formattedDate);
        request.setSalaire(Double.parseDouble(salaireField.getText().trim()));
        request.setStatutContrat(statutContratCombo.getValue());
        request.setDiplome(diplomeField.getText().trim());
        request.setEmailPro(emailProField.getText().trim());
        request.setRole(roleField.getText().trim());
        request.setPassword(passwordField.getText());

        return request;
    }
}