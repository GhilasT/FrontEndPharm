package com.pharmacie.controller;

import com.pharmacie.model.dto.ApprentiCreateRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;

public class AddApprentiDialogController {

    // Champs de saisie pour les informations personnelles et professionnelles de l'apprenti
    @FXML
    private TextField nomField, prenomField, emailField, telephoneField, adresseField,
            salaireField, diplomeField, ecoleField, emailProField;

    // Sélecteur de date pour la date d'embauche
    @FXML
    private DatePicker dateEmbauchePicker;

    // Menu déroulant pour le statut du contrat
    @FXML
    private ComboBox<String> statutContratCombo;

    // Champs pour les mots de passe
    @FXML
    private PasswordField passwordField, confirmPasswordField;

    // Étiquette pour afficher les messages d'erreur
    @FXML
    private Label errorLabel;

    // Boîte de dialogue contenant le formulaire
    private Dialog<ButtonType> dialog;

    // Expressions régulières pour valider l'email et le numéro de téléphone
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    // Constructeur : charge le fichier FXML et initialise la boîte de dialogue
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

    // Initialise les composants du formulaire
    private void initialize() {
        // Ajoute les différentes options dans le menu déroulant
        statutContratCombo.getItems().addAll("CDI", "CDD", "STAGE", "ALTERNANCE");

        // Initialise la date d'embauche à aujourd'hui
        dateEmbauchePicker.setValue(LocalDate.now());

        // S'assure que seul un nombre valide est entré dans le champ salaire
        salaireField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                salaireField.setText(oldValue);
            }
        });
    }

    // Configure la validation avant la soumission du formulaire
    private void configureValidation() {
        // Recherche le bouton OK dans la boîte de dialogue
        ButtonType okButtonType = dialog.getDialogPane().getButtonTypes()
                .stream()
                .filter(bt -> bt.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                .findFirst()
                .orElse(null);

        // Ajoute un filtre sur l'action pour bloquer la soumission si les champs sont invalides
        if (okButtonType != null) {
            Button okButton = (Button) dialog.getDialogPane().lookupButton(okButtonType);
            okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                if (!validateInputs())
                    event.consume();
            });
        }
    }

    // Valide les données saisies dans le formulaire
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();

        // Vérifie que tous les champs obligatoires sont remplis
        if (nomField.getText().trim().isEmpty())
            errors.append("Le nom est obligatoire.\n");
        if (prenomField.getText().trim().isEmpty())
            errors.append("Le prénom est obligatoire.\n");
        if (ecoleField.getText().trim().isEmpty())
            errors.append("L'école est obligatoire.\n");

        // Vérifie la validité de l'email personnel
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            errors.append("L'email est obligatoire.\n");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.append("Format d'email invalide.\n");
        }

        // Vérifie la validité du téléphone
        String telephone = telephoneField.getText().trim();
        if (telephone.isEmpty()) {
            errors.append("Le téléphone est obligatoire.\n");
        } else if (!PHONE_PATTERN.matcher(telephone).matches()) {
            errors.append("Format de téléphone invalide (10 chiffres attendus).\n");
        }

        // Vérifie que la date d'embauche est bien renseignée
        if (dateEmbauchePicker.getValue() == null) {
            errors.append("La date d'embauche est obligatoire.\n");
        }

        // Vérifie que le salaire est renseigné et bien numérique
        if (salaireField.getText().trim().isEmpty()) {
            errors.append("Le salaire est obligatoire.\n");
        } else {
            try {
                Double.parseDouble(salaireField.getText().trim());
            } catch (NumberFormatException e) {
                errors.append("Le salaire doit être un nombre valide.\n");
            }
        }

        // Vérifie que le statut de contrat est choisi
        if (statutContratCombo.getValue() == null) {
            errors.append("Le statut de contrat est obligatoire.\n");
        }

        // Vérifie la validité de l'email professionnel
        String emailPro = emailProField.getText().trim();
        if (emailPro.isEmpty()) {
            errors.append("L'email professionnel est obligatoire.\n");
        } else if (!EMAIL_PATTERN.matcher(emailPro).matches()) {
            errors.append("Format d'email professionnel invalide.\n");
        }

        // Vérifie les champs de mot de passe
        if (passwordField.getText().isEmpty()) {
            errors.append("Le mot de passe est obligatoire.\n");
        } else if (passwordField.getText().length() < 6) {
            errors.append("Le mot de passe doit contenir au moins 6 caractères.\n");
        }

        // Vérifie la correspondance entre les deux champs de mot de passe
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            errors.append("Les mots de passe ne correspondent pas.\n");
        }

        // Affiche les erreurs s’il y en a
        errorLabel.setText(errors.toString());
        return errors.length() == 0;
    }

    // Construit un objet ApprentiCreateRequest avec les données saisies
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

    // Affiche la boîte de dialogue et retourne le bouton choisi
    public Optional<ButtonType> showAndWait() {
        return dialog.showAndWait();
    }
}
