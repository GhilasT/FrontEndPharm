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
 * Contrôleur pour la boîte de dialogue permettant d'ajouter un nouvel administrateur.
 * Gère la validation du formulaire et la collecte des données pour la création de nouveaux utilisateurs administrateurs
 * dans le système. Inclut la validation des informations personnelles, des détails professionnels et des identifiants de compte.
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
     * Initialise le contrôleur de la boîte de dialogue, charge le FXML et configure la boîte de dialogue.
     * Met en place la validation du formulaire et l'initialisation des composants du formulaire.
     *
     * @throws RuntimeException s'il y a une erreur lors du chargement du FXML de la boîte de dialogue.
     */
    public AddAdminDialogController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/AddAdminDialog.fxml"));
            loader.setController(this);

            dialog = new Dialog<>();
            dialog.setTitle("Ajouter un administrateur");
            dialog.setDialogPane(loader.load());

            // Vérifier si les boutons existent déjà dans le FXML
            boolean hasButtonTypes = !dialog.getDialogPane().getButtonTypes().isEmpty();
            
            // N'ajouter les boutons que s'ils n'existent pas déjà
            if (!hasButtonTypes) {
                ButtonType okButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);
            }

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
     * Initialise les composants du formulaire avec des valeurs par défaut.
     * - Remplit le ComboBox du statut de contrat.
     * - Définit la date d'embauche par défaut à aujourd'hui.
     * - Ajoute une validation d'entrée pour le champ salaire afin de s'assurer qu'il ne contient que des valeurs numériques.
     */
    private void initialize() {
        // Initialisation des valeurs du ComboBox
        statutContratCombo.getItems().addAll("CDI", "CDD", "STAGE", "APPRENTISSAGE");

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
     * Affiche la boîte de dialogue et attend la réponse de l'utilisateur.
     *
     * @return un {@link Optional} contenant le {@link ButtonType} cliqué par l'utilisateur.
     */
    public Optional<ButtonType> showAndWait() {
        return dialog.showAndWait();
    }

    /**
     * Valide toutes les entrées de l'utilisateur et affiche des messages d'erreur si la validation échoue.
     * Vérifie les champs obligatoires, le format de l'email, le format du téléphone, le format du salaire,
     * et la correspondance des mots de passe.
     *
     * @return true si toutes les entrées sont valides, false sinon.
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
     * Crée et retourne un objet de requête de création d'administrateur avec les données saisies dans le formulaire.
     * Toutes les valeurs des champs du formulaire sont nettoyées (trim) et converties aux formats appropriés.
     *
     * @return {@link AdministrateurCreateRequest} rempli avec les entrées de l'utilisateur.
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