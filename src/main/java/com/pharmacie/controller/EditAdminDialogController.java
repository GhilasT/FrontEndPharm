package com.pharmacie.controller;

import com.pharmacie.model.dto.AdminUpdateRequest;
import com.pharmacie.model.Admin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

public class EditAdminDialogController {
    @FXML private TextField nomField, prenomField, emailField, telephoneField, 
                          adresseField, salaireField, diplomeField, emailProField, roleField;
    @FXML private ComboBox<String> statutContratCombo;
    @FXML private Label errorLabel;
    
    private final Dialog<ButtonType> dialog = new Dialog<>();
    private AdminUpdateRequest updateRequest = new AdminUpdateRequest();
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    public EditAdminDialogController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/EditAdminDialog.fxml"));
            loader.setController(this);
            DialogPane dialogPane = loader.load();
            dialog.setDialogPane(dialogPane);
            
            configureValidation();

        } catch (IOException e) {
            throw new RuntimeException("Erreur de chargement du formulaire", e);
        }
    }

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

    @FXML
    private void initialize() {
        statutContratCombo.getItems().addAll("CDI", "CDD", "STAGE", "ALTERNANCE");
    }

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

    public Optional<ButtonType> showAndWait() {
        return dialog.showAndWait();
    }

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

    private Double parseDouble(String value) {
        try {
            return value.isEmpty() ? null : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}