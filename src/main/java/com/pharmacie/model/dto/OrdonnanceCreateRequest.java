package com.pharmacie.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
// cote front
public class OrdonnanceCreateRequest {
    @JsonFormat(
            shape   = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH",
            timezone = "UTC"
    )
    private Date dateEmission;
    private String  rppsMedecin;
    private UUID clientId;
    private List<PrescriptionCreateRequest> prescriptions;
}