package com.pharmacie.controller;

import com.pharmacie.model.dto.AdminUpdateRequest;
import com.pharmacie.model.Admin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import java.io.IOException;
import java.util.Optional;

public class EditAdminDialogController {
    @FXML private TextField nomField, prenomField, emailField, telephoneField, adresseField, 
                            salaireField, diplomeField, emailProField, roleField;
    @FXML private ComboBox<String> statutContratCombo;

    private final Dialog<ButtonType> dialog = new Dialog<>();
    private AdminUpdateRequest updateRequest = new AdminUpdateRequest();

    public EditAdminDialogController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/EditAdminDialog.fxml"));
            loader.setController(this);
            DialogPane dialogPane = loader.load();
            dialog.setDialogPane(dialogPane); // Définir le DialogPane
        } catch (IOException e) {
            throw new RuntimeException("Erreur de chargement du formulaire", e);
        }
    }

    @FXML
    private void initialize() {
        statutContratCombo.getItems().addAll("CDI", "CDD", "STAGE","APRENTISSAGE");
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