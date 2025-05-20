package com.pharmacie.util;

import java.util.UUID;

import com.pharmacie.model.Client;

/**
 * Classe utilitaire contenant des constantes et des variables globales pour l'application.
 * Fournit un accès centralisé à la configuration de base comme l'URL de l'API et le jeton d'authentification.
 */
public class Global {
    /**
     * Instance globale d'un client. Peut être utilisé pour stocker des informations sur un client actif.
     */
    public static Client GlobalClient = new Client();
    private static final String Hosted_URL = "https://pharmaplus-c71b024acb6b.herokuapp.com/api";
    /**
     * URL de base pour l'API en environnement local.
     */
    public static final String Local_URL = "http://localhost:8080/api";
    /**
     * URL de base actuellement utilisée pour les appels API.
     */
    public static final String BASE_URL = Hosted_URL;

    /**
     * Récupère l'URL de base pour les appels API.
     *
     * @return L'URL de base de l'API.
     */
    public static String getBaseUrl() {
        return BASE_URL;
    }
    private static String token = "";
    
    /**
     * Récupère le jeton d'authentification global.
     * Renvoie une chaîne vide si le jeton est null.
     *
     * @return Le jeton d'authentification actuel, ou une chaîne vide.
     */
    public static String getToken() {
        return token != null ? token : "";
    }
    
    /**
     * Définit le jeton d'authentification global.
     * Si le nouveau jeton est null, le jeton global est défini comme une chaîne vide.
     * Affiche des messages de log concernant la mise à jour du jeton.
     *
     * @param newToken Le nouveau jeton d'authentification à stocker.
     */
    public static void setToken(String newToken) {
        if (newToken != null) {
            token = newToken;
            System.out.println("Token set successfully: " + token.substring(0, Math.min(10, token.length())) + "...");
        } else {
            token = "";
            System.out.println("Warning: Null token received");
        }
    }

    /**
     * Récupère l'identifiant du client global ({@link #GlobalClient}).
     *
     * @return L'UUID du client global, ou null si le client ou son ID n'est pas défini.
     */
    public static UUID getClientId() {
        return GlobalClient.getIdPersonne();
    }
    

}
