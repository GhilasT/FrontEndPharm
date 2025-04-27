package com.pharmacie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedecinResponse {
    private UUID idMedecin;  // Identifiant du médecin
    private String civilite;  // Civilité (M. / Mme, etc.)
    private String nomExercice;  // Nom d'exercice
    private String prenomExercice;  // Prénom d'exercice
    private String rppsMedecin;  // Numéro RPPS unique
    private String categorieProfessionnelle;  // Civil, militaire, étudiant, etc.
    private String profession;  // Profession (Médecin, Chirurgien, etc.)
    private String modeExercice;  // Mode d'exercice (libéral, salarié, bénévole)
    private String qualifications;  // Qualifications, diplômes, autorisations, savoir-faire
    private String structureExercice;  // Coordonnées des structures d'exercice (cabinet, hôpital, etc.)
    private String fonctionActivite;  // Fonction dans l'exercice médical (par exemple, Médecin traitant)
    private String genreActivite;  // Genre d’activité (consultation, chirurgie, soins palliatifs, etc.)
}