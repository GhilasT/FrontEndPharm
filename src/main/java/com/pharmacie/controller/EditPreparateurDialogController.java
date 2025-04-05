// EditPreparateurDialogController.java
package com.pharmacie.controller;

import com.pharmacie.model.Preparateur;
import com.pharmacie.model.dto.PreparateurUpdateRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import java.util.Optional;
import java.util.regex.Pattern;

public class EditPreparateurDialogController {
    @FXML
    private TextField nomField, prenomField, emailField, telephoneField,
            adresseField, salaireField, diplomeField, emailProField;
    @FXML
    private DatePicker dateEmbauchePicker;
    @FXML
    private ComboBox<String> statutContratCombo;
    @FXML
    private Label errorLabel;

    private Dialog<ButtonType> dialog = new Dialog<>();
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    public EditPreparateurDialogController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/EditPreparateurDialog.fxml"));
            loader.setController(this);
            dialog.setDialogPane(loader.load());
            initialize();
            setupValidation();
        } catch (Exception e) {
            throw new RuntimeException("Erreur chargement dialog", e);
        }
    }

    private void initialize() {
        statutContratCombo.getItems().addAll("CDI", "CDD", "STAGE", "ALTERNANCE");
    }

    private void setupValidation() {
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

    public void setPreparateur(Preparateur preparateur) {
        nomField.setText(preparateur.getNom());
        prenomField.setText(preparateur.getPrenom());
        emailField.setText(preparateur.getEmail());
        telephoneField.setText(preparateur.getTelephone());
        adresseField.setText(preparateur.getAdresse());
        salaireField.setText(String.valueOf(preparateur.getSalaire()));
        statutContratCombo.setValue(preparateur.getStatutContrat());
        diplomeField.setText(preparateur.getDiplome());
        emailProField.setText(preparateur.getEmailPro());
        dateEmbauchePicker.setValue(preparateur.getDateEmbauche());
    }

    public PreparateurUpdateRequest getUpdateRequest() {
        PreparateurUpdateRequest req = new PreparateurUpdateRequest();
        req.setNom(nomField.getText());
        req.setPrenom(prenomField.getText());
        req.setEmail(emailField.getText());
        req.setTelephone(telephoneField.getText());
        req.setAdresse(adresseField.getText());
        req.setSalaire(Double.parseDouble(salaireField.getText()));
        req.setStatutContrat(statutContratCombo.getValue());
        req.setDiplome(diplomeField.getText());
        req.setEmailPro(emailProField.getText());
        req.setDateEmbauche(dateEmbauchePicker.getValue().toString());
        return req;
    }

    public Optional<ButtonType> showAndWait() {
        return dialog.showAndWait();
    }
}