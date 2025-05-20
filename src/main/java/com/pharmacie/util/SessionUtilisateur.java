package com.pharmacie.util;

import com.pharmacie.model.Employe;

/**
 * Gère la session de l'utilisateur connecté.
 * Cette classe conserve une référence à l'employé actuellement connecté à l'application.
 */
public class SessionUtilisateur {
    private static Employe utilisateurConnecte;

    /**
     * Récupère l'employé actuellement connecté.
     *
     * @return L'objet {@link Employe} représentant l'utilisateur connecté, ou null si personne n'est connecté.
     */
    public static Employe getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    /**
     * Définit l'employé actuellement connecté.
     *
     * @param utilisateur L'objet {@link Employe} représentant l'utilisateur à définir comme connecté.
     */
    public static void setUtilisateurConnecte(Employe utilisateur) {
        utilisateurConnecte = utilisateur;
    }
}