package com.pharmacie.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OrdonnanceCreateRequest {

    @JsonFormat(
            shape    = JsonFormat.Shape.STRING,
            pattern  = "yyyy-MM-dd'T'HH",
            timezone = "UTC"
    )
    private Date dateEmission;

    private String rppsMedecin;

    private UUID clientId;

    private List<PrescriptionCreateRequest> prescriptions;

    /** Constructeur vide */
    public OrdonnanceCreateRequest() {
    }

    /** Constructeur avec tous les champs */
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

    public Date getDateEmission() {
        return dateEmission;
    }

    public String getRppsMedecin() {
        return rppsMedecin;
    }

    public UUID getClientId() {
        return clientId;
    }

    public List<PrescriptionCreateRequest> getPrescriptions() {
        return prescriptions;
    }

    // --- Setters ---

    public void setDateEmission(Date dateEmission) {
        this.dateEmission = dateEmission;
    }

    public void setRppsMedecin(String rppsMedecin) {
        this.rppsMedecin = rppsMedecin;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public void setPrescriptions(List<PrescriptionCreateRequest> prescriptions) {
        this.prescriptions = prescriptions;
    }


}