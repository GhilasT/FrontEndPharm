package com.pharmacie.util;

import com.pharmacie.model.Employe;

public class SessionUtilisateur {
    private static Employe utilisateurConnecte;

    public static Employe getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public static void setUtilisateurConnecte(Employe utilisateur) {
        utilisateurConnecte = utilisateur;
    }
}