package com.pharmacie.controller;

/**
 * Contrôleur pour la boîte de dialogue d'ajout d'un pharmacien adjoint.
 * 
 * Cette classe est responsable de :
 * - Afficher un formulaire pour saisir les informations d'un nouveau pharmacien adjoint.
 * - Valider les données saisies (email, téléphone, mot de passe, etc.).
 * - Créer une requête d'ajout de pharmacien adjoint avec les données validées.
 * 
 * Les validations incluent :
 * - Vérification du format d'email et de téléphone.
 * - Vérification de la correspondance des mots de passe.
 * - Validation des champs obligatoires.
 * - Vérification du format des valeurs numériques comme le salaire.
 * 
 * Le contrôleur utilise un modèle de dialogue JavaFX pour l'interaction utilisateur.
 */
import com.pharmacie.model.dto.PharmacienAdjointCreateRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;

public class AddPharmacienAdjointDialogController {
    @FXML
    private TextField nomField, prenomField, emailField, telephoneField, adresseField,
            salaireField, diplomeField, emailProField;
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
     * Constructeur qui initialise la boîte de dialogue et configure les validations.
     */
    public AddPharmacienAdjointDialogController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/AddPharmacienAdjointDialog.fxml"));
            loader.setController(this);
            dialog = new Dialog<>();
            dialog.setTitle("Ajouter un pharmacien adjoint");
            dialog.setDialogPane(loader.load());
            initialize();
            configureValidation();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement de la boîte de dialogue", e);
        }
    }

    /**
     * Initialise les composants de l'interface utilisateur.
     * Configure les valeurs par défaut pour les champs et les contrôles.
     */
    private void initialize() {
        statutContratCombo.getItems().addAll("CDI", "CDD", "STAGE", "APPRENTISSAGE");
        dateEmbauchePicker.setValue(LocalDate.now());
        salaireField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                salaireField.setText(oldValue);
            }
        });
    }

    /**
     * Configure la validation des champs avant la soumission du formulaire.
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
     * Valide les données saisies par l'utilisateur.
     * 
     * @return true si toutes les données sont valides, false sinon.
     */
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();

        if (nomField.getText().trim().isEmpty())
            errors.append("Le nom est obligatoire.\n");
        if (prenomField.getText().trim().isEmpty())
            errors.append("Le prénom est obligatoire.\n");

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
     * Crée une requête pour ajouter un pharmacien adjoint avec les données saisies.
     * 
     * @return Un objet PharmacienAdjointCreateRequest contenant les données du formulaire.
     */
    public PharmacienAdjointCreateRequest getCreateRequest() {
        PharmacienAdjointCreateRequest request = new PharmacienAdjointCreateRequest();
        request.setNom(nomField.getText().trim());
        request.setPrenom(prenomField.getText().trim());
        request.setEmail(emailField.getText().trim());
        request.setTelephone(telephoneField.getText().trim());
        request.setAdresse(adresseField.getText().trim());
        request.setDateEmbauche(dateEmbauchePicker.getValue().toString());
        request.setSalaire(Double.parseDouble(salaireField.getText().trim()));
        request.setStatutContrat(statutContratCombo.getValue());
        request.setDiplome(diplomeField.getText().trim());
        request.setEmailPro(emailProField.getText().trim());
        request.setPassword(passwordField.getText());
        return request;
    }

    /**
     * Affiche la boîte de dialogue et attend la réponse de l'utilisateur.
     * 
     * @return Un Optional contenant le ButtonType cliqué par l'utilisateur.
     */
    public Optional<ButtonType> showAndWait() {
        return dialog.showAndWait();
    }
}