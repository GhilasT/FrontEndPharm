package com.pharmacie.controller;

import com.pharmacie.model.PharmacienAdjoint;
import com.pharmacie.model.dto.PharmacienAdjointUpdateRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Contrôleur pour la boîte de dialogue de modification d'un pharmacien adjoint.
 * Gère l'interface utilisateur, l'initialisation des composants et la validation
 * pour la mise à jour des informations d'un pharmacien adjoint.
 */
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

    /**
     * Constructeur. Charge le fichier FXML de la boîte de dialogue, configure le contrôleur,
     * initialise les composants et met en place la validation.
     * @throws RuntimeException si le chargement du fichier FXML échoue.
     */
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

    /**
     * Initialise les composants du formulaire, comme le ComboBox pour le statut du contrat.
     */
    private void initialize() {
        statutContratCombo.getItems().addAll("CDI", "CDD", "STAGE", "ALTERNANCE");
    }

    /**
     * Configure la validation des champs du formulaire.
     * Ajoute un filtre d'événement au bouton "OK" pour exécuter la validation avant de fermer la boîte de dialogue.
     */
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

    /**
     * Valide les champs du formulaire (email et téléphone).
     * Affiche les messages d'erreur s'il y en a.
     * @return true si tous les champs sont valides, false sinon.
     */
    private boolean validate() {
        StringBuilder errors = new StringBuilder();
        if (!EMAIL_PATTERN.matcher(emailField.getText()).matches())
            errors.append("Email invalide\n");
        if (!PHONE_PATTERN.matcher(telephoneField.getText()).matches())
            errors.append("Téléphone invalide\n");
        errorLabel.setText(errors.toString());
        return errors.length() == 0;
    }

    /**
     * Remplit les champs du formulaire avec les informations d'un pharmacien adjoint existant.
     * @param pharmacien Le pharmacien adjoint dont les informations doivent être affichées.
     */
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

    /**
     * Récupère les informations mises à jour du pharmacien adjoint à partir des champs du formulaire.
     * @return Un objet {@link PharmacienAdjointUpdateRequest} contenant les données mises à jour.
     */
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

    /**
     * Affiche la boîte de dialogue et attend que l'utilisateur interagisse avec elle.
     * @return Un {@link Optional} contenant le {@link ButtonType} sur lequel l'utilisateur a cliqué.
     */
    public Optional<ButtonType> showAndWait() {
        return dialog.showAndWait();
    }
}