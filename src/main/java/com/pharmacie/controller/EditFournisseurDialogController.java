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
    private Fournisseur currentFournisseur;

    /**
     * Constructeur. Charge le fichier FXML de la boîte de dialogue et configure le contrôleur.
     * Initialise également la validation des champs.
     * @throws RuntimeException si le chargement du fichier FXML échoue.
     */
    public EditFournisseurDialogController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/view/EditFournisseurDialog.fxml"));
            loader.setController(this);
            dialog.setTitle("Modifier un fournisseur");
            
            // Charger le DialogPane depuis le FXML
            dialog.setDialogPane(loader.load());
            
            // Vérifier si les boutons existent déjà dans le FXML
            boolean hasOkButton = dialog.getDialogPane().getButtonTypes().stream()
                .anyMatch(bt -> bt.getButtonData() == ButtonBar.ButtonData.OK_DONE);
            boolean hasCancelButton = dialog.getDialogPane().getButtonTypes().stream()
                .anyMatch(bt -> bt.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE);
            
            // N'ajouter les boutons que s'ils n'existent pas déjà
            if (!hasOkButton) {
                ButtonType okButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().add(okButtonType);
            }
            
            if (!hasCancelButton) {
                ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().add(cancelButtonType);
            }
            
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
                if (!validate()) {
                    e.consume();
                }
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
        
        if (nomSocieteField.getText().trim().isEmpty()) {
            errors.append("Le nom de société est obligatoire\n");
        }
        
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            errors.append("L'email est obligatoire\n");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.append("Format email invalide\n");
        }
        
        String telephone = telephoneField.getText().trim();
        if (telephone.isEmpty()) {
            errors.append("Le téléphone est obligatoire\n");
        } else if (!PHONE_PATTERN.matcher(telephone).matches()) {
            errors.append("Téléphone doit contenir 10 chiffres\n");
        }
        
        if (adresseField.getText().trim().isEmpty()) {
            errors.append("L'adresse est obligatoire\n");
        }
        
        errorLabel.setText(errors.toString());
        return errors.length() == 0;
    }

    /**
     * Remplit les champs du formulaire avec les informations d'un fournisseur existant.
     * @param fournisseur Le fournisseur dont les informations doivent être affichées.
     */
    public void setFournisseur(Fournisseur fournisseur) {
        this.currentFournisseur = fournisseur;
        nomSocieteField.setText(fournisseur.getNomSociete() != null ? fournisseur.getNomSociete() : "");
        emailField.setText(fournisseur.getEmail() != null ? fournisseur.getEmail() : "");
        telephoneField.setText(fournisseur.getTelephone() != null ? fournisseur.getTelephone() : "");
        adresseField.setText(fournisseur.getAdresse() != null ? fournisseur.getAdresse() : "");
        sujetFonctionField.setText(fournisseur.getSujetFonction() != null ? fournisseur.getSujetFonction() : "");
        faxField.setText(fournisseur.getFax() != null ? fournisseur.getFax() : "");
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
    
    /**
     * Récupère le fournisseur actuellement en cours d'édition.
     * @return Le fournisseur en cours d'édition.
     */
    public Fournisseur getCurrentFournisseur() {
        return currentFournisseur;
    }
}