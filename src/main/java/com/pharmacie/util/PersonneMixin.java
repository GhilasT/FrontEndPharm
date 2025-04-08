package com.pharmacie.util;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.UUID;

public abstract class PersonneMixin {
    // Ici, on suppose que la classe Personne possède un getter getIdPersonne()
    // qui correspond à l'attribut d'identification.
    @JsonAlias("idPersonne")
    public abstract UUID getIdPersonne();
}