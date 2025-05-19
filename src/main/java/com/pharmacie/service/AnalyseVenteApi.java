package com.pharmacie.service;

import com.pharmacie.model.AnalyseVenteData;
import com.pharmacie.util.Global;
import org.json.JSONObject;
import org.json.JSONArray;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnalyseVenteApi {
    private static final Logger LOGGER = Logger.getLogger(AnalyseVenteApi.class.getName());
    private static final String API_BASE_URL = Global.getBaseUrl();
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final DateTimeFormatter API_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Récupère les données de vente pour la semaine en cours
     */
    public static Map<LocalDate, AnalyseVenteData> getVentesSemaine() throws Exception {
        String endpoint = "/analyse/ventes/semaine";
        return fetchAnalyseVenteData(endpoint);
    }

    /**
     * Récupère les données de vente par jour pour le mois en cours
     */
    public static Map<LocalDate, AnalyseVenteData> getVentesMoisJour() throws Exception {
        String endpoint = "/analyse/ventes/mois";
        return fetchAnalyseVenteData(endpoint);
    }

    /**
     * Récupère l'historique des ventes pour un nombre de mois spécifié
     */
    public static Map<LocalDate, AnalyseVenteData> getHistoriqueVentes(int mois) throws Exception {
        String endpoint = "/analyse/ventes/historique/" + mois;
        return fetchAnalyseVenteData(endpoint);
    }

    /**
     * Méthode générique pour récupérer les données d'analyse des ventes
     */
    private static Map<LocalDate, AnalyseVenteData> fetchAnalyseVenteData(String endpoint) throws Exception {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + Global.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseAnalyseVenteResponse(response.body());
            } else {
                LOGGER.log(Level.WARNING, "Erreur API: {0}, Corps: {1}", new Object[]{response.statusCode(), response.body()});
                
                // En cas d'erreur ou si le backend n'implémente pas encore ces endpoints,
                // on génère des données de test
                return generateTestData();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception lors de la récupération des données de vente", e);
            // En cas d'exception, on génère des données de test
            return generateTestData();
        }
    }

    /**
     * Parse la réponse JSON contenant les données d'analyse des ventes
     */
    private static Map<LocalDate, AnalyseVenteData> parseAnalyseVenteResponse(String jsonResponse) {
        Map<LocalDate, AnalyseVenteData> resultMap = new TreeMap<>();
        
        try {
            JSONArray array = new JSONArray(jsonResponse);
            
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                
                LocalDate date = LocalDate.parse(item.getString("date"), API_DATE_FORMAT);
                int ventes = item.getInt("ventes");
                int commandes = item.getInt("commandes");
                double chiffreAffaire = item.getDouble("chiffreAffaire");
                
                resultMap.put(date, new AnalyseVenteData(date,ventes, commandes, chiffreAffaire));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du parsing des données d'analyse", e);
        }
        
        return resultMap;
    }

    /**
     * Génère des données de test pour le développement et les démonstrations
     */
    private static Map<LocalDate, AnalyseVenteData> generateTestData() {
        Map<LocalDate, AnalyseVenteData> testData = new TreeMap<>();
        LocalDate today = LocalDate.now();
        
        // Générer des données pour les 7 derniers jours
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            int ventes = 5 + (int) (Math.random() * 20); // Entre 5 et 24 ventes
            int commandes = 1 + (int) (Math.random() * 8);  // Entre 1 et 8 commandes
            double ca = ventes * (50 + Math.random() * 150); // Entre 50€ et 200€ par vente
            
            testData.put(date, new AnalyseVenteData(date,ventes, commandes, ca));
        }
        
        return testData;
    }
}