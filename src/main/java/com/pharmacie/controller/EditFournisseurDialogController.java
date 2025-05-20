// EditFournisseurDialogController.java
package com.pharmacie.controller;

import com.pharmacie.model.Fournisseur;
import com.pharmacie.model.dto.FournisseurUpdateRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Contrôleur pour la boîte de dialogue de modification d'un fournisseur.
 * Gère l'interface utilisateur et la logique de validation pour la mise à jour des informations d'un fournisseur.
 */
public class EditFournisseurDialogController {
    @FXML
    private TextField nomSocieteField, sujetFonctionField, faxField, emailField, telephoneField, adresseField;
    @FXML
    private Label errorLabel;
    
    private Dialog<ButtonType> dialog = new Dialog<>();
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    /**
     * Constructeur. Charge le fichier FXML de la boîte de dialogue et configure le contrôleur.
     * Initialise également la validation des champs.
     * @throws RuntimeException si le chargement du fichier FXML échoue.
     */
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

    /**
     * Configure la validation des champs du formulaire.
     * Ajoute un filtre d'événement au bouton "OK" pour exécuter la validation avant de fermer la boîte de dialogue.
     */
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
     * Remplit les champs du formulaire avec les informations d'un fournisseur existant.
     * @param fournisseur Le fournisseur dont les informations doivent être affichées.
     */
    public void setFournisseur(Fournisseur fournisseur) {
        nomSocieteField.setText(fournisseur.getNomSociete());
        emailField.setText(fournisseur.getEmail());
        telephoneField.setText(fournisseur.getTelephone());
        adresseField.setText(fournisseur.getAdresse());
        sujetFonctionField.setText(fournisseur.getSujetFonction());
        faxField.setText(fournisseur.getFax());
    }

    /**
     * Récupère les informations mises à jour du fournisseur à partir des champs du formulaire.
     * @return Un objet {@link FournisseurUpdateRequest} contenant les données mises à jour.
     */
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

    /**
     * Affiche la boîte de dialogue et attend que l'utilisateur interagisse avec elle (par exemple, clique sur "OK" ou "Annuler").
     * @return Un {@link Optional} contenant le {@link ButtonType} sur lequel l'utilisateur a cliqué.
     */
    public Optional<ButtonType> showAndWait() {
        return dialog.showAndWait();
    }
}