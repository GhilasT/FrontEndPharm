package com.pharmacie.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * DTO pour la création d'une ordonnance.
 * Contient les informations nécessaires pour enregistrer une nouvelle ordonnance.
 */
public class OrdonnanceCreateRequest {

    /** Date d'émission de l'ordonnance. */
    @JsonFormat(
            shape    = JsonFormat.Shape.STRING,
            pattern  = "yyyy-MM-dd'T'HH",
            timezone = "UTC"
    )
    private Date dateEmission;

    /** Numéro RPPS du médecin prescripteur. */
    private String rppsMedecin;

    /** Identifiant unique du client concerné par l'ordonnance. */
    private UUID clientId;

    /** Liste des prescriptions associées à cette ordonnance. */
    private List<PrescriptionCreateRequest> prescriptions;

    /**
     * Constructeur par défaut.
     */
    public OrdonnanceCreateRequest() {
    }

    /**
     * Constructeur avec tous les champs.
     *
     * @param dateEmission Date d'émission de l'ordonnance.
     * @param rppsMedecin Numéro RPPS du médecin.
     * @param clientId Identifiant du client.
     * @param prescriptions Liste des prescriptions.
     */
    public OrdonnanceCreateRequest(
            Date dateEmission,
            String rppsMedecin,
            UUID clientId,
            List<PrescriptionCreateRequest> prescriptions
    ) {
        this.dateEmission   = dateEmission;
        this.rppsMedecin    = rppsMedecin;
        this.clientId       = clientId;
        this.prescriptions  = prescriptions;
    }

    // --- Getters ---

    /**
     * Obtient la date d'émission de l'ordonnance.
     * @return La date d'émission.
     */
    public Date getDateEmission() {
        return dateEmission;
    }

    /**
     * Obtient le numéro RPPS du médecin.
     * @return Le numéro RPPS du médecin.
     */
    public String getRppsMedecin() {
        return rppsMedecin;
    }

    /**
     * Obtient l'identifiant du client.
     * @return L'identifiant du client.
     */
    public UUID getClientId() {
        return clientId;
    }

    /**
     * Obtient la liste des prescriptions.
     * @return La liste des prescriptions.
     */
    public List<PrescriptionCreateRequest> getPrescriptions() {
        return prescriptions;
    }

    // --- Setters ---

    /**
     * Définit la date d'émission de l'ordonnance.
     * @param dateEmission La nouvelle date d'émission.
     */
    public void setDateEmission(Date dateEmission) {
        this.dateEmission = dateEmission;
    }

    /**
     * Définit le numéro RPPS du médecin.
     * @param rppsMedecin Le nouveau numéro RPPS du médecin.
     */
    public void setRppsMedecin(String rppsMedecin) {
        this.rppsMedecin = rppsMedecin;
    }

    /**
     * Définit l'identifiant du client.
     * @param clientId Le nouvel identifiant du client.
     */
    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    /**
     * Définit la liste des prescriptions.
     * @param prescriptions La nouvelle liste des prescriptions.
     */
    public void setPrescriptions(List<PrescriptionCreateRequest> prescriptions) {
        this.prescriptions = prescriptions;
    }


}