package com.pharmacie.controller;

import com.pharmacie.model.PharmacienAdjoint;
import com.pharmacie.model.dto.PharmacienAdjointUpdateRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import java.util.Optional;
import java.util.regex.Pattern;

public class EditPharmacienAdjointDialogController {
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

    public EditPharmacienAdjointDialogController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/EditPharmacienAdjointDialog.fxml"));
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

    public void setPharmacienAdjoint(PharmacienAdjoint pharmacien) {
        nomField.setText(pharmacien.getNom());
        prenomField.setText(pharmacien.getPrenom());
        emailField.setText(pharmacien.getEmail());
        telephoneField.setText(pharmacien.getTelephone());
        adresseField.setText(pharmacien.getAdresse());
        salaireField.setText(String.valueOf(pharmacien.getSalaire()));
        statutContratCombo.setValue(pharmacien.getStatutContrat());
        diplomeField.setText(pharmacien.getDiplome());
        emailProField.setText(pharmacien.getEmailPro());
        dateEmbauchePicker.setValue(pharmacien.getDateEmbauche());
    }

    public PharmacienAdjointUpdateRequest getUpdateRequest() {
        PharmacienAdjointUpdateRequest req = new PharmacienAdjointUpdateRequest();
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