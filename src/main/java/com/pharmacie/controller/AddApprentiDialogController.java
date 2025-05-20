package com.pharmacie.controller;

import com.pharmacie.model.dto.ApprentiCreateRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Contrôleur de la fenêtre permettant d'ajouter un nouvel apprenti.
 * Gère la validation des formulaires et la collecte des données pour créer de nouveaux apprentis.
 */
public class AddApprentiDialogController {
    @FXML
    private TextField nomField, prenomField, emailField, telephoneField, adresseField,
            salaireField, diplomeField, ecoleField, emailProField;
    @FXML
    private DatePicker dateEmbauchePicker;
    @FXML
    private ComboBox<String> statutContratCombo;
    @FXML
    private PasswordField passwordField, confirmPasswordField;
    @FXML
    private Label errorLabel;

    private Dialog<ButtonType> dialog;
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    /**
     * Initialise le contrôleur du dialogue, charge le FXML et configure la fenêtre.
     * Met en place la validation et l'initialisation des composants du formulaire.
     *
     * @throws RuntimeException si une erreur survient lors du chargement du FXML
     */
    public AddApprentiDialogController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/AddApprentiDialog.fxml"));
            loader.setController(this);
            dialog = new Dialog<>();
            dialog.setTitle("Ajouter un apprenti");
            dialog.setDialogPane(loader.load());
            initialize();
            configureValidation();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement de la boîte de dialogue", e);
        }
    }

    /**
     * Initialise les composants du formulaire avec des valeurs par défaut.
     * - Remplit la ComboBox des statuts de contrat
     * - Définit la date d'embauche par défaut à aujourd'hui
     * - Ajoute une validation des saisies pour le champ salaire
     */
    private void initialize() {
        statutContratCombo.getItems().addAll("CDI", "CDD", "STAGE", "ALTERNANCE");
        dateEmbauchePicker.setValue(LocalDate.now());
        salaireField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                salaireField.setText(oldValue);
            }
        });
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
                if (!validateInputs())
                    event.consume();
            });
        }
    }

    /**
     * Valide toutes les saisies utilisateur et affiche les messages d'erreur si la validation échoue.
     * Vérifie les champs obligatoires, les formats d'email et de téléphone, le format du salaire et la correspondance des mots de passe.
     *
     * @return true si toutes les saisies sont valides, false sinon
     */
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();

        if (nomField.getText().trim().isEmpty())
            errors.append("Le nom est obligatoire.\n");
        if (prenomField.getText().trim().isEmpty())
            errors.append("Le prénom est obligatoire.\n");
        if (ecoleField.getText().trim().isEmpty())
            errors.append("L'école est obligatoire.\n");

        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            errors.append("L'email est obligatoire.\n");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.append("Format d'email invalide.\n");
        }

        String telephone = telephoneField.getText().trim();
        if (telephone.isEmpty()) {
            errors.append("Le téléphone est obligatoire.\n");
        } else if (!PHONE_PATTERN.matcher(telephone).matches()) {
            errors.append("Format de téléphone invalide (10 chiffres attendus).\n");
        }

        if (dateEmbauchePicker.getValue() == null) {
            errors.append("La date d'embauche est obligatoire.\n");
        }

        if (salaireField.getText().trim().isEmpty()) {
            errors.append("Le salaire est obligatoire.\n");
        } else {
            try {
                Double.parseDouble(salaireField.getText().trim());
            } catch (NumberFormatException e) {
                errors.append("Le salaire doit être un nombre valide.\n");
            }
        }

        if (statutContratCombo.getValue() == null) {
            errors.append("Le statut de contrat est obligatoire.\n");
        }

        String emailPro = emailProField.getText().trim();
        if (emailPro.isEmpty()) {
            errors.append("L'email professionnel est obligatoire.\n");
        } else if (!EMAIL_PATTERN.matcher(emailPro).matches()) {
            errors.append("Format d'email professionnel invalide.\n");
        }

        if (passwordField.getText().isEmpty()) {
            errors.append("Le mot de passe est obligatoire.\n");
        } else if (passwordField.getText().length() < 6) {
            errors.append("Le mot de passe doit contenir au moins 6 caractères.\n");
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            errors.append("Les mots de passe ne correspondent pas.\n");
        }

        errorLabel.setText(errors.toString());
        return errors.length() == 0;
    }

    /**
     * Crée et renvoie un objet de requête de création d'apprenti contenant les données du formulaire.
     *
     * @return ApprentiCreateRequest rempli avec les saisies utilisateur
     */
    public ApprentiCreateRequest getCreateRequest() {
        ApprentiCreateRequest request = new ApprentiCreateRequest();
        request.setNom(nomField.getText().trim());
        request.setPrenom(prenomField.getText().trim());
        request.setEmail(emailField.getText().trim());
        request.setTelephone(telephoneField.getText().trim());
        request.setAdresse(adresseField.getText().trim());
        request.setDateEmbauche(dateEmbauchePicker.getValue().toString());
        request.setSalaire(Double.parseDouble(salaireField.getText().trim()));
        request.setStatutContrat(statutContratCombo.getValue());
        request.setDiplome(diplomeField.getText().trim());
        request.setEcole(ecoleField.getText().trim());
        request.setEmailPro(emailProField.getText().trim());
        request.setPassword(passwordField.getText());
        return request;
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
