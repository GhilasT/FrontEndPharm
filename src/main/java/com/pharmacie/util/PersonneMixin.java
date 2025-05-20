package com.pharmacie.util;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.UUID;

/**
 * Classe Mixin abstraite pour la sérialisation/désérialisation JSON de la classe Personne.
 * Elle permet de mapper l'attribut d'identification JSON 'idPersonne' à la méthode correspondante.
 */
public abstract class PersonneMixin {
    // Ici, on suppose que la classe Personne possède un getter getIdPersonne()
    // qui correspond à l'attribut d'identification.

    /**
     * Méthode abstraite pour obtenir l'identifiant de la personne.
     * L'annotation {@link JsonAlias} est utilisée pour mapper le champ JSON "idPersonne"
     * à cette méthode lors de la désérialisation.
     *
     * @return L'UUID de la personne.
     */
    @JsonAlias("idPersonne")
    public abstract UUID getIdPersonne();
}