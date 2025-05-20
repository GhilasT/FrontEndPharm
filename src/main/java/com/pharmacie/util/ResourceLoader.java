package com.pharmacie.util;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilitaire pour charger les ressources de manière cohérente
 * tant dans l'environnement de développement qu'après compilation en JAR.
 */
public class ResourceLoader {
    private static final Logger LOGGER = Logger.getLogger(ResourceLoader.class.getName());
    
    /**
     * Charge une image à partir de son chemin relatif avec gestion d'erreur.
     * Essaie plusieurs chemins possibles pour trouver la ressource.
     * 
     * @param path Le chemin de base de la ressource (sans préfixe)
     * @return L'objet Image chargé ou null si non trouvé
     */
    public static Image loadImage(String path) {
        // Nettoyer le chemin d'accès
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        
        // Essayer différentes combinaisons de chemins
        String[] possiblePaths = {
            "/" + path,
            "/com/pharmacie/" + path,
            "/com/pharmacie/images/" + path,
            "/" + path.replace("com/pharmacie/", ""),
            path
        };
        
        for (String possiblePath : possiblePaths) {
            try {
                InputStream stream = ResourceLoader.class.getResourceAsStream(possiblePath);
                if (stream != null) {
                    LOGGER.info("Image chargée avec succès: " + possiblePath);
                    return new Image(stream);
                }
            } catch (Exception e) {
                // Continuer avec le prochain chemin
            }
        }
        
        LOGGER.log(Level.WARNING, "Impossible de charger l'image: {0}", path);
        return null;
    }
    
    /**
     * Obtient un InputStream pour une ressource avec gestion d'erreur.
     * Essaie plusieurs chemins possibles pour trouver la ressource.
     * 
     * @param path Le chemin de base de la ressource (sans préfixe)
     * @return L'InputStream pour la ressource ou null si non trouvée
     */
    public static InputStream getResourceAsStream(String path) {
        // Nettoyer le chemin d'accès
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        
        // Essayer différentes combinaisons de chemins
        String[] possiblePaths = {
            "/" + path,
            "/com/pharmacie/" + path,
            "/com/pharmacie/images/" + path,
            "/" + path.replace("com/pharmacie/", ""),
            path
        };
        
        for (String possiblePath : possiblePaths) {
            InputStream stream = ResourceLoader.class.getResourceAsStream(possiblePath);
            if (stream != null) {
                LOGGER.info("Ressource chargée avec succès: " + possiblePath);
                return stream;
            }
        }
        
        LOGGER.log(Level.WARNING, "Impossible de charger la ressource: {0}", path);
        return null;
    }
}
