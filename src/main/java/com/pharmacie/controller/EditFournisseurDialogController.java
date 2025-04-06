// EditFournisseurDialogController.java
package com.pharmacie.controller;

import com.pharmacie.model.Fournisseur;
import com.pharmacie.model.dto.FournisseurUpdateRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import java.util.Optional;
import java.util.regex.Pattern;

public class EditFournisseurDialogController {
    @FXML
    private TextField nomSocieteField, sujetFonctionField, faxField, emailField, telephoneField, adresseField;
    @FXML
    private Label errorLabel;
    
    private Dialog<ButtonType> dialog = new Dialog<>();
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    public EditFournisseurDialogController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/EditFournisseurDialog.fxml"));
            loader.setController(this);
            dialog.setDialogPane(loader.load());
            configureValidation();
        } catch (Exception e) {
            throw new RuntimeException("Erreur chargement dialog", e);
        }
    }

    private void configureValidation() {
        ButtonType okButtonType = dialog.getDialogPane().getButtonTypes()
                .stream()
                .filter(bt -> bt.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                .findFirst()
                .orElse(null);

        if (okButtonType != null) {
            Button okButton = (Button) dialog.getDialogPane().lookupButton(okButtonType);
            okButton.addEventFilter(javafx.event.ActionEvent.ACTION, e -> {
                if (!validate())
                    e.consume();
            });
        }
    }

    private boolean validate() {
        StringBuilder errors = new StringBuilder();
        
        if (!EMAIL_PATTERN.matcher(emailField.getText()).matches())
            errors.append("Email invalide\n");
        if (!PHONE_PATTERN.matcher(telephoneField.getText()).matches())
            errors.append("Téléphone invalide\n");
        
        errorLabel.setText(errors.toString());
        return errors.length() == 0;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        nomSocieteField.setText(fournisseur.getNomSociete());
        emailField.setText(fournisseur.getEmail());
        telephoneField.setText(fournisseur.getTelephone());
        adresseField.setText(fournisseur.getAdresse());
        sujetFonctionField.setText(fournisseur.getSujetFonction());
        faxField.setText(fournisseur.getFax());
    }

    public FournisseurUpdateRequest getUpdateRequest() {
        FournisseurUpdateRequest req = new FournisseurUpdateRequest();
        req.setNomSociete(nomSocieteField.getText().trim());
        req.setEmail(emailField.getText().trim());
        req.setTelephone(telephoneField.getText().trim());
        req.setAdresse(adresseField.getText().trim());
        req.setSujetFonction(sujetFonctionField.getText().trim());
        req.setFax(faxField.getText().trim());
        return req;
    }

    public Optional<ButtonType> showAndWait() {
        return dialog.showAndWait();
    }
}